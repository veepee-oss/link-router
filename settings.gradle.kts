pluginManagement {
    // includeBuild("../gradle-plugins")
    // includeBuild("../gradle-plugins/settings")
    repositories {
        gradlePluginPortal()
        maven { url = uri("https://registry.vptech.eu/artifactory/offer-discovery-android-maven/") }
    }
}
dependencyResolutionManagement {
    repositories {
        maven { url = uri("https://registry.vptech.eu/artifactory/offer-discovery-android-maven/") }
    }
}

rootProject.name = "Link-Router"
include(":library")

plugins {
    /**
     * NOTE: Gradle does not allow the usage of version catalogs inside settings.gradle.kts.
     * */
    id("com.veepee.settings") version "0.4.18"
}
