plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
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
}

// The Kotlin behind this build's own scripts, shared with gradle-conventions so the two can't
// drift. Precompiled scripts are only picked up from src/main/kotlin, hence the small copies.
sourceSets.main { kotlin.srcDir("../gradle-conventions/src/shared/kotlin") }

dependencies {
    implementation(libs.stratastax.style.plugin)
}
