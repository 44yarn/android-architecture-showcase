plugins {
    id("showcase.convention.app")
    id("showcase.primitive.compose")
    id("showcase.primitive.navigation")
    id("showcase.primitive.hilt")
    id("showcase.primitive.logging")
    id("showcase.primitive.unit-test")
}

android {
    namespace = "io.github.yarn44.showcase"

    defaultConfig {
        applicationId = "io.github.yarn44.showcase"
    }
}

dependencies {
    implementation(project(":core:foundation"))
    implementation(project(":core:ui"))
    implementation(project(":core:data"))
    implementation(project(":feature:home"))
    implementation(project(":feature:info"))
    implementation(project(":feature:login"))
}
