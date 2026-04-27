package showcase.primitive

import showcase.util.libs
import showcase.util.library

plugins {
    id("org.jetbrains.kotlin.plugin.serialization")
}

dependencies {
    add("implementation", libs.library("kotlinxSerializationJson"))
}
