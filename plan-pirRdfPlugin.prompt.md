# Plan: Construire et Déployer le Projet PIR RDF Plugin

## Vue d'ensemble

Le projet est une application multi-services composée de :
- **API** : Spring Boot 3.4.5 (Java 17) avec PostgreSQL
- **Plugin** : React (annotation plugin pour navigateur)
- **Website** : Vite + Vue/React
- **Database** : PostgreSQL
- **Orchestration** : Docker Compose

Les services communiquent entre eux via une API REST sécurisée par JWT.

## Problèmes identifiés

1. **Dépendances Maven manquantes** : Bucket4j 7.10.0 n'existe pas dans Maven Central
2. **Configuration SMTP manquante** : Les identifiants SMTP doivent être configurés
3. **Fichiers de configuration manquants** : Les fichiers `.env` et `application.properties` manquent
4. **JWT_SECRET manquant** : Variable d'environnement non définie pour Docker
5. **Plugin non buildé** : Le plugin doit être compilé avant utilisation

## Plan d'action

### Phase 1 : Configuration des fichiers

#### 1.1 Créer le fichier `.env` racine
- Copier `.env.example` vers `.env`
- Configurer les identifiants PostgreSQL
- Configurer les variables CORS
- Configurer les identifiants SMTP
- Générer et ajouter JWT_SECRET (minimum 64 caractères)
- Configurer les URLs API pour les services frontend

**Fichier** : `/.env`
**Variables critiques** :
```
POSTGRES_USER=postgres
POSTGRES_PASSWORD=<strong_password>
POSTGRES_DB=rdf
POSTGRES_PORT=5432

SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=<your_email>
SPRING_MAIL_PASSWORD=<app_password>

JWT_SECRET=<64_chars_random_string>

CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:5173,http://localhost:8080
CORS_ALLOWED_METHODS=GET,POST,PUT,DELETE,OPTIONS
CORS_ALLOWED_HEADERS=*

API_BASE_URL=http://localhost:3001/api
REACT_APP_API_BASE_URL=http://localhost:3001/api
VITE_APP_API_BASE_URL=http://localhost:3001/api
```

#### 1.2 Créer `API/src/main/resources/application.properties`
- Copier `application.properties.example` vers `application.properties`
- Adapter les propriétés Spring selon l'environnement
- Configurer les variables SMTP
- Configurer la connexion PostgreSQL

### Phase 2 : Correction des dépendances Maven

#### 2.1 Corriger le pom.xml
- Remplacer `io.github.bucket4j:bucket4j-core:7.10.0` par `com.github.vladimir-bukhtoyarov:bucket4j-core:7.4.0`
- Vérifier la compatibilité de toutes les dépendances avec Spring Boot 3.4.5
- Valider les dépendances de sécurité (CVE)

**Fichier** : `/API/pom.xml`
**Changement** :
```xml
<!-- Rate Limiting with Bucket4j -->
<dependency>
    <groupId>com.github.vladimir-bukhtoyarov</groupId>
    <artifactId>bucket4j-core</artifactId>
    <version>7.4.0</version>
</dependency>
```

#### 2.2 Build et test Maven
```bash
cd API
mvn clean install -DskipTests
```

### Phase 3 : Exécution locale (optionnel)

#### 3.1 Préparer l'environnement local
- Installer PostgreSQL 12+ en local
- Créer la base de données `rdf`
- Installer Java 17+
- Installer Maven 3.8+
- Installer Node.js 18+

#### 3.2 Exécuter l'API localement
```bash
cd API
mvn spring-boot:run
# Accès : http://localhost:3001/api
# Swagger : http://localhost:3001/swagger.html
```

#### 3.3 Exécuter le Plugin localement
```bash
cd plugin
npm install
npm run dev
# Accès : http://localhost:3000
# Build : npm run build
# Charger dans Chrome : chrome://extensions → Load Extension → sélectionner plugin/build
```

#### 3.4 Exécuter le Website localement
```bash
cd website
npm install
npm run dev
# Accès : http://localhost:5173
```

### Phase 4 : Déploiement Docker

#### 4.1 Builder et lancer les services
```bash
# À la racine du projet
docker compose up -d --build
```

**Services disponibles** :
- **PostgreSQL** : `localhost:5432` (variable POSTGRES_PORT)
- **API** : `http://localhost:3001` (variable API_PORT)
- **Website** : `http://localhost:8080` (variable WEBSITE_PORT)
- **Plugin build** : Volume monté sur API

**Temps de démarrage** :
- PostgreSQL : ~10s avant readiness
- API : ~30-40s après PostgreSQL (Flyway migrations)
- Website : ~5s
- Plugin build : ~2-3min (npm install + build)

#### 4.2 Vérifier la santé des services
```bash
# Vérifier les logs
docker compose logs -f api

# Vérifier les conteneurs
docker compose ps

# Tester l'API
curl http://localhost:3001/api/hello
```

### Phase 5 : Configuration initiale et utilisation

