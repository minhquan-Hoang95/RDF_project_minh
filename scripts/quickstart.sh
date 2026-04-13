#!/bin/bash
# Quick start script for the PIR RDF Plugin project

set -e

echo "🚀 PIR RDF Plugin - Quick Start Script"
echo "======================================"

# Check prerequisites
echo "📋 Checking prerequisites..."

if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed. Please install Docker first."
    exit 1
fi

if ! command -v docker compose &> /dev/null; then
    echo "❌ Docker Compose is not installed. Please install Docker Compose first."
    exit 1
fi

echo "✅ Docker and Docker Compose found"

# Check .env file
if [ ! -f .env ]; then
    echo "⚠️  .env file not found. Creating from .env.example..."
    cp .env.example .env
    echo "📝 Please update .env with your JWT_SECRET and other configurations"
    echo "   Run: openssl rand -base64 64  to generate a strong JWT_SECRET"
    exit 1
fi

# Check JWT_SECRET is not default
if grep -q "JWT_SECRET=changeMe" .env; then
    echo "⚠️  JWT_SECRET is still set to default value!"
    echo "   Please generate a strong secret with: openssl rand -base64 64"
    exit 1
fi

echo "✅ .env configuration found"

# Build and start services
echo ""
echo "🔨 Building and starting services..."
docker compose down 2>/dev/null || true
docker compose up -d --build

echo ""
echo "⏳ Waiting for services to be ready (30 seconds)..."
sleep 30

# Check service health
echo ""
echo "🏥 Checking service health..."

POSTGRES_HEALTH=$(docker compose exec -T postgres pg_isready -U postgres 2>&1 || echo "not ready")
API_HEALTH=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:3001/api/health || echo "000")
WEBSITE_HEALTH=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080 || echo "000")

echo "Postgres: $POSTGRES_HEALTH"
echo "API: HTTP $API_HEALTH"
echo "Website: HTTP $WEBSITE_HEALTH"

echo ""
echo "✅ Services started successfully!"
echo ""
echo "🌐 Access your services:"
echo "   - Website: http://localhost:8080"
echo "   - API: http://localhost:3001"
echo "   - API Swagger: http://localhost:3001/swagger.html"
echo "   - API Health: http://localhost:3001/api/health"
echo ""
echo "📝 Next steps:"
echo "   1. Go to http://localhost:8080"
echo "   2. Create a new account (Sign Up)"
echo "   3. Use the plugin with your credentials"
echo ""
echo "📊 View logs:"
echo "   docker compose logs -f api"
echo "   docker compose logs -f postgres"
echo ""
echo "🛑 Stop services:"
echo "   docker compose down"

