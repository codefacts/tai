package test.orm.entities;

public enum ActiveStatus {
    active(true), inactive(false);

    public final boolean isActive;

    ActiveStatus(boolean isActive) {
        this.isActive = isActive;
    }
}
