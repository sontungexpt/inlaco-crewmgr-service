package com.inlaco.crewmgrservice.endpoint;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@Component
@ReadingConverter
public class APIEndpointNameCodeReadingConverter
    implements Converter<APIEndpointCode, APIEndpointName> {

  @Override
  public APIEndpointName convert(APIEndpointCode source) {
    return APIEndpointName.fromCode(source);
  }
}
