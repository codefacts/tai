package orm.entities;

import com.google.common.base.Preconditions;

public enum LeaveType {
    half_day_leave("half_day_leave"),
    sick_leave("sick_leave"),
    casual_leave("casual_leave"),

    rt_close("rt_close"),

    day_off("day_off"),
    training("training"),
    absent("absent"),
    present("present");

    public final String value;

    LeaveType(String value) {
        Preconditions.checkNotNull(value);
        this.value = value;
    }
}
