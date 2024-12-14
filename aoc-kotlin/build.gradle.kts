plugins {
    kotlin("jvm") version "2.1.0"
    application
}

group = "me.sol"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
    testImplementation("org.assertj:assertj-core:3.26.3")
}

tasks.test {
    useJUnitPlatform()
}

//tasks.withType<KotlinCompile> {
//	kotlinOptions.jvmTarget = "11"
//}

application {
//    mainClass = project.hasProperty("mainClass") ? project.getProperty("mainClass") : "NULL"
    mainClass.set((project.properties["mainClass"] as String?) ?: "NULL")
}
