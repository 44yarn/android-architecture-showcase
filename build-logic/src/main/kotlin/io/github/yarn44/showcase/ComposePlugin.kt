package io.github.yarn44.showcase

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class ComposePlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        with(pluginManager) {
            apply("org.jetbrains.kotlin.plugin.compose")
        }

        android {
            buildFeatures.compose = true
        }

        dependencies {
            add("implementation", platform(libs.library("composeBom")))
            add("implementation", libs.library("composeAnimation"))
            add("implementation", libs.library("composeMaterial3"))
            add("implementation", libs.library("composeMaterialIconsExtended"))
            add("implementation", libs.library("composeUi"))
            add("implementation", libs.library("composeUiToolingPreview"))
            add("implementation", libs.library("composeUiUtil"))
            add("implementation", libs.library("composeFoundation"))
            add("implementation", libs.library("lifecycleRuntimeCompose"))
            add("implementation", libs.library("lifecycleViewmodelCompose"))
            add("implementation", libs.library("navigationCompose"))
            add("debugImplementation", libs.library("composeUiTooling"))
        }
    }
}
