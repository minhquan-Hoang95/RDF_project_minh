# PIR RDF plugin

## .env

### Locally

Take a look at the different README.md.

### Docker

Copy the .env.example to the .env and change variables (SMTP).

## RUN

### Locally

See locally README.md.

### Docker

```sh
docker compose up -d --build
```

## ON

Postgres run on [http://localhost:5432](http://localhost:5432).<br>
API run on [http://localhost:3001](http://localhost:3001).<br>
Website run on [http://localhost:8080](http://localhost:8080).

You can create an account and in the DB change `role_id` of `2` by `1` for an admin account (getAllUsers).
