package fr.framelab.DAO;
import fr.framelab.Model.Projet;

import java.sql.*;

public class ProjetDAO {

        private final Connection connection;

        public ProjetDAO(Connection connection){
            this.connection = connection;
            initializeTable();
        }

        private void initializeTable(){
            String sql = """
                CREATE TABLE IF NOT EXISTS projets {
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    name TEXT (255) NOT NULL,
                    picture TEXT (255) NOT NULL,
                    date_start TEXT (255) NOT NULL,
                    date_last_edit TEXT (255) NOT NULL,
                    id_challenge INT
                };
        """;

            try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
                pstmt.execute();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to create projects table: " + e.getMessage(), e);
            }
        }


    public void saveProjet(Projet projet, int projetId) {
        String sql = "INSERT INTO projets (name) VALUES (?)";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, projet.getName());
            pstmt.executeUpdate();

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    projet.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save slide: " + e.getMessage(), e);
        }
    }

}
