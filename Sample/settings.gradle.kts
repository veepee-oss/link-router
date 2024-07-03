rootProject.name = "sample"
pluginManagement {
   // includeBuild("../../gradle-plugins")
   // includeBuild("../../gradle-plugins/settings")
    repositories {
        gradlePluginPortal()
        maven { url = uri("https://registry.vptech.eu/artifactory/offer-discovery-android-maven/") }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url = uri("https://registry.vptech.eu/artifactory/offer-discovery-android-maven/") }
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

include(":app")
include(":feature_a")
include(":feature_b")
include(":login")
include(":routes")
includeBuild("../") {
    dependencySubstitution {
        substitute(module("com.veepee.vpcore.link-router:link-router")).using(project(":library"))
    }
}


plugins {
    /**
     * NOTE: Gradle does not allow the usage of version catalogs inside settings.gradle.kts.
     * */
    id("com.veepee.settings") version "0.4.1"
}
