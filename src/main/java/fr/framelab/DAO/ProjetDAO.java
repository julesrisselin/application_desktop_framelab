package fr.framelab.DAO;

import fr.framelab.DTO.ChallengeDataDTO;
import fr.framelab.Model.Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class ProjetDAO {

    private final Connection connection;

    public ProjetDAO(Connection connection) {
        this.connection = connection;
    }


    public void createProjet(Project project) {
        String sql = "INSERT INTO projets (name, picture, date_start, date_last_edit, id_challenge) VALUES (?,?,?,?,?)";

        ChallengeDataDTO currentChallenge = new ChallengeDataDTO();

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, project.getName());
            pstmt.setString(2, currentChallenge.getFullPicture());
            pstmt.setString(3, project.getDate_start());
            pstmt.setString(4, project.getDate_last_edit());
            pstmt.setInt(5, project.getId_challenge());
            pstmt.executeUpdate();

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    project.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save project: " + e.getMessage(), e);
        }
    }

    public void updateProjet(Project project) {
        String sql = "UPDATE projets SET name = ?,picture = ?, date_start = ?, date_last_edit = ?, id_challenge = ?, rotate = ? WHERE id = ?";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {

            pstmt.setString(1, project.getName());
            pstmt.setString(2, project.getPicture());
            pstmt.setString(3, project.getDate_start());
            pstmt.setString(4, project.getDate_last_edit());
            pstmt.setInt(5, project.getId_challenge());
            pstmt.setInt(6, project.getRotate());
            pstmt.setInt(7, project.getId());
            pstmt.executeUpdate();

            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                throw new IllegalArgumentException("Projet not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save projet: " + e.getMessage(), e);
        }
    }

    public Optional<Project> readProjet(int projetId) {
        String sql = "SELECT id, name, picture, date_start, date_last_edit, id_challenge, rotate FROM Projets WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, projetId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new Project(
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


    public ArrayList<Project> readAllProjects() {
        String sql = "SELECT * FROM Projets";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            ArrayList<Project> Allprojects = new ArrayList<Project>();

            while (rs.next()) {
                Allprojects.add(new Project(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("picture"),
                        rs.getString("date_start"),
                        rs.getString("date_last_edit"),
                        rs.getInt("id_challenge")
                ));
            }
            return Allprojects;
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