package test.orm.entities;

import com.google.common.base.Preconditions;

public enum UserType {
    system("system"), admin("admin"), admin_readonly("admin_readonly"), sec("sec"), foe("foe"), fom("fom");

    public final String value;

    UserType(String value) {
        Preconditions.checkNotNull(value);
        this.value = value;
    }
}
