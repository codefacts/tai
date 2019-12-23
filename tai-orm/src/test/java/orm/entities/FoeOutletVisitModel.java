package orm.entities;

public interface FoeOutletVisitModel extends BaseModel {
    String foe = "foe";
    String outlet = "outlet";
    String visitDate = "visitDate";
    String indexingDate = "indexingDate";
    String clientSideDate = "clientSideDate";
    String latitude = "latitude";
    String longitude = "longitude";
    String accuracy = "accuracy";
    String pictureUri = "pictureUri";
    String geoAddress = "geoAddress";
}
