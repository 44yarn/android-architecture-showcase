package io.github.yarn44.showcase

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Provides type-safe Navigation dependencies.
 * Applies SerializationPlugin (for @Serializable routes) and adds hilt-navigation-compose.
 *
 * Note: navigationCompose is already provided by ComposePlugin.
 */
class NavigationPlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        with(pluginManager) {
            apply("showcase.SerializationPlugin")
        }

        dependencies {
            add("implementation", libs.library("androidxHiltNavCompose"))
        }
    }
}
