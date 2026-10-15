plugins {
    `kotlin-dsl`
    id("stratastax.repositories")
    id("stratastax.publishing")
    id("stratastax.formatting")
}

description = "Convention plugins and the settings plugin shared by all stratastax builds"

dependencies {
    implementation(libs.kotlin.plugin.jvm)
    implementation(libs.kover.plugin)
    implementation(libs.dokka.plugin)
    implementation(libs.stratastax.style.plugin)
}

// Versions the plugins need at runtime: their own (for the catalog) and JaCoCo's.
val generateBuildConstants =
    tasks.register("generateBuildConstants") {
        description = "Generate build constants for the plugins"
        val output = layout.buildDirectory.dir("generated/constants")
        val version = project.version.toString()
        val jacoco = libs.versions.jacoco.get()
        inputs.property("version", version)
        inputs.property("jacoco", jacoco)
        outputs.dir(output)
        doLast {
            val file = output.get().file("dev/buijs/stratastax/gradle/StratastaxGradle.kt").asFile
            file.parentFile.mkdirs()
            file.writeText(
                """
                |package dev.buijs.stratastax.gradle
                |
                |internal object StratastaxGradle {
                |    const val VERSION = "$version"
                |    const val JACOCO_VERSION = "$jacoco"
                |}
                |
                """.trimMargin(),
            )
        }
    }

sourceSets.main {
    kotlin.srcDir("src/shared/kotlin")
    kotlin.srcDir(generateBuildConstants)
}
