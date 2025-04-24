plugins {
    java
    `java-library`
    `maven-publish`
    jacoco
    alias(libs.plugins.publishdata)
}

group = "net.theevilreaper.aves"
version = "1.6.1"
description = "Aves"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations.all {
    resolutionStrategy.cacheDynamicVersionsFor(1, "minutes")
}

dependencies {
    implementation(platform(libs.bom.base))
    implementation(platform(libs.microtus.bom))
    compileOnly(libs.minestom)

    testImplementation(platform(libs.bom.base))
    testImplementation(libs.minestom)
    testImplementation(libs.minestom.test)
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.jupiter.engine)
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(21)
    }

    jacocoTestReport {
        dependsOn(rootProject.tasks.test)
        reports {
            xml.required.set(true)
            csv.required.set(true)
        }
    }

    test {
        finalizedBy(rootProject.tasks.jacocoTestReport)
        useJUnitPlatform()
        jvmArgs("-Dminestom.inside-test=true")
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}

publishData {
    addMainRepo("https://repo.onelitefeather.dev/onelitefeather-releases")
    addMasterRepo("https://repo.onelitefeather.dev/onelitefeather-releases")
    addSnapshotRepo("https://repo.onelitefeather.dev/onelitefeather-snapshots")
    publishTask("jar")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            // configure the publication as defined previously.
           publishData.configurePublication(this)
           version = publishData.getVersion(false)
        }
    }
    repositories {
        maven {
            authentication {
                credentials(PasswordCredentials::class) {
                    // Those credentials need to be set under "Settings -> Secrets -> Actions" in your repository
                    username = System.getenv("ONELITEFEATHER_MAVEN_USERNAME")
                    password = System.getenv("ONELITEFEATHER_MAVEN_PASSWORD")
                }
            }

            name = "OneLiteFeatherRepository"
            // Get the detected repository from the publish data
            url = uri(publishData.getRepository())
        }
    }
}
