plugins {
    java
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "edu.kennesaw"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

tasks.bootBuildImage {
    createdDate = "now"
    imageName="452350997852.dkr.ecr.us-east-2.amazonaws.com/senior_project"

}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql")
    implementation(platform("software.amazon.awssdk:bom:2.23.16"))
    implementation("software.amazon.awssdk:s3")
    implementation(platform("org.hibernate.search:hibernate-search-bom:7.0.0.Final"))
    implementation("org.hibernate.search:hibernate-search-mapper-orm")
    implementation("org.hibernate.search:hibernate-search-backend-lucene")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
