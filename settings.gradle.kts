rootProject.name = "aves"
pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://eldonexus.de/repository/maven-public/")
    }
}
dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        maven {
            name = "OneLiteFeatherRepository"
            url = uri("https://repo.onelitefeather.dev/onelitefeather")
            if (System.getenv("CI") != null) {
                credentials {
                    username = System.getenv("ONELITEFEATHER_MAVEN_USERNAME")
                    password = System.getenv("ONELITEFEATHER_MAVEN_PASSWORD")
                }
            } else {
                credentials(PasswordCredentials::class)
                authentication {
                    create<BasicAuthentication>("basic")
                }
            }
        }
    }
    versionCatalogs {
        create("libs") {
            version("minestom", "a1d1920a04")
            version("cyano", "0.1.0")
            version("junit", "5.12.2")
            version("microtus","1.5.1")
            version("bom", "1.1.2")
            version("publishdata", "1.4.0")
            library("bom.base", "net.theevilreaper.mycelium.bom", "mycelium-bom").versionRef("bom")
            library("microtus-bom", "net.onelitefeather.microtus", "bom").versionRef("microtus")
            library("minestom","net.minestom", "minestom-snapshots").versionRef("minestom")
            //library("minestom", "net.onelitefeather.microtus", "Microtus").withoutVersion()
            library("cyano", "net.onelitefeather.cyano", "cyano").versionRef("cyano")
            //library("minestom-test", "net.onelitefeather.microtus.testing", "testing").withoutVersion()
            library("junit-jupiter", "org.junit.jupiter", "junit-jupiter").versionRef("junit")
            library("junit-jupiter-engine", "org.junit.jupiter", "junit-jupiter-engine").versionRef("junit")
            library("junit.platform.launcher", "org.junit.platform", "junit-platform-launcher").version("1.12.2")
            plugin("publishdata", "de.chojo.publishdata").versionRef("publishdata")
        }
    }
}
