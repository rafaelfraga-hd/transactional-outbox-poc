# Transactional Outbox POC

This project is a Proof of Concept (POC) demonstrating how to implement an **asynchronous ingestion flow** using the **Transactional Outbox pattern**, along with **idempotency** and **safe concurrent processing**.

## 🎯 Goal

The main goal of this project is to simulate a real-world scenario where:

- An API receives requests and returns **202 Accepted**
- Data is stored reliably
- Processing happens asynchronously
- The system avoids **data inconsistency (dual write problem)**
- The system supports **horizontal scaling of workers**
- Duplicate requests are handled via **idempotency keys**

---

## 🧠 Problem Being Solved

A very common pattern in distributed systems:

1. Receive request
2. Persist data in database
3. Publish event (Kafka, queue, etc.)

The issue:

- If DB write succeeds but event publishing fails → **data inconsistency**
- If event is published but DB transaction fails → **invalid state**

This is known as the **dual write problem**.

---

## 💡 Solution: Transactional Outbox

Instead of writing to two systems:

- Persist business data
- Persist event in an **outbox table (same transaction)**

Then:

- A worker reads from the outbox
- Processes the event
- Marks it as processed

This guarantees **atomicity and reliability**.

---

## 🏗️ Architecture Overview

### Flow

1. Client calls `POST /ingestions`
2. API:
    - Validates idempotency key
    - Stores request in `ingestion_request`
    - Stores event in `outbox_event`
    - Returns `202 Accepted` with protocol
3. Worker:
    - Reads from `outbox_event`
    - Claims records safely (no duplication)
    - Processes the data
    - Updates status
4. Client calls `GET /ingestions/{protocol}` to check status

---

## 🔑 Key Concepts

### Idempotency

- Each request must provide an `Idempotency-Key`
- Prevents duplicate processing
- Enforced via unique constraint in DB

---

### Transactional Outbox

- Business data + event are saved in the same transaction
- Avoids dual write problem

---

### Concurrent Workers (Safe Scaling)

- Multiple workers can run in parallel
- Uses `FOR UPDATE SKIP LOCKED` to avoid processing the same record twice

---

## 🧱 Tech Stack

- Kotlin
- Spring Boot
- Maven
- PostgreSQL
- Flyway
- Docker

---

## 🗄️ Database Structure

### ingestion_request

Stores incoming requests and their processing status.

### outbox_event

Stores events to be processed asynchronously.

---

## 🚀 Running the project

### 1. Start PostgreSQL

```bash
docker compose up -d