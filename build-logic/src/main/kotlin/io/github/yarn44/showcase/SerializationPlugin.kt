package io.github.yarn44.showcase

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Provides kotlinx-serialization support.
 * Applies kotlin-serialization plugin and adds kotlinx-serialization-json.
 */
class SerializationPlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        with(pluginManager) {
            apply("org.jetbrains.kotlin.plugin.serialization")
        }

        dependencies {
            add("implementation", libs.library("kotlinxSerializationJson"))
        }
    }
}
