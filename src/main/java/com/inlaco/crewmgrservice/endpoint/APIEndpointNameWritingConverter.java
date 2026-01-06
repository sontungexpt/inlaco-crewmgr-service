package com.inlaco.crewmgrservice.endpoint;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class APIEndpointNameWritingConverter
    implements Converter<APIEndpointName, APIEndpointCode> {

  @Override
  public APIEndpointCode convert(APIEndpointName source) {
    return source.getCode();
  }
}
