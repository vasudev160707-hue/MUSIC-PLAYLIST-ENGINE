MUSIC PLAYLIST MANAGER - QUICK SETUP

1. Copy the Java files into your Music Playlist Engine folder.
2. Put mysql-connector-j-26.1.0.jar inside the lib folder.
3. In DBConnection.java, replace your_password with your MySQL root password.
4. Run Database.sql in MySQL Workbench (if tables already exist, do not run duplicate sample INSERTs again).
5. Open a terminal in this folder and run:

   javac -cp ".;lib/mysql-connector-j-26.1.0.jar" *.java
   java -cp ".;lib/mysql-connector-j-26.1.0.jar" Main

Features: add song, view playlist, remove from playlist, next/previous selection,
shuffle for current session, recently played, search, sort.

Note: This is a terminal-based manager. It does not play audio.
Song durations in sample data are approximate and can vary by version.
