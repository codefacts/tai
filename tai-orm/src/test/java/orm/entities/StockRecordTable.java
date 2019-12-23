package orm.entities;

public interface StockRecordTable extends BaseTable {
    String stock_id = "stock_id";
    String outlet_id = "outlet_id";
    String total_amount = "total_amount";
    String total_quantity = "total_quantity";
    String stock_update_date = "stock_update_date";
    String status = "status";
    String remarks = "remarks";
    String status_update_date = "status_update_date";
    String status_updated_by = "status_updated_by";
    String capture_date = "capture_date";
    String indexing_date = "indexing_date";
}
