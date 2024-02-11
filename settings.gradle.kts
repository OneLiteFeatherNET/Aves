rootProject.name = "aves"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("minestom", "1.3.0")
            version("junit", "5.10.1")
            version("mockito", "5.9.0")
            library("strigiformes", "com.github.PatrickZdarsky", "Strigiformes").version("e89dd8352c")
            library("minestom", "net.onelitefeather.microtus", "Minestom").versionRef("minestom")
            library("minestom-test", "net.onelitefeather.microtus.testing", "testing").versionRef("minestom")
            library("junit-jupiter", "org.junit.jupiter", "junit-jupiter").versionRef("junit")
            library("junit-jupiter-engine", "org.junit.jupiter", "junit-jupiter-engine").versionRef("junit")
            library("mockito-core", "org.mockito", "mockito-core").versionRef("mockito")
            library("mockito-junit", "org.mockito", "mockito-junit-jupiter").versionRef("mockito")
        }
    }
}
