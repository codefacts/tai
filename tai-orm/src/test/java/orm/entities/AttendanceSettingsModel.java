package orm.entities;

public interface AttendanceSettingsModel extends BaseModel {
    String attendanceStartTime = "attendanceStartTime";
    String attendanceStopTime = "attendanceStopTime";
    String halfDayUpperHalfStartTime = "halfDayUpperHalfStartTime";
    String halfDayUpperHalfStopTime = "halfDayUpperHalfStopTime";
    String halfDayLowerHalfStartTime = "halfDayLowerHalfStartTime";
    String halfDayLowerHalfStopTime = "halfDayLowerHalfStopTime";
}
