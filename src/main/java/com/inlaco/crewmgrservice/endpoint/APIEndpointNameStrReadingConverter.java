package com.inlaco.crewmgrservice.endpoint;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@Component
@ReadingConverter
public class APIEndpointNameStrReadingConverter implements Converter<String, APIEndpointName> {

  @Override
  public APIEndpointName convert(String source) {
    return APIEndpointName.fromCode(source);
  }
}
