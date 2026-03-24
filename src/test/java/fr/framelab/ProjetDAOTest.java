package fr.framelab;

import fr.framelab.DAO.DatabaseManager;
import fr.framelab.DAO.ProjetDAO;
import fr.framelab.Model.Projet;
import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class ProjetDAOTest {

    private ProjetDAO projetDAO;
    private Connection testConnection;
    private static final int TEST_SESSION_ID = 1;

    @BeforeEach
    void setUp() throws SQLException {
        this.testConnection = DatabaseManager.getConnection("memory");

        Statement stmt = this.testConnection.createStatement();
        stmt.execute("PRAGMA foreign_keys = ON");

        this.projetDAO = new ProjetDAO(this.testConnection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (this.testConnection != null && !this.testConnection.isClosed()) {
            this.testConnection.close();
        }
    }

    @Test
    void shouldCreateProjet() {
        Projet projet = new Projet("projet 1", "/test.png", "12/02/25", "13/02/25", 1);

        this.projetDAO.createProjet(projet);

        try (Statement stmt = this.testConnection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM projets WHERE id = " + projet.getId())) {

            assertTrue(rs.next());
            assertEquals("projet 1", rs.getString("name"));
            assertEquals("/test.png", rs.getString("picture"));
            assertEquals("12/02/25", rs.getString("date_start"));
            assertEquals("13/02/25", rs.getString("date_last_edit"));
            assertEquals(1, rs.getInt("id_challenge"));
        } catch (SQLException e) {
            fail("Erreur SQL : " + e.getMessage());
        }
    }

    @Test
    void shouldUpdateProjet() {
        Projet projet = new Projet("projet 1", "/test.png", "12/02/25", "13/02/25", 1);

        this.projetDAO.createProjet(projet);

        projet.setName("test 2");
        projet.setPicture("/test2.png");
        projet.setDate_start("12/03/25");
        projet.setDate_last_edit("13/03/25");
        projet.setId_challenge(2);

        this.projetDAO.updateProjet(projet);

        assertNotEquals(0, projet.getId());
        try (Statement stmt = this.testConnection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM projets WHERE id = " + projet.getId())) {

            assertTrue(rs.next());
            assertEquals("test 2", rs.getString("name"));
            assertEquals("/test2.png", rs.getString("picture"));
            assertEquals("12/03/25", rs.getString("date_start"));
            assertEquals("13/03/25", rs.getString("date_last_edit"));
            assertEquals(2, rs.getInt("id_challenge"));
        } catch (SQLException e) {
            fail("Erreur SQL : " + e.getMessage());
        }
    }

    @Test
    void shouldReadProjet(){
        Projet projet = new Projet("projet 1", "/test.png", "12/02/25", "13/02/25", 1);

        this.projetDAO.createProjet(projet);
        this.projetDAO.readProjet(projet.getId());

        try (Statement stmt = this.testConnection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM projets WHERE id = " + projet.getId())) {

            assertTrue(rs.next());
            assertEquals("projet 1", rs.getString("name"));
            assertEquals("/test.png", rs.getString("picture"));
            assertEquals("12/02/25", rs.getString("date_start"));
            assertEquals("13/02/25", rs.getString("date_last_edit"));
            assertEquals(1, rs.getInt("id_challenge"));
        } catch (SQLException e) {
            fail("Erreur SQL : " + e.getMessage());
        }
    }

    @Test
    void shouldReadAllProjet(){
        Projet projet = new Projet("projet 1", "/test.png", "12/02/25", "13/02/25", 1);
        Projet projet2 = new Projet("projet 2", "/test2.png", "14/02/25", "17/02/25", 2);

        this.projetDAO.createProjet(projet);
        this.projetDAO.readAllProjects();

        try (Statement stmt = this.testConnection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM projets")) {

            assertTrue(rs.next());
            assertEquals("projet 1", rs.getString("name"));
            assertEquals("/test.png", rs.getString("picture"));
            assertEquals("12/02/25", rs.getString("date_start"));
            assertEquals("13/02/25", rs.getString("date_last_edit"));
            assertEquals(1, rs.getInt("id_challenge"));
            assertEquals("projet 2", rs.getString("name"));
            assertEquals("/test2.png", rs.getString("picture"));
            assertEquals("14/02/25", rs.getString("date_start"));
            assertEquals("17/02/25", rs.getString("date_last_edit"));
            assertEquals(2, rs.getInt("id_challenge"));
        } catch (SQLException e) {
            fail("Erreur SQL : " + e.getMessage());
        }
    }

    @Test
    void shouldDeleteProjet(){
        Projet projet = new Projet("projet 1", "/test.png", "12/02/25", "13/02/25", 1);

        this.projetDAO.createProjet(projet);

        this.projetDAO.deleteProjet(projet.getId());

        try (Statement stmt = this.testConnection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM projets WHERE id = " + projet.getId())){

            assertFalse(rs.next());
        } catch (SQLException e){
            fail("Erreur SQL : " + e.getMessage());
        }
    }


}


