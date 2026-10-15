// A published Gradle plugin module; `java-gradle-plugin` brings its own `pluginMaven` publication.
plugins {
    id("stratastax.kotlin")
    id("stratastax.repositories")
    id("stratastax.publishing")
    id("stratastax.formatting")
    id("stratastax.coverage")
    id("stratastax.dokka")
    `java-gradle-plugin`
}
