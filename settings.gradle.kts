rootProject.name = "aves"
pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://eldonexus.de/repository/maven-public/")
    }
}
dependencyResolutionManagement {
    if (System.getenv("CI") != null) {
        repositoriesMode = RepositoriesMode.PREFER_SETTINGS
        repositories {
            maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            maven("https://repo.htl-md.schule/repository/Gitlab-Runner/")
            maven {
                val groupdId = 28 // Gitlab Group
                val ciApiv4Url = System.getenv("CI_API_V4_URL")
                url = uri("$ciApiv4Url/groups/$groupdId/-/packages/maven")
                name = "GitLab"
                credentials(HttpHeaderCredentials::class.java) {
                    name = "Job-Token"
                    value = System.getenv("CI_JOB_TOKEN")
                }
                authentication {
                    create<HttpHeaderAuthentication>("header")
                }
            }
        }
    } else {
        repositories {
            maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            maven {
                val groupdId = 28 // Gitlab Group
                url = uri("https://gitlab.onelitefeather.dev/api/v4/groups/$groupdId/-/packages/maven")
                name = "GitLab"
                credentials(HttpHeaderCredentials::class.java) {
                    name = "Private-Token"
                    value = providers.gradleProperty("gitLabPrivateToken").get()
                }
                authentication {
                    create<HttpHeaderAuthentication>("header")
                }
            }
            maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            mavenCentral()
            maven("https://jitpack.io")
        }
    }
    versionCatalogs {
        create("libs") {
            version("microtus","1.5.1")
            version("bom", "1.1.1")
            version("publishdata", "1.4.0")
            library("bom.base", "net.theevilreaper.dungeon.bom", "base").versionRef("bom")
            library("microtus-bom", "net.onelitefeather.microtus", "bom").versionRef("microtus")
            library("minestom", "net.onelitefeather.microtus", "Microtus").withoutVersion()
            library("minestom-test", "net.onelitefeather.microtus.testing", "testing").withoutVersion()
            library("junit-jupiter", "org.junit.jupiter", "junit-jupiter").withoutVersion()
            library("junit-jupiter-engine", "org.junit.jupiter", "junit-jupiter-engine").withoutVersion()
            library("mockito-core", "org.mockito", "mockito-core").withoutVersion()
            library("mockito-junit", "org.mockito", "mockito-junit-jupiter").withoutVersion()

            plugin("publishdata", "de.chojo.publishdata").versionRef("publishdata")
        }
    }
}
