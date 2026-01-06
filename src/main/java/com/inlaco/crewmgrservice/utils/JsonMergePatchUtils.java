package com.inlaco.crewmgrservice.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.inlaco.crewmgrservice.annotation.JsonPatchIgnore;
import com.inlaco.crewmgrservice.annotation.JsonPatchIgnoreProperties;
import com.inlaco.crewmgrservice.exceptions.ResourceNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/**
 * Utility service for applying RFC 7386 (JSON Merge Patch) to MongoDB entities.
 *
 * <p>This class supports:
 *
 * <ul>
 *   <li>Applying JSON Merge Patch to Java objects
 *   <li>Ignoring specific fields by path
 *   <li>Ignoring fields annotated with {@link JsonPatchIgnore}
 *   <li>Ignoring audit fields such as {@link CreatedBy}, {@link CreatedDate}, {@link
 *       LastModifiedBy}, {@link LastModifiedDate}
 *   <li>Ignoring fields defined via {@link JsonPatchIgnoreProperties}
 *   <li>Persisting patched entities to MongoDB
 * </ul>
 *
 * <p>The patch process is safe:
 *
 * <ul>
 *   <li>The original patch request is never mutated
 *   <li>All ignored fields are removed before patch application
 * </ul>
 *
 * <p>This service is designed to be reused across REST PATCH endpoints.
 */
@Slf4j
@Lazy
@Service
@RequiredArgsConstructor
public class JsonMergePatchUtils {

  private final MongoTemplate mongoTemplate;
  private final ObjectMapper mapper = RawJsonConvertor.getMapper();

  /* ======================== PUBLIC API ======================== */

  /**
   * Applies a JSON Merge Patch (RFC 7386) to a given object while excluding specific field paths.
   *
   * <p>The patch is applied in the following order:
   *
   * <ol>
   *   <li>Clone the patch request to avoid side effects
   *   <li>Remove fields defined by {@code ignoreFieldPaths}
   *   <li>Remove fields annotated with ignore annotations
   *   <li>Apply JSON Merge Patch to the original object
   * </ol>
   *
   * @param origin the original object to patch (must not be {@code null})
   * @param patchRequest JSON Merge Patch request
   * @param ignoreFieldPaths dot-separated field paths to exclude from patching
   * @param <E> entity type
   * @return patched entity instance
   * @throws IllegalStateException if patch application or deserialization fails
   */
  public <E> E apply(E origin, JsonNode patchRequest, String... ignoreFieldPaths) {
    try {
      JsonNode sanitizedPatch = sanitizePatch(origin.getClass(), patchRequest, ignoreFieldPaths);
      JsonMergePatch patch = JsonMergePatch.fromJson(sanitizedPatch);
      JsonNode patched = patch.apply(mapper.valueToTree(origin));
      return mapper.treeToValue(patched, (Class<E>) origin.getClass());
    } catch (IOException | JsonPatchException ex) {
      throw new IllegalStateException("Failed to apply JSON Merge Patch", ex);
    }
  }

  /**
   * Applies a JSON Merge Patch to an object while automatically ignoring:
   *
   * <ul>
   *   <li>Fields annotated with {@link JsonPatchIgnore}
   *   <li>Audit fields (createdBy, createdDate, etc.)
   *   <li>Fields declared in {@link JsonPatchIgnoreProperties}
   * </ul>
   *
   * <p>This is the recommended method for PATCH endpoints.
   *
   * @param origin the original object
   * @param patchRequest JSON Merge Patch request
   * @param <E> entity type
   * @return patched entity
   */
  public <E> E apply(E origin, JsonNode patchRequest) {
    return apply(origin, patchRequest, getJsonPatchIgnoreProperties(origin.getClass()));
  }

  /**
   * Retrieves an entity by its identifier, applies a JSON Merge Patch, and persists the updated
   * entity to MongoDB.
   *
   * @param id entity identifier
   * @param entityClass entity class
   * @param patchRequest JSON Merge Patch request
   * @param ignoreFieldPaths The paths of fields to ignore during the patching.
   * @param <E> entity type
   * @return updated and saved entity
   * @throws ResourceNotFoundException if the entity does not exist
   */
  public <E> E patch(
      Object id, Class<E> entityClass, JsonNode patchRequest, String... ignoreFieldPaths) {

    E entity = getEntity(id, entityClass);
    return mongoTemplate.save(apply(entity, patchRequest, ignoreFieldPaths));
  }

  /* ======================== INTERNAL ======================== */

