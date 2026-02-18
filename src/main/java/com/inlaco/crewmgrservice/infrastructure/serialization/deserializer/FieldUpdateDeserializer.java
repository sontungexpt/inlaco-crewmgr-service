package com.inlaco.crewmgrservice.infrastructure.serialization.deserializer;

import com.inlaco.crewmgrservice.shared.application.model.FieldUpdate;
import java.util.Collection;
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
public class FieldUpdateDeserializer extends ValueDeserializer<FieldUpdate<?>> {

  private JavaType valueType;

  // default ctor (Jackson / framework needs)
  public FieldUpdateDeserializer() {}

  private FieldUpdateDeserializer(JavaType valueType) {
    this.valueType = valueType;
  }

  // @Override
  // public ValueDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty
  // property) {
  //   JavaType type = (property != null) ? property.getType() : ctxt.getContextualType();

  //   if (type == null || !type.hasRawClass(FieldUpdate.class)) {
  //     return this;
  //   }

  //   // T = kiểu bên trong FieldUpdate<T>
  //   JavaType innerType = type.containedType(0);

  //   // Xử lý đệ quy cho Map
  //   if (innerType != null && innerType.isMapLikeType()) {
  //     JavaType keyType = innerType.getKeyType();
  //     JavaType valueType = innerType.getContentType();

  //     // Nếu Value bên trong Map chưa phải là FieldUpdate, ta bọc nó lại thành FieldUpdate<Value>
  //     if (valueType != null && !valueType.hasRawClass(FieldUpdate.class)) {

  //       // 1. Tạo kiểu FieldUpdate<ValueType>
  //       JavaType fieldUpdateValueType =
  //           ctxt.getTypeFactory().constructParametricType(FieldUpdate.class, valueType);

  //       // 2. Tạo kiểu Map<Key, FieldUpdate<Value>>
  //       // Trong Jackson 3, dùng constructMapType với 3 tham số:
  //       innerType =
  //           ctxt.getTypeFactory()
  //               .constructMapType(
  //                   (Class<? extends Map>) innerType.getRawClass(), keyType,
  // fieldUpdateValueType);
  //     }
  //   }

  //   return new FieldUpdateDeserializer(innerType);
  // }

  // @Override
  // public ValueDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty
  // property) {
  //   JavaType wrapperType;

  //   if (property != null) {
  //     wrapperType = property.getType();
  //   } else {
  //     wrapperType = ctxt.getContextualType();
  //   }

  //   if (wrapperType == null || !wrapperType.hasRawClass(FieldUpdate.class)) {
  //     return this;
  //   }

  //   // Get the fist argument type -> FieldUpdate<T> -> T
  //   JavaType innerType = wrapperType.containedType(0);
  //   return new FieldUpdateDeserializer(innerType);
  // }

  /**
   * Called when the property is completely absent (undefined) in incoming JSON. Return an
   * "unchanged" marker so caller can detect that the field was not provided.
   */
  @Override
  public Object getAbsentValue(DeserializationContext ctxt) throws JacksonException {
    // <-- adjust this if your FieldUpdate API uses a different name for the unchanged marker
    return FieldUpdate.unchanged();
  }

  /**
   * Called when JSON literal `null` is encountered for the property. We treat it as an explicit
   * update to `null`.
   */
  @Override
  public Object getNullValue(DeserializationContext ctxt) throws JacksonException {
    return FieldUpdate.of(null);
  }

  @Override
  public FieldUpdate<?> deserialize(JsonParser p, DeserializationContext ctxt)
      throws JacksonException {

    JsonToken token = p.currentToken();

    // explicit JSON null: treat as Update(null)
    if (token == JsonToken.VALUE_NULL) {
      return FieldUpdate.of(null);
    }

    // normal value: read typed value and wrap into Update(value)
    Object value = ctxt.readValue(p, valueType);
    return FieldUpdate.of(value);
  }

  private JavaType wrapRecursively(DeserializationContext ctxt, JavaType type) {

    var factory = ctxt.getTypeFactory();

    // 1️⃣ Map<K, V>
    if (type.isMapLikeType()) {

      JavaType keyType = type.getKeyType();
      JavaType valueType = type.getContentType();

      // Đệ quy xuống value
      JavaType wrappedValueType = wrapRecursively(ctxt, valueType);

      // Nếu value chưa phải FieldUpdate thì wrap nó
      if (!wrappedValueType.hasRawClass(FieldUpdate.class)) {
        wrappedValueType = factory.constructParametricType(FieldUpdate.class, wrappedValueType);
      }

      return factory.constructMapType(
          (Class<? extends Map>) type.getRawClass(), keyType, wrappedValueType);
    }

    // 2️⃣ Collection / List
    if (type.isCollectionLikeType()) {

      JavaType contentType = type.getContentType();

      JavaType wrappedContent = wrapRecursively(ctxt, contentType);

      return factory.constructCollectionType(
          (Class<? extends Collection>) type.getRawClass(), wrappedContent);
    }

    // 3️⃣ Nếu đã là FieldUpdate thì giữ nguyên
    if (type.hasRawClass(FieldUpdate.class)) {
      return type;
    }

    // 4️⃣ Base case: return nguyên type (sẽ được wrap ở level trên)
    return type;
  }

  @Override
  public ValueDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {

    JavaType type = (property != null) ? property.getType() : ctxt.getContextualType();

    if (type == null || !type.hasRawClass(FieldUpdate.class)) {
      return this;
    }

    JavaType innerType = type.containedType(0);

    if (innerType != null) {
      innerType = wrapRecursively(ctxt, innerType);
    }

    return new FieldUpdateDeserializer(innerType);
  }
}
