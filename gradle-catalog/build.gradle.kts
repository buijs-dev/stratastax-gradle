plugins {
    `version-catalog`
    id("stratastax.publishing")
}

description = "The version catalog shared by all stratastax builds"

catalog {
    versionCatalog { from(files("../libs.versions.toml")) }
}

publishing {
    publications.create<MavenPublication>("mavenCatalog") { from(components["versionCatalog"]) }
}
