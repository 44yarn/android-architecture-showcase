package io.github.yarn44.showcase

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class HiltPlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        with(pluginManager) {
            apply("com.google.devtools.ksp")
            apply("com.google.dagger.hilt.android")
        }

        dependencies {
            add("implementation", libs.library("daggerHiltCore"))
            add("implementation", libs.library("androidxHiltNavCompose"))
            add("ksp", libs.library("daggerHiltCompiler"))
            add("ksp", libs.library("androidxHiltCompiler"))
            // Kotlin 2.3.x workaround: Hilt needs updated kotlin-metadata-jvm
            add("ksp", "org.jetbrains.kotlin:kotlin-metadata-jvm:${libs.version("kotlin")}")
        }
    }
}
