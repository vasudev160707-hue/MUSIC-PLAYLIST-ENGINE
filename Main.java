import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        SongDAO dao = new SongDAO();
        int playlistId;

        try {
            playlistId = dao.getOrCreateDefaultPlaylist();
            System.out.println("Database connected successfully!");
        } catch (SQLException e) {
            System.out.println("Could not connect to database: " + e.getMessage());
            sc.close();
            return;
        }

        ArrayList<Song> playlist = loadPlaylist(dao, playlistId);
        int currentIndex = -1;

        while (true) {
            System.out.println("\\n========== MUSIC PLAYLIST MANAGER ==========");
            System.out.println("1. Add Song");
            System.out.println("2. View Playlist");
            System.out.println("3. Remove Song from Playlist");
            System.out.println("4. Play Next Song");
            System.out.println("5. Play Previous Song");
            System.out.println("6. Shuffle Playlist");
            System.out.println("7. Recently Played");
            System.out.println("8. Search by Title or Artist");
            System.out.println("9. Sort Playlist");
            System.out.println("10. Exit");
            System.out.print("Enter choice: ");

            int choice = readInt(sc);
            try {
                switch (choice) {
                    case 1:
                        System.out.print("Song title: ");
                        String title = sc.nextLine().trim();
                        System.out.print("Artist: ");
                        String artist = sc.nextLine().trim();
                        System.out.print("Album: ");
                        String album = sc.nextLine().trim();
                        System.out.print("Duration in seconds: ");
                        int duration = readInt(sc);
                        if (title.isEmpty() || artist.isEmpty() || duration <= 0) {
                            System.out.println("Title, artist and a positive duration are required.");
                            break;
                        }
                        boolean added = dao.addSongToPlaylist(
        new Song(0, title, artist, album, duration), playlistId);

if (added) {
    System.out.println("Song added successfully!");
    playlist = loadPlaylist(dao, playlistId);
    currentIndex = -1;
} else {
    System.out.println("Song already exists in this playlist!");
    System.out.println("Duplicate song was not added.");
}
                        break;

                    case 2:
                        playlist = loadPlaylist(dao, playlistId);
                        dao.printSongs(playlist);
                        break;

                    case 3:
                        System.out.print("Enter song ID to remove: ");
                        int removeId = readInt(sc);
                        dao.removeSongFromPlaylist(playlistId, removeId);
                        playlist = loadPlaylist(dao, playlistId);
                        currentIndex = -1;
                        break;

                    case 4:
                        if (playlist.isEmpty()) {
                            System.out.println("Playlist is empty.");
                            break;
                        }
                        currentIndex = (currentIndex + 1) % playlist.size();
                        playSong(dao, playlist.get(currentIndex));
                        break;

                    case 5:
                        if (playlist.isEmpty()) {
                            System.out.println("Playlist is empty.");
                            break;
                        }
                        if (currentIndex == -1) currentIndex = 0;
                        currentIndex = (currentIndex - 1 + playlist.size()) % playlist.size();
                        playSong(dao, playlist.get(currentIndex));
                        break;

                    case 6:
                        playlist = loadPlaylist(dao, playlistId);
                        Collections.shuffle(playlist);
                        currentIndex = -1;
                        System.out.println("Playlist shuffled for this session.");
                        dao.printSongs(playlist);
                        break;

                    case 7:
                        dao.printSongs(dao.getRecentlyPlayed());
                        break;

                    case 8:
                        System.out.print("Enter title or artist to search: ");
                        String keyword = sc.nextLine().trim();
                        dao.printSongs(dao.searchSongs(keyword));
                        break;

                    case 9:
                        System.out.println("Sort by: 1. Title  2. Artist  3. Duration");
                        System.out.print("Choose: ");
                        String sortChoice = sc.nextLine().trim();
                        if (!sortChoice.equals("1") && !sortChoice.equals("2") && !sortChoice.equals("3")) {
                            System.out.println("Invalid sort choice.");
                            break;
                        }
                        playlist = dao.getSortedSongs(playlistId, sortChoice);
                        currentIndex = -1;
                        dao.printSongs(playlist);
                        break;

                    case 10:
                        System.out.println("Thank you for using Music Playlist Manager!");
                        sc.close();
                        return;

                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        }
    }

    private static int readInt(Scanner sc) {
        while (true) {
            String value = sc.nextLine().trim();
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }

    private static ArrayList<Song> loadPlaylist(SongDAO dao, int playlistId) {
        try {
            return dao.getPlaylistSongs(playlistId);
        } catch (SQLException e) {
            System.out.println("Could not load playlist: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private static void playSong(SongDAO dao, Song song) throws SQLException {
        System.out.println("\\nNow selected (audio is not played):");
        song.display();
        dao.addRecentlyPlayed(song.getId());
    }
}
