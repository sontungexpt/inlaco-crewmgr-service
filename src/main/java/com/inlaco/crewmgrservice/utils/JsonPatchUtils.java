package com.inlaco.crewmgrservice.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JsonPatchUtils {

  @SuppressWarnings("unchecked")
  public <E> E applyPatch(E origin, JsonNode patchRequest, String... ignoredFields) {
    JsonNode modifiedEntityNode = patchRequest.deepCopy();
    for (String ignoredField : ignoredFields) {
      if (modifiedEntityNode.has(ignoredField)) {
        String[] ignoredFieldPath = ignoredField.split("\\.");

        JsonNode node = modifiedEntityNode;
        for (int i = 0; i < ignoredFieldPath.length - 1; i++) {
          if (node.has(ignoredFieldPath[i])) {
            node = node.get(ignoredFieldPath[i]);
          } else {
            break;
          }
        }
        ((ObjectNode) node).remove(ignoredField);
      }
    }

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
    JsonNode modifiedEntityNode = patchRequest.deepCopy();
    for (String ignoredField : ignoredFields) {
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
}
