plugins {
    id("showcase.convention.module")
    id("showcase.primitive.compose")
    id("showcase.primitive.navigation")
    id("showcase.primitive.hilt")
    id("showcase.primitive.logging")
    id("showcase.primitive.unit-test")
}

android {
    namespace = "io.github.yarn44.showcase.feature.login"
}

dependencies {
    implementation(project(":core:foundation"))
    implementation(project(":core:ui-kit"))
    implementation(project(":core:data"))
}
