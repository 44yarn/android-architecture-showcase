package io.github.yarn44.showcase

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class ModulePlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        with(pluginManager) {
            apply("com.android.library")
            apply("org.jetbrains.kotlin.android")
        }

        extensions.configure<LibraryExtension> {
            compileSdk = AndroidConfig.compileSdk

            defaultConfig {
                minSdk = AndroidConfig.minSdk
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            compileOptions {
                sourceCompatibility = AndroidConfig.javaVersion
                targetCompatibility = AndroidConfig.javaVersion
            }
        }

        tasks.withType<KotlinCompile>().configureEach {
            compilerOptions.jvmTarget.set(AndroidConfig.jvmTarget)
        }

        // KtlintPlugin is applied here (not in build.gradle.kts) because code style
        // enforcement is mandatory for all modules, unlike optional feature plugins.
        pluginManager.apply(KtlintPlugin::class.java)

        dependencies {
            add("implementation", libs.library("androidxCoreKtx"))
            add("implementation", libs.library("kotlinxCoroutinesCore"))
        }
    }
}
