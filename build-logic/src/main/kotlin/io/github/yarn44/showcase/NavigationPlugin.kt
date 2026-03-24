package io.github.yarn44.showcase

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Provides type-safe Navigation dependencies.
 * Applies kotlin-serialization plugin and adds
 * kotlinx-serialization-json and hilt-navigation-compose.
 *
 * Note: navigationCompose is already provided by ComposePlugin.
 */
class NavigationPlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        with(pluginManager) {
            apply("org.jetbrains.kotlin.plugin.serialization")
        }

        dependencies {
            add("implementation", libs.library("kotlinxSerializationJson"))
            add("implementation", libs.library("androidxHiltNavCompose"))
        }
    }
}
