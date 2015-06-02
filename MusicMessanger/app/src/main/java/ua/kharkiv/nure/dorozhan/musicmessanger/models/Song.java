package ua.kharkiv.nure.dorozhan.musicmessanger.models;

public class Song {
    private long id;
    private String title;
    private String album;
    private String performer;

    public Song() {
    }

    public Song(long id, String title, String album, String performer) {
        this.id = id;
        this.title = title;
        this.album = album;
        this.performer = performer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
