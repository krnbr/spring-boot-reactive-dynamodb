package in.neuw.learn.db.repositories;

import in.neuw.learn.db.entities.UserEntity;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
public class UserRepository extends DynamoDbDaoBase<UserEntity> {

    public UserRepository(final DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient,
                          final @Value("${aws.user-table.name}") String tableName) {
        super(LoggerFactory.getLogger(UserRepository.class), tableName, dynamoDbEnhancedAsyncClient);
        super.dynamoDbAsyncTable = dynamoDbEnhancedAsyncClient.table(tableName, TableSchema.fromClass(UserEntity.class));
    }

}
