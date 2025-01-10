plugins {
    kotlin("jvm") version "2.0.0"
    `maven-publish`
}

group = "com.github.stivais"
version = project.findProperty("version") as String

val lwjglVersion = "3.3.5-SNAPSHOT"
val lwjglNatives = "natives-windows"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    testImplementation(kotlin("test"))

    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

    compileOnly("org.lwjgl", "lwjgl")
    compileOnly("org.lwjgl", "lwjgl-opengl")
    implementation("org.joml:joml:1.10.5")
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