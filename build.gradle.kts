plugins {
    `java-library`
    `maven-publish`
    jacoco
    alias(libs.plugins.jmh)
}

group = "net.theevilreaper"
version = "1.15.2"
description = "Aves"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
    withJavadocJar()
    withSourcesJar()
}

configurations.all {
    resolutionStrategy.cacheDynamicVersionsFor(1, "minutes")
}

dependencies {
    implementation(platform(libs.mycelium.bom))
    implementation(libs.slf4j.api)

    compileOnly(libs.adventure)
    compileOnly(libs.adventure.nbt)
    compileOnly(libs.annotations)
    compileOnly(libs.minestom)

    testImplementation(libs.adventure)
    testImplementation(libs.adventure.nbt)
    testImplementation(libs.annotations)
    testImplementation(libs.minestom)
    testImplementation(libs.cyano)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.platform.launcher)
    testRuntimeOnly(libs.junit.jupiter.engine)

    // The benchmarks live in their own source set (src/jmh/java). Nothing declared here reaches the
    // main or the test classpath, so the published library never carries a jmh dependency.
    jmhImplementation(platform(libs.mycelium.bom))
    jmhImplementation(platform(libs.adventure.bom))
    jmhImplementation(libs.adventure.nbt)
    jmhImplementation(libs.annotations)
    jmhImplementation(libs.jmh.core)
    // No jmh annotation processor is declared on purpose. The plugin already generates the harness
    // classes with its bytecode generator, and declaring the processor as well makes both of them
    // emit the same classes, which leaves the jar with two copies of every benchmark.
}

jmh {
    jmhVersion.set(libs.versions.jmh)
    // The tests of this project need a Minestom server, which a benchmark jar cannot start.
    includeTests.set(false)
    resultFormat.set("JSON")
    resultsFile.set(layout.buildDirectory.file("reports/jmh/results.json"))
    humanOutputFile.set(layout.buildDirectory.file("reports/jmh/human.txt"))

    // A full run takes the better part of an hour, so a single benchmark has to be reachable
    // without editing this file.
    // Usage: ./gradlew jmh -Pjmh.include='BitPackerBenchmark.pack'
    val include = providers.gradleProperty("jmh.include").orNull

    if (include != null) {
        includes.set(listOf(include))
    }
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(25)
    }

    compileJmhJava {
        options.encoding = "UTF-8"
    }

    // The benchmarks are compiled by a normal build but never executed by one. A run takes the
    // better part of an hour and its numbers are far too noisy on a shared runner to gate anything
    // on, while a benchmark that stopped compiling after a refactoring should fail like any other
    // source set.
    check {
        dependsOn(compileJmhJava)
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
        // The chunk loader tests allocate payloads of about one mebibyte to cover the external
        // chunk file path. Without an explicit heap the worker can die while other build tasks
        // run in parallel, which surfaces as an EOFException instead of a test failure.
        maxHeapSize = "1g"
        jvmArgs("-Dminestom.inside-test=true")
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }

    repositories {
        maven {
            authentication {
                credentials(PasswordCredentials::class) {
                    username = System.getenv("ONELITEFEATHER_MAVEN_USERNAME")
                    password = System.getenv("ONELITEFEATHER_MAVEN_PASSWORD")
                }
            }
            name = "OneLiteFeatherRepository"
            url = if (project.version.toString().contains("SNAPSHOT")) {
                uri("https://repo.onelitefeather.dev/onelitefeather-snapshots")
            } else {
                uri("https://repo.onelitefeather.dev/onelitefeather-releases")
            }
        }
    }
}
