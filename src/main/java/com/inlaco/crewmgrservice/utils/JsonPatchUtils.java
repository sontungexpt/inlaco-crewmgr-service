package com.inlaco.crewmgrservice.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.inlaco.crewmgrservice.annotation.JsonPatchIgnore;
import com.inlaco.crewmgrservice.annotation.JsonPatchIgnoreProperties;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class JsonPatchUtils {

  private MongoTemplate mongoTemplate;

  private JsonNode removeIgnoreFields(JsonNode requestNode, String... ignoredFieldPaths) {
    JsonNode modifiedEntityNode = requestNode.deepCopy();
    for (String ignoredField : ignoredFieldPaths) {
      if (modifiedEntityNode.has(ignoredField)) {
        JsonNode node = modifiedEntityNode;
        String[] ignoredFieldPath = ignoredField.split("\\.");

        boolean shouldRemove = true;
        for (int i = 0; i < ignoredFieldPath.length - 1; i++) {
          if ((node = node.get(ignoredFieldPath[i])) == null) {
            shouldRemove = false;
            break;
          }
        }
        if (shouldRemove == true) {
          ((ObjectNode) node).remove(ignoredField);
        }
      }
    }
    return modifiedEntityNode;
  }

  private <T> List<String> getJsonPatchIgnoreProperties(Class<T> clazz) {
    List<String> ignoredProperties = new ArrayList<>();
    collectJsonPatchIgnoreProperties(clazz, "", ignoredProperties);
    return ignoredProperties;
  }

  private void collectJsonPatchIgnoreProperties(
      Class<?> clazz, String parentPath, List<String> ignoredFieldPaths) {
    JsonPatchIgnoreProperties ignoreProperties =
        clazz.getAnnotation(JsonPatchIgnoreProperties.class);
    if (ignoreProperties != null) {
      for (String value : ignoreProperties.value()) {
        String path = parentPath.isEmpty() ? value : parentPath + "." + value;
        ignoredFieldPaths.add(parentPath + path);
      }
    }

    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      if (!field.getType().isPrimitive() && !field.getType().equals(String.class)) {
        collectJsonPatchIgnoreProperties(field.getType(), field.getName(), ignoredFieldPaths);
      }
    }
  }

  private <T> List<String> getJsonPatchIgnoreFields(Class<T> clazz) {
    Field[] fields = clazz.getDeclaredFields();
    List<String> ignoredFields = new ArrayList<>();

    for (Field field : fields) {
      JsonPatchIgnore ignoreField = field.getAnnotation(JsonPatchIgnore.class);
      if (ignoreField != null) {

        ignoredFields.add(field.getName());
      }
    }
    return ignoredFields;
  }

  private <T> List<String> getJsonPatchIgnore(Class<T> clazz) {
    List<String> ignoredFields = new ArrayList<>();
    collectJsonPatchIgnore(clazz, "", ignoredFields);
    return ignoredFields;
  }

  private void collectJsonPatchIgnore(
      Class<?> clazz, String parentPath, List<String> ignoredFields) {
    Field[] fields = clazz.getDeclaredFields();

    for (Field field : fields) {
      JsonPatchIgnore ignoreField = field.getAnnotation(JsonPatchIgnore.class);
      String fieldName = field.getName();
      String fieldPath = parentPath.isEmpty() ? fieldName : parentPath + "." + fieldName;

      if (ignoreField != null) {
        ignoredFields.add(fieldPath);
        continue; // no need to recursive because the parent field is ignored
      }

      if (!field.getType().isPrimitive() && !field.getType().equals(String.class)) {
        collectJsonPatchIgnore(field.getType(), fieldPath, ignoredFields);
      }
    }
  }

  private <T> String[] getIgnoredFields(Class<T> clazz) {
    List<String> ignoredFields = new ArrayList<>();
    ignoredFields.addAll(getJsonPatchIgnoreProperties(clazz));
    ignoredFields.addAll(getJsonPatchIgnoreFields(clazz));
    return ignoredFields.toArray(new String[0]);
  }

  @SuppressWarnings("unchecked")
  public <E> E applyPatch(E origin, JsonNode patchRequest, String... ignoredFields) {
    ObjectMapper mapper = RawJsonConvertor.getMapper();
    try {
      JsonNode entityNode = mapper.convertValue(origin, JsonNode.class);
      JsonPatch jsonPatch = JsonPatch.fromJson(patchRequest);
      JsonNode updatedJsonNode = jsonPatch.apply(entityNode);
      return (E) mapper.treeToValue(updatedJsonNode, origin.getClass());
    } catch (IOException ex) {
      throw new IllegalStateException("Failed to apply patch", ex);
    } catch (JsonPatchException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  public <E> E applyMergePatch(E origin, JsonNode patchRequest, String... ignoredFields) {
    JsonNode modifiedEntityNode = removeIgnoreFields(patchRequest, ignoredFields);
    ObjectMapper mapper = RawJsonConvertor.getMapper();
    try {
      JsonNode entityNode = mapper.convertValue(origin, JsonNode.class);
      JsonMergePatch jsonMergePatch = JsonMergePatch.fromJson(modifiedEntityNode);
      JsonNode updatedJsonNode = jsonMergePatch.apply(entityNode);
      return (E) mapper.treeToValue(updatedJsonNode, origin.getClass());
    } catch (IOException ex) {
      throw new IllegalStateException("Failed to apply patch", ex);
    } catch (JsonPatchException e) {
      throw new RuntimeException(e);
    }
  }

  // public <ID, E> E applyMergePatch(ID id, JsonNode patchRequest, Class<E> clazz) {

  //   E entity = mongoTemplate.findById(id, clazz);

  //   if (entity == null) {
  //     throw new ResourceNotFoundException(clazz, "id", id);
  //   }

  //   E updatedEntity = applyMergePatch(entity, patchRequest);
  // }
  //

}
