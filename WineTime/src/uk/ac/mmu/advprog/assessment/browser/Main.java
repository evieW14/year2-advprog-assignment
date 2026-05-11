package uk.ac.mmu.advprog.assessment.browser;

import java.io.File;
import java.sql.SQLException;

import uk.ac.mmu.advprog.assessment.shared.DBConnection;

public class Main {
	public static void main(String[] args) throws SQLException {
		System.out.println("Browser starting...");
		System.out.println("Browser DB path = " + new File("data/winetime.db").getAbsolutePath());

		DBConnection dbConnection = new DBConnection();
		DatabaseManager dbManager = new DatabaseManager(dbConnection);

		WineApp wineApp = new WineApp(dbManager);
	}
}
