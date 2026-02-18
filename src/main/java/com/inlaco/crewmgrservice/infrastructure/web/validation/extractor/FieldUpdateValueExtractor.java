package com.inlaco.crewmgrservice.infrastructure.web.validation.extractor;

import com.inlaco.crewmgrservice.shared.application.model.FieldUpdate;
import jakarta.validation.valueextraction.ExtractedValue;
import jakarta.validation.valueextraction.UnwrapByDefault;
import jakarta.validation.valueextraction.ValueExtractor;

@UnwrapByDefault
public class FieldUpdateValueExtractor implements ValueExtractor<FieldUpdate<@ExtractedValue ?>> {

  @Override
  public void extractValues(FieldUpdate<?> originalValue, ValueReceiver receiver) {
    if (originalValue instanceof FieldUpdate.Updated<?> updated) {
      receiver.value(null, updated.value());
    }
  }
}
