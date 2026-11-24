
import java.sql.*;
import java.util.ArrayList;

public class SongDAO {

    public int getOrCreateDefaultPlaylist() throws SQLException {
        String find = "SELECT id FROM playlists ORDER BY id LIMIT 1";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(find);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt("id");
        }

        String insert = "INSERT INTO playlists(name) VALUES (?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     insert, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, "My Playlist");
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }

        throw new SQLException("Could not create a playlist.");
    }

    // Add song with duplicate checking
    public boolean addSongToPlaylist(Song song, int playlistId)
            throws SQLException {

        try (Connection con = DBConnection.getConnection()) {

            int songId = -1;

            // Find an existing song with the same title and artist
            String findSong =
                    "SELECT id FROM songs " +
                    "WHERE LOWER(TRIM(title)) = LOWER(TRIM(?)) " +
                    "AND LOWER(TRIM(artist)) = LOWER(TRIM(?)) LIMIT 1";

            try (PreparedStatement ps = con.prepareStatement(findSong)) {
                ps.setString(1, song.getTitle());
                ps.setString(2, song.getArtist());

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        songId = rs.getInt("id");
                    }
                }
            }

            // Check if this song is already in this playlist
            if (songId != -1) {

                String check =
                        "SELECT 1 FROM playlist_songs " +
                        "WHERE playlist_id = ? AND song_id = ?";

                try (PreparedStatement ps = con.prepareStatement(check)) {
                    ps.setInt(1, playlistId);
                    ps.setInt(2, songId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            return false;
                        }
                    }
                }

            } else {

                // Insert a new song into the songs table
                String insertSong =
                        "INSERT INTO songs(title, artist, album, duration) " +
                        "VALUES (?, ?, ?, ?)";

                try (PreparedStatement ps = con.prepareStatement(
                        insertSong, Statement.RETURN_GENERATED_KEYS)) {

                    ps.setString(1, song.getTitle());
                    ps.setString(2, song.getArtist());
                    ps.setString(3, song.getAlbum());
                    ps.setInt(4, song.getDuration());

                    ps.executeUpdate();

                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys.next()) {
                            songId = keys.getInt(1);
                        }
                    }
                }
            }

            // Add the song to the playlist
            addSongLink(con, playlistId, songId);

            return true;
        }
    }

    private void addSongLink(Connection con, int playlistId, int songId)
            throws SQLException {

        String sql =
                "INSERT INTO playlist_songs(playlist_id, song_id, position) " +
                "VALUES (?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, playlistId);
            ps.setInt(2, songId);
            ps.setInt(3, songId);
            ps.executeUpdate();
        }
    }

    public ArrayList<Song> getPlaylistSongs(int playlistId)
            throws SQLException {

        String sql =
                "SELECT s.* FROM songs s " +
                "JOIN playlist_songs ps ON s.id = ps.song_id " +
                "WHERE ps.playlist_id = ? ORDER BY ps.position, s.id";

        ArrayList<Song> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, playlistId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(readSong(rs));
                }
            }
        }

        return list;
    }

    public void removeSongFromPlaylist(int playlistId, int songId)
            throws SQLException {

        String sql =
                "DELETE FROM playlist_songs " +
                "WHERE playlist_id = ? AND song_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, playlistId);
            ps.setInt(2, songId);

            int rows = ps.executeUpdate();

            System.out.println(rows > 0
                    ? "Song removed from playlist."
                    : "Song ID not found in this playlist.");
        }
    }

    public void addRecentlyPlayed(int songId) throws SQLException {

        String sql = "INSERT INTO recently_played(song_id) VALUES (?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, songId);
            ps.executeUpdate();
        }
    }

    public ArrayList<Song> getRecentlyPlayed() throws SQLException {

        String sql =
                "SELECT s.* FROM recently_played r " +
                "JOIN songs s ON r.song_id = s.id " +
                "ORDER BY r.played_at DESC, r.id DESC LIMIT 20";

        ArrayList<Song> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(readSong(rs));
            }
        }

        return list;
    }

    public ArrayList<Song> searchSongs(String keyword)
            throws SQLException {

        String sql =
                "SELECT * FROM songs " +
                "WHERE title LIKE ? OR artist LIKE ? ORDER BY title";

        ArrayList<Song> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String value = "%" + keyword + "%";

            ps.setString(1, value);
            ps.setString(2, value);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(readSong(rs));
                }
            }
        }

        return list;
    }

    public ArrayList<Song> getSortedSongs(int playlistId, String sortBy)
            throws SQLException {

        String order;

        switch (sortBy) {
            case "1":
                order = "s.title";
                break;
            case "2":
                order = "s.artist";
                break;
            case "3":
                order = "s.duration";
                break;
            default:
                order = "ps.position";
        }

        String sql =
                "SELECT s.* FROM songs s " +
                "JOIN playlist_songs ps ON s.id = ps.song_id " +
                "WHERE ps.playlist_id = ? ORDER BY " + order + ", s.title";

        ArrayList<Song> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, playlistId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(readSong(rs));
                }
            }
        }

        return list;
    }

    private Song readSong(ResultSet rs) throws SQLException {
        return new Song(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("artist"),
                rs.getString("album"),
                rs.getInt("duration")
        );
    }

    public void printSongs(ArrayList<Song> songs) {

        if (songs.isEmpty()) {
            System.out.println("No songs found.");
            return;
        }

        System.out.println(
                "ID   | TITLE                        | ARTIST                 | ALBUM                | DURATION");

        System.out.println(
                "------------------------------------------------------------------------------------------------");

        for (Song song : songs) {
            song.display();
        }
    }
}
