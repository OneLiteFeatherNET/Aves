plugins {
    java
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.sonarqube") version "3.5.0.2730"
    jacoco
}

group = "de.icevizion.lib"
val baseVersion = "1.2.0"
description = "Aves"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

val sonarKey = "dungeon_aves_AYQjkAfDgiTSvWSTxrGx"
val minestomVersion = "master-SNAPSHOT"
val strigiVersion = "e89dd8352c"

dependencies {
    implementation("com.github.PatrickZdarsky:Strigiformes:$strigiVersion")

    compileOnly("com.github.Minestom:Minestom:$minestomVersion")

    testImplementation("com.github.Minestom:Minestom:$minestomVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testImplementation("org.mockito:mockito-core:5.0.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.0.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(17)
    }

    jar {
        dependsOn("shadowJar")
    }

    jacocoTestReport {
        dependsOn(rootProject.tasks.test)
        reports {
            xml.required.set(true)
        }
    }

    test {
        finalizedBy(rootProject.tasks.jacocoTestReport)
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    getByName("sonarqube") {
        dependsOn(rootProject.tasks.test)
    }
}

version = if (System.getenv().containsKey("CI")) {
    "${baseVersion}+${System.getenv("CI_COMMIT_SHORT_SHA")}"
} else {
    baseVersion
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
    if (System.getenv().containsKey("CI")) {
        repositories {
            maven {
                name = "GitLab"
                val ciApiv4Url = System.getenv("CI_API_V4_URL")
                val projectId = System.getenv("CI_PROJECT_ID")
                url = uri("$ciApiv4Url/projects/$projectId/packages/maven")
                credentials(HttpHeaderCredentials::class.java) {
                    name = "Job-Token"
                    value = System.getenv("CI_JOB_TOKEN")
                }
                authentication {
                    create<HttpHeaderAuthentication>("header")
                }
            }
        }
    }

}

sonarqube {
    properties {
        property("sonar.projectKey", sonarKey)
        property("sonar.qualitygate.wait", true)
    }
}
