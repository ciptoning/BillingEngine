# 💸 BillingEngine

A simple Spring Boot web application to manage billing details and generate loan payment schedules. It features a basic UI, REST-style controllers, and some business logic around user billing information.

---

## 📚 Table of Contents

- [Overview](#-overview)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [How to Run](#-how-to-run)
- [Running Tests](#-running-tests)
- [Endpoints](#-api-endpoints)
- [Summary](#-summary)

---

## 🧾 Overview

BillingEngine is designed for small-scale billing operations such as:
- Managing user billing profiles
- Calculating and viewing loan payment schedules
- Displaying user billing details via a basic web interface

---

## 🛠️ Tech Stack

- **Java 17**
- **Spring Boot 3.5.3**
- **Maven**
- **Thymeleaf (HTML templating)**
- **JPA (non-persistent test storage)**
- **JUnit (for testing)**

---

## 📁 Project Structure

- src/
    - main/
        - java/com/ciptoning/billingengine/
            - BillingEngineApplication.java  # App entry point
            - BillingConstants.java
            - BillingRepository.java
            - LocaleConfig.java
            - user/
                - BillingUser.java
                - LoanService.java
                - PaymentSchedule.java
                - IndexController.java
                - UserDetailController.java
        - resources/
            - templates/              # HTML templates (Thymeleaf)
            - static/                 # CSS, images
            - application.properties  # App config
    - test/                       # Unit tests

---

## 🚀 Getting Started


### ✅ Prerequisites

- Java 17+ ([Oracle](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html))
- Maven 3.8+

Check installed versions:
```bash
java -version
mvn -version
```

---

### ▶️ How to Run

**Using Maven Wrapper:**

```bash
./mvnw spring-boot:run
```

**Or with your local Maven:**

```bash
mvn spring-boot:run
```

**Or with your Java 17+ command:**

```bash
java -jar target/BillingEngine-0.0.1-SNAPSHOT.jar
```

**Or you can double-click .jar file:**

```bash
BillingEngine-0.0.1-SNAPSHOT.jar
```

Once started, the app will be available at:

👉 [http://localhost:8080](http://localhost:8080)

On the main page, if you finish testing it, you can click
```bash
Shutdown
```
to kill the application.

---

### 🧪 Running Tests

To run unit tests:

```bash
mvn test
```

---

### 🌐 API Endpoints
| HTTP Method | Endpoint        | Description (inferred)     |
| ----------- | --------------- |----------------------------|
| `GET`       | `/`             | Main billing dashboard     |
| `GET`       | `/shutdown`     | Trigger system shutdown    |
| `POST`      | `/login`        | User login                 |
| `GET`       | `/user_detail`  | View detailed billing info |
| `POST`      | `/logout`       | User logout                |
| `POST`      | `/create_loan`  | Create a new loan          |
| `POST`      | `/make_payment` | Make a loan payment        |
| `POST`      | `/skip_payment` | Skip a loan payment        |

---

### ✨ Summary

* Start at `BillingEngineApplication.java` – this is the app entry point.
* Controllers like `IndexController` and `UserDetailController` handle the web routes and map them to views.
* Business logic lives in `LoanService`.
* UI templates are found in `src/main/resources/templates`.

---