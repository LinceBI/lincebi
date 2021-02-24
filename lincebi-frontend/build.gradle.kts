tasks.register<Copy>("build") {
	dependsOn("npmRunBuild")

	from("${projectDir}/packages/login/dist/", {
		into("/Login/")
	})

	from("${projectDir}/packages/home/dist/", {
		into("/Home/")
	})

	into("${buildDir}/")
}

tasks.register<Exec>("clean") {
	dependsOn("npmInstall")

	commandLine("npm", "run", "clean")

	delete("${buildDir}/")
}

tasks.register<Exec>("npmRunBuild") {
	dependsOn("npmInstall")
	dependsOn("npmRunLint")

	commandLine("npm", "run", "build")

	outputs.upToDateWhen { File("${buildDir}/").exists() }
}

tasks.register<Exec>("npmRunLint") {
	dependsOn("npmInstall")

	commandLine("npm", "run", "lint")

	outputs.upToDateWhen { false }
}

tasks.register<Exec>("npmInstall") {
	commandLine("npm", "install")

	outputs.upToDateWhen {
		File("${projectDir}/node_modules/").exists()
		&& File("${projectDir}/packages/common/node_modules/").exists()
		&& File("${projectDir}/packages/login/node_modules/").exists()
		&& File("${projectDir}/packages/home/node_modules/").exists()
	}
}
