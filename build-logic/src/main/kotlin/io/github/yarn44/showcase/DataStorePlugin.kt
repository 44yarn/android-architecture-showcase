package io.github.yarn44.showcase

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Provides AndroidX DataStore Preferences dependency.
 */
class DataStorePlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        dependencies {
            add("implementation", libs.library("datastorePreferences"))
        }
    }
}
