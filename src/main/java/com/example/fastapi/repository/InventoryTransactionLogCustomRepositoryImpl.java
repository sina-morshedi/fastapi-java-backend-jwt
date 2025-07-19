// پیاده سازی
package com.example.fastapi.repository;

import com.example.fastapi.dto.InventoryTransactionResponseDTO;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InventoryTransactionLogCustomRepositoryImpl implements InventoryTransactionLogCustomRepository {

    private final MongoTemplate mongoTemplate;

    public InventoryTransactionLogCustomRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<InventoryTransactionResponseDTO> findAllTransactions() {

        // مرحله match در صورت نیاز (اگر بخواهی فیلتر بزنی، فعلاً حذف شده)
        // AggregationOperation matchAll = Aggregation.match(new Criteria());

        AggregationOperation addCarInfoIdObject = context -> new Document("$addFields",
                new Document("carInfoIdObject", new Document("$toObjectId", "$carInfoId"))
        );

        AggregationOperation addCustomerIdObject = context -> new Document("$addFields",
                new Document("customerIdObject", new Document("$toObjectId", "$customerId"))
        );

        AggregationOperation addUserIdObject = context -> new Document("$addFields",
                new Document("userIdObject", new Document("$toObjectId", "$userId"))
        );

        AggregationOperation addInventoryItemIdObject = context -> new Document("$addFields",
                new Document("inventoryItemIdObject", new Document("$toObjectId", "$inventoryItemId"))
        );

        LookupOperation lookupCarInfo = LookupOperation.newLookup()
                .from("carInfo")
                .localField("carInfoIdObject")
                .foreignField("_id")
                .as("carInfo");

        LookupOperation lookupCustomer = LookupOperation.newLookup()
                .from("customers")
                .localField("customerIdObject")
                .foreignField("_id")
                .as("customer");

        LookupOperation lookupUser = LookupOperation.newLookup()
                .from("users")
                .localField("userIdObject")
                .foreignField("_id")
                .as("creatorUser");

        LookupOperation lookupInventoryItem = LookupOperation.newLookup()
                .from("inventoryItems")
                .localField("inventoryItemIdObject")
                .foreignField("_id")
                .as("inventoryItem");

        UnwindOperation unwindCarInfo = Aggregation.unwind("carInfo", true);
        UnwindOperation unwindCustomer = Aggregation.unwind("customer", true);
        UnwindOperation unwindUser = Aggregation.unwind("creatorUser", true);
        UnwindOperation unwindInventoryItem = Aggregation.unwind("inventoryItem", true);

        Aggregation aggregation = Aggregation.newAggregation(
                addCarInfoIdObject,
                addCustomerIdObject,
                addUserIdObject,
                addInventoryItemIdObject,
                lookupCarInfo,
                unwindCarInfo,
                lookupCustomer,
                unwindCustomer,
                lookupUser,
                unwindUser,
                lookupInventoryItem,
                unwindInventoryItem
        );

        AggregationResults<InventoryTransactionResponseDTO> results = mongoTemplate.aggregate(
                aggregation,
                "inventoryTransactionLog",
                InventoryTransactionResponseDTO.class
        );

        List<InventoryTransactionResponseDTO> list = results.getMappedResults();

        // (دلخواه) چاپ خروجی برای دیباگ
        list.forEach(item -> System.out.println(item));

        return list;
    }
}
