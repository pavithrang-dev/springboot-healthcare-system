package com.healthcare.patient_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PatientServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PatientServiceApplication.class, args);
	}

}
//Tech Stack Justification
//Backend — Spring Boot + Hibernate ORM
//Why: Spring Boot is the industry standard for Java microservices. Auto-configuration, embedded server, and production-ready features (actuator, health checks) drastically reduce boilerplate.
//Hibernate ORM: Abstracts SQL, provides caching (L1/L2), lazy loading, and database-agnostic queries via HQL/JPQL. Reduces manual JDBC code by ~70%.
//Advantage: Massive ecosystem, battle-tested in enterprise, seamless integration with every other layer in this stack.
//		Frontend — Angular
//Why: TypeScript-first, opinionated framework with built-in routing, forms, HTTP client, and dependency injection. Ideal for enterprise SPAs.
//Advantage: Strong typing catches bugs at compile time, CLI scaffolding accelerates development, and Angular's module system enforces clean architecture. Pairs well with Spring Boot REST APIs.
//Database — PostgreSQL + Flyway
//Why PostgreSQL: Open-source, ACID-compliant, supports JSON/JSONB (hybrid relational + document model), full-text search, and scales well. No licensing cost vs. Oracle/SQL Server.
//Why Flyway: Version-controlled, repeatable database migrations. Every schema change is tracked in Git, making deployments deterministic and rollback-safe.
//		Advantage: Schema changes are code-reviewed and automated — no manual DDL scripts in production.
//Messaging — Apache Kafka
//Why: Distributed, fault-tolerant event streaming. Handles async communication between microservices (decoupling), event sourcing, and high-throughput scenarios (millions of messages/sec).
//Advantage: Services stay loosely coupled. If one service goes down, messages persist in Kafka topics. Enables CQRS and event-driven architecture patterns.
//		Auth — OIDC + Spring Security
//Why: OpenID Connect is the standard for identity (built on OAuth 2.0). Spring Security integrates natively with OIDC providers (Keycloak, Okta, AWS Cognito).
//Advantage: Stateless JWT-based auth, role-based access control (RBAC), and zero custom auth code — delegates to a battle-tested identity provider. Eliminates the #1 OWASP vulnerability (broken authentication).
//Testing — JUnit 5 + Playwright
//Why JUnit 5: Modern Java testing with parameterized tests, nested tests, and extensions. Integrates with Spring Boot's @SpringBootTest for integration testing.
//Why Playwright: Cross-browser E2E testing for Angular UI. Faster and more reliable than Selenium. Supports auto-waiting, network interception, and parallel execution.
//Advantage: Full test pyramid coverage — unit (JUnit) → integration (JUnit + Testcontainers) → E2E (Playwright).
//CI/CD — Maven, JaCoCo, SonarQube, Docker
//Tool	Role
//Maven	Build lifecycle, dependency management, reproducible builds
//JaCoCo	Code coverage reports (enforce minimum thresholds, e.g., 80%)
//SonarQube	Static analysis — bugs, vulnerabilities, code smells, duplication
//Docker	Containerized deployments — consistent across dev/staging/prod
//Advantage: Automated quality gates. Code that fails coverage or has critical SonarQube issues cannot be merged.
//Infrastructure — NGINX + AWS
//NGINX: Reverse proxy, load balancing, SSL termination, and serves Angular static files. Single entry point for both API (/api/* → Spring Boot) and UI (/* → Angular dist).
//AWS: Scalable cloud hosting. Key services:
//ECS/EKS — run Docker containers
//RDS — managed PostgreSQL
//MSK — managed Kafka
//ALB — load balancing
//Cognito — OIDC provider (optional)