package orm.entities;

public interface VideoTutorialViewTable extends BaseTable {
    String user_id = "user_id";
    String video_tutorial_id = "video_tutorial_id";
    String viewer_id = "viewer_id";
    String view_date = "view_date";
    String view_duration = "view_duration";
}
