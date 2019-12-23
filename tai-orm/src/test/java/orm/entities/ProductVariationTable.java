package orm.entities;

public interface ProductVariationTable extends BaseTable {
    String product_id = "product_id";
    String name = "name";
    String sku = "sku";
    String active = "active";
}
