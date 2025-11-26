# Pokedex Service

A lightweight backend service that provides Pokémon metadata, user authentication, token-based authorization, server-side pagination, and user Pokémon collections.
This project was created as a coding exercise but is structured like a real-world microservice.

## Description

Pokedex Service is a Spring Boot application exposing a REST API for:
* User registration & login (JWT-based authentication)
* Browsing Pokémon (server-side pagination)
* Filtering Pokémon by name/type
* Retrieving a single Pokémon
* Managing a user collection (add/remove Pokémon)
* The system supports both authenticated users (via JWT) and guests, where guests can access only non-restricted API endpoints without providing any token

A prebuilt Pokémon JSON dataset is loaded on startup and cached in memory as both a List and a Map for fast lookup.

The service runs fully via Docker Compose, which starts both the Spring Boot app and a PostgreSQL database.

## Getting Started

### Dependencies

Before running the project you will need:
* Docker Desktop (Windows/macOS/Linux)
* Java 17+
* OS: Windows 10/11, macOS, Linux
No manual database installation is required — Docker starts it automatically.

### Installing

* Clone the repository and enter the project directory:
```
git clone <your repo url>
cd pokedex-service
```
* Build and start all services (app + PostgreSQL):
```
docker compose up --build
```

This will:
* Build pokedex-service Docker image
* Start PostgreSQL on port 5432 (or your configured port)
* Start the Spring Boot service on 8079

### Executing program

Available API Endpoints
* /api/v1/all						(GET)		retrieve all Pokemons
* /api/v1/list						(GET)		retrieve a bulk of Pokemons with pagination filtering options
* /api/v1/pokemon/{id}				(GET)		get Pokemon details by id
* /api/v1/register					(POST)		register with email, username (optional) and password
* /api/v1/login						(POST) 		login to the system with email and password (return JWT)
* /api/v1/collection				(GET)		get user's owned Pokemons
* /api/v1/collection/{pokemonId}	(POST)		add Pokemon (by id) to user's Pokemons collection
* /api/v1/collection/{pokemonId}	(DELETE)	remove Pokemon (by id) from user's Pokemons collection

## Frontend UI

A simple client-side UI was added to make interacting with the Pokedex Service easier.

**UI URL:**
http://localhost:8079/home

The UI allows you to:
* Register & Login (JWT-based authentication)
* Browse all Pokémon
* Filter by name or type
* View Pokémon details
* Manage your personal Pokémon collection

## Help

Check container logs:
```
docker logs pokedex-app
```
Connect manually to PostgreSQL:
```
docker exec -it pokedex-db bash
psql -U pokemon -d pokedex
```

## Authors

Tomer
Email: tommery@gmail.com

## Acknowledgments

* ChatGPT
* GitHub Copilot
* Claude
