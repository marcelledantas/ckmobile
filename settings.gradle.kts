pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven {
            url = uri("https://bitbucket.org/endler/contextnet-dependencies/raw/master")
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://bitbucket.org/endler/contextnet-dependencies/raw/master")
        }
    }
}

rootProject.name = "sefaz-mobile"
include(":app")
