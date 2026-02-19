package com.inlaco.crewmgrservice.infrastructure.serialization.deserializer;

import com.inlaco.crewmgrservice.shared.application.model.Patch;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ValueDeserializer;

@Component
public class PatchDeserializer extends ValueDeserializer<Patch<?>> {

  private final JavaType valueType;

  public PatchDeserializer() {
    this.valueType = null;
  }

  private PatchDeserializer(JavaType valueType) {
    this.valueType = valueType;
  }

  @Override
  public Object getAbsentValue(DeserializationContext ctxt) {
    return Patch.unchanged();
  }

  @Override
  public Object getNullValue(DeserializationContext ctxt) {
    return Patch.of(null);
  }

  @Override
  public Patch<?> deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
    JsonToken token = p.currentToken();
    if (token == null) {
      token = p.nextToken();
    }

    if (token == JsonToken.VALUE_NULL) {
      return Patch.of(null);
    }

    if (valueType == null) {
      Object value = ctxt.readValue(p, Object.class);
      return wrapRecursive(value);
    }

    // Deserialize fully typed FIRST (preserve polymorphism)
    Object value = ctxt.readValue(p, valueType);
    return wrapRecursive(value);
  }

  /** Recursively wrap Map values only. Collection is NOT recursed per requirement. */
  private Patch<?> wrapRecursive(Object value) {
    if (value == null) {
      return Patch.of(null);
    }

    if (value instanceof Patch<?>) {
      return (Patch<?>) value;
    }

    // ===== Map recursion =====
    if (value instanceof Map<?, ?> map) {
      Map<String, Patch<?>> patched = new LinkedHashMap<>();
      for (Map.Entry<?, ?> entry : map.entrySet()) {
        Object innerValue = entry.getValue();
        patched.put(String.valueOf(entry.getKey()), wrapRecursive(innerValue));
      }

      return Patch.of(patched);
    }

    // ===== Collection (NO recursion) =====
    if (value instanceof Iterable<?>) {
      return Patch.of(value);
    }

    // ===== Leaf =====
    return Patch.of(value);
  }

  @Override
  public ValueDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {

    JavaType wrapperType = property != null ? property.getType() : ctxt.getContextualType();

    if (wrapperType == null || !wrapperType.hasRawClass(Patch.class)) {
      return this;
    }

    JavaType inner = wrapperType.containedType(0);
    if (inner == null) {
      inner = ctxt.constructType(Object.class);
    }

    return new PatchDeserializer(inner);
  }
}
// package com.inlaco.crewmgrservice.infrastructure.serialization.deserializer;

// import com.inlaco.crewmgrservice.shared.application.model.Patch;
// import com.inlaco.crewmgrservice.shared.support.ConsoleUtils;
// import java.util.LinkedHashMap;
// import java.util.Map;
// import org.springframework.stereotype.Component;
// import tools.jackson.core.JacksonException;
// import tools.jackson.core.JsonParser;
// import tools.jackson.core.JsonToken;
// import tools.jackson.databind.BeanProperty;
// import tools.jackson.databind.DeserializationContext;
// import tools.jackson.databind.JavaType;
// import tools.jackson.databind.JsonNode;
// import tools.jackson.databind.ValueDeserializer;

// @Component
// public class PatchDeserializer extends ValueDeserializer<Patch<?>> {

//   private final JavaType valueType;

//   public PatchDeserializer() {
//     this.valueType = null;
//   }

//   private PatchDeserializer(JavaType valueType) {
//     this.valueType = valueType;
//   }

//   /** Field is absent in JSON -> unchanged */
//   @Override
//   public Object getAbsentValue(DeserializationContext ctxt) throws JacksonException {
//     ConsoleUtils.print("Field is absent in JSON -> unchanged");
//     return Patch.unchanged();
//   }

