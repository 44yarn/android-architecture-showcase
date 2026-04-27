package showcase.primitive

import showcase.util.libs
import showcase.util.library

/**
 * Type-safe Navigation. Pulls in the serialization plugin for `@Serializable`
 * routes, plus hilt-navigation-compose for ViewModel access from NavBackStack.
 *
 * Note: `navigationCompose` itself is provided by `showcase.primitive.compose`.
 */
pluginManager.apply("showcase.primitive.serialization")

dependencies {
    add("implementation", libs.library("androidxHiltNavCompose"))
}