  /**
   * Retrieves an entity from the MongoDB database by its ID. If the entity is not found, a {@link
   * <p>ResourceNotFoundException} is thrown.
   *
   * @param <E> The type of the entity to retrieve.
   * @param id The ID of the entity to retrieve.
   * @param entityClass The class of the entity to retrieve.
   */
  private <E> E getEntity(Object id, Class<E> clazz) {
    return Optional.ofNullable(mongoTemplate.findById(id, clazz))
        .orElseThrow(() -> new ResourceNotFoundException(clazz, "id", id));
  }

  /**
   * Pre-processes a JSON Merge Patch request before application.
   *
   * <p>This method ensures:
   *
   * <ul>
   *   <li>The original patch request is not mutated
   *   <li>Explicitly ignored field paths are removed
   *   <li>Fields annotated with ignore annotations are removed
   *   <li>Fields defined in {@link JsonPatchIgnoreProperties} are removed
   * </ul>
   *
   * @param clazz target entity class
   * @param patch original patch request
   * @param ignorePaths explicit field paths to ignore
   * @return sanitized patch node ready for application
   */
  private JsonNode sanitizePatch(Class<?> clazz, JsonNode patch, String... ignorePaths) {

    JsonNode node = patch.deepCopy();

    if (ignorePaths != null && ignorePaths.length > 0) {
      node = JsonNodeUtils.removeFields(node, ignorePaths);
    }

    node = removeAnnotatedIgnoreFields(node, clazz);
    node = removeJsonPatchIgnorePropertiesFields(node, clazz);

    return node;
  }

  /* ======================== FIELD FILTER ======================== */

  /**
   * Removes all fields that must never be patched, including:
   *
   * <ul>
   *   <li>{@link JsonPatchIgnore}
   *   <li>Spring Data audit fields
   * </ul>
   *
   * @param node JSON patch node
   * @param clazz entity class
   * @return sanitized JSON node
   */
  public JsonNode removeAnnotatedIgnoreFields(JsonNode node, Class<?> clazz) {
    return removeAnnotatedFields(
        node,
        clazz,
        Set.of(
            JsonPatchIgnore.class,
            CreatedBy.class,
            CreatedDate.class,
            LastModifiedBy.class,
            LastModifiedDate.class));
  }

  private JsonNode removeAnnotatedFields(
      JsonNode node, Class<?> clazz, Set<Class<? extends Annotation>> ignoredAnnotations) {

    if (!(node instanceof ObjectNode objectNode)) {
      return node;
    }

    List<String> toRemove = new ArrayList<>();

    for (Map.Entry<String, JsonNode> entry : objectNode.properties()) {
      Field field = ReflectionUtils.getDeclaredField(clazz, entry.getKey());
      if (field == null) continue;

      if (ignoredAnnotations.stream().anyMatch(field::isAnnotationPresent)) {
        toRemove.add(entry.getKey());
        continue;
      }

      JsonNode child = entry.getValue();
      Class<?> fieldType = field.getType();

      if (child.isObject()) {
        removeAnnotatedFields(child, fieldType, ignoredAnnotations);
      } else if (child.isArray() && fieldType.isArray()) {
        child.forEach(
            item -> {
              if (item.isObject()) {
                removeAnnotatedFields(item, fieldType.getComponentType(), ignoredAnnotations);
              }
            });
      }
    }

    toRemove.forEach(objectNode::remove);
    return node;
  }

  /**
   * Removes fields defined in {@link JsonPatchIgnoreProperties} on the entity class.
   *
   * <p>This allows class-level configuration of fields that must never be patched.
   *
   * @param node JSON patch node
   * @param clazz entity class
   * @return JSON node without ignored properties
   */
  private JsonNode removeJsonPatchIgnorePropertiesFields(JsonNode node, Class<?> clazz) {
    JsonPatchIgnoreProperties props = clazz.getAnnotation(JsonPatchIgnoreProperties.class);
    return props == null ? node : JsonNodeUtils.removeFields(node, props.value());
  }

  /**
   * Get the fields to be ignored from the {@link JsonPatchIgnoreProperties} annotation on the
   *
   * @param clazz The class of the original object being patched.
   * @return An array of field names to be ignored.
   */
  private String[] getJsonPatchIgnoreProperties(Class<?> clazz) {
    JsonPatchIgnoreProperties props = clazz.getAnnotation(JsonPatchIgnoreProperties.class);
    return props == null ? new String[0] : props.value();
  }
}
