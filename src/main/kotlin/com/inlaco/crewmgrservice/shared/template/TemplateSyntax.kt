// package com.inlaco.crewmgrservice.shared.template

// import java.util.regex.Pattern

// enum class TemplateSyntax(pattern: String) {
//   DOLLAR_CURLY("""(\\*)\$\{([a-zA-Z0-9_]+)(?::([^}]+))?}"""),
//   DOUBLE_CURLY("""(\\*)\{\{([a-zA-Z0-9_]+)(?::([^}]+))?}}"""),
//   DOLLAR_SIMPLE("""(\\*)\$([a-zA-Z0-9_]+)"""),
//   DOUBLE_ANGLE("""(\\*)<<([a-zA-Z0-9_]+)(?::([^>]+))?>>""");
//   val pattern: Pattern = Pattern.compile(pattern)
// }
