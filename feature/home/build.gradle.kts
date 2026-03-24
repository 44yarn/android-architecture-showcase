plugins {
    id("showcase.ModulePlugin")
    id("showcase.ComposePlugin")
    id("showcase.NavigationPlugin")
    id("showcase.HiltPlugin")
    id("showcase.LoggingPlugin")
    id("showcase.UnitTestPlugin")
}

android {
    namespace = "io.github.yarn44.showcase.feature.home"
}

dependencies {
    implementation(project(":core:foundation"))
    implementation(project(":core:ui-kit"))
    implementation(project(":core:data"))
}
