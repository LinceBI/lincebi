plugins {
	id("java-library")
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
	implementation("jakarta.ws.rs:jakarta.ws.rs-api:2.1.6")
	implementation("org.codehaus.enunciate:enunciate-core-annotations:1.31")

	compileOnly("pentaho:pentaho-platform-api:8.3.0.19-1132") { isTransitive = false }
	compileOnly("pentaho:pentaho-platform-core:8.3.0.19-1132") { isTransitive = false }
	compileOnly("pentaho:pentaho-platform-extensions:8.3.0.19-1132") { isTransitive = false }
	compileOnly("pentaho:pentaho-platform-repository:8.3.0.19-1132") { isTransitive = false }

	testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.1")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.1")
}

tasks.build {
	finalizedBy("copyDependencies")
}

tasks.register<Copy>("copyDependencies") {
	from(configurations.runtimeClasspath)

	into("${buildDir}/libs/")
}

tasks.test {
	useJUnitPlatform()
}
