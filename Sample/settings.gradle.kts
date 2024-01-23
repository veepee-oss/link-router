rootProject.name = "sample"
pluginManagement {
    repositories {
        google()
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://registry.vptech.eu/artifactory/offer-discovery-android-maven/") }
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "shot") {
                useModule("com.karumi:shot:${requested.version}")
            }
        }
    }

}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenLocal()
        mavenCentral()
        maven { url = uri("https://registry.vptech.eu/artifactory/offer-discovery-android-maven/") }
        // detekt gitlab integration
        maven { url = uri("https://gitlab.com/api/v4/projects/25796063/packages/maven") }
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
