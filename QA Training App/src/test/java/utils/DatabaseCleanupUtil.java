package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Direct JDBC utility for cleaning up database records that cannot be deleted
 * through the API due to foreign key constraints.
 *
 * Workaround for BUG-007: The inventory table has a FK constraint on plants.id,
 * and there is no /api/inventory endpoint to clear inventory records via API.
 * This blocks deletion of plants (and subsequently categories) through the API.
 */
public class DatabaseCleanupUtil {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/qa_training?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root123";

    /**
     * Deletes all records from the inventory table via direct JDBC.
     * This must be called before deleting plants to avoid FK constraint violations.
     */
    public static void deleteAllInventory() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            int deleted = stmt.executeUpdate("DELETE FROM inventory");
            System.out.println("Deleted " + deleted + " inventory record(s) via JDBC.");
        } catch (SQLException e) {
            System.out.println("Warning: Failed to delete inventory records via JDBC: " + e.getMessage());
        }
    }
}
