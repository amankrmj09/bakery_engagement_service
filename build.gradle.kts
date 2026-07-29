plugins {
    java
    id("org.springframework.boot") version "3.5.15"
    id("io.spring.dependency-management") version "1.1.7"
}

description = "bakery_engagement_service"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        name = "GitHubBakeryCommonCore"
        url = uri("https://maven.pkg.github.com/amankrmj09/bakery_common_core")
        credentials {
            username = System.getenv("GITHUB_ACTOR") ?: project.findProperty("gpr.user") as String?
            password = System.getenv("GITHUB_TOKEN") ?: project.findProperty("gpr.key") as String?
        }
    }
    maven {
        name = "GitHubBakeryCommonFeign"
        url = uri("https://maven.pkg.github.com/amankrmj09/bakery_common_feign")
        credentials {
            username = System.getenv("GITHUB_ACTOR") ?: project.findProperty("gpr.user") as String?
            password = System.getenv("GITHUB_TOKEN") ?: project.findProperty("gpr.key") as String?
        }
    }
    maven {
        name = "GitHubBakeryCommonMessaging"
        url = uri("https://maven.pkg.github.com/amankrmj09/bakery_common_messaging")
        credentials {
            username = System.getenv("GITHUB_ACTOR") ?: project.findProperty("gpr.user") as String?
            password = System.getenv("GITHUB_TOKEN") ?: project.findProperty("gpr.key") as String?
        }
    }
    maven {
        name = "GitHubBakeryCommonSecurity"
        url = uri("https://maven.pkg.github.com/amankrmj09/bakery_common_security")
        credentials {
            username = System.getenv("GITHUB_ACTOR") ?: project.findProperty("gpr.user") as String?
            password = System.getenv("GITHUB_TOKEN") ?: project.findProperty("gpr.key") as String?
        }
    }
}

extra["springCloudVersion"] = "2025.0.3"

dependencies {
    implementation("org.blubakery.libs:bakery_common_security:1.0.0")
    implementation("org.blubakery.libs:bakery_common_messaging:1.0.0")
    implementation("org.blubakery.libs:bakery_common_core:1.0.0")
    implementation("org.blubakery.libs:bakery_common_feign:1.0.0")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.cloud:spring-cloud-starter-config")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.8.4")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testCompileOnly("org.projectlombok:lombok")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testAnnotationProcessor("org.projectlombok:lombok")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
