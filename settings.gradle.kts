@file:Suppress("UnstableApiUsage")

include(":testing-basics")


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
