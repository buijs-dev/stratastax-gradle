# Stratastax Gradle

[![](https://img.shields.io/badge/Buijs-Software-blue)](https://buijs.dev/)
[![GitHub](https://img.shields.io/github/license/buijs-dev/stratastax-gradle?color=black)](https://github.com/buijs-dev/stratastax-gradle/blob/main/LICENSE)

The build logic shared by all stratastax repositories: convention plugins and a version catalog,
published like any other stratastax artifact (releases to GitHub Packages, snapshots to Repsy).

## Usage
In `settings.gradle.kts` (the `pluginManagement` repositories are needed to find this plugin):

```kotlin
pluginManagement {
    repositories {
        mavenLocal()
        maven("https://maven.pkg.github.com/buijs-dev/*") { /* gpr.user / gpr.key */ }
        maven("https://repo.repsy.io/mvn/buijs-dev/maven")
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("dev.buijs.stratastax.settings") version "1.0.0"
    // or, for builds with Quarkus modules:
    // id("dev.buijs.stratastax.settings.quarkus") version "1.0.0"
}
```

This registers the stratastax repositories, the shared `libs` catalog (`gradle-catalog`, same
version as the plugin) and, when `stratastax.versions.toml` exists in the project root, a
`stratastax` catalog for the versions of stratastax modules. Remove `gradle/libs.versions.toml`:
Gradle would import it as `libs` too, which clashes with the shared one.

The convention plugins are then on the classpath; apply them without a version:

| Plugin | What it does |
|---|---|
| `stratastax.library` | A published Kotlin library: all of the below plus `java-library` |
| `stratastax.gradle-plugin` | A published Gradle plugin: all of the below plus `java-gradle-plugin` |
| `stratastax.kotlin` | Kotlin JVM on Java 25, sources and javadoc jars, JUnit Platform |
| `stratastax.repositories` | The stratastax repositories |
| `stratastax.publishing` | POM defaults and the GitHub Packages / Repsy repositories |
| `stratastax.formatting` | The stratastax style with the license header |
| `stratastax.coverage` | Kover on JaCoCo, XML and HTML reports on `check` |
| `stratastax.dokka` | Dokka HTML with source links to GitHub |
| `stratastax.quarkus`, `stratastax.quarkus-extension` | Quarkus (`settings.quarkus` only) |

`stratastax.publishing` and `stratastax.dokka` read `stratastax.repository` and
`stratastax.displayName` from `gradle.properties`.

## Building
`gradle-build` bootstraps this build with the repositories, publishing and formatting scripts; their
Kotlin lives in `gradle-conventions/src/shared/kotlin`, compiled into both. Test changes locally
with `./gradlew publishToMavenLocal` and a `-SNAPSHOT` version in the consuming build.
