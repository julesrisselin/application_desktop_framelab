package fr.framelab;
import fr.framelab.DAO.ProjetDAO;
import fr.framelab.Model.Projet;
import org.junit.jupiter.api.*;
import org.sqlite.SQLiteConfig;
import java.sql.*;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class ProjetDAOTest {

        private ProjetDAO projetDAO ;
        private Connection testConnection;
        private static final int TEST_SESSION_ID = 1;

        @BeforeEach
        void setUp() throws SQLException {
            this.testConnection = DriverManager.getConnection("jdbc:sqlite::memory:");

            Statement stmt = this.testConnection.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON");

            this.projetDAO = new ProjetDAO(this.testConnection);
        }

        @AfterEach
        void tearDown() throws SQLException {
            if(this.testConnection != null && !this.testConnection.isClosed()) {
                this.testConnection.close();
            }
        }

        @Test
        void shouldSaveProjet() {
            Projet projet = new Projet();

            this.projetDAO.saveProjet(projet, this.TEST_SESSION_ID);

            assertNotEquals(0, projet.getId());

            try (Statement stmt = this.testConnection.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM session WHERE id = " + projet.getId())) {

                assertTrue(rs.next());
                assertEquals("Présentation", rs.getString("name"));
                assertEquals(0, rs.getInt("position"));
                assertEquals(this.TEST_SESSION_ID, rs.getInt("session_id"));
            } catch (SQLException e) {
                fail("Erreur SQL : " + e.getMessage());
            }
        }

    }
}
