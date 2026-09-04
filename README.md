# linkly-api

Acortador de URLs con analítica de clics en tiempo real. Arquitectura hexagonal sobre Spring Boot
4.1 y Java 25. El diseño completo, con las decisiones y el porqué de cada una, está en
[`SAD_Linkly_Acortador_Analitica.md`](../SAD_Linkly_Acortador_Analitica.md).

## Desarrollo local

Levantar Postgres:

```bash
docker compose -f docker/docker-compose.yml up -d
```

Copiar las variables de entorno y correr la app:

```bash
cp .env.example .env
mvn spring-boot:run
```

La API queda en `http://localhost:8080`.

## Tests

```bash
mvn verify
```

Las pruebas de integración usan Testcontainers — levantan su propio Postgres en Docker, no
dependen del `docker compose` de arriba.
