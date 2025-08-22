plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.7"
}

group = "me.zimzaza4"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven("https://repo.opencollab.dev/main/")
}

dependencies {
    compileOnly("org.geysermc.geyser:api:2.8.3-SNAPSHOT")

    compileOnly(files("libs/geyserutils-geyser-1.0-SNAPSHOT.jar"))

    implementation("org.spongepowered:configurate-yaml:4.2.0-GeyserMC-SNAPSHOT")
    implementation("com.google.code.gson:gson:2.13.1")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks.compileJava {
    options.encoding = "UTF-8"
}

tasks.shadowJar {
    archiveFileName.set("${rootProject.name}-${version}.jar")

    relocate("org.spongepowered.configurate", "me.zimzaza4.geysermodelenginepackgenerator.libs.configurate")
}

tasks.build {
    dependsOn("shadowJar")
}
