# Prueba de carga de la redirección

Verifica RNF-01 (redirección con acierto de caché < 50ms) con 50 usuarios virtuales constantes
durante 30 segundos.

```bash
CODE=$(curl -s -X POST http://localhost:8080/api/v1/urls \
  -H "Content-Type: application/json" \
  -d '{"originalUrl": "https://example.com/load-test"}' | grep -o '"shortCode":"[^"]*"' | cut -d'"' -f4)

docker run --rm -i --add-host=host.docker.internal:host-gateway \
  -e SHORT_CODE="$CODE" -e BASE_URL=http://host.docker.internal:8080 \
  grafana/k6 run - < load-tests/redirect.js
```

## Último resultado (2026-09-04)

50 VUs, 30s, 100.881 requests, 0% de fallas:

| Métrica | Valor |
| :--- | :--- |
| p50 | 12.34ms |
| p90 | 23.44ms |
| p95 | 28.86ms |
| p99 | 43.81ms |
| Throughput | ~3.362 req/s |

Bien dentro del objetivo de <50ms en p95.
