package io.github.yarn44.showcase

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.JavaExec
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.register

/**
 * Convention Plugin that registers ktlintCheck and ktlintFormat tasks.
 * Applied by ModulePlugin and AppPlugin so every module gets ktlint support.
 */
class KtlintPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = with(project) {
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
    }
}
