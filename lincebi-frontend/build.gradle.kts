val version = "${project.property("version")}${project.property("versionSuffix")}"
val gtagId = project.property("gtagId")
val datadogSite = project.property("datadogSite")
val datadogApiKey = project.property("datadogApiKey")
val datadogClientToken = project.property("datadogClientToken")
val datadogMinifiedPathPrefix = project.property("datadogMinifiedPathPrefix")

tasks.register<Sync>("build") {
	if (!File("${projectDir}/packages/login/dist/").exists() ||
		!File("${projectDir}/packages/home/dist/").exists()
	) {
		dependsOn("pnpmRunBuild")
	}

	from("${projectDir}/packages/login/dist/", {
		into("/Login/")
		rename("^(index)\\.html$", "$1.jsp")
	})

	from("${projectDir}/packages/home/dist/", {
		into("/Home/")
		rename("^(index)\\.html$", "$1.jsp")
	})

	into("${buildDir}/")
}

tasks.register<Delete>("clean") {
	delete("${buildDir}/")
	delete("${projectDir}/packages/login/dist/")
	delete("${projectDir}/packages/home/dist/")
}

tasks.register<Exec>("pnpmRunBuild") {
	if (!File("${projectDir}/node_modules/").exists() ||
		!File("${projectDir}/packages/login/node_modules/").exists() ||
		!File("${projectDir}/packages/home/node_modules/").exists()
	) {
		dependsOn("pnpmInstall")
	}

	environment(mapOf(
		"VITE_VERSION" to version,
		"VITE_GTAG_ID" to gtagId,
		"VITE_DATADOG_SITE" to datadogSite,
		"VITE_DATADOG_CLIENT_TOKEN" to datadogClientToken
	))

	commandLine("pnpm", "run", "build")

	outputs.upToDateWhen { false }
}

tasks.register<Exec>("pnpmRunDev") {
	if (!File("${projectDir}/node_modules/").exists() ||
		!File("${projectDir}/packages/login/node_modules/").exists() ||
		!File("${projectDir}/packages/home/node_modules/").exists()
	) {
		dependsOn("pnpmInstall")
	}

	environment(mapOf(
		"VITE_VERSION" to version,
		"VITE_GTAG_ID" to gtagId,
		"VITE_DATADOG_SITE" to datadogSite,
		"VITE_DATADOG_CLIENT_TOKEN" to datadogClientToken
	))

	commandLine("pnpm", "run", "dev")

	outputs.upToDateWhen { false }
}

tasks.register<Exec>("pnpmExecDatadogSourcemapsUpload") {
	if (!File("${projectDir}/node_modules/").exists()) {
		dependsOn("pnpmInstall")
	}

	environment(mapOf(
		"DATADOG_SITE" to datadogSite,
		"DATADOG_API_KEY" to datadogApiKey
	))

	commandLine("pnpm", "exec", "datadog-ci", "sourcemaps", "upload", "${buildDir}/",
		"--service", "lincebi",
		"--release-version", version.toLowerCase(),
		"--minified-path-prefix", datadogMinifiedPathPrefix)

	outputs.upToDateWhen { false }
}

tasks.register<Exec>("pnpmInstall") {
	commandLine("pnpm", "install")

	outputs.upToDateWhen { false }
}

tasks.withType<AbstractArchiveTask>().configureEach {
	isPreserveFileTimestamps = false
	isReproducibleFileOrder = true
}
