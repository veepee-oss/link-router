pluginManagement {
    repositories {
        google()
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://registry.vptech.eu/artifactory/offer-discovery-android-maven/") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://registry.vptech.eu/artifactory/offer-discovery-android-maven/") }
    }
}

rootProject.name = "Link-Router"
include(":library")
