# 💬 Engagement Service

![Java](https://img.shields.io/badge/Java-21%2B-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)

Welcome to the **Engagement Service**, a core component of the Blu's Bakery Microservice Platform that handles customer feedback, testimonials, and contact inquiries.

## 📑 Table of Contents
- [Features](#-features)
- [Folder Structure](#-folder-structure)
- [Dependencies](#-dependencies)
- [Endpoints](#-endpoints)
- [How to Run](#-how-to-run)
- [Related Links](#-related-links)

## ✨ Features
- Complete management for customer feedbacks and testimonials.
- Admin curation to feature specific testimonials on the storefront (limited to max 5).
- Fast and reliable search capabilities using Elasticsearch.
- Automatic synchronous saving to MongoDB and asynchronous indexing to Elasticsearch.

## 📁 Folder Structure
The main `src/main/java` directory is organized as follows:
```text
src/
└── main/
    └── java/.../bakery_engagement_service/
        ├── controller/ # REST endpoints for engagement interactions.
        ├── entity/     # Database entities mapping to MongoDB (Testimonial, Feedback).
        ├── repository/ # MongoDB repositories.
        ├── search/     # Elasticsearch document models and search repositories.
        └── service/    # Core logic handling data flow and admin curation constraints.
```

## 🛠️ Dependencies
- **Framework:** Spring Boot
- **Database & Search:** MongoDB (Primary DB), Elasticsearch (Search Index)
- **Key Modules:** Eureka Client, Spring Data JPA, Spring Data Elasticsearch

## 🌐 Endpoints
> [!NOTE]
> For complete and detailed API definitions, please refer to the `API_REFERENCE.md` or OpenAPI available via the API Gateway's Swagger UI.

- `POST /api/v1/engagement/testimonials` - Submit a new testimonial.
- `GET /api/v1/engagement/testimonials` - Retrieve testimonials (with search by username).
- `PUT /api/v1/engagement/testimonials/{id}/feature` - Toggle featuring a testimonial on storefront.
- `GET /api/v1/engagement/testimonials/featured` - Retrieve featured testimonials (max 5).
- `POST /api/v1/engagement/feedback` - Submit new feedback or contact message.
- `GET /api/v1/engagement/feedback` - Retrieve feedbacks (with query search).

## 🚀 How to Run

1. **Clone the repository:**
   ```bash
   git clone https://github.com/amankrmj01/bakery.git
   cd bakery/bakery_engagement_service
   ```

2. **Configure Environment:**
   Ensure your `.env` properties (including MongoDB and ES connections) are set appropriately.

3. **Run the application:**
   ```bash
   ./gradlew bootRun
   ```

## 🔗 Related Links
- [Main Platform README](../README.md)
- [API Reference](API_REFERENCE.md)
