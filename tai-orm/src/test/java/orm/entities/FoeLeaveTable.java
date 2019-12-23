package orm.entities;

public interface FoeLeaveTable extends BaseTable {
    String foe_id = "foe_id";
    String territory_master_id = "territory_master_id";
    String foe_outlet_visit_id = "foe_outlet_visit_id";
    String leave_date = "leave_date";
    String authorized_by = "authorized_by";
    String leave_type = "leave_type";
    String remarks = "remarks";
    String active = "active";
}
