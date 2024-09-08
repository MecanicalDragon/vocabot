val hibernateTypesVersion: String by project
val h2Version: String by project
val flywayVersion: String by project
val tgVersion: String by project
val loggingVersion: String by project
val mockitoVersion: String by project

plugins {
    // JVM support
    kotlin("jvm") version "2.0.20"
    application

    // Spring support
    id("org.springframework.boot") version "3.3.3"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("plugin.spring") version "2.0.20"
    kotlin("plugin.jpa") version "2.0.20"
    kotlin("plugin.allopen") version "2.0.20"
    kotlin("kapt") version "2.0.20"

    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
}

ktlint {
    verbose.set(true)                  // Enable verbose output
    outputToConsole.set(true)          // Enable output to the console
    coloredOutput.set(true)            // Use colored output for better visibility
    ignoreFailures.set(true)          // Fail the build on ktlint errors
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN) // Plain text output to the console
    }
}

group = "net.medrag"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    kapt("org.springframework.boot:spring-boot-configuration-processor")

    // makes @ConfigurationProperties work
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.vladmihalcea:hibernate-types-60:$hibernateTypesVersion")
    implementation("com.h2database:h2:$h2Version")
    implementation("org.flywaydb:flyway-core:$flywayVersion")

    implementation("org.telegram:telegrambots:$tgVersion")
    implementation("org.telegram:telegrambotsextensions:$tgVersion")

    implementation("io.github.microutils:kotlin-logging-jvm:$loggingVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:$mockitoVersion")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

application {
    mainClass.set("net.medrag.vocabot.VocaBotAppKt")
}

allOpen {
    annotation("org.springframework.boot.context.properties.ConfigurationProperties")
}

tasks.getByName<Jar>("jar") {
    enabled = false
}

tasks.named("build") {
    doLast {
        copy {
            from(layout.buildDirectory.dir("libs"))
            into(layout.buildDirectory.dir("lib"))
        }
        delete(fileTree(layout.projectDirectory.dir("build")).exclude("bootScripts", "lib"))
    }
}
