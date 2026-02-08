package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;

/**
 * Direct JDBC utility for cleaning up database records that cannot be deleted
 * through the API due to foreign key constraints.
 *
 * Workaround for BUG-007: The inventory table has a FK constraint on plants.id,
 * and there is no /api/inventory endpoint to clear inventory records via API.
 * This blocks deletion of plants (and subsequently categories) through the API.
 */
public class DatabaseCleanupUtil {

    private static final EnvironmentVariables envVars = SystemEnvironmentVariables.createEnvironmentVariables();

    private static String getDbUrl() {
        return EnvironmentSpecificConfiguration.from(envVars).getProperty("db.url");
    }

    private static String getDbUser() {
        return EnvironmentSpecificConfiguration.from(envVars).getProperty("db.user");
    }

    private static String getDbPassword() {
        return EnvironmentSpecificConfiguration.from(envVars).getProperty("db.password");
    }

    /**
     * Deletes all records from the inventory table via direct JDBC.
     * This must be called before deleting plants to avoid FK constraint violations.
     */
    public static void deleteAllInventory() {
        try (Connection conn = DriverManager.getConnection(getDbUrl(), getDbUser(), getDbPassword());
             Statement stmt = conn.createStatement()) {
            int deleted = stmt.executeUpdate("DELETE FROM inventory");
            System.out.println("Deleted " + deleted + " inventory record(s) via JDBC.");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete inventory records via JDBC - subsequent plant deletions will fail", e);
        }
    }

    public static void deleteInventoryForPlant(int plantId) {
        try (Connection conn = DriverManager.getConnection(getDbUrl(), getDbUser(), getDbPassword());
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM inventory WHERE plant_id = ?")) {
            stmt.setInt(1, plantId);
            int deleted = stmt.executeUpdate();
            System.out.println("Deleted " + deleted + " inventory record(s) for plant ID " + plantId);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete inventory for plant " + plantId, e);
        }
    }
}
