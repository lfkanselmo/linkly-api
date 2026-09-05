import http from 'k6/http';
import { check } from 'k6';

export const options = {
  scenarios: {
    redirect_load: {
      executor: 'constant-vus',
      vus: 50,
      duration: '30s',
    },
  },
  thresholds: {
    http_req_duration: ['p(95)<50', 'p(99)<100'],
    http_req_failed: ['rate<0.01'],
  },
};

const SHORT_CODE = __ENV.SHORT_CODE;
const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export default function () {
  const res = http.get(`${BASE_URL}/${SHORT_CODE}`, { redirects: 0 });
  check(res, {
    'status is 302': (r) => r.status === 302,
  });
}
