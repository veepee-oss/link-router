rootProject.name = "sample"
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
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

