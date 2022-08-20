plugins {
    java
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.sonarqube") version "3.4.0.2513"
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

val minestomVersion = "master-SNAPSHOT"
val strigiVersion = "e89dd8352c"

dependencies {
    implementation("com.github.PatrickZdarsky:Strigiformes:$strigiVersion")

    compileOnly("com.github.Minestom:Minestom:$minestomVersion")

    testImplementation("com.github.Minestom:Minestom:$minestomVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
    testImplementation("org.mockito:mockito-core:4.6.1");
    testImplementation("org.mockito:mockito-junit-jupiter:4.6.1");
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

    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
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
        property("sonar.projectKey", "dungeon_aves_AYINRMy8pSUXqILAYb0z")
    }
}
