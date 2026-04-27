package showcase.convention

import com.android.build.gradle.LibraryExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import showcase.config.AndroidConfig
import showcase.util.libs
import showcase.util.library

/**
 * Base convention for library modules (`com.android.library`).
 * Configures Android Library fundamentals + ktlint + the always-on
 * `kotlinx-coroutines-core` dependency that every module relies on.
 */
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

// Cross-module ktlint applied at runtime: precompiled scripts cannot list
// own-module sibling scripts in their `plugins { ... }` block.
pluginManager.apply("showcase.primitive.ktlint")

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
    // KT-73255: apply annotations like @StringRes / @DrawableRes / @ApplicationContext
    // to both the constructor parameter and the property/field, silencing Lint warnings
    // about resource ID annotations on `val` constructor params.
    compilerOptions.freeCompilerArgs.add("-Xannotation-default-target=param-property")
}

dependencies {
    add("implementation", libs.library("androidxCoreKtx"))
    add("implementation", libs.library("kotlinxCoroutinesCore"))
}
