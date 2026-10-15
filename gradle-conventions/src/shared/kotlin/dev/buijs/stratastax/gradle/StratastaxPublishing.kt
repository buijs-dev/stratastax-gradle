/**
 * Copyright (c) 2021 - 2026 Buijs Software
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package dev.buijs.stratastax.gradle

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication

/**
 * The defaults every stratastax publication shares. The version comes from `version` in
 * gradle.properties; the repository and display name from `stratastax.repository` and
 * `stratastax.displayName` there. A module only sets its own `description`.
 *
 * Release versions go to GitHub Packages, snapshots to Repsy. Credentials come from the
 * environment set by the buijs-dev/ci-templates publish workflows.
 */
fun Project.configureStratastaxPublishing() {
    group = "dev.buijs.stratastax"
    val repository = providers.gradleProperty("stratastax.repository").get()
    val displayName = providers.gradleProperty("stratastax.displayName").get()
    val githubUrl = "https://github.com/buijs-dev/$repository"
    val pomName = if (this == rootProject) displayName else "$displayName - $name"
    val pomDescription = provider { description }

    extensions.configure(PublishingExtension::class.java) {
        publications.withType(MavenPublication::class.java).configureEach {
            pom {
                name.convention(pomName)
                description.convention(pomDescription)
                url.convention(githubUrl)

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("$githubUrl/blob/main/LICENSE")
                    }
                }

                developers {
                    developer {
                        id.set("buijs-dev")
                        name.set("Gillian Buijs")
                        email.set("info@buijs.dev")
                    }
                }

                scm {
                    connection.set("scm:git:$githubUrl.git")
                    developerConnection.set(
                        "scm:git:ssh://git@github.com/buijs-dev/$repository.git",
                    )
                    url.set(githubUrl)
                }
            }
        }

        repositories {
            maven {
                name = "GitHubPackages"
                setUrl("https://maven.pkg.github.com/buijs-dev/$repository")
                credentials {
                    username = providers.environmentVariable("GITHUB_ACTOR").orNull
                    password = providers.environmentVariable("GITHUB_TOKEN").orNull
                }
            }

            maven {
                name = "Repsy"
                setUrl("https://repo.repsy.io/mvn/buijs-dev/maven")
                credentials {
                    username = providers.environmentVariable("REPSY_USERNAME").orNull
                    password = providers.environmentVariable("REPSY_PASSWORD").orNull
                }
            }
        }
    }
}
