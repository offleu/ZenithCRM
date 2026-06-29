FUTURO
TENTANDO DPLOY
segundo deploy

## Upgrade backend - atendimento e financeiro

### Pacientes

O cadastro/edicao de paciente agora recebe `sessionValue`, valor da sessao usado no financeiro automatico:

```json
{
  "fullName": "Nome do paciente",
  "phone": "11999999999",
  "email": "paciente@email.com",
  "birthDate": "1990-01-01",
  "cpf": "00000000000",
  "sessionValue": 180.00,
  "clinicalNotes": "Observacoes"
}
```

### Agenda do dia

```http
GET /api/appointments/day
GET /api/appointments/day?date=2026-06-29
```

### Fluxo do atendimento

```http
POST /api/appointments/{id}/start
POST /api/appointments/{id}/finish
POST /api/appointments/{id}/absence
```

- `start`: marca o atendimento como `IN_PROGRESS` e grava `startedAt`.
- `finish`: exige atendimento iniciado, marca como `COMPLETED` e cria um pagamento `PENDING` com o `sessionValue` do paciente.
- `absence`: marca como `ABSENT` e nao cria lancamento financeiro.
