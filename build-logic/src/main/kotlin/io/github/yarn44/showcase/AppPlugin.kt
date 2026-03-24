package io.github.yarn44.showcase

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Base plugin for the app module (com.android.application).
 * Only configures Android Application fundamentals.
 * Compose, Hilt, UnitTest etc. should be applied explicitly in build.gradle.kts.
 */
class AppPlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        with(pluginManager) {
            apply("com.android.application")
            apply("org.jetbrains.kotlin.android")
        }

        androidApplication {
            compileSdk = AndroidConfig.compileSdk

            defaultConfig {
                minSdk = AndroidConfig.minSdk
                targetSdk = AndroidConfig.targetSdk
                versionCode = AndroidConfig.versionCode
                versionName = AndroidConfig.versionName
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            compileOptions {
                sourceCompatibility = AndroidConfig.javaVersion
                targetCompatibility = AndroidConfig.javaVersion
            }

            buildFeatures {
                buildConfig = true
            }
        }

        tasks.withType<KotlinCompile>().configureEach {
            compilerOptions.jvmTarget.set(AndroidConfig.jvmTarget)
        }

        dependencies {
            add("implementation", libs.library("activityCompose"))
            add("implementation", libs.library("androidxCoreKtx"))
        }
    }
}
