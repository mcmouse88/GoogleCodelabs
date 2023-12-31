@file:Suppress("UnstableApiUsage")

include(":testing-jetpack-compose")


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
    ":theming-compose",
    ":basic-layout-compose",
    ":jetpack-compose-state",
    ":incorporate_lifecycle_aware_components",
    ":animating-elements-in-jetpack-compose"
)
