package orm.entities;

import com.google.common.base.Preconditions;

public enum FoeLeaveType {
    half_day_leave("half_day_leave"),
    sick_leave("sick_leave"),
    casual_leave("casual_leave"),

    day_off("day_off"),
    training("training"),
    meeting("meeting"),
    absent("absent"),
    present("present");

    public final String value;

    FoeLeaveType(String value) {
        Preconditions.checkNotNull(value);
        this.value = value;
    }
}
