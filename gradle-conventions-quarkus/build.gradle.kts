plugins {
    `kotlin-dsl`
    id("stratastax.repositories")
    id("stratastax.publishing")
    id("stratastax.formatting")
}

description = "The stratastax conventions plus the Quarkus convention plugins"

dependencies {
    api(project(":gradle-conventions"))
    implementation(libs.quarkus.plugin)
    implementation(libs.quarkus.extension.plugin)
}
