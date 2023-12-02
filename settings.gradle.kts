@file:Suppress("UnstableApiUsage")

include(":theming-compose")


include(":jetpack-compose-basic")


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
include(":android-accessibility")
include (":kotlin-coroutines")
include(":coroutine-advanced")
include(":coroutine-advanced:sunflower")
