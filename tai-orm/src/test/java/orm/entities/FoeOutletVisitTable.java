package orm.entities;

public interface FoeOutletVisitTable extends BaseTable {
    String foe_id = "foe_id";
    String outlet_id = "outlet_id";
    String visit_date = "visit_date";
    String indexing_date = "indexing_date";
    String client_side_date = "client_side_date";
    String latitude = "latitude";
    String longitude = "longitude";
    String accuracy = "accuracy";
    String picture_uri = "picture_uri";
    String geo_address = "geo_address";
}
