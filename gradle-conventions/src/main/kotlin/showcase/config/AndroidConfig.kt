package showcase.config

import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

@Suppress("ConstPropertyName")
object AndroidConfig {
    const val compileSdk: Int = 36
    const val minSdk: Int = 31
    const val targetSdk: Int = compileSdk

    const val versionCode: Int = 1
    const val versionName: String = "1.0.0"

    val javaVersion: JavaVersion = JavaVersion.VERSION_21
    val jvmTarget: JvmTarget = JvmTarget.JVM_21
}
