package test.orm.entities;

import com.google.common.base.Preconditions;

public enum SecReviewType {
    reporting_documentation("reporting-&-documentation"),
    appearance_hygiene("appearance-&-hygiene"),
    communication("communication");

    public final String value;

    SecReviewType(String value) {
        Preconditions.checkNotNull(value);
        this.value = value;
    }
}
