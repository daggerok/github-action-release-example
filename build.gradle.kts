import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
    id("org.springframework.boot") version "2.6.5"
    id("com.github.ben-manes.versions") version "0.42.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

group = "daggerok"
version = "0.0.1-SNAPSHOT"

java {
    // sourceCompatibility = JavaVersion.VERSION_17
    // targetCompatibility = JavaVersion.VERSION_17
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    //
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.webjars:bootstrap:${property("bootstrapVersion")}")
    //
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    //
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    //
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }
    named<Jar>("jar") {
        archiveExtension.set("zip")
    }
    withType<Test> {
        useJUnitPlatform()
        testLogging {
            showExceptions = true
            showStandardStreams = true
            events(
                org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
            )
        }
    }
    named<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask>("dependencyUpdates") {
        resolutionStrategy {
            componentSelection {
                all {
                    // val rejected = listOf("alpha", "beta", "rc", "cr", "m", "preview", "b", "ea", "M1", "BUILD-SNAPSHOT", "SNAPSHOT")
                    val rejected = listOf("alpha", "M", "RC") // ch.qos.logback:logback-classic:1.3.0-alpha*, io.vavr:vavr:1.0.0-alpha-*
                        // com.tngtech.jgiven:jgiven-*:1.0.0-RC1 com.tngtech.jgiven:jgiven-*:1.0.0-RC2
                        .map { qualifier -> Regex("(?i).*[.-]$qualifier[.\\d-+]*") }
                        .any { it.matches(candidate.version) }
                    if (rejected) reject("Release candidate")
                }
            }
        }
        outputFormatter = "plain" // "json"
    }
}

tasks
defaultTasks("clean", "build")
