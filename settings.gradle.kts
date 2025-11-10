pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        maven(url = "https://jitpack.io")
        mavenCentral()
        gradlePluginPortal()
        maven (url = "https://maven.neshan.org/artifactory/public-maven" )

    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        maven(url = "https://jitpack.io")
        mavenCentral()

        maven (url = "https://maven.neshan.org/artifactory/public-maven" )



    }
}

rootProject.name = "Mazejoo"
include(":app")
 