rootProject.name = "aves"
pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://eldonexus.de/repository/maven-public/")
    }
}
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("microtus","1.4.2-SNAPSHOT")
            library("microtus-bom", "net.onelitefeather.microtus", "bom").versionRef("microtus")
            library("microtus-core", "net.onelitefeather.microtus", "Microtus").withoutVersion()
            library("microtus-test", "net.onelitefeather.microtus.testing", "testing").withoutVersion()

            version("junit", "5.10.2")
            version("mockito", "5.11.0")
            version("publishdata", "1.4.0")
            library("strigiformes", "com.github.PatrickZdarsky", "Strigiformes").version("e89dd8352c")
            library("junit-jupiter", "org.junit.jupiter", "junit-jupiter").versionRef("junit")
            library("junit-jupiter-engine", "org.junit.jupiter", "junit-jupiter-engine").versionRef("junit")
            library("mockito-core", "org.mockito", "mockito-core").versionRef("mockito")
            library("mockito-junit", "org.mockito", "mockito-junit-jupiter").versionRef("mockito")

            plugin("publishdata", "de.chojo.publishdata").versionRef("publishdata")
        }
    }
}
