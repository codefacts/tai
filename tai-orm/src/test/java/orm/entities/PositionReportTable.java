package orm.entities;

public interface PositionReportTable extends BaseTable {
    String latitude = "latitude";
    String longitude = "longitude";
    String accuracy = "accuracy";

    String time = "time";
    String sr_id = "sr_id";
    String outlet_id = "outlet_id";
}
