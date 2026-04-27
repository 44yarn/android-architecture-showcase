package showcase.convention

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import showcase.config.AndroidConfig
import showcase.util.androidApplication
import showcase.util.libs
import showcase.util.library

/**
 * Base convention for the application module (`com.android.application`).
 * Configures the Android Application fundamentals + ktlint.
 *
 * Compose, Hilt, navigation, etc. are opt-in and applied explicitly by each
 * module via the corresponding `showcase.primitive.*` plugin IDs.
 */
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

// Cross-module ktlint applied at runtime: precompiled scripts cannot list
// own-module sibling scripts in their `plugins { ... }` block.
pluginManager.apply("showcase.primitive.ktlint")

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
    // KT-73255: apply annotations like @StringRes / @DrawableRes / @ApplicationContext
    // to both the constructor parameter and the property/field, silencing Lint warnings
    // about resource ID annotations on `val` constructor params.
    compilerOptions.freeCompilerArgs.add("-Xannotation-default-target=param-property")
}

dependencies {
    add("implementation", libs.library("activityCompose"))
    add("implementation", libs.library("androidxCoreKtx"))
}
