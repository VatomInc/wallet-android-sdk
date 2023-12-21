pluginManagement {
    repositories {
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {setUrl("https://jitpack.io") }
        maven {setUrl("https://kotlin.bintray.com/kotlinx")}
    }
}

rootProject.name = "android-embed-sdk"
include(":app")
include(":wallet-embedded-sdk")
