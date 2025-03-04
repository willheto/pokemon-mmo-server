
<h1>g8e</h1>

**early development**

## About the project

Open-source MMORPG game-engine/server written from scratch. 600ms tick-based game loop, optimized to support up to 1000+ players online (on the same chunk!) at the same time. Supports serving game assets, models, textures and more from server side. Also includes update server, login server, and register server. Chunking strategy is implemented, and because of that and custom encoding of packets, game remains playable even on 56kbps connection.

This reposiroty also includes register server, update server and login server.

## Register server

Constains one simple /create-account endpoint.

## Update server

The game engine/server supports serving assets from server side. Players first connect to update server, which checks if current cache is stil valid,
or if there is an update available. If update is available, the update server packs and sends new assets to the client.

## Login server

Login server handles login, logout, count players and reset world requests.

## Getting Started

1. Install docker and gradle
2. Run docker-compose up -d on root to initialize database container
3. Run gradle run --args="migrate" to run migrations
4. Run gradle run to spin up the server

## Technologies used

1. Java 21
2. Gradle
3. Docker
4. MariaDB


