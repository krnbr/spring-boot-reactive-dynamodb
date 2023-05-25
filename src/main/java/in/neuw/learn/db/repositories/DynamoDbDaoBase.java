package in.neuw.learn.db.repositories;

import in.neuw.learn.exception.AppRuntimeException;
import in.neuw.learn.utils.constants.ErrorCodes;
import org.slf4j.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.DeleteItemEnhancedRequest;

public abstract class DynamoDbDaoBase<T> {

    protected Logger logger;

    protected String tableName;
    protected DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;

    protected DynamoDbAsyncTable<T> dynamoDbAsyncTable;

    public DynamoDbDaoBase(final Logger logger,
                           final String tableName,
                           final DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient) {
        this.logger = logger;
        this.tableName = tableName;
        this.dynamoDbEnhancedAsyncClient = dynamoDbEnhancedAsyncClient;
    }

    public Mono<T> save(final T t) {
        return Mono.fromFuture(dynamoDbAsyncTable.putItem(t)).onErrorResume((e) -> {
            this.logger.error("Error '{}' while saving data to the {}", e.getMessage(), tableName);
            // business logic to check a specific exception here
            return Mono.error(new AppRuntimeException("Error while saving data to the table "+tableName, ErrorCodes.INTERNAL_SERVER_ERROR, e));
        }).then(Mono.just(t));
    }
    public Mono<T> getById(final String id) {
        return Mono.fromFuture(dynamoDbAsyncTable.getItem(getKeyById(id))).onErrorResume((e) -> {
            this.logger.error("Error '{}' while fetching data to the {}", e.getMessage(), tableName);
            // business logic to check a specific exception here
            return Mono.error(new AppRuntimeException("Error while fetching data to the table "+tableName, ErrorCodes.INTERNAL_SERVER_ERROR, e));
        }).doOnSuccess(t -> {
            logger.info("fetched data with id = {}", id);
        });
    }

    // not a recommended way of fetching, inefficient scan operation, marking it private to prevent inheritance
    public Flux<T> getAll() {
        return Flux.from(dynamoDbAsyncTable.scan().items());
    }
    public Mono<T> deleteById(final String id) {
        return Mono.fromFuture(dynamoDbAsyncTable.deleteItem(getKeyById(id)));
    }
    public Mono<T> update(T t) {
        return Mono.fromFuture(dynamoDbAsyncTable.updateItem(t));
    }

    public DynamoDbEnhancedAsyncClient getDynamoDbEnhancedAsyncClient() {
        return dynamoDbEnhancedAsyncClient;
    }

    public String getTableName() {
        return tableName;
    }

    public DynamoDbAsyncTable<T> getDynamoDbAsyncTable() {
        return dynamoDbAsyncTable;
    }

    public Key getKeyById(final String id) {
        return Key.builder().partitionValue(id).build();
    }
}
