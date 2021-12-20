import java.security.MessageDigest

plugins {
    signing
    `maven-publish`
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
    id("de.undercouch.download") version "4.1.2"
}

val HMCL_VERSION_PATTERN = Regex("^[0-9]+\\.[0-9]+\\.[0-9]+(\\.[0-9]+)?$")
val HMCL_BUILD_NUMBER_PATTERN = Regex("^[0-9]+$")

val exts = listOf("exe", "jar", "pack", "pack.gz", "pack.xz")

enum class HMCLChannel(
    val artifactId: String,
    val ciUrlBase: String
) {
    DEV("hmcl-dev", "https://ci.huangyuhui.net/job/HMCL"),
    STABLE("hmcl-stable", "https://ci.huangyuhui.net/job/HMCL-stable");
}

val hmclVersion =
    findProperty("hmcl.version")?.toString()
        ?: System.getenv("HMCL_VERSION") ?: throw GradleException("HMCL version not specified")

val ciBuildNumber =
    findProperty("hmcl.ci.buildNumber")?.toString()
        ?: System.getenv("HMCL_CI_BUILD_NUMBER") ?: throw GradleException("HMCL CI build number not specified")

val hmclChannel =
    when (
        val c = findProperty("hmcl.channel")?.toString()
            ?: System.getenv("HMCL_CHANNEL") ?: throw GradleException("HMCL channel not specified")
    ) {
        "dev" -> HMCLChannel.DEV
        "stable" -> HMCLChannel.STABLE
        else -> throw GradleException("Bad HMCL channel: $c")
    }

if (!HMCL_VERSION_PATTERN.matches(hmclVersion)) {
    throw GradleException("Bad HMCL version: $hmclVersion")
}

if (!HMCL_BUILD_NUMBER_PATTERN.matches(ciBuildNumber)) {
    throw GradleException("Bad HMCL CI build number: $ciBuildNumber")
}

val downloadDir = buildDir.resolve("downloads")

val downloadArtifacts = tasks.create<de.undercouch.gradle.tasks.download.Download>("downloadArtifacts") {
    doFirst {
        if (!downloadDir.exists()) {
            downloadDir.mkdirs()
        }
    }

    for (ext in exts) {
        src("${hmclChannel.ciUrlBase}/$ciBuildNumber/artifact/HMCL/build/libs/HMCL-$hmclVersion.$ext")
        src("${hmclChannel.ciUrlBase}/$ciBuildNumber/artifact/HMCL/build/libs/HMCL-$hmclVersion.$ext.sha1")
    }

    overwrite(false)
    quiet(false)
    dest(downloadDir)
    retries(5)
}

val verifyDownload = tasks.create("verifyDownload") {
    dependsOn(downloadArtifacts)

    doLast {
        var failed = false
        val md = MessageDigest.getInstance("SHA-1")

        for (ext in exts) {
            val file = downloadDir.resolve("HMCL-$hmclVersion.$ext")
            if (!file.exists()) {
                logger.log(LogLevel.ERROR, "$file does not exist")
                failed = true
                continue
            }

            val hashFile = downloadDir.resolve("HMCL-$hmclVersion.$ext.sha1")
            if (!hashFile.exists()) {
                logger.log(LogLevel.ERROR, "$hashFile does not exist")
                failed = true
                continue
            }

            md.reset()
            md.update(file.readBytes())

            val hash = md.digest().joinToString(separator = "") { "%02x".format(it) }
            val recordedHash = hashFile.readText().trim()

            if (hash == recordedHash) {
                logger.quiet("$file passed validation")
            } else {
                logger.log(LogLevel.ERROR, "SHA-1 of file $file ($hash) does not match $recordedHash")
                file.delete()
                hashFile.delete()
                failed = true
            }
        }

        if (failed) {
            throw GradleException("Verification failed")
        }
    }
}


val updateJsonFile = downloadDir.resolve("HMCL-$hmclVersion.json")

val generateUpdateJson = tasks.create("generateUpdateJson") {
    dependsOn(verifyDownload)

    doLast {
        if (!downloadDir.exists()) {
            downloadDir.mkdirs()
        }

        fun downloadLink(ext: String) =
            "https://maven.aliyun.com/repository/central/org/glavo/hmcl/${hmclChannel.artifactId}/$hmclVersion/${hmclChannel.artifactId}-$hmclVersion.$ext"

        fun sha1(ext: String) =
            downloadDir.resolve("HMCL-$hmclVersion.$ext.sha1").readText().trim()

        val data = mapOf(
            "pack" to downloadLink("pack"),
            "packsha1" to sha1("pack"),
            "packxz" to downloadLink("pack.xz"),
            "packxzsha1" to sha1("pack.xz"),
            "jar" to downloadLink("jar"),
            "jarsha1" to sha1("jar"),
            "version" to hmclVersion,
            "universal" to "https://www.mcbbs.net/forum.php?mod=viewthread&tid=142335"
        )

        downloadDir.resolve("HMCL-$hmclVersion.json").writeText(
            data.map { (key, value) -> """"$key":"$value"""" }.joinToString(",", "{", "}")
        )
    }
}

tasks.withType<GenerateModuleMetadata>().configureEach {
    enabled = false
}

tasks.withType<GenerateMavenPom>().configureEach {
    dependsOn(verifyDownload, generateUpdateJson)
}

configure<PublishingExtension> {
    publications {
        create<MavenPublication>("hmcl") {
            groupId = "org.glavo.hmcl"
            artifactId = hmclChannel.artifactId
            version = hmclVersion

            for (ext in exts) {
                artifact(downloadDir.resolve("HMCL-$hmclVersion.$ext")) {
                    extension = ext
                    classifier = ""
                }

                artifact(downloadDir.resolve("HMCL-$hmclVersion.$ext.sha1")) {
                    extension = "$ext.sha1"
                    classifier = ""
                }
            }

            artifact(downloadDir.resolve("HMCL-$hmclVersion.json")) {
                extension = "json"
                classifier = ""
            }

            pom {
                name.set("Hello Minecraft! Launcher ")
                description.set("A Minecraft Launcher which is multi-functional, cross-platform and popular")
                url.set("https://github.com/huanghongxun/HMCL")
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
                }
                scm {
                    url.set("https://github.com/huanghongxun/HMCL")
                }
            }
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
    "sonatypeUsername" to "OSSRH_USERNAME",
    "sonatypePassword" to "OSSRH_PASSWORD",
    "sonatypeStagingProfileId" to "SONATYPE_STAGING_PROFILE_ID",
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
            stagingProfileId.set(rootProject.ext["sonatypeStagingProfileId"].toString())
            username.set(rootProject.ext["sonatypeUsername"].toString())
            password.set(rootProject.ext["sonatypePassword"].toString())
        }
    }
}
