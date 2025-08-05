plugins {
	id("com.netflix.nebula.dependency-lock") version "16.0.1"
}

subprojects {
	apply(plugin = "com.netflix.nebula.dependency-lock")

	tasks.register<DependencyReportTask>("dependenciesForAll")
}
