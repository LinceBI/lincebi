plugins {
	id("java")
	id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "${project.property("group")}"
version = "${project.property("version")}${project.property("versionSuffix")}"

java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
	maven { url = uri("https://repo.stratebi.com/repository/pentaho-mvn/") }
}

dependencies {
	implementation("com.fasterxml.jackson.core:jackson-annotations:2.13.3")
	implementation("com.fasterxml.jackson.core:jackson-core:2.13.3")
	implementation("com.fasterxml.jackson.core:jackson-databind:2.13.3")
	implementation("com.microsoft.azure:msal4j:1.12.0")
	implementation("com.networknt:json-schema-validator:1.0.70")
	implementation("commons-io:commons-io:2.11.0")
	implementation("jakarta.ws.rs:jakarta.ws.rs-api:2.1.6")
	implementation("jakarta.xml.bind:jakarta.xml.bind-api:2.3.3")
	implementation("org.apache.commons:commons-lang3:3.12.0")
	implementation("org.codehaus.enunciate:enunciate-core-annotations:1.31")
	implementation("org.ehcache:ehcache:3.10.0")
	implementation("org.slf4j:slf4j-reload4j:1.7.36")
	implementation("org.springframework:spring-web:5.3.20")
	implementation("org.thymeleaf:thymeleaf:3.0.15.RELEASE")

	compileOnly("org.pentaho:commons-database-model:9.3.0.0-428") { isTransitive = false }
	compileOnly("org.pentaho:pentaho-metadata:9.3.0.0-428") { isTransitive = false }
	compileOnly("pentaho:pentaho-platform-api:9.3.0.0-428") { isTransitive = false }
	compileOnly("pentaho:pentaho-platform-core:9.3.0.0-428") { isTransitive = false }
	compileOnly("pentaho:pentaho-platform-extensions:9.3.0.0-428") { isTransitive = false }
	compileOnly("pentaho:pentaho-platform-repository:9.3.0.0-428") { isTransitive = false }

	testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

tasks.build {
	dependsOn("shadowJar")
}

tasks.shadowJar {
	archiveClassifier.set("bundle")
	mergeServiceFiles()

	val prefix = "${project.property("group")}.shaded"
	fun shade(path : String) { relocate(path, "${prefix}.${path}") }

	shade("com.azure")
	shade("com.ctc")
	shade("com.fasterxml")
	shade("com.microsoft")
	shade("com.networknt")
	shade("com.nimbusds")
	shade("io.netty")
	shade("javassist")
	shade("net.jcip")
	shade("net.minidev")
	shade("ognl")
	shade("org.apache")
	shade("org.attoparser")
	shade("org.codehaus")
	shade("org.ehcache")
	shade("org.objectweb")
	shade("org.reactivestreams")
	shade("org.springframework")
	shade("org.terracotta")
	shade("org.thymeleaf")
	shade("org.unbescape")
	shade("reactor")
}

tasks.test {
	useJUnitPlatform()
}

tasks.withType<AbstractArchiveTask>().configureEach {
	isPreserveFileTimestamps = false
	isReproducibleFileOrder = true
}
