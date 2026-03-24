package io.github.yarn44.showcase

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class UnitTestPlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        dependencies {
            add("testImplementation", libs.library("junit"))
            add("testImplementation", libs.library("truth"))
            add("testImplementation", libs.library("mockk"))
            add("testImplementation", libs.library("kotlinxCoroutinesTest"))
            add("testImplementation", libs.library("turbine"))
        }
    }
}
