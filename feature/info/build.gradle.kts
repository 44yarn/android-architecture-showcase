plugins {
    id("showcase.ModulePlugin")
    id("showcase.ComposePlugin")
    id("showcase.HiltPlugin")
    id("showcase.UnitTestPlugin")
}

android {
    namespace = "io.github.yarn44.showcase.feature.info"
}

dependencies {
    implementation(project(":core:foundation"))
    implementation(project(":core:ui-kit"))
}
