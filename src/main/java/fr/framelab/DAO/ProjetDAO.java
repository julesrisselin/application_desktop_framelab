package fr.framelab.DAO;

import fr.framelab.DTO.ChallengeDataDTO;
import fr.framelab.Model.Projet;

import java.sql.*;
import java.util.Optional;

public class ProjetDAO {

    private final Connection connection;

    public ProjetDAO(Connection connection) {
        this.connection = connection;
    }


    public void createProjet(Projet projet, int projetId) {
        String sql = "INSERT INTO projets (name, picture, date_start, date_last_edit, id_challenge) VALUES (?,?,?,?,?)";

        ChallengeDataDTO currentChallenge = new ChallengeDataDTO();

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, projet.getName());
            pstmt.setString(2, currentChallenge.getPicture());
            pstmt.setString(3, projet.getDate_start());
            pstmt.setString(4, projet.getDate_last_edit());
            pstmt.setInt(5, projet.getId_challenge());
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

    public void updateProjet(Projet projet, int projetId) {
        String sql = "UPDATE projets SET name = ?,picture = ?, date_start = ?, date_last_edit = ?, id_challenge = ?";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {

            pstmt.setString(1, projet.getName());
            pstmt.setString(2, projet.getPicture());
            pstmt.setString(3, projet.getDate_start());
            pstmt.setString(4, projet.getDate_last_edit());
            pstmt.setInt(5, projet.getId_challenge());
            pstmt.executeUpdate();

            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                throw new IllegalArgumentException("Projet not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save slide: " + e.getMessage(), e);
        }
    }

    public Optional<Projet> readProjet(int projetId) {
        String sql = "SELECT id, name, picture, date_start, date_last_edit, id_challenge FROM Projets WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, projetId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new Projet(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("picture"),
                        rs.getString("date_start"),
                        rs.getString("date_last_edit"),
                        rs.getInt("id_challenge")
                ));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Find failed", e);
        }
    }

    public boolean deleteProjet(int projetId) {
        String sql = "DELETE FROM projets WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, projetId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Delete failed", e);
        }
    }
}