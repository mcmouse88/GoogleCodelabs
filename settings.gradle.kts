@file:Suppress("UnstableApiUsage")

include()

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
    ":macro-benchmark",
    ":macro-benchmark:main-app"
)
