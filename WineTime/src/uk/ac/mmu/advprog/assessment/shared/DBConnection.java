package uk.ac.mmu.advprog.assessment.shared;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

	private static final String DB_URL =
		    "jdbc:sqlite:data/winetime.db";

    public Connection getConnection() {
        try {
            System.out.println("Connecting to database...");
            return DriverManager.getConnection(DB_URL);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
