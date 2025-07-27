// پیاده سازی
package com.example.fastapi.repository;

import com.example.fastapi.dto.InventoryTransactionResponseDTO;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Date;

@Repository
public class InventoryTransactionLogCustomRepositoryImpl implements InventoryTransactionLogCustomRepository {

    private final MongoTemplate mongoTemplate;

    public InventoryTransactionLogCustomRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<InventoryTransactionResponseDTO> findAllTransactions() {

        AggregationOperation addCarInfoIdObject = context -> new Document("$addFields",
                new Document("carInfoIdObject", new Document("$toObjectId", "$carInfoId"))
        );

        AggregationOperation addCustomerIdObject = context -> new Document("$addFields",
                new Document("customerIdObject", new Document("$toObjectId", "$customerId"))
        );

        AggregationOperation addCreatorUserIdObject = context -> new Document("$addFields",
                new Document("creatorUserIdObject", new Document("$toObjectId", "$creatorUserId"))
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

        LookupOperation lookupCreatorUser = LookupOperation.newLookup()
                .from("users")
                .localField("creatorUserIdObject")
                .foreignField("_id")
                .as("creatorUser");

        LookupOperation lookupInventoryItem = LookupOperation.newLookup()
                .from("inventoryItems")
                .localField("inventoryItemIdObject")
                .foreignField("_id")
                .as("inventoryItem");

        UnwindOperation unwindCarInfo = Aggregation.unwind("carInfo", true);
        UnwindOperation unwindCustomer = Aggregation.unwind("customer", true);
        UnwindOperation unwindCreatorUser = Aggregation.unwind("creatorUser", true);
        UnwindOperation unwindInventoryItem = Aggregation.unwind("inventoryItem", true);

        // تبدیل roleId و permissionId به ObjectId داخل creatorUser
        AggregationOperation addRolePermissionObjectIds = context -> new Document("$addFields",
                new Document("creatorUser.roleIdObject", new Document("$toObjectId", "$creatorUser.roleId"))
                        .append("creatorUser.permissionIdObject", new Document("$toObjectId", "$creatorUser.permissionId"))
        );

        // Lookup رول و پرمیشن
        LookupOperation lookupRole = LookupOperation.newLookup()
                .from("roles")
                .localField("creatorUser.roleIdObject")
                .foreignField("_id")
                .as("creatorUser.role");

        LookupOperation lookupPermission = LookupOperation.newLookup()
                .from("permissions")
                .localField("creatorUser.permissionIdObject")
                .foreignField("_id")
                .as("creatorUser.permission");

        UnwindOperation unwindRole = Aggregation.unwind("creatorUser.role", true);
        UnwindOperation unwindPermission = Aggregation.unwind("creatorUser.permission", true);

        // اضافه کردن فیلد userId به صورت رشته (String) برای ارسال به فرانت‌اند
        AggregationOperation addCreatorUserUserId = context -> new Document("$addFields",
                new Document("creatorUser.userId", new Document("$toString", "$creatorUser._id"))
        );

        // پروژه کردن خروجی نهایی
        ProjectionOperation project = Aggregation.project()
                .andExpression("_id").as("id")
                .and("carInfo").as("carInfo")
                .and("customer").as("customer")
                .and("creatorUser.userId").as("creatorUser.userId")
                .and("creatorUser.username").as("creatorUser.username")
                .and("creatorUser.firstName").as("creatorUser.firstName")
                .and("creatorUser.lastName").as("creatorUser.lastName")
                .and("creatorUser.role").as("creatorUser.role")
                .and("creatorUser.permission").as("creatorUser.permission")
                .and("inventoryItem").as("inventoryItem")
                .and("quantity").as("quantity")
                .and("type").as("type")
                .and("description").as("description")
                .and("dateTime").as("dateTime");

        Aggregation aggregation = Aggregation.newAggregation(
                addCarInfoIdObject,
                addCustomerIdObject,
                addCreatorUserIdObject,
                addInventoryItemIdObject,
                lookupCarInfo,
                unwindCarInfo,
                lookupCustomer,
                unwindCustomer,
                lookupCreatorUser,
                unwindCreatorUser,
                addRolePermissionObjectIds,
                lookupRole,
                unwindRole,
                lookupPermission,
                unwindPermission,
                addCreatorUserUserId,
                lookupInventoryItem,
                unwindInventoryItem,
                project
        );

        AggregationResults<InventoryTransactionResponseDTO> results = mongoTemplate.aggregate(
                aggregation,
                "inventoryTransactionLog",
                InventoryTransactionResponseDTO.class
        );

        return results.getMappedResults();
    }

    public List<InventoryTransactionResponseDTO> findAllTransactionsPaginated(int page, int size) {
        int skip = page * size;

        AggregationOperation addCarInfoIdObject = context -> new Document("$addFields",
                new Document("carInfoIdObject", new Document("$toObjectId", "$carInfoId"))
        );

        AggregationOperation addCustomerIdObject = context -> new Document("$addFields",
                new Document("customerIdObject", new Document("$toObjectId", "$customerId"))
        );

        AggregationOperation addCreatorUserIdObject = context -> new Document("$addFields",
                new Document("creatorUserIdObject", new Document("$toObjectId", "$creatorUserId"))
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

        LookupOperation lookupCreatorUser = LookupOperation.newLookup()
                .from("users")
                .localField("creatorUserIdObject")
                .foreignField("_id")
                .as("creatorUser");

        LookupOperation lookupInventoryItem = LookupOperation.newLookup()
                .from("inventoryItems")
                .localField("inventoryItemIdObject")
                .foreignField("_id")
                .as("inventoryItem");

        UnwindOperation unwindCarInfo = Aggregation.unwind("carInfo", true);
        UnwindOperation unwindCustomer = Aggregation.unwind("customer", true);
        UnwindOperation unwindCreatorUser = Aggregation.unwind("creatorUser", true);
        UnwindOperation unwindInventoryItem = Aggregation.unwind("inventoryItem", true);

        AggregationOperation addRolePermissionObjectIds = context -> new Document("$addFields",
                new Document("creatorUser.roleIdObject", new Document("$toObjectId", "$creatorUser.roleId"))
                        .append("creatorUser.permissionIdObject", new Document("$toObjectId", "$creatorUser.permissionId"))
        );

        LookupOperation lookupRole = LookupOperation.newLookup()
                .from("roles")
                .localField("creatorUser.roleIdObject")
                .foreignField("_id")
                .as("creatorUser.role");

        LookupOperation lookupPermission = LookupOperation.newLookup()
                .from("permissions")
                .localField("creatorUser.permissionIdObject")
                .foreignField("_id")
                .as("creatorUser.permission");

        UnwindOperation unwindRole = Aggregation.unwind("creatorUser.role", true);
        UnwindOperation unwindPermission = Aggregation.unwind("creatorUser.permission", true);

        AggregationOperation addCreatorUserUserId = context -> new Document("$addFields",
                new Document("creatorUser.userId", new Document("$toString", "$creatorUser._id"))
        );

        ProjectionOperation project = Aggregation.project()
                .andExpression("_id").as("id")
                .and("carInfo").as("carInfo")
                .and("customer").as("customer")
                .and("creatorUser.userId").as("creatorUser.userId")
                .and("creatorUser.username").as("creatorUser.username")
                .and("creatorUser.firstName").as("creatorUser.firstName")
                .and("creatorUser.lastName").as("creatorUser.lastName")
                .and("creatorUser.role").as("creatorUser.role")
                .and("creatorUser.permission").as("creatorUser.permission")
                .and("inventoryItem").as("inventoryItem")
                .and("quantity").as("quantity")
                .and("type").as("type")
                .and("description").as("description")
                .and("dateTime").as("dateTime");

        SortOperation sortByDateTimeDesc = Aggregation.sort(Sort.by(Sort.Direction.DESC, "dateTime"));

        AggregationOperation skipOp = Aggregation.skip((long) skip);
        AggregationOperation limitOp = Aggregation.limit(size);

        Aggregation aggregation = Aggregation.newAggregation(
                addCarInfoIdObject,
                addCustomerIdObject,
                addCreatorUserIdObject,
                addInventoryItemIdObject,
                lookupCarInfo,
                unwindCarInfo,
                lookupCustomer,
                unwindCustomer,
                lookupCreatorUser,
                unwindCreatorUser,
                addRolePermissionObjectIds,
                lookupRole,
                unwindRole,
                lookupPermission,
                unwindPermission,
                addCreatorUserUserId,
                lookupInventoryItem,
                unwindInventoryItem,
                sortByDateTimeDesc,
                skipOp,
                limitOp,
                project
        );

        AggregationResults<InventoryTransactionResponseDTO> results = mongoTemplate.aggregate(
                aggregation,
                "inventoryTransactionLog",
                InventoryTransactionResponseDTO.class
        );

        return results.getMappedResults();
    }

    public List<InventoryTransactionResponseDTO> findTransactionsByDateRangePaginated(
            LocalDateTime startDate,
            LocalDateTime endDate,
            int page,
            int size
    ) {
        int skip = page * size;

        MatchOperation dateMatch = Aggregation.match(
                Criteria.where("dateTime").gte(startDate).lte(endDate)
        );

        AggregationOperation addCarInfoIdObject = context -> new Document("$addFields",
                new Document("carInfoIdObject", new Document("$toObjectId", "$carInfoId"))
        );

        AggregationOperation addCustomerIdObject = context -> new Document("$addFields",
                new Document("customerIdObject", new Document("$toObjectId", "$customerId"))
        );

        AggregationOperation addCreatorUserIdObject = context -> new Document("$addFields",
                new Document("creatorUserIdObject", new Document("$toObjectId", "$creatorUserId"))
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

        LookupOperation lookupCreatorUser = LookupOperation.newLookup()
                .from("users")
                .localField("creatorUserIdObject")
                .foreignField("_id")
                .as("creatorUser");

        LookupOperation lookupInventoryItem = LookupOperation.newLookup()
                .from("inventoryItems")
                .localField("inventoryItemIdObject")
                .foreignField("_id")
                .as("inventoryItem");

        UnwindOperation unwindCarInfo = Aggregation.unwind("carInfo", true);
        UnwindOperation unwindCustomer = Aggregation.unwind("customer", true);
        UnwindOperation unwindCreatorUser = Aggregation.unwind("creatorUser", true);
        UnwindOperation unwindInventoryItem = Aggregation.unwind("inventoryItem", true);

        AggregationOperation addRolePermissionObjectIds = context -> new Document("$addFields",
                new Document("creatorUser.roleIdObject", new Document("$toObjectId", "$creatorUser.roleId"))
                        .append("creatorUser.permissionIdObject", new Document("$toObjectId", "$creatorUser.permissionId"))
        );

        LookupOperation lookupRole = LookupOperation.newLookup()
                .from("roles")
                .localField("creatorUser.roleIdObject")
                .foreignField("_id")
                .as("creatorUser.role");

        LookupOperation lookupPermission = LookupOperation.newLookup()
                .from("permissions")
                .localField("creatorUser.permissionIdObject")
                .foreignField("_id")
                .as("creatorUser.permission");

        UnwindOperation unwindRole = Aggregation.unwind("creatorUser.role", true);
        UnwindOperation unwindPermission = Aggregation.unwind("creatorUser.permission", true);

        AggregationOperation addCreatorUserUserId = context -> new Document("$addFields",
                new Document("creatorUser.userId", new Document("$toString", "$creatorUser._id"))
        );

        ProjectionOperation project = Aggregation.project()
                .andExpression("_id").as("id")
                .and("carInfo").as("carInfo")
                .and("customer").as("customer")
                .and("creatorUser.userId").as("creatorUser.userId")
                .and("creatorUser.username").as("creatorUser.username")
                .and("creatorUser.firstName").as("creatorUser.firstName")
                .and("creatorUser.lastName").as("creatorUser.lastName")
                .and("creatorUser.role").as("creatorUser.role")
                .and("creatorUser.permission").as("creatorUser.permission")
                .and("inventoryItem").as("inventoryItem")
                .and("quantity").as("quantity")
                .and("type").as("type")
                .and("description").as("description")
                .and("dateTime").as("dateTime");

        SortOperation sortByDateTimeDesc = Aggregation.sort(Sort.by(Sort.Direction.DESC, "dateTime"));
        AggregationOperation skipOp = Aggregation.skip((long) skip);
        AggregationOperation limitOp = Aggregation.limit(size);

        Aggregation aggregation = Aggregation.newAggregation(
                dateMatch,
                addCarInfoIdObject,
                addCustomerIdObject,
                addCreatorUserIdObject,
                addInventoryItemIdObject,
                lookupCarInfo,
                unwindCarInfo,
                lookupCustomer,
                unwindCustomer,
                lookupCreatorUser,
                unwindCreatorUser,
                addRolePermissionObjectIds,
                lookupRole,
                unwindRole,
                lookupPermission,
                unwindPermission,
                addCreatorUserUserId,
                lookupInventoryItem,
                unwindInventoryItem,
                sortByDateTimeDesc,
                skipOp,
                limitOp,
                project
        );

        AggregationResults<InventoryTransactionResponseDTO> results = mongoTemplate.aggregate(
                aggregation,
                "inventoryTransactionLog",
                InventoryTransactionResponseDTO.class
        );

        return results.getMappedResults();
    }

    public List<InventoryTransactionResponseDTO> findTransactionsByDateRange(
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        MatchOperation dateMatch = Aggregation.match(
                Criteria.where("dateTime").gte(startDate).lte(endDate)
        );

        AggregationOperation addCarInfoIdObject = context -> new Document("$addFields",
                new Document("carInfoIdObject", new Document("$toObjectId", "$carInfoId"))
        );

        AggregationOperation addCustomerIdObject = context -> new Document("$addFields",
                new Document("customerIdObject", new Document("$toObjectId", "$customerId"))
        );

        AggregationOperation addCreatorUserIdObject = context -> new Document("$addFields",
                new Document("creatorUserIdObject", new Document("$toObjectId", "$creatorUserId"))
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

        LookupOperation lookupCreatorUser = LookupOperation.newLookup()
                .from("users")
                .localField("creatorUserIdObject")
                .foreignField("_id")
                .as("creatorUser");

        LookupOperation lookupInventoryItem = LookupOperation.newLookup()
                .from("inventoryItems")
                .localField("inventoryItemIdObject")
                .foreignField("_id")
                .as("inventoryItem");

        UnwindOperation unwindCarInfo = Aggregation.unwind("carInfo", true);
        UnwindOperation unwindCustomer = Aggregation.unwind("customer", true);
        UnwindOperation unwindCreatorUser = Aggregation.unwind("creatorUser", true);
        UnwindOperation unwindInventoryItem = Aggregation.unwind("inventoryItem", true);

        AggregationOperation addRolePermissionObjectIds = context -> new Document("$addFields",
                new Document("creatorUser.roleIdObject", new Document("$toObjectId", "$creatorUser.roleId"))
                        .append("creatorUser.permissionIdObject", new Document("$toObjectId", "$creatorUser.permissionId"))
        );

        LookupOperation lookupRole = LookupOperation.newLookup()
                .from("roles")
                .localField("creatorUser.roleIdObject")
                .foreignField("_id")
                .as("creatorUser.role");

        LookupOperation lookupPermission = LookupOperation.newLookup()
                .from("permissions")
                .localField("creatorUser.permissionIdObject")
                .foreignField("_id")
                .as("creatorUser.permission");

        UnwindOperation unwindRole = Aggregation.unwind("creatorUser.role", true);
        UnwindOperation unwindPermission = Aggregation.unwind("creatorUser.permission", true);

        AggregationOperation addCreatorUserUserId = context -> new Document("$addFields",
                new Document("creatorUser.userId", new Document("$toString", "$creatorUser._id"))
        );

        ProjectionOperation project = Aggregation.project()
                .andExpression("_id").as("id")
                .and("carInfo").as("carInfo")
                .and("customer").as("customer")
                .and("creatorUser.userId").as("creatorUser.userId")
                .and("creatorUser.username").as("creatorUser.username")
                .and("creatorUser.firstName").as("creatorUser.firstName")
                .and("creatorUser.lastName").as("creatorUser.lastName")
                .and("creatorUser.role").as("creatorUser.role")
                .and("creatorUser.permission").as("creatorUser.permission")
                .and("inventoryItem").as("inventoryItem")
                .and("quantity").as("quantity")
                .and("type").as("type")
                .and("description").as("description")
                .and("dateTime").as("dateTime");

        SortOperation sortByDateTimeDesc = Aggregation.sort(Sort.by(Sort.Direction.DESC, "dateTime"));

        Aggregation aggregation = Aggregation.newAggregation(
                dateMatch,
                addCarInfoIdObject,
                addCustomerIdObject,
                addCreatorUserIdObject,
                addInventoryItemIdObject,
                lookupCarInfo,
                unwindCarInfo,
                lookupCustomer,
                unwindCustomer,
                lookupCreatorUser,
                unwindCreatorUser,
                addRolePermissionObjectIds,
                lookupRole,
                unwindRole,
                lookupPermission,
                unwindPermission,
                addCreatorUserUserId,
                lookupInventoryItem,
                unwindInventoryItem,
                sortByDateTimeDesc,
                project
        );

        AggregationResults<InventoryTransactionResponseDTO> results = mongoTemplate.aggregate(
                aggregation,
                "inventoryTransactionLog",
                InventoryTransactionResponseDTO.class
        );

        return results.getMappedResults();
    }


    public List<InventoryTransactionResponseDTO> findTransactionsByType(String type) {
        InventoryTransactionResponseDTO.TransactionType transactionType;
        try {
            transactionType = InventoryTransactionResponseDTO.TransactionType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid transaction type: " + type);
        }

        AggregationOperation matchType = Aggregation.match(Criteria.where("type").is(transactionType.name()));

        AggregationOperation addCarInfoIdObject = context -> new Document("$addFields",
                new Document("carInfoIdObject", new Document("$toObjectId", "$carInfoId"))
        );

        AggregationOperation addCustomerIdObject = context -> new Document("$addFields",
                new Document("customerIdObject", new Document("$toObjectId", "$customerId"))
        );

        AggregationOperation addCreatorUserIdObject = context -> new Document("$addFields",
                new Document("creatorUserIdObject", new Document("$toObjectId", "$creatorUserId"))
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

        LookupOperation lookupCreatorUser = LookupOperation.newLookup()
                .from("users")
                .localField("creatorUserIdObject")
                .foreignField("_id")
                .as("creatorUser");

        LookupOperation lookupInventoryItem = LookupOperation.newLookup()
                .from("inventoryItems")
                .localField("inventoryItemIdObject")
                .foreignField("_id")
                .as("inventoryItem");

        UnwindOperation unwindCarInfo = Aggregation.unwind("carInfo", true);
        UnwindOperation unwindCustomer = Aggregation.unwind("customer", true);
        UnwindOperation unwindCreatorUser = Aggregation.unwind("creatorUser", true);
        UnwindOperation unwindInventoryItem = Aggregation.unwind("inventoryItem", true);

        AggregationOperation addRolePermissionObjectIds = context -> new Document("$addFields",
                new Document("creatorUser.roleIdObject", new Document("$toObjectId", "$creatorUser.roleId"))
                        .append("creatorUser.permissionIdObject", new Document("$toObjectId", "$creatorUser.permissionId"))
        );

        LookupOperation lookupRole = LookupOperation.newLookup()
                .from("roles")
                .localField("creatorUser.roleIdObject")
                .foreignField("_id")
                .as("creatorUser.role");

        LookupOperation lookupPermission = LookupOperation.newLookup()
                .from("permissions")
                .localField("creatorUser.permissionIdObject")
                .foreignField("_id")
                .as("creatorUser.permission");

        UnwindOperation unwindRole = Aggregation.unwind("creatorUser.role", true);
        UnwindOperation unwindPermission = Aggregation.unwind("creatorUser.permission", true);

        AggregationOperation addCreatorUserUserId = context -> new Document("$addFields",
                new Document("creatorUser.userId", new Document("$toString", "$creatorUser._id"))
        );

        ProjectionOperation project = Aggregation.project()
                .andExpression("_id").as("id")
                .and("carInfo").as("carInfo")
                .and("customer").as("customer")
                .and("creatorUser.userId").as("creatorUser.userId")
                .and("creatorUser.username").as("creatorUser.username")
                .and("creatorUser.firstName").as("creatorUser.firstName")
                .and("creatorUser.lastName").as("creatorUser.lastName")
                .and("creatorUser.role").as("creatorUser.role")
                .and("creatorUser.permission").as("creatorUser.permission")
                .and("inventoryItem").as("inventoryItem")
                .and("quantity").as("quantity")
                .and("type").as("type")
                .and("description").as("description")
                .and("dateTime").as("dateTime");

        Aggregation aggregation = Aggregation.newAggregation(
                matchType,
                addCarInfoIdObject,
                addCustomerIdObject,
                addCreatorUserIdObject,
                addInventoryItemIdObject,
                lookupCarInfo,
                unwindCarInfo,
                lookupCustomer,
                unwindCustomer,
                lookupCreatorUser,
                unwindCreatorUser,
                addRolePermissionObjectIds,
                lookupRole,
                unwindRole,
                lookupPermission,
                unwindPermission,
                addCreatorUserUserId,
                lookupInventoryItem,
                unwindInventoryItem,
                project
        );

        AggregationResults<InventoryTransactionResponseDTO> results = mongoTemplate.aggregate(
                aggregation,
                "inventoryTransactionLog",
                InventoryTransactionResponseDTO.class
        );

        return results.getMappedResults();
    }

    public List<InventoryTransactionResponseDTO> findByCustomerFullName(String fullName) {
        AggregationOperation matchCustomerName = Aggregation.match(
                Criteria.where("customer.fullName").regex(".*" + fullName + ".*", "i")
        );

        AggregationOperation addCarInfoIdObject = context -> new Document("$addFields",
                new Document("carInfoIdObject", new Document("$toObjectId", "$carInfoId"))
        );

        AggregationOperation addCustomerIdObject = context -> new Document("$addFields",
                new Document("customerIdObject", new Document("$toObjectId", "$customerId"))
        );

        AggregationOperation addCreatorUserIdObject = context -> new Document("$addFields",
                new Document("creatorUserIdObject", new Document("$toObjectId", "$creatorUserId"))
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

        LookupOperation lookupCreatorUser = LookupOperation.newLookup()
                .from("users")
                .localField("creatorUserIdObject")
                .foreignField("_id")
                .as("creatorUser");

        LookupOperation lookupInventoryItem = LookupOperation.newLookup()
                .from("inventoryItems")
                .localField("inventoryItemIdObject")
                .foreignField("_id")
                .as("inventoryItem");

        UnwindOperation unwindCarInfo = Aggregation.unwind("carInfo", true);
        UnwindOperation unwindCustomer = Aggregation.unwind("customer", true);
        UnwindOperation unwindCreatorUser = Aggregation.unwind("creatorUser", true);
        UnwindOperation unwindInventoryItem = Aggregation.unwind("inventoryItem", true);

        AggregationOperation addRolePermissionObjectIds = context -> new Document("$addFields",
                new Document("creatorUser.roleIdObject", new Document("$toObjectId", "$creatorUser.roleId"))
                        .append("creatorUser.permissionIdObject", new Document("$toObjectId", "$creatorUser.permissionId"))
        );

        LookupOperation lookupRole = LookupOperation.newLookup()
                .from("roles")
                .localField("creatorUser.roleIdObject")
                .foreignField("_id")
                .as("creatorUser.role");

        LookupOperation lookupPermission = LookupOperation.newLookup()
                .from("permissions")
                .localField("creatorUser.permissionIdObject")
                .foreignField("_id")
                .as("creatorUser.permission");

        UnwindOperation unwindRole = Aggregation.unwind("creatorUser.role", true);
        UnwindOperation unwindPermission = Aggregation.unwind("creatorUser.permission", true);

        AggregationOperation addCreatorUserUserId = context -> new Document("$addFields",
                new Document("creatorUser.userId", new Document("$toString", "$creatorUser._id"))
        );

        ProjectionOperation project = Aggregation.project()
                .andExpression("_id").as("id")
                .and("carInfo").as("carInfo")
                .and("customer").as("customer")
                .and("creatorUser.userId").as("creatorUser.userId")
                .and("creatorUser.username").as("creatorUser.username")
                .and("creatorUser.firstName").as("creatorUser.firstName")
                .and("creatorUser.lastName").as("creatorUser.lastName")
                .and("creatorUser.role").as("creatorUser.role")
                .and("creatorUser.permission").as("creatorUser.permission")
                .and("inventoryItem").as("inventoryItem")
                .and("quantity").as("quantity")
                .and("type").as("type")
                .and("description").as("description")
                .and("dateTime").as("dateTime");

        Aggregation aggregation = Aggregation.newAggregation(
                matchCustomerName,
                addCarInfoIdObject,
                addCustomerIdObject,
                addCreatorUserIdObject,
                addInventoryItemIdObject,
                lookupCarInfo,
                unwindCarInfo,
                lookupCustomer,
                unwindCustomer,
                lookupCreatorUser,
                unwindCreatorUser,
                addRolePermissionObjectIds,
                lookupRole,
                unwindRole,
                lookupPermission,
                unwindPermission,
                addCreatorUserUserId,
                lookupInventoryItem,
                unwindInventoryItem,
                project
        );

        AggregationResults<InventoryTransactionResponseDTO> results = mongoTemplate.aggregate(
                aggregation,
                "inventoryTransactionLog",
                InventoryTransactionResponseDTO.class
        );

        return results.getMappedResults();
    }

    public InventoryTransactionResponseDTO findLastTransactionByCustomerFullName(String fullName) {
        AggregationOperation matchCustomerName = Aggregation.match(
                Criteria.where("customer.fullName").regex(".*" + fullName + ".*", "i")
        );

        // ادامه مثل قبل - اضافه کردن ObjectIdها، lookup ها، unwind ها

        AggregationOperation addCarInfoIdObject = context -> new Document("$addFields",
                new Document("carInfoIdObject", new Document("$toObjectId", "$carInfoId"))
        );

        AggregationOperation addCustomerIdObject = context -> new Document("$addFields",
                new Document("customerIdObject", new Document("$toObjectId", "$customerId"))
        );

        AggregationOperation addCreatorUserIdObject = context -> new Document("$addFields",
                new Document("creatorUserIdObject", new Document("$toObjectId", "$creatorUserId"))
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

        LookupOperation lookupCreatorUser = LookupOperation.newLookup()
                .from("users")
                .localField("creatorUserIdObject")
                .foreignField("_id")
                .as("creatorUser");

        LookupOperation lookupInventoryItem = LookupOperation.newLookup()
                .from("inventoryItems")
                .localField("inventoryItemIdObject")
                .foreignField("_id")
                .as("inventoryItem");

        UnwindOperation unwindCarInfo = Aggregation.unwind("carInfo", true);
        UnwindOperation unwindCustomer = Aggregation.unwind("customer", true);
        UnwindOperation unwindCreatorUser = Aggregation.unwind("creatorUser", true);
        UnwindOperation unwindInventoryItem = Aggregation.unwind("inventoryItem", true);

        AggregationOperation addRolePermissionObjectIds = context -> new Document("$addFields",
                new Document("creatorUser.roleIdObject", new Document("$toObjectId", "$creatorUser.roleId"))
                        .append("creatorUser.permissionIdObject", new Document("$toObjectId", "$creatorUser.permissionId"))
        );

        LookupOperation lookupRole = LookupOperation.newLookup()
                .from("roles")
                .localField("creatorUser.roleIdObject")
                .foreignField("_id")
                .as("creatorUser.role");

        LookupOperation lookupPermission = LookupOperation.newLookup()
                .from("permissions")
                .localField("creatorUser.permissionIdObject")
                .foreignField("_id")
                .as("creatorUser.permission");

        UnwindOperation unwindRole = Aggregation.unwind("creatorUser.role", true);
        UnwindOperation unwindPermission = Aggregation.unwind("creatorUser.permission", true);

        AggregationOperation addCreatorUserUserId = context -> new Document("$addFields",
                new Document("creatorUser.userId", new Document("$toString", "$creatorUser._id"))
        );

        ProjectionOperation project = Aggregation.project()
                .andExpression("_id").as("id")
                .and("carInfo").as("carInfo")
                .and("customer").as("customer")
                .and("creatorUser.userId").as("creatorUser.userId")
                .and("creatorUser.username").as("creatorUser.username")
                .and("creatorUser.firstName").as("creatorUser.firstName")
                .and("creatorUser.lastName").as("creatorUser.lastName")
                .and("creatorUser.role").as("creatorUser.role")
                .and("creatorUser.permission").as("creatorUser.permission")
                .and("inventoryItem").as("inventoryItem")
                .and("quantity").as("quantity")
                .and("type").as("type")
                .and("description").as("description")
                .and("dateTime").as("dateTime");

        SortOperation sortByDateTimeDesc = Aggregation.sort(Sort.by(Sort.Direction.DESC, "dateTime"));

        AggregationOperation limitOne = Aggregation.limit(1);

        Aggregation aggregation = Aggregation.newAggregation(
                matchCustomerName,
                addCarInfoIdObject,
                addCustomerIdObject,
                addCreatorUserIdObject,
                addInventoryItemIdObject,
                lookupCarInfo,
                unwindCarInfo,
                lookupCustomer,
                unwindCustomer,
                lookupCreatorUser,
                unwindCreatorUser,
                addRolePermissionObjectIds,
                lookupRole,
                unwindRole,
                lookupPermission,
                unwindPermission,
                addCreatorUserUserId,
                lookupInventoryItem,
                unwindInventoryItem,
                sortByDateTimeDesc,
                limitOne,
                project
        );

        AggregationResults<InventoryTransactionResponseDTO> results = mongoTemplate.aggregate(
                aggregation,
                "inventoryTransactionLog",
                InventoryTransactionResponseDTO.class
        );

        return results.getUniqueMappedResult();
    }

}
