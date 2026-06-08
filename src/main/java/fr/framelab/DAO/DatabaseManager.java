package fr.framelab.DAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {
    private static Connection connection;

    public DatabaseManager(){

    }

    public static Connection getConnection() throws SQLException {
        return getConnection("framelab.db");
    }

    public static Connection getConnection(String name) throws SQLException {
        if (connection == null){
            connection = DriverManager.getConnection("jdbc:sqlite:"+ name);
            connection.createStatement().execute("PRAGMA foreign_keys = ON");
            initializeTableProjets();
        }
        return connection;
    }

    private static void initializeTableProjets() {
        String sql = """
                        CREATE TABLE IF NOT EXISTS projets (
                            id INTEGER PRIMARY KEY,
                            name TEXT NOT NULL,
                            picture TEXT NOT NULL,
                            date_start TEXT NOT NULL,
                            date_last_edit TEXT NOT NULL,
                            id_challenge INTEGER,
                            rotate INTEGER
                        );
                """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create projects table: " + e.getMessage(), e);
        }
    }
}
