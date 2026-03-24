plugins {
    id("showcase.ModulePlugin")
    id("showcase.DataStorePlugin")
    id("showcase.HiltPlugin")
    id("showcase.UnitTestPlugin")
}

android {
    namespace = "io.github.yarn44.showcase.core.data"
}

dependencies {
    implementation(project(":core:foundation"))
}
