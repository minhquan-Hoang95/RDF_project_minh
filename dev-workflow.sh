#!/bin/bash

# --- COLORS ---
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}Starting PIR-RDF Plugin Development Workflow...${NC}"

# --- HELPER FUNCTIONS ---
check_error() {
    if [ $? -ne 0 ]; then
        echo -e "${RED}ERROR: $1 failed. Aborting.${NC}"
        exit 1
    fi
}

# --- PLUGIN WORKFLOW ---
update_plugin() {
    echo -e "${GREEN}==> Updating Plugin (Frontend)...${NC}"
    cd plugin || exit
    
    echo "Running React Tests..."
    npm test -- --watchAll=false
    check_error "Plugin tests"

    echo "Building Plugin..."
    npm run build
    check_error "Plugin build"

    cd ..
    echo -e "${GREEN}SUCCESS: Plugin updated in 'plugin/build'. Remember to refresh it in your browser (chrome://extensions or about:debugging).${NC}"
}

# --- API WORKFLOW ---
update_api() {
    echo -e "${GREEN}==> Updating API (Backend)...${NC}"
    cd API || exit

    echo "Running Maven Tests..."
    mvn test
    check_error "API tests"

    cd ..
    echo "Rebuilding API Container..."
    docker compose up -d --build api
    check_error "API container rebuild"

    echo -e "${GREEN}SUCCESS: API is restarted and healthy at http://localhost:3001/api/hello${NC}"
}

# --- DATABASE FIX (METADATA) ---
fix_db() {
    echo -e "${GREEN}==> Ensuring Database Metadata (Roles)...${NC}"
    docker exec -it postgres psql -U postgres -d rdf -c "INSERT INTO roles (id, name) SELECT 1, 'ADMIN' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE id = 1);"
    docker exec -it postgres psql -U postgres -d rdf -c "INSERT INTO roles (id, name) SELECT 2, 'USER' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE id = 2);"
    echo "Database roles verified."
}

# --- USAGE ---
case "$1" in
    plugin)
        update_plugin
        ;;
    api)
        update_api
        ;;
    full)
        update_plugin
        update_api
        ;;
    fix-db)
        fix_db
        ;;
    *)
        echo "Usage: ./dev-workflow.sh {plugin|api|full|fix-db}"
        echo "  plugin: Test and rebuild the browser extension"
        echo "  api: Test and restart the Spring Boot container"
        echo "  full: Run both plugin and api updates"
        echo "  fix-db: Inject missing roles into the database"
        exit 1
        ;;
esac
