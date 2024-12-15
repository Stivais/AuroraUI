plugins {
    kotlin("jvm")
}

group = "com.github.stivais"
version = "2.0.0-alpha"

val lwjglVersion = "3.3.5-SNAPSHOT"
val lwjglNatives = "natives-windows"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(project(":"))

    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

    implementation("org.lwjgl", "lwjgl")
    implementation("org.lwjgl", "lwjgl-glfw")
    implementation("org.lwjgl", "lwjgl-opengl")
    runtimeOnly("org.lwjgl", "lwjgl", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}