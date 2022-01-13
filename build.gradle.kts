plugins {
	id("nebula.dependency-lock") version "12.2.1"
}

subprojects {
	apply(plugin = "nebula.dependency-lock")

	tasks.register<DependencyReportTask>("dependenciesForAll")
}
