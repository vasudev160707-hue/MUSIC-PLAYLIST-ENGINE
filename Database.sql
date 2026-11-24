CREATE DATABASE IF NOT EXISTS music_manager;
USE music_manager;

CREATE TABLE IF NOT EXISTS songs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    artist VARCHAR(100) NOT NULL,
    album VARCHAR(100),
    duration INT
);

CREATE TABLE IF NOT EXISTS playlists (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS playlist_songs (
    playlist_id INT,
    song_id INT,
    position INT,
    FOREIGN KEY (playlist_id) REFERENCES playlists(id),
    FOREIGN KEY (song_id) REFERENCES songs(id)
);

CREATE TABLE IF NOT EXISTS recently_played (
    id INT PRIMARY KEY AUTO_INCREMENT,
    song_id INT,
    played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (song_id) REFERENCES songs(id)
);

INSERT INTO playlists(name)
SELECT 'My Playlist'
WHERE NOT EXISTS (SELECT 1 FROM playlists WHERE name = 'My Playlist');

-- Optional sample songs. Durations are approximate and can vary by version.
INSERT INTO songs(title, artist, album, duration) VALUES
('Tum Hi Ho', 'Arijit Singh', 'Aashiqui 2', 262),
('Channa Mereya', 'Arijit Singh', 'Ae Dil Hai Mushkil', 289),
('Agar Tum Saath Ho', 'Arijit Singh, Alka Yagnik', 'Tamasha', 341),
('Kesariya', 'Arijit Singh', 'Brahmastra', 268),
('Apna Bana Le', 'Arijit Singh', 'Bhediya', 261),
('Phir Le Aya Dil', 'Arijit Singh', 'Barfi!', 346),
('Choo Lo', 'The Local Train', 'Aalas Ka Pedh', 265),
('Aaftaab', 'The Local Train', 'Aalas Ka Pedh', 275),
('Dil Mere', 'The Local Train', 'Aalas Ka Pedh', 245),
('Bandey', 'The Local Train', 'Aalas Ka Pedh', 260),
('Ilahi', 'Arijit Singh', 'Yeh Jawaani Hai Deewani', 228),
('Kabira (Encore)', 'Arijit Singh, Harshdeep Kaur', 'Yeh Jawaani Hai Deewani', 235);

-- Add only the sample songs above to My Playlist; avoid duplicate links.
INSERT INTO playlist_songs(playlist_id, song_id, position)
SELECT p.id, s.id, s.id
FROM playlists p
JOIN songs s ON s.title IN (
    'Tum Hi Ho', 'Channa Mereya', 'Agar Tum Saath Ho', 'Kesariya',
    'Apna Bana Le', 'Phir Le Aya Dil', 'Choo Lo', 'Aaftaab',
    'Dil Mere', 'Bandey', 'Ilahi', 'Kabira (Encore)'
)
WHERE p.name = 'My Playlist'
AND NOT EXISTS (
    SELECT 1 FROM playlist_songs ps
    WHERE ps.playlist_id = p.id AND ps.song_id = s.id
);
