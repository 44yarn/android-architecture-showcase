plugins {
    id("showcase.AppPlugin")
    id("showcase.ComposePlugin")
    id("showcase.HiltPlugin")
    id("showcase.LoggingPlugin")
    id("showcase.UnitTestPlugin")
}

android {
    namespace = "io.github.yarn44.showcase"

    defaultConfig {
        applicationId = "io.github.yarn44.showcase"
    }
}

dependencies {
    implementation(project(":core:foundation"))
    implementation(project(":core:ui-kit"))
    implementation(project(":core:data"))
    implementation(project(":feature:login"))
}
