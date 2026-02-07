// package com.inlaco.crewmgrservice.shared.template

// import java.net.URI
// import java.nio.charset.StandardCharsets
// import java.nio.file.Files
// import java.nio.file.Path
// import java.util.function.Function
// import java.util.regex.Matcher
// import org.springframework.core.io.ClassPathResource

// class TextTemplateBuilder private constructor(private val template: String) {

//   private val variables: MutableMap<String, String> = HashMap()
//   private var syntax: TemplateSyntax = TemplateSyntax.DOLLAR_CURLY

//   /* ---------- config ---------- */

//   fun syntax(syntax: TemplateSyntax): TextTemplateBuilder = apply { this.syntax = syntax }

//   fun `var`(key: String, value: String): TextTemplateBuilder = apply { variables[key] = value }

//   fun vars(map: Map<String, String>): TextTemplateBuilder = apply { variables.putAll(map) }

//   /* ---------- build ---------- */

//   /** Kotlin-native build */
//   fun build(): String = buildInternal { it }

//   /** Java-friendly build (Function<String,String>) */
//   fun build(keyFormatter: Function<String, String>): String = buildInternal {
//     keyFormatter.apply(it)
//   }

//   private fun buildInternal(keyFormatter: (String) -> String): String {
//     val matcher = syntax.pattern.matcher(template)
//     val result = StringBuffer()

//     while (matcher.find()) {
//       val backslashes = matcher.group(1)
//       val rawKey = matcher.group(2)
//       val defaultValue = matcher.group(3)

//       val key = keyFormatter(rawKey)
//       val value = variables[key] ?: defaultValue.orEmpty()
//       val slashCount = backslashes.length

//       if (slashCount % 2 == 1) {
//         matcher.appendReplacement(
//                 result,
//                 Matcher.quoteReplacement(backslashes.substring(1) + matcher.group(0))
//         )
//       } else {
//         matcher.appendReplacement(
//                 result,
//                 Matcher.quoteReplacement("\\".repeat(slashCount / 2) + value)
//         )
//       }
//     }

//     matcher.appendTail(result)
//     return result.toString()
//   }

//   /* ---------- factory ---------- */

//   companion object {

//     @JvmStatic fun content(content: String): TextTemplateBuilder = TextTemplateBuilder(content)

//     @JvmStatic
//     fun relativePath(path: String): TextTemplateBuilder =
//             TextTemplateBuilder(Files.readString(Path.of(path)))

//     @JvmStatic
//     fun src(uri: URI): TextTemplateBuilder = TextTemplateBuilder(Files.readString(Path.of(uri)))

//     @JvmStatic
//     fun resourcePath(resourcePath: String): TextTemplateBuilder {
//       val resource = ClassPathResource(resourcePath)
//       val content = String(resource.inputStream.readAllBytes(), StandardCharsets.UTF_8)
//       return TextTemplateBuilder(content)
//     }
//   }
// }
