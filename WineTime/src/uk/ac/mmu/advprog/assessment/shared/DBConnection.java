package uk.ac.mmu.advprog.assessment.shared;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

	private static final String DB_URL =
		    "jdbc:sqlite:/Users/eviewhite/assignment/WineTime/data/winetime.db";

    public Connection getConnection() {
        try {
            System.out.println("Connecting to database...");
            System.out.println("JDBC opening: " + new File("data/winetime.db").getAbsolutePath());
            return DriverManager.getConnection(DB_URL);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
