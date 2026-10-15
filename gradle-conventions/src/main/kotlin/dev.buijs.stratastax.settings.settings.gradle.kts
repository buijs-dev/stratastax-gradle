// The entry point of a stratastax build, applied in settings.gradle.kts:
//     plugins { id("dev.buijs.stratastax.settings") version "<version>" }
// Puts the stratastax.* convention plugins on the build's classpath (apply them without a
// version), resolves dependencies from the stratastax repositories and registers the shared
// `libs` catalog in the same version as this plugin. Versions of stratastax modules belong in
// stratastax.versions.toml, registered as the `stratastax` catalog when present.
import dev.buijs.stratastax.gradle.StratastaxGradle
import dev.buijs.stratastax.gradle.stratastax

dependencyResolutionManagement {
    repositories { stratastax(providers) }

    versionCatalogs {
        create("libs") { from("dev.buijs.stratastax:gradle-catalog:${StratastaxGradle.VERSION}") }

        val local = layout.rootDirectory.file("stratastax.versions.toml")
        if (local.asFile.exists()) {
            create("stratastax") { from(layout.rootDirectory.files(local)) }
        }
    }
}
