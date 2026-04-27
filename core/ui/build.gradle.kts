plugins {
    id("showcase.convention.module")
    id("showcase.primitive.compose")
    id("showcase.primitive.hilt")
    id("showcase.primitive.unit-test")
}

android {
    namespace = "io.github.yarn44.showcase.core.ui"
}

dependencies {
    implementation(project(":core:foundation"))
}
