package showcase.primitive

import showcase.util.libs
import showcase.util.library
import showcase.util.version

plugins {
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

dependencies {
    add("implementation", libs.library("daggerHiltCore"))
    add("implementation", libs.library("androidxHiltNavCompose"))
    add("ksp", libs.library("daggerHiltCompiler"))
    add("ksp", libs.library("androidxHiltCompiler"))
    // Kotlin 2.3.x workaround: Hilt needs updated kotlin-metadata-jvm
    add("ksp", "org.jetbrains.kotlin:kotlin-metadata-jvm:${libs.version("kotlin")}")
}
