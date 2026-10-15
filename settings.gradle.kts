pluginManagement {
    repositories {
        mavenLocal()
        maven("https://maven.pkg.github.com/buijs-dev/*") {
            credentials {
                username = providers.gradleProperty("gpr.user")
                    .orElse(providers.environmentVariable("GITHUB_ACTOR"))
                    .orNull
                password = providers.gradleProperty("gpr.key")
                    .orElse(providers.environmentVariable("GITHUB_TOKEN"))
                    .orNull
            }
            mavenContent {
                releasesOnly()
                includeGroupByRegex("dev\\.buijs.*")
            }
        }
        maven("https://repo.repsy.io/mvn/buijs-dev/maven") {
            mavenContent {
                snapshotsOnly()
                includeGroupByRegex("dev\\.buijs.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

// The conventions can't build themselves: gradle-build compiles a few of their own scripts
// (repositories, publishing, formatting) straight from gradle-conventions/src.
includeBuild("gradle-build")

dependencyResolutionManagement {
    versionCatalogs { create("libs") { from(files("libs.versions.toml")) } }
}

include(":gradle-catalog")
include(":gradle-conventions")
include(":gradle-conventions-quarkus")
