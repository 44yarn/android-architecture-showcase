plugins {
    id("showcase.convention.module")
    id("showcase.primitive.datastore")
    id("showcase.primitive.hilt")
    id("showcase.primitive.unit-test")
}

android {
    namespace = "io.github.yarn44.showcase.core.data"
}

dependencies {
    implementation(project(":core:foundation"))
}
