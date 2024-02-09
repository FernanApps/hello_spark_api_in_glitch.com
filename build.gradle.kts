import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.7.20"
    id("com.github.johnrengelman.shadow").version("7.1.2")
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.sparkjava:spark-kotlin:1.0.0-alpha")
    implementation("org.slf4j:slf4j-simple:1.7.21")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0")


}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}


val mainClassBase = ""
val mainClassBotNew = mainClassBase + "MainKt"

val shadow  = "shadow"
val nameShadowBotNew = shadow + "RestApi"

tasks.register<Jar>("shadowJarRestApi") {
    dependsOn("shadowJar")
    manifest {
        attributes["Main-Class"] = mainClassBotNew
    }
    archiveBaseName.set(nameShadowBotNew)
    archiveClassifier.set("")
    archiveVersion.set("")
    println(projectDir)
    println(tasks.shadowJar.get().archiveFile.get())
    from(zipTree(tasks.shadowJar.get().archiveFile.get()))
    destinationDirectory.set(projectDir)

}