package com.inlaco.crewmgrservice.shared.support;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

@Slf4j
public class ConsoleUtils {

  private static final ObjectMapper OBJECT_MAPPER =
      JsonMapper.builder()
          // .changeDefaultPropertyInclusion(
          //     incl -> incl.withValueInclusion(JsonInclude.Include.NON_NULL))
          // .changeDefaultPropertyInclusion(
          //     incl -> incl.withContentInclusion(JsonInclude.Include.NON_NULL))
          // .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
          // .changeDefaultVisibility(
          //     vc ->
          //         vc.withFieldVisibility(JsonAutoDetect.Visibility.ANY)
          //             .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
          //             .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE))
          .build();

  // static {
  //   objectMapper.registerModule(new JavaTimeModule());

  //   //
  // https://stackoverflow.com/questions/45662820/how-to-set-format-of-string-for-java-time-instant-using-objectmapper
  //   objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
  // }

  @SneakyThrows
  public static void print(Object... objects) {
    System.out.println("------------------ CONSOLE_UTILS ------------------");

    if (objects == null || objects.length == 0) {
      System.out.println("No object to log: null");
      return;
    }

    for (Object object : objects) {
      if (object == null) {
        System.out.println("------------------ Type: " + null + " ------------------");
        System.out.println("null");
      } else {
        System.out.println(
            "------------------ Type: "
                + object.getClass().getSimpleName()
                + " ------------------");
        System.out.println(
            OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object));
      }
    }
  }
}
