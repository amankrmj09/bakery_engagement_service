# 📖 Bakery Engagement Service - API Reference

This document provides a detailed API reference for the **Bakery Engagement Service**. 

- **Direct Service Base URLs:**
  - Public & Customer Endpoints: `/api/engagement`
  - Admin Endpoints: `/api/admin/engagement`
- **API Gateway Base URL:** `/api/v1/engagement` (and `/api/v1/admin/engagement`)

---

## 📋 Table of Contents
- [1. Testimonials](#1-testimonials)
- [2. Feedback & Contact Details](#2-feedback--contact-details)
- [3. Product Reviews](#3-product-reviews)
- [4. Admin Reviews](#4-admin-reviews)
- [5. Data Transfer Objects (DTOs)](#5-data-transfer-objects-dtos)

---

## 1. Testimonials

**Base Path:** `/api/engagement/testimonials`

### 1.1 Create Testimonial
- **HTTP Method:** `POST`
- **Path:** `/api/engagement/testimonials`
- **Access Control:** Public
- **Request Body:** [`TestimonialRequest`](./src/main/java/com/blubugtech/bakery_engagement_service/dto/testimonial/TestimonialRequest.java) (JSON)

  ```json
  {
    "authorName": "Jane Doe",
    "content": "The sourdough bread and cupcakes were absolutely delicious!",
    "rating": 5,
    "role": "Verified Customer",
    "avatarUrl": "https://example.com/avatar.jpg"
  }
  ```
- **Response:** `201 Created`
  - Body: [`TestimonialResponse`](./src/main/java/com/blubugtech/bakery_engagement_service/dto/testimonial/TestimonialResponse.java)

  ```json
  {
    "id": "test_123",
    "authorName": "Jane Doe",
    "content": "The sourdough bread and cupcakes were absolutely delicious!",
    "rating": 5,
    "role": "Verified Customer",
    "avatarUrl": "https://example.com/avatar.jpg",
    "isApproved": true,
    "isFeatured": false,
    "createdAt": "2026-08-05T20:30:00"
  }
  ```

### 1.2 Get All Testimonials
- **HTTP Method:** `GET`
- **Path:** `/api/engagement/testimonials`
- **Access Control:** Public
- **Query Parameters:**
  | Parameter | Type | Default | Description |
  |-----------|------|---------|-------------|
  | `page` | `int` | `0` | Zero-based page index |
  | `size` | `int` | `20` | Page size |
  | `sortBy` | `string` | `createdAt` | Field to sort by |
  | `sortDir` | `string` | `DESC` | Sort direction (`ASC` / `DESC`) |
- **Response:** `200 OK`
  - Body: `PagedModel<[`TestimonialResponse`](./src/main/java/com/blubugtech/bakery_engagement_service/dto/testimonial/TestimonialResponse.java)>`

  ```json
  {
    "content": [
      {
        "id": "test_123",
        "authorName": "Jane Doe",
        "content": "The sourdough bread and cupcakes were absolutely delicious!",
        "rating": 5,
        "role": "Verified Customer",
        "avatarUrl": "https://example.com/avatar.jpg",
        "isApproved": true,
        "isFeatured": false,
        "createdAt": "2026-08-05T20:30:00"
      }
    ],
    "page": {
      "size": 20,
      "number": 0,
      "totalElements": 1,
      "totalPages": 1
    }
  }
  ```

### 1.3 Get Featured Testimonials
- **HTTP Method:** `GET`
- **Path:** `/api/engagement/testimonials/featured`
- **Access Control:** Public
- **Query Parameters:**
  | Parameter | Type | Default | Description |
  |-----------|------|---------|-------------|
  | `page` | `int` | `0` | Zero-based page index |
  | `size` | `int` | `20` | Page size |
  | `sortBy` | `string` | `createdAt` | Field to sort by |
  | `sortDir` | `string` | `DESC` | Sort direction (`ASC` / `DESC`) |
- **Response:** `200 OK`
  - Body: `PagedModel<[`TestimonialResponse`](./src/main/java/com/blubugtech/bakery_engagement_service/dto/testimonial/TestimonialResponse.java)>`

  ```json
  {
    "content": [
      {
        "id": "test_123",
        "authorName": "Jane Doe",
        "content": "The sourdough bread and cupcakes were absolutely delicious!",
        "rating": 5,
        "role": "Verified Customer",
        "avatarUrl": "https://example.com/avatar.jpg",
        "isApproved": true,
        "isFeatured": false,
        "createdAt": "2026-08-05T20:30:00"
      }
    ],
    "page": {
      "size": 20,
      "number": 0,
      "totalElements": 1,
      "totalPages": 1
    }
  }
  ```

### 1.4 Search Testimonials
- **HTTP Method:** `GET`
- **Path:** `/api/engagement/testimonials/search`
- **Access Control:** Public
- **Query Parameters:**
  | Parameter | Type | Default | Description |
  |-----------|------|---------|-------------|
  | `username` | `string` | — | Search term for author username (Elasticsearch query) |
  | `page` | `int` | `0` | Zero-based page index |
  | `size` | `int` | `20` | Page size |
  | `sortBy` | `string` | `createdAt` | Field to sort by |
  | `sortDir` | `string` | `DESC` | Sort direction (`ASC` / `DESC`) |
- **Response:** `200 OK`
  - Body: `PagedModel<[`TestimonialResponse`](./src/main/java/com/blubugtech/bakery_engagement_service/dto/testimonial/TestimonialResponse.java)>`

  ```json
  {
    "content": [
      {
        "id": "test_123",
        "authorName": "Jane Doe",
        "content": "The sourdough bread and cupcakes were absolutely delicious!",
        "rating": 5,
        "role": "Verified Customer",
        "avatarUrl": "https://example.com/avatar.jpg",
        "isApproved": true,
        "isFeatured": false,
        "createdAt": "2026-08-05T20:30:00"
      }
    ],
    "page": {
      "size": 20,
      "number": 0,
      "totalElements": 1,
      "totalPages": 1
    }
  }
  ```

### 1.5 Toggle Featured Status
- **HTTP Method:** `PUT`
- **Path:** `/api/engagement/testimonials/{id}/feature`
- **Access Control:** Admin
- **Path Parameters:**
  - `id` (string): Testimonial ID
- **Query Parameters:**
  - `featured` (boolean, required): Set `true` to feature on storefront, `false` to unfeature
- **Response:** `200 OK`
  - Body: [`TestimonialResponse`](./src/main/java/com/blubugtech/bakery_engagement_service/dto/testimonial/TestimonialResponse.java)

  ```json
  {
    "id": "test_123",
    "authorName": "Jane Doe",
    "content": "The sourdough bread and cupcakes were absolutely delicious!",
    "rating": 5,
    "role": "Verified Customer",
    "avatarUrl": "https://example.com/avatar.jpg",
    "isApproved": true,
    "isFeatured": false,
    "createdAt": "2026-08-05T20:30:00"
  }
  ```

### 1.6 Delete Testimonial
- **HTTP Method:** `DELETE`
- **Path:** `/api/engagement/testimonials/{id}`
- **Access Control:** Admin
- **Path Parameters:**
  - `id` (string): Testimonial ID
- **Response:** `204 No Content`

---

## 2. Feedback & Contact Details

**Base Path:** `/api/engagement/feedback` & `/api/engagement/contact-details`

### 2.1 Create Feedback
- **HTTP Method:** `POST`
- **Path:** `/api/engagement/feedback`
- **Access Control:** Public
- **Request Body:** [`FeedbackRequest`](./src/main/java/com/blubugtech/bakery_engagement_service/dto/feedback/FeedbackRequest.java) (JSON)

  ```json
  {
    "type": "GENERAL",
    "message": "Loved the customer service at the downtown store!",
    "contactEmail": "customer@example.com",
    "name": "Alex Smith"
  }
  ```
- **Response:** `201 Created`
  - Body: [`FeedbackResponse`](./src/main/java/com/blubugtech/bakery_engagement_service/dto/feedback/FeedbackResponse.java)

  ```json
  {
    "id": "feed_123",
    "type": "GENERAL",
    "message": "Loved the customer service at the downtown store!",
    "contactEmail": "customer@example.com",
    "name": "Alex Smith",
    "status": "NEW",
    "createdAt": "2026-08-05T20:30:00"
  }
  ```

### 2.2 Get All Feedbacks
- **HTTP Method:** `GET`
- **Path:** `/api/engagement/feedback`
- **Access Control:** Admin / Protected
- **Query Parameters:**
  | Parameter | Type | Default | Description |
  |-----------|------|---------|-------------|
  | `page` | `int` | `0` | Zero-based page index |
  | `size` | `int` | `20` | Page size |
  | `sortBy` | `string` | `createdAt` | Field to sort by |
  | `sortDir` | `string` | `DESC` | Sort direction (`ASC` / `DESC`) |
- **Response:** `200 OK`
  - Body: `PagedModel<[`FeedbackResponse`](./src/main/java/com/blubugtech/bakery_engagement_service/dto/feedback/FeedbackResponse.java)>`

  ```json
  {
    "content": [
      {
        "id": "feed_123",
        "type": "GENERAL",
        "message": "Loved the customer service at the downtown store!",
        "contactEmail": "customer@example.com",
        "name": "Alex Smith",
        "status": "NEW",
        "createdAt": "2026-08-05T20:30:00"
      }
    ],
    "page": {
      "size": 20,
      "number": 0,
      "totalElements": 1,
      "totalPages": 1
    }
  }
  ```

### 2.3 Search Feedbacks
- **HTTP Method:** `GET`
- **Path:** `/api/engagement/feedback/search`
- **Access Control:** Admin / Protected
- **Query Parameters:**
  | Parameter | Type | Default | Description |
  |-----------|------|---------|-------------|
  | `query` | `string` | — | Full-text query term (Elasticsearch) |
  | `type` | `string` | — | Feedback category/type filter |
  | `page` | `int` | `0` | Zero-based page index |
  | `size` | `int` | `20` | Page size |
  | `sortBy` | `string` | `createdAt` | Field to sort by |
  | `sortDir` | `string` | `DESC` | Sort direction (`ASC` / `DESC`) |
- **Response:** `200 OK`
  - Body: `PagedModel<[`FeedbackResponse`](./src/main/java/com/blubugtech/bakery_engagement_service/dto/feedback/FeedbackResponse.java)>`

  ```json
  {
    "content": [
      {
        "id": "feed_123",
        "type": "GENERAL",
        "message": "Loved the customer service at the downtown store!",
        "contactEmail": "customer@example.com",
        "name": "Alex Smith",
        "status": "NEW",
        "createdAt": "2026-08-05T20:30:00"
      }
    ],
    "page": {
      "size": 20,
      "number": 0,
      "totalElements": 1,
      "totalPages": 1
    }
  }
  ```

### 2.4 Update Feedback Status
- **HTTP Method:** `PUT`
- **Path:** `/api/engagement/feedback/{id}/status`
- **Access Control:** Admin
- **Path Parameters:**
  - `id` (string): Feedback ID
- **Query Parameters:**
  - `status` (string, required): New status string (e.g., `RESOLVED`, `IN_REVIEW`)
- **Response:** `200 OK`
  - Body: [`FeedbackResponse`](./src/main/java/com/blubugtech/bakery_engagement_service/dto/feedback/FeedbackResponse.java)

  ```json
  {
    "id": "feed_123",
    "type": "GENERAL",
    "message": "Loved the customer service at the downtown store!",
    "contactEmail": "customer@example.com",
    "name": "Alex Smith",
    "status": "NEW",
    "createdAt": "2026-08-05T20:30:00"
  }
  ```

### 2.5 Delete Feedback
- **HTTP Method:** `DELETE`
- **Path:** `/api/engagement/feedback/{id}`
- **Access Control:** Admin
- **Path Parameters:**
  - `id` (string): Feedback ID
- **Response:** `204 No Content`

### 2.6 Get Contact Details
- **HTTP Method:** `GET`
- **Path:** `/api/engagement/contact-details`
- **Access Control:** Public
- **Response:** `200 OK`
  - Body: [`ContactDetailsResponse`](./src/main/java/com/blubugtech/bakery_engagement_service/dto/contact/ContactDetailsResponse.java)

  ```json
  {
    "id": "contact_1",
    "address": "123 Bakery Lane, Pastry City, PC 56001",
    "phoneNumbers": ["+1-555-0198", "+1-555-0199"],
    "emails": ["support@blusbakery.com", "info@blusbakery.com"],
    "socialLinks": {
      "instagram": "https://instagram.com/blusbakery",
      "facebook": "https://facebook.com/blusbakery"
    },
    "createdAt": "2026-08-05T20:30:00",
    "updatedAt": "2026-08-05T20:30:00"
  }
  ```

### 2.7 Update Contact Details
- **HTTP Method:** `PUT`
- **Path:** `/api/engagement/contact-details`
- **Access Control:** Admin
- **Request Body:** [`ContactDetailsRequest`](./src/main/java/com/blubugtech/bakery_engagement_service/dto/contact/ContactDetailsRequest.java) (JSON)

  ```json
  {
    "address": "123 Bakery Lane, Pastry City, PC 56001",
    "phoneNumbers": ["+1-555-0198", "+1-555-0199"],
    "emails": ["support@blusbakery.com", "info@blusbakery.com"],
    "socialLinks": {
      "instagram": "https://instagram.com/blusbakery",
      "facebook": "https://facebook.com/blusbakery"
    }
  }
  ```
- **Response:** `200 OK`
  - Body: [`ContactDetailsResponse`](./src/main/java/com/blubugtech/bakery_engagement_service/dto/contact/ContactDetailsResponse.java)

  ```json
  {
    "id": "contact_1",
    "address": "123 Bakery Lane, Pastry City, PC 56001",
    "phoneNumbers": ["+1-555-0198", "+1-555-0199"],
    "emails": ["support@blusbakery.com", "info@blusbakery.com"],
    "socialLinks": {
      "instagram": "https://instagram.com/blusbakery",
      "facebook": "https://facebook.com/blusbakery"
    },
    "createdAt": "2026-08-05T20:30:00",
    "updatedAt": "2026-08-05T20:30:00"
  }
  ```

---

## 3. Product Reviews

**Base Path:** `/api/engagement/reviews`

### 3.1 Add Product Review
- **HTTP Method:** `POST`
- **Path:** `/api/engagement/reviews/product/{id}`
- **Access Control:** Authenticated User (`@PreAuthorize("isAuthenticated()")`)
- **Path Parameters:**
  - `id` (string): Product ID
- **Request Body:** [`ReviewRequest`](./src/main/java/com/blubugtech/bakery_engagement_service/dto/review/ReviewRequest.java) (JSON)

  ```json
  {
    "orderId": "ORD-987654",
    "userId": "usr_12345",
    "userName": "John Baker",
    "rating": 5,
    "comment": "Crispy crust and soft center. Perfect loaf!"
  }
  ```
- **Success Response:** `201 Created`
  - Body: [`ReviewResponse`](./src/main/java/com/blubugtech/bakery_engagement_service/dto/review/ReviewResponse.java)

  ```json
  {
    "id": "rev_123",
    "productId": "prod_1",
    "orderId": "ORD-987654",
    "userId": "usr_12345",
    "userName": "John Baker",
    "rating": 5,
    "comment": "Crispy crust and soft center. Perfect loaf!",
    "createdAt": "2026-08-05T20:30:00",
    "updatedAt": "2026-08-05T20:30:00",
    "isReported": false,
    "isVerifiedPurchase": true,
    "isApproved": true,
    "reportReason": null,
    "reportedAt": null
  }
  ```
- **Error Responses:**
  - `400 Bad Request`: Validation failure or business logic error.
  - `401 Unauthorized`: Not authenticated.

### 3.2 Update Product Review
- **HTTP Method:** `PUT`
- **Path:** `/api/engagement/reviews/product/{id}/{reviewId}`
- **Access Control:** Authenticated User (`@PreAuthorize("isAuthenticated()")`)
- **Path Parameters:**
  - `id` (string): Product ID
  - `reviewId` (string): Review ID
- **Headers:**
  - `X-User-Id` (optional): User ID header for validation
- **Request Body:** [`ReviewUpdateRequest`](./src/main/java/com/blubugtech/bakery_engagement_service/dto/review/ReviewUpdateRequest.java) (JSON)

  ```json
  {
    "rating": 4,
    "comment": "Updated comment: Still delicious, but slightly overbaked this time."
  }
  ```
- **Success Response:** `200 OK`
  - Body: [`ReviewResponse`](./src/main/java/com/blubugtech/bakery_engagement_service/dto/review/ReviewResponse.java)

  ```json
  {
    "id": "rev_123",
    "productId": "prod_1",
    "orderId": "ORD-987654",
    "userId": "usr_12345",
    "userName": "John Baker",
    "rating": 5,
    "comment": "Crispy crust and soft center. Perfect loaf!",
    "createdAt": "2026-08-05T20:30:00",
    "updatedAt": "2026-08-05T20:30:00",
    "isReported": false,
    "isVerifiedPurchase": true,
    "isApproved": true,
    "reportReason": null,
    "reportedAt": null
  }
  ```
- **Error Responses:**
  - `400 Bad Request`: Validation failure or business logic error.
  - `401 Unauthorized`: Not authenticated.
  - `404 Not Found`: Review not found.

### 3.3 Get Product Reviews
- **HTTP Method:** `GET`
- **Path:** `/api/engagement/reviews/product/{id}`
- **Access Control:** Public
- **Path Parameters:**
  - `id` (string): Product ID
- **Query Parameters:**
  | Parameter | Type | Default | Description |
  |-----------|------|---------|-------------|
  | `page` | `int` | `0` | Zero-based page index |
  | `size` | `int` | `20` | Page size |
  | `sortBy` | `string` | `createdAt` | Field to sort by |
  | `sortDir` | `string` | `DESC` | Sort direction (`ASC` / `DESC`) |
- **Success Response:** `200 OK`
  - Body: `PagedModel<[`ReviewResponse`](./src/main/java/com/blubugtech/bakery_engagement_service/dto/review/ReviewResponse.java)>`

  ```json
  {
    "content": [
      {
        "id": "rev_123",
        "productId": "prod_1",
        "orderId": "ORD-987654",
        "userId": "usr_12345",
        "userName": "John Baker",
        "rating": 5,
        "comment": "Crispy crust and soft center. Perfect loaf!",
        "createdAt": "2026-08-05T20:30:00",
        "updatedAt": "2026-08-05T20:30:00",
        "isReported": false,
        "isVerifiedPurchase": true,
        "isApproved": true,
        "reportReason": null,
        "reportedAt": null
      }
    ],
    "page": {
      "size": 20,
      "number": 0,
      "totalElements": 1,
      "totalPages": 1
    }
  }
  ```

### 3.4 Delete Product Review
- **HTTP Method:** `DELETE`
- **Path:** `/api/engagement/reviews/product/{id}/{reviewId}`
- **Access Control:** Authenticated User (`@PreAuthorize("isAuthenticated()")`)
- **Path Parameters:**
  - `id` (string): Product ID
  - `reviewId` (string): Review ID
- **Headers:**
  - `X-User-Id` (optional): User ID header
- **Success Response:** `204 No Content`
- **Error Responses:**
  - `401 Unauthorized`: Not authenticated.
  - `404 Not Found`: Review not found.

### 3.5 Report Product Review
- **HTTP Method:** `POST`
- **Path:** `/api/engagement/reviews/product/{id}/{reviewId}/report`
- **Access Control:** Authenticated User (`@PreAuthorize("isAuthenticated()")`)
- **Path Parameters:**
  - `id` (string): Product ID
  - `reviewId` (string): Review ID
- **Request Body:** JSON
  ```json
  {
    "reason": "Spam or irrelevant text"
  }
  ```
- **Success Response:** `200 OK`
- **Error Responses:**
  - `400 Bad Request`: Business logic error.
  - `401 Unauthorized`: Not authenticated.
  - `404 Not Found`: Review not found.

---

## 4. Admin Reviews

**Base Path:** `/api/admin/engagement/reviews`

### 4.1 Get Reported Reviews
- **HTTP Method:** `GET`
- **Path:** `/api/admin/engagement/reviews/reported`
- **Access Control:** Admin (`@PreAuthorize("hasRole('ADMIN')")`)
- **Query Parameters:**
  | Parameter | Type | Default | Description |
  |-----------|------|---------|-------------|
  | `page` | `int` | `0` | Zero-based page index |
  | `size` | `int` | `20` | Page size |
  | `sortBy` | `string` | `createdAt` | Field to sort by |
  | `sortDir` | `string` | `DESC` | Sort direction (`ASC` / `DESC`) |
- **Response:** `200 OK`
  - Body: `PagedModel<[`ReviewResponse`](./src/main/java/com/blubugtech/bakery_engagement_service/dto/review/ReviewResponse.java)>`

  ```json
  {
    "content": [
      {
        "id": "rev_123",
        "productId": "prod_1",
        "orderId": "ORD-987654",
        "userId": "usr_12345",
        "userName": "John Baker",
        "rating": 5,
        "comment": "Crispy crust and soft center. Perfect loaf!",
        "createdAt": "2026-08-05T20:30:00",
        "updatedAt": "2026-08-05T20:30:00",
        "isReported": false,
        "isVerifiedPurchase": true,
        "isApproved": true,
        "reportReason": null,
        "reportedAt": null
      }
    ],
    "page": {
      "size": 20,
      "number": 0,
      "totalElements": 1,
      "totalPages": 1
    }
  }
  ```

### 4.2 Dismiss Review Report
- **HTTP Method:** `POST`
- **Path:** `/api/admin/engagement/reviews/{reviewId}/dismiss-report`
- **Access Control:** Admin (`@PreAuthorize("hasRole('ADMIN')")`)
- **Path Parameters:**
  - `reviewId` (string): Review ID
- **Response:** `200 OK`

---

## 5. Data Transfer Objects (DTOs)

### [TestimonialRequest](./src/main/java/com/blubugtech/bakery_engagement_service/dto/testimonial/TestimonialRequest.java)
| Field | Type | Validation | Description |
|-------|------|------------|-------------|
| `authorName` | String | `@NotBlank`, Max 100 | Name of testimonial author |
| `content` | String | `@NotBlank`, Max 1000 | Testimonial text content |
| `rating` | Integer | Min 1, Max 5 | Customer rating (1-5) |
| `role` | String | Max 100 | Role/designation (e.g. Regular Customer) |
| `avatarUrl` | String | Max 255 | Optional avatar image URL |

### [TestimonialResponse](./src/main/java/com/blubugtech/bakery_engagement_service/dto/testimonial/TestimonialResponse.java)
| Field | Type | Description |
|-------|------|-------------|
| `id` | String | Unique ID |
| `authorName` | String | Author name |
| `content` | String | Testimonial text |
| `rating` | Integer | Rating (1-5) |
| `role` | String | Role |
| `avatarUrl` | String | Avatar image URL |
| `isApproved` | boolean | Moderation status |
| `isFeatured` | boolean | Featured status for storefront |
| `createdAt` | LocalDateTime | Timestamp created |

### [FeedbackRequest](./src/main/java/com/blubugtech/bakery_engagement_service/dto/feedback/FeedbackRequest.java)
| Field | Type | Validation | Description |
|-------|------|------------|-------------|
| `type` | String | `@NotBlank`, Max 50 | Category (e.g., `GENERAL`, `DELIVERY`, `PRODUCT`) |
| `message` | String | `@NotBlank`, Max 2000 | Feedback message content |
| `contactEmail` | String | `@Email`, Max 255 | Contact email address |
| `name` | String | Max 100 | Submitter's name |

### [FeedbackResponse](./src/main/java/com/blubugtech/bakery_engagement_service/dto/feedback/FeedbackResponse.java)
| Field | Type | Description |
|-------|------|-------------|
| `id` | String | Unique ID |
| `type` | String | Category |
| `message` | String | Message text |
| `contactEmail` | String | Contact email |
| `name` | String | Name |
| `status` | String | Status (default `NEW`) |
| `createdAt` | LocalDateTime | Timestamp created |

### [ContactDetailsRequest](./src/main/java/com/blubugtech/bakery_engagement_service/dto/contact/ContactDetailsRequest.java)
| Field | Type | Validation | Description |
|-------|------|------------|-------------|
| `address` | String | `@NotBlank` | Bakery location address |
| `phoneNumbers` | List\<String\> | Optional | List of contact numbers |
| `emails` | List\<String\> | Optional | List of contact email addresses |
| `socialLinks` | Map\<String, String\> | Optional | Map of social media handles/links |

### [ContactDetailsResponse](./src/main/java/com/blubugtech/bakery_engagement_service/dto/contact/ContactDetailsResponse.java)
| Field | Type | Description |
|-------|------|-------------|
| `id` | String | Document ID |
| `address` | String | Bakery location address |
| `phoneNumbers` | List\<String\> | Contact numbers |
| `emails` | List\<String\> | Contact emails |
| `socialLinks` | Map\<String, String\> | Social media links |
| `createdAt` | LocalDateTime | Timestamp created |
| `updatedAt` | LocalDateTime | Timestamp updated |

### [ReviewRequest](./src/main/java/com/blubugtech/bakery_engagement_service/dto/review/ReviewRequest.java)
| Field | Type | Validation | Description |
|-------|------|------------|-------------|
| `orderId` | String | `@NotBlank` | Order ID associated with purchase |
| `userId` | String | `@NotBlank` | User ID of reviewer |
| `userName` | String | `@NotBlank` | Display name of reviewer |
| `rating` | Integer | `@NotNull`, Min 1, Max 5 | Product rating (1-5) |
| `comment` | String | Optional | Written review comment |

### [ReviewUpdateRequest](./src/main/java/com/blubugtech/bakery_engagement_service/dto/review/ReviewUpdateRequest.java)
| Field | Type | Validation | Description |
|-------|------|------------|-------------|
| `rating` | Integer | `@NotNull`, Min 1, Max 5 | Updated rating (1-5) |
| `comment` | String | Optional | Updated written review comment |

### [ReviewResponse](./src/main/java/com/blubugtech/bakery_engagement_service/dto/review/ReviewResponse.java)
| Field | Type | Description |
|-------|------|-------------|
| `id` | String | Unique review ID |
| `productId` | String | Product ID |
| `orderId` | String | Order ID |
| `userId` | String | User ID |
| `userName` | String | User display name |
| `rating` | Integer | Rating (1-5) |
| `comment` | String | Written comment |
| `createdAt` | LocalDateTime | Created timestamp |
| `updatedAt` | LocalDateTime | Last updated timestamp |
| `isReported` | Boolean | Whether review is reported for moderation |
| `isVerifiedPurchase` | Boolean | Verified purchase status |
| `isApproved` | Boolean | Moderation approval status |
| `reportReason` | String | Reason given if reported |
| `reportedAt` | LocalDateTime | Reported timestamp |
