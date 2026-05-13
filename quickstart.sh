#!/bin/bash
# API Gateway Stack Quick Start Script
# This script helps you set up and start all services

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}=== API Gateway + Keycloak + LDAP Quick Start ===${NC}\n"

# Step 1: Create Docker network
echo -e "${YELLOW}[1/4] Creating Docker network (shared_net)...${NC}"
docker network create shared_net 2>/dev/null || echo "Network already exists ✓"

# Step 2: Build API Gateway (optional - Docker will build on compose up)
echo -e "${YELLOW}[2/4] Building API Gateway...${NC}"
./mvnw clean package -DskipTests -f api-gateway/pom.xml -q
echo "API Gateway built successfully ✓"

# Step 3: Start services
echo -e "${YELLOW}[3/4] Starting Docker services...${NC}"
docker compose up -d
echo "Docker services started ✓"

# Step 4: Wait for services and show status
echo -e "${YELLOW}[4/4] Waiting for services to be healthy...${NC}\n"

# Function to check service health
check_service() {
    local url=$1
    local name=$2
    local max_attempts=30
    local attempt=1

    while [ $attempt -le $max_attempts ]; do
        if curl -s "$url" > /dev/null 2>&1; then
            echo -e "  ${GREEN}✓${NC} $name is healthy"
            return 0
        fi
        echo -ne "  ⏳ $name ($attempt/$max_attempts)...\r"
        attempt=$((attempt + 1))
        sleep 2
    done

    echo -e "  ${RED}✗${NC} $name failed to start (check: docker logs)"
    return 1
}

# Check services
check_service "http://localhost:8500/v1/status/leader" "Consul"
check_service "http://localhost:8090/health/live" "Keycloak"
check_service "http://localhost:8001/actuator/health" "API Gateway"

echo -e "\n${GREEN}=== Services Started Successfully ===${NC}\n"

echo -e "📌 ${YELLOW}Next Steps:${NC}"
echo ""
echo "1. Configure Keycloak Realm (first time only):"
echo "   ${YELLOW}open http://localhost:8090${NC}"
echo "   - Login: admin / admin"
echo "   - Create realm: finki-services"
echo "   - Create roles: service.user, service.admin"
echo "   - Create test user or use LDAP users"
echo "   - Register client: gateway-tester"
echo ""
echo "2. View Consul Service Registry:"
echo "   ${YELLOW}open http://localhost:8500${NC}"
echo ""
echo "3. Get a token from Keycloak:"
echo "   ${YELLOW}curl -s -X POST \\${NC}"
echo "   ${YELLOW}  -d \"client_id=gateway-tester\" \\${NC}"
echo "   ${YELLOW}  -d \"client_secret=<YOUR_CLIENT_SECRET>\" \\${NC}"
echo "   ${YELLOW}  -d \"grant_type=password\" \\${NC}"
echo "   ${YELLOW}  -d \"username=ben\" \\${NC}"
echo "   ${YELLOW}  -d \"password=benspassword\" \\${NC}"
echo "   ${YELLOW}  http://localhost:8090/realms/finki-services/protocol/openid-connect/token | jq .${NC}"
echo ""
echo "4. Test the gateway with a token:"
echo "   ${YELLOW}curl -i -H \"Authorization: Bearer <ACCESS_TOKEN>\" \\${NC}"
echo "   ${YELLOW}  http://localhost:8000/internship-management/swagger-ui.html${NC}"
echo ""
echo "📖 For detailed setup instructions:"
echo "   ${YELLOW}cat API_GATEWAY_SETUP.md${NC}"
echo ""
echo -e "${GREEN}Setup complete! Happy coding! 🚀${NC}\n"

# Show service URLs
echo "Service URLs:"
echo "  API Gateway:    http://localhost:8000"
echo "  Keycloak:       http://localhost:8090"
echo "  Consul UI:      http://localhost:8500"
echo "  Kafka UI:       http://localhost:8085"
echo "  Axon Server:    http://localhost:8024"
echo "  Internship App: http://localhost:8086"

