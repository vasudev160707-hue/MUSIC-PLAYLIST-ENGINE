public class Song {
    private int id;
    private String title;
    private String artist;
    private String album;
    private int duration;

    public Song(int id, String title, String artist, String album, int duration) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getAlbum() { return album; }
    public int getDuration() { return duration; }

    public void display() {
        System.out.printf("%-4d | %-28s | %-22s | %-20s | %d sec%n",
                id, title, artist, album, duration);
    }
}
