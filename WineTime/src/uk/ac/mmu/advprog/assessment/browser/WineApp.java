package uk.ac.mmu.advprog.assessment.browser;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import uk.ac.mmu.advprog.assessment.importer.ParsedWineRow;

public class WineApp {
	private static DatabaseManager dbManager;
	private static UIManager uiManager;

	public WineApp(DatabaseManager databaseManager) {
		// 1. Setup engine first
		dbManager = databaseManager;
		uiManager = new UIManager(this);

		// 2. Create the window
		JFrame frame = new JFrame("WineTime");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(1500, 800);

		// 3. Create UI — UIManager builds all panels internally
		frame.add(uiManager);

		// 4. Show window last
		frame.setVisible(true);
	}

	public List<ParsedWineRow> getWines() {
		return new ArrayList<>();
	}

	public static void applyFilters(FilterCriteria criteria) throws SQLException {
		List<ParsedWineRow> filterWines = dbManager.search(criteria);
		uiManager.getResultsPanel().updateResults(filterWines);
		uiManager.getDetailPanel().clear();
	}

	public static void showDetail(ParsedWineRow wine) {
		try {
			dbManager.loadFullDetails(wine);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		uiManager.getDetailPanel().showWine(wine);
	}
}
