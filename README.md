# 🚀 Engagement Service

![Java](https://img.shields.io/badge/Java-25-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.15-brightgreen.svg)
![Database](https://img.shields.io/badge/Database-MongoDB%20|%20Elasticsearch-blue.svg)

The Engagement Service is a core component of the Blu's Bakery Microservice Platform that handles customer feedback, testimonials, and contact inquiries. It provides advanced search capabilities and administrative curation tools.

## 📑 Table of Contents
- [Architecture & Design](#-architecture--design)
- [Features](#-features)
- [Folder Structure](#-folder-structure)
- [API Reference](#-api-reference)
- [Configuration](#-configuration)
- [How to Run Locally](#-how-to-run-locally)
- [Testing](#-testing)
- [Dependencies](#-dependencies)
- [Related Links](#-related-links)

## 🏗️ Architecture & Design
Provide a brief overview of the architecture of this service.
- **Data Storage**: MongoDB for primary document persistence and Elasticsearch for fast and reliable search capabilities.
- **Communication**: REST API for client-facing communication, Kafka for asynchronous messaging, and Eureka/Config Server for discovery and configuration.
- **Key Design Patterns**: MVC, Repository Pattern, DTO Pattern.

## ✨ Features
List the core capabilities and features of this service.
- Complete management for customer feedbacks and testimonials.
- Admin curation to feature specific testimonials on the storefront (limited to max 5).
- Fast and reliable search capabilities using Elasticsearch.
- Automatic synchronous saving to MongoDB and asynchronous indexing to Elasticsearch.

## 📁 Folder Structure

```text
bakery_engagement_service/
├── .env
├── .env.example
├── Dockerfile
├── README.md
├── API_REFERENCE.md
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
└── src/
    ├── main/
    │   ├── java/com/blubugtech/bakery_engagement_service/
    │   │   ├── controller/               # REST API Controllers (Testimonial, Feedback, Review, AdminReview, ContactDetails)
    │   │   ├── dto/                      # Data Transfer Objects (Request/Response DTOs for each module)
    │   │   │   ├── contact/
    │   │   │   ├── feedback/
    │   │   │   ├── review/
    │   │   │   └── testimonial/
    │   │   ├── entity/                   # MongoDB Domain Entities (Testimonial, Feedback, Review, ContactDetails)
    │   │   ├── event/                    # Kafka / Domain Event payloads
    │   │   ├── exception/                # Global Exception Handler and custom exception definitions
    │   │   ├── listener/                 # Event Listeners for domain events
    │   │   ├── mapper/                   # Mappers converting between Entities and DTOs
    │   │   ├── repository/               # Spring Data MongoDB Repositories
    │   │   ├── search/                   # Elasticsearch indexing, document models, and search repositories
    │   │   │   ├── document/
    │   │   │   └── repository/
    │   │   ├── service/                  # Business Logic Services handling data flow & validation
    │   │   └── BakeryEngagementServiceApplication.java
    │   └── resources/
    │       ├── application.yaml          # Primary application configuration
    │       ├── application-docker.yml    # Docker environment profile configuration
    │       └── logback-spring.xml        # Logging configurations
    └── test/
        └── java/com/blubugtech/bakery_engagement_service/
            └── BakeryEngagementServiceApplicationTests.java
```

## 🌐 API Reference
> [!NOTE]
> For detailed API definitions, request/response DTO schemas, and query parameters, please refer to the [API_REFERENCE.md](./API_REFERENCE.md) file or Swagger UI.

**Key Endpoints Overview:**

- **Testimonials (`/api/engagement/testimonials`)**
  - `POST /api/engagement/testimonials` — Submit a new testimonial
  - `GET /api/engagement/testimonials` — Retrieve paginated list of testimonials
  - `GET /api/engagement/testimonials/featured` — Retrieve featured testimonials
  - `GET /api/engagement/testimonials/search` — Search testimonials by username (Elasticsearch)
  - `PUT /api/engagement/testimonials/{id}/feature` — Toggle featured status for storefront
  - `DELETE /api/engagement/testimonials/{id}` — Delete a testimonial

- **Feedback & Contact (`/api/engagement/feedback`, `/api/engagement/contact-details`)**
  - `POST /api/engagement/feedback` — Submit feedback or contact message
  - `GET /api/engagement/feedback` — Retrieve paginated feedbacks
  - `GET /api/engagement/feedback/search` — Search feedbacks by query or type (Elasticsearch)
  - `PUT /api/engagement/feedback/{id}/status` — Update status of a feedback
  - `DELETE /api/engagement/feedback/{id}` — Delete a feedback item
  - `GET /api/engagement/contact-details` — Get bakery contact details
  - `PUT /api/engagement/contact-details` — Update contact details

- **Customer & Admin Reviews (`/api/engagement/reviews`, `/api/admin/engagement/reviews`)**
  - `POST /api/engagement/reviews/product/{id}` — Add a review for a product (Protected)
  - `PUT /api/engagement/reviews/product/{id}/{reviewId}` — Update an existing review (Protected)
  - `GET /api/engagement/reviews/product/{id}` — Get reviews for a product
  - `DELETE /api/engagement/reviews/product/{id}/{reviewId}` — Delete a review (Protected)
  - `POST /api/engagement/reviews/product/{id}/{reviewId}/report` — Report inappropriate review (Protected)
  - `GET /api/admin/engagement/reviews/reported` — Get reported reviews (Admin)
  - `POST /api/admin/engagement/reviews/{reviewId}/dismiss-report` — Dismiss reported review (Admin)

## ⚙️ Configuration
List required environment variables and configurations.
You can copy `.env.example` to `.env` and fill in the values.

| Variable | Description | Default / Example |
|----------|-------------|-------------------|
| `ACTIVE_PROFILE` | Active Spring profile | `dev` |
| `CONFIG_SERVER_URL` | URL of the config server | `http://localhost:8081` |
| `ELASTICSEARCH_URIS` | Elasticsearch URL | `http://localhost:9200` |
| `ELASTIC_PASSWORD` | Elasticsearch password | `changeme` |
| `EUREKA_URL`| Eureka server URL | `http://localhost:8761/eureka/` |
| `KAFKA_BOOTSTRAP_SERVERS` | Kafka bootstrap servers | `localhost:9092` |
| `ENGAGEMENT_DB_URL` | MongoDB connection URL | `mongodb://product_user:product_password@bakery-products-db:27017/bakery_engagement?authSource=admin` |
| `SERVER_PORT` | Port for the service | `8089` |

## 🚀 How to Run Locally

### Prerequisites
- JDK 25+
- Gradle
- MongoDB, Elasticsearch, Kafka, Config Server, Eureka Server

### Steps
1. **Clone the repository:**
   ```bash
   git clone https://github.com/amankrmj01/bakery.git
   cd bakery/bakery_engagement_service
   ```

2. **Configure Environment:**
   Set up your `.env` file based on `.env.example`. Make sure backing services (like MongoDB, Elasticsearch, and Kafka) are running.

3. **Run the application:**
   ```bash
   ./gradlew bootRun
   ```

## 🧪 Testing
To run the test suite:
```bash
./gradlew test
```

## 🛠️ Dependencies
- **Framework:** Spring Boot 3.5.15
- **Database:** MongoDB, Elasticsearch
- **Key Modules:** Spring Web, Spring Data MongoDB, Spring Data Elasticsearch, Spring Kafka, Eureka Client, Spring Cloud Config

## 🔗 Related Links
- [Parent Repository](https://github.com/amankrmj09/Blu_s_Bakery)
- [Main Platform README](../README.md)
- [API Reference](./API_REFERENCE.md)
