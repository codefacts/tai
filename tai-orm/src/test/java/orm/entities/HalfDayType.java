package orm.entities;

import com.google.common.base.Preconditions;

public enum HalfDayType {
    
    upper_half("upper_half"), lower_half("lower_half");

    final String value;

    HalfDayType(String value) {
        Preconditions.checkNotNull(value);
        this.value = value;
    }
}
