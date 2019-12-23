package test.orm.entities;

public interface SaleItemTable extends BaseTable {
    String sale_id = "sale_id";
    String product_variation_id = "product_variation_id";
    String quantity = "quantity";
    String unit_price = "unit_price";
    String billed_amount = "billed_amount";
    String paid_amount = "paid_amount";
    String bar_code = "bar_code";
    String picture_uri = "picture_uri";
}
