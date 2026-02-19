package com.inlaco.crewmgrservice.infrastructure.web.validation.extractor;

import com.inlaco.crewmgrservice.shared.application.model.Patch;
import jakarta.validation.valueextraction.ExtractedValue;
import jakarta.validation.valueextraction.UnwrapByDefault;
import jakarta.validation.valueextraction.ValueExtractor;

@UnwrapByDefault
public class PatchValueExtractor implements ValueExtractor<Patch<@ExtractedValue ?>> {

  @Override
  public void extractValues(Patch<?> originalValue, ValueReceiver receiver) {
    if (originalValue == null) return;

    // Only validate if patch is updated
    if (originalValue.isUpdated()) {
      receiver.value(null, ((Patch.Updated) originalValue).value());
    }
  }
}