#### 5.1 Créer un compte utilisateur
1. Accéder au website : `http://localhost:8080`
2. S'enregistrer avec email + mot de passe
3. Un email de confirmation sera envoyé (configurer SMTP pour recevoir)

#### 5.2 Passer l'utilisateur en admin
```bash
# Dans la base de données PostgreSQL
docker compose exec postgres psql -U postgres -d rdf

# Exécuter le SQL
UPDATE users SET role_id = 1 WHERE id = <user_id>;
```

#### 5.3 Charger le plugin dans Chrome
1. Ouvrir `chrome://extensions`
2. Activer "Mode développeur" (coin supérieur droit)
3. Cliquer "Load unpacked"
4. Sélectionner le dossier `/plugin/build`
5. Activer l'extension

### Phase 6 : Configuration SMTP

#### 6.1 Utiliser Gmail (recommandé pour dev)
1. Activer l'authentification 2FA sur le compte Gmail
2. Générer une "App Password" : https://myaccount.google.com/apppasswords
3. Dans `.env` :
```
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=votre.email@gmail.com
SPRING_MAIL_PASSWORD=<app_password_generee>
```

#### 6.2 Utiliser SendGrid
1. Créer un compte SendGrid
2. Générer une clé API
3. Dans `.env` :
```
SPRING_MAIL_HOST=smtp.sendgrid.net
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=apikey
SPRING_MAIL_PASSWORD=<sendgrid_api_key>
```

#### 6.3 Utiliser MailHog (local dev - pas d'email réel)
```bash
docker run -p 1025:1025 -p 8025:8025 mailhog/mailhog

# Dans .env :
SPRING_MAIL_HOST=localhost
SPRING_MAIL_PORT=1025

# Voir les emails : http://localhost:8025
```

### Phase 7 : Validation et tests

#### 7.1 Valider les endpoints API
```bash
# Health check
curl http://localhost:3001/api/hello

# Swagger UI
curl http://localhost:3001/swagger-ui.html
```

#### 7.2 Tester l'authentification JWT
```bash
# Créer un compte
curl -X POST http://localhost:3001/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email":"test@example.com",
    "password":"Password123!"
  }'

# Se connecter et récupérer le token JWT
curl -X POST http://localhost:3001/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email":"test@example.com",
    "password":"Password123!"
  }'
```

#### 7.3 Tester le plugin
1. Naviguer vers un site web
2. Cliquer sur l'icône du plugin
3. Les annotations doivent être récupérées depuis l'API

## Troubleshooting

### "No plugin found for prefix 'spring-boot'"
**Solution** : Exécuter `mvn clean install` avant `mvn spring-boot:run`

### "Could not resolve dependencies"
**Solution** : Mettre à jour les dépendances Maven et vérifier la version 7.4.0 pour bucket4j

### "Connection refused" pour PostgreSQL
**Solution** : Vérifier que le conteneur PostgreSQL est en cours d'exécution et sain
```bash
docker compose ps
docker compose logs postgres
```

### "SMTP authentication failed"
**Solution** : Vérifier les identifiants SMTP et que l'authentification 2FA/app password est activée

### Plugin ne charge pas
**Solution** : Vérifier que `npm run build` a créé le dossier `plugin/build` avec les fichiers compilés

## Structure des fichiers clés

```
/
├── .env                                    # À créer
├── docker-compose.yml                      # Déploiement Docker
├── API/
│   ├── pom.xml                            # Dépendances Maven (à corriger)
│   ├── Dockerfile                         # Build API
│   └── src/main/resources/
│       ├── application.properties          # À créer (à partir de .example)
│       └── application.properties.example
├── plugin/
│   ├── package.json                       # Dépendances npm
│   ├── Dockerfile                         # Build plugin
│   ├── .env.example
│   └── public/manifest.json               # Config extension Chrome
├── website/
│   ├── package.json                       # Dépendances npm
│   ├── Dockerfile                         # Build website
│   └── .env.example
└── postgres/
    ├── Dockerfile                         # Setup PostgreSQL
    └── init.sh                            # Migrations Flyway
```

## Checkpoints de validation

- [ ] Fichier `.env` créé avec tous les secrets configurés
- [ ] `API/src/main/resources/application.properties` créé
- [ ] `pom.xml` corrigé (bucket4j 7.4.0)
- [ ] `mvn clean install -DskipTests` réussit
- [ ] `docker compose up -d --build` démarre tous les services
- [ ] API répond à `http://localhost:3001/api/hello`
- [ ] Website accessible à `http://localhost:8080`
- [ ] Compte utilisateur créé et SMTP fonctionne
- [ ] Plugin chargé dans Chrome et fonctionnel

## Prochaines étapes après déploiement

1. **Configurer le CI/CD** (GitHub Actions, GitLab CI)
2. **Ajouter des tests unitaires et d'intégration**
3. **Documenter l'API avec OpenAPI/Swagger**
4. **Mettre en place le monitoring et les logs**
5. **Configurer la sauvegarde de base de données**
6. **Déployer sur un serveur de production**

