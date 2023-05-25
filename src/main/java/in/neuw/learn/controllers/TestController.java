package in.neuw.learn.controllers;

import in.neuw.learn.db.entities.UserEntity;
import in.neuw.learn.model.UserData;
import in.neuw.learn.model.ServiceResponse;
import in.neuw.learn.service.TestDataService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@RestController
public class TestController {

    private final DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;

    private final TestDataService testDataService;

    @Value("${aws.user-table.name}")
    private String tableName;

    public TestController(DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient,
                          TestDataService testDataService) {
        this.dynamoDbEnhancedAsyncClient = dynamoDbEnhancedAsyncClient;
        this.testDataService = testDataService;
    }

    // direct interaction for developer interaction and similar purpose only, not intended to be in prod
    @GetMapping("/table")
    public Mono<Void> createTable() {
        return Mono.fromFuture(dynamoDbEnhancedAsyncClient.table(tableName, TableSchema.fromClass(UserEntity.class)).createTable());
    }

    // direct interaction for developer interaction and similar purpose only, not intended to be in prod
    @DeleteMapping("/table")
    public Mono<Void> deleteTable() {
        return Mono.fromFuture(dynamoDbEnhancedAsyncClient.table(tableName, TableSchema.fromClass(UserEntity.class)).deleteTable());
    }

    @GetMapping("/user")
    public Mono<ServiceResponse> getUser(@RequestParam String id) {
        return testDataService.getUserById(id);
    }

    @PostMapping("/user")
    public Mono<ServiceResponse> saveUser(final UserData userData) {
        return testDataService.saveUser(userData);
    }

}
