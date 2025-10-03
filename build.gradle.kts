import de.undercouch.gradle.tasks.download.Download
import org.glavo.hmcl.update.CheckExisting
import org.glavo.hmcl.update.CheckUpdate
import org.glavo.hmcl.update.UpdateChannel
import java.nio.file.Files
import java.security.MessageDigest

plugins {
    signing
    `maven-publish`
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
    id("de.undercouch.download") version "5.6.0"
}

group = "org.glavo.hmcl"

val hmclChannel = project.findProperty("hmcl.channel")?.let { name ->
    UpdateChannel.entries.find { channel ->
        channel.name.contentEquals(name.toString(), true)
    } ?: throw GradleException("channel '$name' is not defined")
} ?: throw GradleException("hmcl.channel is not defined")


CheckUpdate.apply(project, hmclChannel)

val hmclVersion: String = ext.get(CheckUpdate.HMCL_VERSION).toString()
val hmclDownloadBaseUri: String = ext.get(CheckUpdate.HMCL_DOWNLOAD_BASE_URI).toString()

version = hmclVersion

val downloadDir = layout.buildDirectory.dir("downloads")

val checkExisting by tasks.registering(CheckExisting::class) {
    version.set(hmclVersion)
    channel.set(hmclChannel)
}

val downloadArtifacts by tasks.registering(Download::class) {
    dependsOn(checkExisting)

    src("$hmclDownloadBaseUri/HMCL-$hmclVersion.jar")
    src("$hmclDownloadBaseUri/HMCL-$hmclVersion.jar.sha256")

    overwrite(false)
    quiet(false)
    dest(downloadDir)
    retries(5)
}

val verifyArtifacts by tasks.registering {
    dependsOn(downloadArtifacts)

    doLast {
        val dir = downloadDir.get().asFile.toPath()
        val expected = Files.readString(dir.resolve("HMCL-$hmclVersion.jar.sha256")).trim()

        val digest = MessageDigest.getInstance("SHA-256")
        digest.update(Files.readAllBytes(dir.resolve("HMCL-$hmclVersion.jar")))

        val actual = digest.digest().toHexString()
        if (!expected.contentEquals(actual, true)) {
            throw GradleException("Checksum not matching: expected $expected, actual $actual")
        }
    }
}

val javadocJar by tasks.registering(Jar::class) {
    archiveBaseName.set("HMCL")
    archiveClassifier.set("javadoc")

    archiveVersion.set(hmclVersion)
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveBaseName.set("HMCL")
    archiveClassifier.set("sources")

    archiveVersion.set(hmclVersion)
}

tasks.withType<GenerateModuleMetadata> {
    enabled = false
}

tasks.withType<Sign> {
    dependsOn(verifyArtifacts, javadocJar, sourcesJar)
}

tasks.withType<GenerateMavenPom> {
    dependsOn(verifyArtifacts, javadocJar, sourcesJar)
}

val hmclPublication = publishing.publications.create<MavenPublication>("hmcl") {
    groupId = project.group.toString()
    artifactId = hmclChannel.mavenArtifactId

    version = hmclVersion
    artifact(downloadDir.map { it.file("HMCL-$hmclVersion.jar") }) {
        builtBy(verifyArtifacts)
        this.extension = "jar"
        this.classifier = ""
    }

    artifact(sourcesJar)
    artifact(javadocJar)

    pom {
        name.set("Hello Minecraft! Launcher ")
        description.set("A Minecraft Launcher which is multi-functional, cross-platform and popular")
        url.set("https://github.com/HMCL-dev/HMCL")
        licenses {
            license {
                name.set("GPL 3.0")
                url.set("https://www.gnu.org/licenses/gpl-3.0.html")
            }
        }
        developers {
            developer {
                id.set("huanghongxun")
                name.set("Yuhui Huang")
                email.set("jackhuang1998@gmail.com")
            }

            developer {
                id.set("Glavo")
                name.set("Glavo")
                email.set("zjx001202@gmail.com")
            }
        }
        scm {
            url.set("https://github.com/HMCL-dev/HMCL")
        }
    }
}

// Maven Central

var secretPropsFile = project.rootProject.file("gradle/maven-central-publish.properties")
if (!secretPropsFile.exists()) {
    secretPropsFile =
        file(System.getProperty("user.home")).resolve(".gradle").resolve("maven-central-publish.properties")
}

if (secretPropsFile.exists()) {
    // Read local.properties file first if it exists
    val p = java.util.Properties()
    secretPropsFile.reader().use {
        p.load(it)
    }

    p.forEach { (name, value) ->
        rootProject.ext[name.toString()] = value
    }
}

listOf(
    "sonatypeUsername" to "SONATYPE_USERNAME",
    "sonatypePassword" to "SONATYPE_PASSWORD",
    // "sonatypeStagingProfileId" to "SONATYPE_STAGING_PROFILE_ID",
    "signing.keyId" to "SIGNING_KEY_ID",
    "signing.password" to "SIGNING_PASSWORD",
    "signing.key" to "SIGNING_KEY"
).forEach { (p, e) ->
    if (!rootProject.ext.has(p)) {
        rootProject.ext[p] = System.getenv(e)
    }
}

signing {
    useInMemoryPgpKeys(
        rootProject.ext["signing.keyId"].toString(),
        rootProject.ext["signing.key"].toString(),
        rootProject.ext["signing.password"].toString(),
    )
    sign(publishing.publications["hmcl"])
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))

            // stagingProfileId.set(rootProject.ext["sonatypeStagingProfileId"].toString())
            username.set(rootProject.ext["sonatypeUsername"].toString())
            password.set(rootProject.ext["sonatypePassword"].toString())
        }
    }
}
