plugins {
    java
    `java-library`
    `maven-publish`
    id("io.github.goooler.shadow") version "8.1.7"
    jacoco
    alias(libs.plugins.publishdata)
}

group = "de.icevizion.lib"
version = "1.4.4"
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
    implementation(libs.strigiformes)
    implementation(platform(libs.microtus.bom))
    compileOnly(libs.microtus.core)

    testImplementation(libs.microtus.core)
    testImplementation(libs.microtus.test)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.junit)
    testRuntimeOnly(libs.junit.jupiter.engine)
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(21)
    }

    jar {
        dependsOn("shadowJar")
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
    addBuildData()
    useGitlabReposForProject("16", "https://gitlab.themeinerlp.dev/")
}
publishing {
    publications {
        create<MavenPublication>("maven") {
            publishData.configurePublication(this)
        }
    }
    repositories {
        maven {
            credentials(HttpHeaderCredentials::class) {
                name = "Job-Token"
                value = System.getenv("CI_JOB_TOKEN")
            }
            authentication {
                create("header", HttpHeaderAuthentication::class)
            }

            name = "Gitlab"
            // Get the detected repository from the publish data
            url = uri(publishData.getRepository())
        }
    }

}
