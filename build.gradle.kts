plugins {
    kotlin("jvm") version "1.8.0"
    java
    `maven-publish`
    signing
}

group = "io.github.linhelurking"
version = "0.1.1"

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

tasks.compileKotlin {
    kotlinOptions {
        jvmTarget = "17"
    }
}

tasks.publishToMavenLocal {
    dependsOn(tasks["jar"])
}

tasks.publish {
    dependsOn(tasks["jar"])
}

val sourceJar by tasks.registering(Jar::class) {
    from(sourceSets["main"].allSource)
}

val javadocJar by tasks.registering(Jar::class) {
    dependsOn(tasks.javadoc)
    from(tasks.javadoc.get())
}

publishing {
    publications {
        create<MavenPublication>("Maven") {
            pom {
                name.set("kt-snbt")
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

                // Must provide scm information. Otherwise, POM validation fails.
                scm {
                    connection.set("git@github.com:LinHeLurking/SNBT.KT.git")
                    developerConnection.set("git@github.com:LinHeLurking/SNBT.KT.git")
                    url.set("https://github.com/LinHeLurking/SNBT.KT")
                }
            }
            artifact(tasks["jar"])
            artifact(sourceJar) {
                classifier = "sources"
            }
            artifact(javadocJar) {
                classifier = "javadoc"
            }
        }
    }

    repositories {
        maven {
            val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
            credentials {
                username = properties["ossrhUsername"] as String?
                password = properties["ossrhPassword"] as String?
            }
        }
    }
}

signing {
    useGpgCmd()
    // This is correct. Sign for `configurations.archives` won't work.
    publishing.publications.forEach {
        sign(it)
    }
}