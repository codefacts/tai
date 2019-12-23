package test.orm.entities;

public interface AttendanceTable extends BaseTable {
    String sr_id = "sr_id";
    String outlet_id = "outlet_id";

    String present = "present";
    String present_location_id = "present_location_id";

    String picture_uri = "picture_uri";
    String picture_update_date = "picture_update_date";

    String present_date = "present_date";
    String device_date_time = "device_date_time";
    String indexing_date = "indexing_date";
}
