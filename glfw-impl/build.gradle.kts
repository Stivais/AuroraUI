plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "8.0.0"
}

group = "com.github.stivais"
version = "2.0.0-alpha"

val lwjglVersion = "3.3.6"
val lwjglNatives = listOf("windows", "linux")

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

val shadowImpl: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

dependencies {
    testImplementation(kotlin("test"))
    shadowImpl(project(":"))

    shadowImpl(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

    for (type in listOf("", "-glfw", "-opengl")) {
        shadowImpl("org.lwjgl", "lwjgl$type")
        for (native in lwjglNatives) {
            shadowImpl("org.lwjgl", "lwjgl$type", classifier = "natives-$native")
        }
    }

//    shadowImpl("org.lwjgl", "lwjgl")
//    shadowImpl("org.lwjgl", "lwjgl-glfw")
//    shadowImpl("org.lwjgl", "lwjgl-opengl")
//    shadowImpl("org.lwjgl", "lwjgl", classifier = lwjglNatives)
//    shadowImpl("org.lwjgl", "lwjgl-glfw", classifier = lwjglNatives)
//    shadowImpl("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)

    shadowImpl("org.joml:joml:1.10.5")

    shadowImpl(kotlin("stdlib"))
}

tasks.shadowJar {
    configurations = listOf(shadowImpl) // Ensure shadowJar includes shadowImpl dependencies
    archiveBaseName.set("my-application")
    archiveVersion.set("2.0.0-alpha")
    mergeServiceFiles()
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "com.github.stivais.MainKt"
        )
    }
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(8)
}