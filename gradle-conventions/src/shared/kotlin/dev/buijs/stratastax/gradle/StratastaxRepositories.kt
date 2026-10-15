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

import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.provider.ProviderFactory

/**
 * The repositories every stratastax build resolves from: the public repositories, Maven Local and
 * the buijs-dev repositories, which only serve `dev.buijs` artifacts. Releases come from GitHub
 * Packages (`gpr.user`/`gpr.key` or `GITHUB_ACTOR`/`GITHUB_TOKEN`), snapshots from Repsy.
 */
fun RepositoryHandler.stratastax(providers: ProviderFactory) {
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()
    maven {
        name = "GitHubPackages"
        setUrl("https://maven.pkg.github.com/buijs-dev/*")
        credentials {
            username =
                providers
                    .gradleProperty("gpr.user")
                    .orElse(providers.environmentVariable("GITHUB_ACTOR"))
                    .orNull
            password =
                providers
                    .gradleProperty("gpr.key")
                    .orElse(providers.environmentVariable("GITHUB_TOKEN"))
                    .orNull
        }

        mavenContent {
            releasesOnly()
            includeGroupByRegex("dev\\.buijs.*")
        }
    }

    maven {
        name = "Repsy"
        setUrl("https://repo.repsy.io/mvn/buijs-dev/maven")
        mavenContent {
            snapshotsOnly()
            includeGroupByRegex("dev\\.buijs.*")
        }
    }
}
