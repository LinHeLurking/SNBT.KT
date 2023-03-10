plugins {
    kotlin("jvm") version "1.8.0"
    `maven-publish`
    signing
}

group = "io.github.linhelurking"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

publishing {
    publications {
        create<MavenPublication>("snbt") {
            pom {
                name.set("snbt")
                description.set("A SNBT file reader for Kotlin language")
                url.set("https://github.com/LinHeLurking/SNBT.KT")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://spdx.org/licenses/MIT.html")
                    }
                }

                developers {
                    developer {
                        id.set("LinHeLurking")
                        name.set("LinHe")
                        email.set("LinHe.Lurking@gmail.com")
                    }
                }
            }
            from(components["kotlin"])
        }
    }

    repositories {
        maven {
            name = "LocalRepo"
            val releasesRepoUrl = uri(layout.buildDirectory.dir("repos/releases"))
            val snapshotsRepoUrl = uri(layout.buildDirectory.dir("repos/snapshots"))
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["snbt"])
}