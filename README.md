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
The source code under `src/main/java` is organized as follows:
```text
src/
└── main/
    └── java/com/blubugtech/bakery_engagement_service/
        ├── controller/ # REST endpoints for engagement interactions
        ├── dto/        # Data Transfer Objects
        ├── entity/     # Database entities mapping to MongoDB (Testimonial, Feedback)
        ├── repository/ # MongoDB repositories
        ├── search/     # Elasticsearch document models and search repositories
        └── service/    # Core logic handling data flow and admin curation constraints
```

## 🌐 API Reference
> [!NOTE]
> For detailed API definitions, request/response bodies, and schemas, please refer to the [API_REFERENCE.md](./API_REFERENCE.md) file or the API Gateway's Swagger UI.

**Key Endpoints:**
- `POST /api/v1/engagement/testimonials` - Submit a new testimonial
- `GET /api/v1/engagement/testimonials` - Retrieve testimonials (with search by username)
- `PUT /api/v1/engagement/testimonials/{id}/feature` - Toggle featuring a testimonial on storefront
- `GET /api/v1/engagement/testimonials/featured` - Retrieve featured testimonials (max 5)
- `POST /api/v1/engagement/feedback` - Submit new feedback or contact message
- `GET /api/v1/engagement/feedback` - Retrieve feedbacks (with query search)

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
- [Main Platform README](../README.md)
- [API Reference](./API_REFERENCE.md)
