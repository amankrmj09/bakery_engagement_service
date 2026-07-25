# Bakery Engagement Service API Reference

This document provides a comprehensive reference to the Bakery Engagement Service REST API. 
These APIs are accessible via the API Gateway under the `/api/v1/engagement` path.

---

## 1. Testimonials
**Base Path:** `/api/v1/engagement/testimonials`

### 1.1 Submit a Testimonial
- **Method:** `POST`
- **Path:** `/api/v1/engagement/testimonials`
- **Type of API:** `Public`
- **Request Body:** `Testimonial` (JSON)
  - `name`: string
  - `email`: string
  - `message`: string
  - `rating`: number (1-5)
  - `profileImageUrl`: string (optional)
- **Response Body:** `200 OK`
  Returns the created `Testimonial` object.

### 1.2 Get all Testimonials
- **Method:** `GET`
- **Path:** `/api/v1/engagement/testimonials`
- **Type of API:** `Admin/Protected`
- **Response Body:** `200 OK`
  Returns `List<Testimonial>`.

### 1.3 Search Testimonials
- **Method:** `GET`
- **Path:** `/api/v1/engagement/testimonials/search`
- **Type of API:** `Admin/Protected`
- **Query Params:**
  - `username` (string) - Name to search via Elasticsearch.
- **Response Body:** `200 OK`
  Returns `List<TestimonialDocument>`.

### 1.4 Toggle Featured Status
- **Method:** `PUT`
- **Path:** `/api/v1/engagement/testimonials/{id}/feature`
- **Type of API:** `Admin/Protected`
- **Query Params:**
  - `featured` (boolean) - Whether to feature or unfeature.
- **Response Body:** `200 OK` on success, `400 Bad Request` if attempting to feature more than 5 testimonials.

### 1.5 Get Featured Testimonials
- **Method:** `GET`
- **Path:** `/api/v1/engagement/testimonials/featured`
- **Type of API:** `Public`
- **Response Body:** `200 OK`
  Returns `List<Testimonial>` (Maximum 5 items).

---

## 2. Feedback & Contact
**Base Path:** `/api/v1/engagement/feedback`

### 2.1 Submit Feedback
- **Method:** `POST`
- **Path:** `/api/v1/engagement/feedback`
- **Type of API:** `Public`
- **Request Body:** `Feedback` (JSON)
  - `name`: string
  - `email`: string
  - `message`: string
  - `type`: enum (GENERAL, DELIVERY, PRODUCT, APP, CONTACT_US)
- **Response Body:** `200 OK`
  Returns the created `Feedback` object.

### 2.2 Get all Feedbacks
- **Method:** `GET`
- **Path:** `/api/v1/engagement/feedback`
- **Type of API:** `Admin/Protected`
- **Response Body:** `200 OK`
  Returns `List<Feedback>`.

### 2.3 Search Feedbacks
- **Method:** `GET`
- **Path:** `/api/v1/engagement/feedback/search`
- **Type of API:** `Admin/Protected`
- **Query Params:**
  - `query` (string) - Message text or email to search via Elasticsearch.
- **Response Body:** `200 OK`
  Returns `List<FeedbackDocument>`.
