import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.androidGradlePlugin)
    implementation(libs.kotlinGradlePlugin)
    implementation(libs.kspGradlePlugin)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

gradlePlugin {
    plugins {
        val packageName = "io.github.yarn44.showcase"

        listOf(
            "AppPlugin",
            "ComposePlugin",
            "DataStorePlugin",
            "HiltPlugin",
            "LoggingPlugin",
            "ModulePlugin",
            "UnitTestPlugin",
        ).forEach { className ->
            register(className) {
                id = "showcase.$className"
                implementationClass = "$packageName.$className"
            }
        }
    }
}
