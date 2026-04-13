#!/bin/bash

# Project Verification Workflow Script
# This script ensures that all parts of the project are in a working state.

set -e

# Color codes for output
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo "🚀 Starting Project Verification Workflow..."

# 1. API Verification
echo "--- 🔌 Verifying API (Spring Boot) ---"
cd API
echo "Running Unit Tests (TDD Check)..."
if mvn test -Dtest=fr.pir.controller.OtherControllerTest,fr.pir.service.rdf.AnnotationServiceTest; then
    echo -e "${GREEN}✅ API Tests Passed!${NC}"
else
    echo -e "${RED}❌ API Tests Failed!${NC}"
    exit 1
fi

echo "Compiling Project..."
if mvn clean compile; then
    echo -e "${GREEN}✅ API Compilation Successful!${NC}"
else
    echo -e "${RED}❌ API Compilation Failed!${NC}"
    exit 1
fi
cd ..

# 2. Website Verification
echo "--- 🌐 Verifying Website (Vite/React) ---"
if [ -d "website" ]; then
    cd website
    if [ -f "package.json" ]; then
        echo "Installing Dependencies (if needed)..."
        # We skip npm install if node_modules exist to save time
        if [ ! -d "node_modules" ]; then
            npm install
        fi
        echo "Running Build Check..."
        if npm run build; then
            echo -e "${GREEN}✅ Website Build Successful!${NC}"
        else
            echo -e "${RED}❌ Website Build Failed!${NC}"
            exit 1
        fi
    fi
    cd ..
fi

# 3. Plugin Verification
echo "--- 🔌 Verifying Plugin (Chrome Extension) ---"
if [ -d "plugin" ]; then
    cd plugin
    if [ -f "package.json" ]; then
        if [ ! -d "node_modules" ]; then
            npm install
        fi
        echo "Running Build Check..."
        if npm run build; then
            echo -e "${GREEN}✅ Plugin Build Successful!${NC}"
        else
            echo -e "${RED}❌ Plugin Build Failed!${NC}"
            exit 1
        fi
    fi
    cd ..
fi

echo -e "\n${GREEN}✨ ALL SYSTEMS GO! The project is in a healthy and working state.${NC}"
echo "You can now run 'docker compose up -d --build' to see it in the browser."
