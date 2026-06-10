# ZenithCRM - Sistema Gerencial para Psicologas

MVP com backend em Java 21 + Spring Boot + JWT + PostgreSQL e frontend separado em HTML, CSS e JS.

## Estrutura

```text
.
├── src/              # Backend Spring Boot somente API
├── frontend/         # Frontend independente
├── docker-compose.yml
└── pom.xml
```

## Funcionalidades do MVP

- Cadastro, edicao e exclusao de pacientes.
- Agenda de atendimentos com edicao e link de criacao do evento no Google Agenda.
- Controle financeiro por paciente, com status pago, pendente, atrasado e cancelado.
- Resumo financeiro com totais recebidos, pendentes e atrasados.
- Documentos por paciente para relatorios, anamneses, atestados, evolucoes e outros registros.
- Autenticacao com cadastro/login e JWT.

## Como executar

1. Suba o PostgreSQL:

```bash
docker compose up -d
```

2. Execute o backend:

```bash
mvn spring-boot:run
```

3. Em outro terminal, execute o frontend:

```bash
cd frontend
npm run dev
```

4. Abra o sistema:

```text
http://localhost:5173
```

A API ficara em:

```text
http://localhost:8080
```

## Variaveis de ambiente

```text
DATABASE_URL=jdbc:postgresql://localhost:5432/zenith_psychology
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=postgres
JWT_SECRET=troque-por-uma-chave-longa-em-producao
JWT_EXPIRATION_MINUTES=720
```

## Observacoes

A integracao com Google Agenda no MVP gera um link oficial para criar o evento no calendario do usuario. A estrutura ja guarda `googleEventId`, deixando espaco para uma etapa futura com OAuth e Google Calendar API.

Tambem fica reservado o caminho evolutivo para IA, por exemplo geracao assistida de documentos, analise de inadimplencia, sumarizacao de historico e apoio na organizacao de anamnese.
