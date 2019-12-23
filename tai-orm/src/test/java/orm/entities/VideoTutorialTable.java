package orm.entities;

public interface VideoTutorialTable extends BaseTable {
    String video_url = "video_url";
    String preview_picture_url = "preview_picture_url";
    String title = "title";
    String description = "description";
    String active = "active";
    String file_size = "file_size";
    String file_name = "file_name";
}
