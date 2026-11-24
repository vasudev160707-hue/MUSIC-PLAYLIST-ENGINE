# 🎵 Music Playlist Engine

A Java-based console application that allows users to manage their music playlists using Core Java, JDBC, and MySQL. The project provides features for organizing songs, managing playlists, searching and sorting music, and maintaining recently played history.

## 📌 Project Overview

Music Playlist Engine is a console-based music playlist management system developed using Java and MySQL.

It demonstrates the practical implementation of Object-Oriented Programming, Database Connectivity (JDBC), SQL queries, and data management in Java.

## ✨ Features

- **Add Songs:** Add songs with title, artist, album, and duration.
- **View Playlist:** Display songs available in the playlist.
- **Remove Songs:** Remove selected songs from the playlist.
- **Next & Previous:** Navigate between songs in the playlist.
- **Shuffle Playlist:** Shuffle the playlist for the current session.
- **Recently Played:** Maintain and view recently played song history.
- **Search Songs:** Search songs using title or artist name.
- **Sort Playlist:** Organize songs using sorting functionality.

## 🛠️ Technologies Used

| Technology | Purpose |
|---|---|
| Java | Core application development |
| JDBC | Java Database Connectivity |
| MySQL | Database management |
| SQL | Database creation and queries |
| MySQL Connector/J | Connecting Java with MySQL |

## 📂 Project Structure

```text
MUSIC-PLAYLIST-ENGINE/
│
├── Main.java
├── DBConnection.java
├── Song.java
├── SongDAO.java
├── Database.sql
├── README.md
│
└── lib/
    └── mysql-connector-j-26.10.0.jar
```

## 🗄️ Database Structure

The project uses a MySQL database named `music_manager`.

The database contains the following tables:

| Table | Description |
|---|---|
| songs | Stores song information |
| playlists | Stores playlist details |
| playlist_songs | Maintains songs associated with playlists and their positions |
| recently_played | Stores recently played song history |

The database schema and initial setup are provided in `Database.sql`.

## ⚙️ Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/vasudev160707-hue/MUSIC-PLAYLIST-ENGINE.git
```

### 2. Open the Project

Navigate to the project directory:

```bash
cd MUSIC-PLAYLIST-ENGINE
```

### 3. Configure MySQL

- Make sure MySQL Server is installed and running.
- Open MySQL Workbench or MySQL Command Line.
- Execute the SQL commands present in `Database.sql`.
- This will create the required database and tables.

### 4. Configure Database Connection

Open `DBConnection.java` and update the database credentials according to your MySQL configuration.

```java
String url = "jdbc:mysql://localhost:3306/music_manager";
String username = "root";
String password = "YOUR_PASSWORD";
```

Replace `YOUR_PASSWORD` with your actual MySQL password.

### 5. Compile the Project

Make sure the MySQL Connector/J JAR is present inside the `lib` directory.

For Windows PowerShell:

```powershell
javac -cp ".;lib/mysql-connector-j-26.10.0.jar" *.java
```

### 6. Run the Application

```powershell
java -cp ".;lib/mysql-connector-j-26.10.0.jar" Main
```

## 🧠 Concepts Implemented

This project demonstrates:

- Object-Oriented Programming (OOP)
- Classes and Objects
- Encapsulation
- Constructors and Methods
- Java Collections Framework
- JDBC Connectivity
- MySQL Database Operations
- SQL Queries (CRUD Operations)
- Exception Handling
- Modular Programming

## 🚀 Future Scope

Possible future improvements include:

- Graphical User Interface (GUI)
- Multiple user accounts
- Multiple custom playlists
- Actual audio playback integration
- Improved playlist recommendations

## 👨‍💻 Team Members

| Name | Student ID |
|---|---|
| Vasudev Kansal | 2510990335 |
| Yuvraj Chhabra | 2510990369 |
| Krish | 2510990359 |
| Mehul | 2510990392 |

## 📄 License

This project is developed for educational and academic purposes.

---

**Music Playlist Engine – Organize, Manage, and Explore Your Music Collection.** 🎶
