package orm.entities;

public interface StockRecordModel extends BaseModel {
    String stock = "stock";
    String outlet = "outlet";
    String items = "items";
    String totalAmount = "totalAmount";
    String totalQuantity = "totalQuantity";
    String stockUpdateDate = "stockUpdateDate";
    String status = "status";
    String remarks = "remarks";
    String statusUpdateDate = "statusUpdateDate";
    String statusUpdatedBy = "statusUpdatedBy";
    String captureDate = "captureDate";
    String indexingDate = "indexingDate";
}
