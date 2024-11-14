plugins {
    kotlin("jvm") version "2.0.0"
    `maven-publish`
}

group = "com.github.stivais"
version = project.findProperty("version") as String

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "AuroraUI"
            groupId = "com.github.stivais"
            version = project.findProperty("version") as String
            artifact(tasks.jar.get().archiveFile) }
    }
    repositories {
        mavenLocal()
    }
}

kotlin.jvmToolchain(8)