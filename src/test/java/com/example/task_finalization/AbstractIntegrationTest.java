package com.example.task_finalization;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.BeforeAll;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.rabbitmq.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class AbstractIntegrationTest {

    private static String kcAdminUsername = "admin";
    private static String kcAdminPassword = "admin";
    private static String kcClientId = "test-client";
    private static String realmName = "test-realm";
    private static String kcTestUsername = "test";
    private static String kcTestPassword = "test";
    public static String token;

    @ServiceConnection
    public static PostgreSQLContainer postgres = new PostgreSQLContainer(DockerImageName.parse("postgres:latest"));
    public static ConfluentKafkaContainer kafka = new ConfluentKafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"));
    public static RabbitMQContainer rabbit = new RabbitMQContainer(DockerImageName.parse("rabbitmq:3-management"));
    public static KeycloakContainer keycloak = new KeycloakContainer()
            .withAdminUsername(kcAdminUsername)
            .withAdminPassword(kcAdminPassword)
            .withRealmImportFile("keycloak/test-realm.json");

    static {
        postgres.start();
        kafka.start();
        rabbit.start();
        keycloak.start();
    }

    @BeforeAll
    public static void createKeycloakToken() {
        String authServerUrl = keycloak.getAuthServerUrl();
        Keycloak kc = KeycloakBuilder.builder()
                .serverUrl(authServerUrl)
                .clientId(kcClientId)
                .realm(realmName)
                .username(kcTestUsername)
                .password(kcTestPassword)
                .grantType(OAuth2Constants.PASSWORD)
                .build();

        AccessTokenResponse tokenResponse = kc.tokenManager().getAccessToken();
        token = tokenResponse.getToken();
    }

    @DynamicPropertySource
    public static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> keycloak.getAuthServerUrl() + "/realms/test-realm");
        registry.add("spring.security.oauth2.resourceserver.jwt.jwk-set-uri", () -> keycloak.getAuthServerUrl() + "/realms/test-realm/protocol/openid-connect/certs");
        registry.add("jwt.auth.converter.resource-id", () -> "oauth-client");

        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("spring.kafka.producer.key-serializer", () -> "org.apache.kafka.common.serialization.StringSerializer");
        registry.add("spring.kafka.producer.value-serializer", () -> "org.springframework.kafka.support.serializer.JacksonJsonSerializer");

        registry.add("spring.kafka.consumer.key-deserializer", () -> "org.apache.kafka.common.serialization.StringDeserializer");
        registry.add("spring.kafka.consumer.value-deserializer", () -> "org.springframework.kafka.support.serializer.JacksonJsonDeserializer");
        registry.add("spring.kafka.consumer.group-id", () -> "my-group");
        registry.add("spring.kafka.consumer.properties.spring.json.trusted.packages", () -> "*");
        registry.add("spring.kafka.consumer.properties.spring.json.value.default.type",
                () -> "com.example.task_finalization.model.ProcessingJob");

        registry.add("spring.rabbitmq.host", rabbit::getHost);
        registry.add("spring.rabbitmq.port", rabbit::getAmqpPort);
        registry.add("spring.rabbitmq.username", rabbit::getAdminUsername);
        registry.add("spring.rabbitmq.password", rabbit::getAdminPassword);
    }
}
