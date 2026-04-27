package showcase.primitive

import showcase.util.libs
import showcase.util.library

/**
 * Registers `ktlintCheck` and `ktlintFormat` tasks.
 * Applied automatically by both convention plugins so every module gets ktlint.
 */
val ktlintConf = configurations.create("ktlint")

dependencies {
    add("ktlint", libs.library("ktlintCli"))
}

val jvmArgsList = listOf("--add-opens", "java.base/java.lang=ALL-UNNAMED")

tasks.register<JavaExec>("ktlintCheck") {
    group = "verification"
    description = "Check Kotlin code style."
    classpath = ktlintConf
    mainClass.set("com.pinterest.ktlint.Main")
    args = listOf("src/**/*.kt")
}

tasks.register<JavaExec>("ktlintFormat") {
    group = "formatting"
    description = "Fix Kotlin code style deviations."
    classpath = ktlintConf
    mainClass.set("com.pinterest.ktlint.Main")
    setJvmArgs(jvmArgsList)
    args = listOf("--format", "src/**/*.kt")
}
