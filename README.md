# 💳 Proyecto YaTa – Billetera Digital

Este proyecto es una billetera digital minimalista donde los usuarios pueden:

- Registrarse con su DNI
- Realizar transferencias a otros usuarios
- Ver su historial de movimientos (enviados y recibidos)

Todo esto usando:

- 🟦 Java + Spring Boot
- 🟣 Cassandra como base de datos NoSQL
- 🐳 Docker (solo para la base de datos)
- 🌐 Swagger UI para probar endpoints

---

## 🧾 1. Requisitos previos

Antes de comenzar, asegúrate de tener instalado:

- Docker + Docker Compose
- Java 17+
- Git
- Un IDE (como VS Code o IntelliJ)
- Postman o navegador para usar Swagger (opcional)

---

## 🐳 2. Levantar Cassandra con Docker

Abre una terminal en la raíz del proyecto y ejecuta:

```bash
docker compose -f infra/docker-compose.yml up -d
```
