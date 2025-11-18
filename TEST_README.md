# 🧪 Projet Test Logiciel – Module de Gestion des Commandes Fournisseurs (Tricol)

## 📌 Description
Le projet consiste à développer une suite complète de tests unitaires et tests d’intégration pour le module de gestion des commandes fournisseurs de l’entreprise Tricol.

Ce module couvre :
- Les fournisseurs
- Les produits
- Les commandes fournisseurs
- Les mouvements de stock

Objectifs :
- Vérifier le bon fonctionnement du code
- Tester les interactions entre les composants
- Vérifier les endpoints REST
- Assurer robustesse et qualité via JUnit 5, Mockito, Testcontainers et JaCoCo

## 🎯 Objectifs pédagogiques
- Tester avec JUnit 5
- Mocking avec Mockito
- Tests d’intégration avec Spring Boot Test
- Bases de tests via H2 ou Testcontainers
- Génération de rapport JaCoCo

## 📦 Fonctionnalités à tester

### ### 1. Gestion des fournisseurs
- Création
- Modification
- Suppression
- Consultation

### ### 2. Gestion des produits
- Création
- Suivi

### ### 3. Commandes fournisseurs
- Création
- Validation
- Mise à jour
- Impact sur stock

### ### 4. Mouvements de stock
- Entrées
- Sorties
- Valorisation automatique

### ### 5. Tests des endpoints REST
- MockMvc
- TestRestTemplate

## 🧱 Contraintes fonctionnelles
- Corriger les bugs si détectés
- Tester classes principales
- Ne pas tester les repositories seuls
- Tester cas nominal + erreur

## ⚙️ Contraintes techniques

| Domaine               | Technologie       |
|----------------------|--------------------|
| Tests unitaires      | JUnit 5           |
| Mocking              | Mockito           |
| Tests d’intégration  | Spring Boot Test  |
| Base test            | H2 / Testcontainers |
| Couverture code      | JaCoCo            |
| Backend              | Spring Boot       |
| ORM                  | Spring Data JPA   |

## 🏗️ Technologies utilisées
- Java 17+
- Spring Boot
- Spring Data JPA
- JUnit 5
- Mockito
- Testcontainers
- H2 Database
- JaCoCo
- Maven

## 📁 Structure recommandée

src
└── test
├── java
│   └── com.tricol.gestioncommandes
│       ├── unit
│       │   ├── service
│       │   └── mapper
│       ├── integration
│       │   ├── controller
│       │   └── repository
│       └── utils
└── resources
└── application-test.yml

## 🧪 Types de tests

### ✔️ Tests unitaires
Portent sur :
- Services
- Méthodes métier
- Validations

Utilisent :
- Mockito
- Mocked Beans

### ✔️ Tests d’intégration
Portent sur :
- Endpoin
