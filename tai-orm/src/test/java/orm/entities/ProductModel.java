package orm.entities;

public interface ProductModel extends BaseModel {
    String name = "name";
    String productVariations = "productVariations";
    String unitPrice = "unitPrice";
    String isCurrentTarget = "isCurrentTarget";
    String active = "active";
}
