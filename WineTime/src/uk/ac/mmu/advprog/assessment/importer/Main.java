package uk.ac.mmu.advprog.assessment.importer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

import uk.ac.mmu.advprog.assessment.shared.DBConnection;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {
        System.out.println("Starting WineTime Importer...");
        System.out.println("Deleting existing database...");
        Files.deleteIfExists(Paths.get("data/winetime.db"));

        System.out.println("Creating new database...");
        DBConnection dbConnection = new DBConnection();
        DatabaseManager dbManager = new DatabaseManager(dbConnection);

        System.out.println("Parsing CSV file...");
        CsvImporter importer = new CsvImporter();
        try {
        	importer.run(dbConnection.getConnection(), dbManager);
        } catch (SQLException e) {
			e.printStackTrace();
        }

//        System.out.println("Creating indices...");
//        dbManager.createIndices();

        System.out.println("Import complete!");
    }
}
