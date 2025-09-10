./gradlew :party-archetype-spring:build -x test
podman build -t party-archetype-app .
podman-compose up --buildgit test