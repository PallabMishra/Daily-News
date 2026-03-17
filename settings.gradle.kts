pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
    }
}

rootProject.name = "KMPNewsApp"

include(":composeApp")
include(":core:network")
include(":core:database")
include(":core:result")
include(":core:utils")
include(":core:designsystem")
include(":domain")
include(":data")
include(":feature:splash")
include(":feature:auth")
include(":feature:dashboard")
include(":feature:news")
include(":feature:map")
