pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "claramente"
include(":app")
include(":core:model")
include(":core:network")
include(":core:auth")
include(":core:data")
include(":core:domain")
include(":core:designsystem")
include(":feature:session")
include(":feature:login")
include(":feature:hub")
include(":feature:profile")
include(":feature:ar")
include(":feature:classroom")
include(":feature:teacher")
