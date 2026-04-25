plugins {
	java
	jacoco
	id("org.springframework.boot") version "3.4.2"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.sonarqube") version "5.1.0.4882"
}

group = "br.com.dhentech"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(21))
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.flywaydb:flyway-core")
	implementation("org.flywaydb:flyway-database-postgresql")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("com.auth0:java-jwt:4.4.0")
	implementation("org.mapstruct:mapstruct:1.5.5.Final")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
}

tasks.withType<Test> {
	useJUnitPlatform()
	jvmArgs("-XX:+EnableDynamicAgentLoading", "-Dnet.bytebuddy.experimental=true")
	environment("SPRING_DATASOURCE_URL", System.getenv("SPRING_DATASOURCE_URL") ?: "")
	environment("SPRING_DATASOURCE_USERNAME", System.getenv("SPRING_DATASOURCE_USERNAME") ?: "")
	environment("SPRING_DATASOURCE_PASSWORD", System.getenv("SPRING_DATASOURCE_PASSWORD") ?: "")
}

jacoco {
	toolVersion = "0.8.12"
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
	reports {
		xml.required.set(true)
		html.required.set(true)
	}

	classDirectories.setFrom(
		sourceSets.main.get().output.asFileTree.matching {
			exclude(
				"**/infrastructure/persistence/ExpenseEntity*",
				"**/mapper/**",
				"**/application/dto/**",
				"**/FinanceApiApplication*",
				"**/*MapperImpl*"
			)
		}
	)
}

sonarqube {
	properties {
		property("sonar.projectKey", "denilsonDhentech_finance-api")
		property("sonar.organization", "dhentech")
		property("sonar.host.url", "https://sonarcloud.io")
		property("sonar.coverage.jacoco.xmlReportPaths", "${layout.buildDirectory.get()}/reports/jacoco/test/jacocoTestReport.xml")
	}
}