//   /** Explicit JSON null -> update(null) */
//   @Override
//   public Object getNullValue(DeserializationContext ctxt) throws JacksonException {
//     ConsoleUtils.print("Explicit JSON null -> update(null)");
//     return Patch.of(null);
//   }

//   @Override
//   public Patch<?> deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException
// {

//     JsonToken token = p.currentToken();
//     // If parser not yet positioned, advance (defensive)
//     if (token == null) {
//       token = p.nextToken();
//     }

//     // Explicit null
//     if (token == JsonToken.VALUE_NULL) {
//       return Patch.of(null);
//     }

//     // No generic info -> fallback to Object (use contextual deserializer for Object)
//     if (valueType == null) {
//       Object value = ctxt.readValue(p, Object.class);
//       return wrapIfNecessary(value);
//     }

//     // Map-like -> deep recursive wrapping
//     if (valueType.isMapLikeType()) {
//       JsonNode node = ctxt.readTree(p);
//       return buildPatch(node, ctxt, valueType);
//     }

//     // Collection-like -> DO NOT recurse (your requirement)
//     if (valueType.isCollectionLikeType()) {
//       Object value = ctxt.readValue(p, valueType);
//       return wrapIfNecessary(value);
//     }

//     // Typed leaf (DTO / primitive)
//     Object value = ctxt.readValue(p, valueType);
//     return wrapIfNecessary(value);
//   }

//   /** Recursively build Patch for Map types only. List recursion is intentionally disabled. */
//   private Patch<?> buildPatch(JsonNode node, DeserializationContext ctxt, JavaType currentType)
//       throws JacksonException {
//     if (node == null || node.isNull()) {
//       return Patch.of(null);
//     }

//     // If no meaningful type info -> fallback to structural detection
//     if (currentType == null || currentType.hasRawClass(Object.class)) {

//       if (node.isObject()) {
//         Map<String, Patch<?>> map = new LinkedHashMap<>();
//         for (var entry : node.properties()) {
//           map.put(entry.getKey(), buildPatch(entry.getValue(), ctxt, null));
//         }
//         return Patch.of(map);
//       }

//       // List recursion disabled -> treat as plain value
//       Object value = ctxt.readTreeAsValue(node, Object.class);
//       return wrapIfNecessary(value);
//     }

//     // Handle Map<K,V>
//     if (currentType.isMapLikeType() && node.isObject()) {
//       JavaType valueType = currentType.containedType(1);

//       // If generic type missing -> fallback to Object
//       if (valueType == null) {
//         valueType = ctxt.constructType(Object.class);
//       }

//       Map<String, Patch<?>> map = new LinkedHashMap<>();

//       for (var entry : node.properties()) {
//         map.put(entry.getKey(), buildPatch(entry.getValue(), ctxt, valueType));
//       }

//       return Patch.of(map);
//     }

//     // If declared as Collection but node is array
//     // -> No recursion per requirement
//     if (currentType.isCollectionLikeType() && node.isArray()) {
//       Object value = ctxt.readTreeAsValue(node, currentType);
//       return wrapIfNecessary(value);
//     }

//     // Typed leaf node
//     Object value = ctxt.readTreeAsValue(node, currentType);
//     return wrapIfNecessary(value);
//   }

//   /** Prevent double wrapping. */
//   private Patch<?> wrapIfNecessary(Object value) {
//     if (value instanceof Patch<?>) return (Patch<?>) value;
//     return Patch.of(value);
//   }

//   /** Capture generic type information from Patch<T> */
//   @Override
//   public ValueDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty
// property) {

//     JavaType wrapperType = property != null ? property.getType() : ctxt.getContextualType();

//     if (wrapperType == null || !wrapperType.hasRawClass(Patch.class)) {
//       return this;
//     }

//     JavaType innerType = wrapperType.containedType(0);

//     // If no generic declared -> fallback to Object
//     if (innerType == null) {
//       innerType = ctxt.constructType(Object.class);
//     }

//     return new PatchDeserializer(innerType);
//   }
// }
