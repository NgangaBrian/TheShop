plugins {
	java
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(22)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	runtimeOnly("com.mysql:mysql-connector-j")
	implementation ("com.google.api-client:google-api-client:1.33.0")
	implementation ("com.google.http-client:google-http-client-jackson2:1.40.1")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	compileOnly ("org.projectlombok:lombok:1.18.34")
	implementation ("org.apache.commons:commons-text:1.12.0")
	annotationProcessor ("org.projectlombok:lombok:1.18.34")
	implementation("com.fasterxml.jackson.core:jackson-databind:2.14.0")
	implementation ("com.squareup.okhttp3:okhttp:4.12.0")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
tasks.bootJar{
	enabled = true
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
	archiveFileName.set("TheShop.jar")
}
