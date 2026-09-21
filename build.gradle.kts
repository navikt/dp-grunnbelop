group = "no.nav.dagpenger"

val artifactDescription = "Libraries for Dagpenger"

plugins {
    id("common")
    `java-library`
    `maven-publish`
}

repositories {
    mavenCentral()
}

val cucumberVersion = "7.34.8"
val junitVersion = "6.1.3"
val kotestVersion = "6.2.5"

dependencies {
    testImplementation(platform("org.junit:junit-bom:$junitVersion"))

    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.kotest:kotest-assertions-core-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-table:$kotestVersion")

    testImplementation("com.approvaltests:approvaltests:31.0.0")

    testImplementation("io.cucumber:cucumber-java:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-java8:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:$cucumberVersion")
}

val sourcesJar =
    tasks.register<Jar>("sourcesJar") {
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
    }

val githubUser: String? = project.findProperty("githubUser") as String?
val githubPassword: String? = project.findProperty("githubPassword") as String?

publishing {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/navikt/dp-grunnbelop")
            credentials {
                username = githubUser
                password = githubPassword
            }
        }
    }
    publications {
        create<MavenPublication>("github") {
            from(components["java"])
            artifact(sourcesJar.get())

            pom {
                description.set(artifactDescription)
                name.set(project.name)
                url.set("https://github.com/navikt/dp-grunnbelop")
                withXml {
                    asNode().appendNode("packaging", "jar")
                }
                licenses {
                    license {
                        name.set("MIT License")
                        name.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        organization.set("NAV (Arbeids- og velferdsdirektoratet) - The Norwegian Labour and Welfare Administration")
                        organizationUrl.set("https://www.nav.no")
                    }
                }

                scm {
                    connection.set("scm:git:https://github.com/navikt/dp-grunnbelop.git")
                    developerConnection.set("scm:git:https://github.com/navikt/dp-grunnbelop.git")
                    url.set("https://github.com/navikt/dp-grunnbelop")
                }
            }
        }
    }
}
