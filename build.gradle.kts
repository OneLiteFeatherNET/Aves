import org.sonarqube.gradle.SonarQubeTask

plugins {
    java
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.sonarqube") version "3.4.0.2513"
    jacoco
}

group = "de.icevizion.lib"
version = "1.2.0-SNAPSHOT"
description = "Aves"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

val sonarKey = "dungeon_aves_AYINRMy8pSUXqILAYb0z"
val minestomVersion = "master-SNAPSHOT"
val strigiVersion = "e89dd8352c"

dependencies {
    implementation("com.github.PatrickZdarsky:Strigiformes:$strigiVersion")

    compileOnly("com.github.Minestom:Minestom:$minestomVersion")

    testImplementation("com.github.Minestom:Minestom:$minestomVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
    testImplementation("org.mockito:mockito-core:4.6.1")
    testImplementation("org.mockito:mockito-junit-jupiter:4.6.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
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

    getByName<SonarQubeTask>("sonarqube") {
        dependsOn(rootProject.tasks.test)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.properties["group"] as String?
            artifactId = project.name
            version = project.properties["version"] as String?
            from(components["java"])
        }
    }
}

sonarqube {
    properties {
        property("sonar.projectKey", sonarKey)
    }
}
