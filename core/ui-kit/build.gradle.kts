plugins {
    id("showcase.ModulePlugin")
    id("showcase.ComposePlugin")
    id("showcase.HiltPlugin")
    id("showcase.UnitTestPlugin")
}

android {
    namespace = "io.github.yarn44.showcase.core.uikit"
}

dependencies {
    implementation(project(":core:foundation"))
}
