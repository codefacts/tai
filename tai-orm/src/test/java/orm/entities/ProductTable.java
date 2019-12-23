package orm.entities;

public interface ProductTable extends BaseTable {
    String name = "name";
    String unit_price = "unit_price";
    String is_current_target = "is_current_target";
    String active = "active";
}
