plugins {
	id("nebula.dependency-lock") version "11.2.0"
}

subprojects {
	apply(plugin = "nebula.dependency-lock")

	tasks.register<DependencyReportTask>("dependenciesForAll")
}
