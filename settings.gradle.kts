pluginManagement {
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
        mavenCentral()
    }
    
}
rootProject.name = "zipher"

//enableFeaturePreview("GRADLE_METADATA")
include(":androidApp")
include(":shared")

