package orm.entities;

public interface AttendanceSettingsTable extends BaseTable {
    String attendance_start_time = "attendance_start_time";
    String attendance_stop_time = "attendance_stop_time";
    String half_day_upper_half_start_time = "half_day_upper_half_start_time";
    String half_day_upper_half_stop_time = "half_day_upper_half_stop_time";
    String half_day_lower_half_start_time = "half_day_lower_half_start_time";
    String half_day_lower_half_stop_time = "half_day_lower_half_stop_time";
}
