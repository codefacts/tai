package orm.entities;

public enum StockStatus {
    sec_present("sec_present"), sec_absent("sec_absent"), sec_leave("sec_leave"), sec_training("sec_training"),
    rt_close("rt_close"), day_off("day_off"), vacant("vacant"), others("others");

    public final String value;

    StockStatus(String value) {
        this.value = value;
    }
}
