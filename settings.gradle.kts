@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "GoogleCodelab"
include(
    ":android-accessibility",
    ":kotlin-coroutines",
    ":coroutine-advanced",
    ":coroutine-advanced:sunflower",
    ":jetpack-compose-basic",
    ":theming-compose"
)
