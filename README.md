# Healthcare Microservices

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.x-brightgreen)
![Architecture](https://img.shields.io/badge/Architecture-Microservices-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

---

## Overview

Healthcare Microservices is a Spring Boot–based project that demonstrates a real-world healthcare workflow using microservices architecture.

This project currently includes:

- Patient Service – Manages patient details
- Appointment Service – Handles appointment booking
- REST-based inter-service communication
- Layered architecture (Controller → Service → Repository)

## System Components

- **Client** → communicates with Appointment Service
- **Appointment Service** → depends on:
  - Patient Service (via REST)
  - Appointment Database
- **Patient Service** → depends on Patient Database

  
## Technology Stack

 - Java 17
 - Spring Boot
 - Spring Web
 - Spring Data JPA
 - Maven
 - H2 / PostgreSQL
 - REST APIs

## Microservice Concepts Implemented

 - Service-to-service communication using REST
 - Domain-driven service separation
 - Independent databases per service
 - Layered architecture
 - Loose coupling between services

## Future Enhancements

 - OpenFeign Client
 - Eureka Service Discovery
 - API Gateway
 - Resilience4j (Circuit Breaker)
 - Docker & Docker Compose
 - Kafka (Event-driven communication)
 - JWT Authentication
 - Centralized Logging & Monitoring
