import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.4"
	id("io.spring.dependency-management") version "1.1.4"
	id("org.asciidoctor.jvm.convert") version "3.3.2"
	kotlin("jvm") version "1.9.23"
	kotlin("plugin.spring") version "1.9.23"
	kotlin("plugin.jpa") version "1.9.23"
	kotlin("kapt") version "1.9.21"
}

group = "pw_manager"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}

}

repositories {
	mavenCentral()
}
val asciidoctorExt: Configuration by configurations.creating

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("com.google.firebase:firebase-admin:9.2.0")
	implementation("com.squareup.okhttp3:okhttp")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("com.mysql:mysql-connector-j")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	// https://mvnrepository.com/artifact/org.modelmapper/modelmapper
	implementation("org.modelmapper:modelmapper:3.2.0")
	testImplementation("com.h2database:h2")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
	implementation("org.springframework.boot:spring-boot-starter-security")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("com.ninja-squad:springmockk:3.0.1")
	// https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-api
	implementation("io.jsonwebtoken:jjwt-api:0.12.3")
	implementation("io.jsonwebtoken:jjwt-impl:0.12.3")
	implementation("io.jsonwebtoken:jjwt-jackson:0.12.3")


	implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
	kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
	kapt("jakarta.annotation:jakarta.annotation-api")
	kapt("jakarta.persistence:jakarta.persistence-api")

	testImplementation ("org.springframework.restdocs:spring-restdocs-mockmvc")
	asciidoctorExt ("org.springframework.restdocs:spring-restdocs-asciidoctor")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.test {
	useJUnitPlatform()
	//outputs.dir(snippetsDir) -> @AutoConfigureRestDocs가 자동 설정
	finalizedBy(tasks.asciidoctor)
}


tasks.asciidoctor {
	dependsOn(tasks.test)
	attributes(mapOf(
			"snippets" to file("build/generated-snippets").absolutePath
	))
	finalizedBy("copyDocument")
}

tasks.register("copyDocument", Copy::class){
	doFirst {
		delete(file("src/main/resources/static/docs"))
	}
	from(file("build/docs/asciidoc"))
	into(file("src/main/resources/static/docs"))
}

tasks.bootJar {
	dependsOn(tasks.asciidoctor)
	from ("build/docs/asciidoc/html5") {
		into("static/docs")
	}
}

