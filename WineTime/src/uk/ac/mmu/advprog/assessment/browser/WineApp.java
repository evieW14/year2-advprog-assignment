package uk.ac.mmu.advprog.assessment.browser;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import uk.ac.mmu.advprog.assessment.browser.DatabaseManager;
import uk.ac.mmu.advprog.assessment.importer.ParsedWineRow;

public class WineApp {
	private static DatabaseManager dbManager;
	private static UIManager uiManager;
	
	public WineApp(DatabaseManager databaseManager) {
		// 1. Setup engine first
		dbManager = databaseManager;
		uiManager =  new UIManager(this);
		
		// 2. Create the window
        JFrame frame = new JFrame("WineTime");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // 3. Create UI — UIManager builds all panels internally
        frame.add(uiManager);

        // 5. Show window last
        frame.setVisible(true);
	}
	
	public static List<ParsedWineRow> getSampleWines() {
	    List<ParsedWineRow> list = new ArrayList<>();

	    // --- Wine 1 ---
	    ParsedWineRow w1 = new ParsedWineRow();
	    w1.wineId = 100001;
	    w1.wineName = "Espumante Moscatel";
	    w1.type = "Sparkling";
	    w1.elaborate = "Varietal/100%";
	    w1.grapes = List.of("Muscat/Moscato");
	    w1.harmonize = List.of("Pork", "Rich Fish", "Shellfish");
	    w1.abv = 7.5;
	    w1.body = "Medium-bodied";
	    w1.acidity = "High";
	    w1.code = "BR";
	    w1.country = "Brazil";
	    w1.regionId = 1001;
	    w1.regionName = "Serra Gaúcha";
	    w1.wineryId = 10001;
	    w1.wineryName = "Casa Perini";
	    w1.website = "http://www.vinicolaperini.com.br";
	    w1.vintages = List.of(2020, 2019, 2018, 2017, 2016);
	    list.add(w1);

	    // --- Wine 2 ---
	    ParsedWineRow w2 = new ParsedWineRow();
	    w2.wineId = 100002;
	    w2.wineName = "Ancellotta";
	    w2.type = "Red";
	    w2.elaborate = "Varietal/100%";
	    w2.grapes = List.of("Ancellotta");
	    w2.harmonize = List.of("Beef", "Barbecue", "Codfish", "Pasta", "Pizza", "Cheese");
	    w2.abv = 12.0;
	    w2.body = "Medium-bodied";
	    w2.acidity = "Medium";
	    w2.code = "BR";
	    w2.country = "Brazil";
	    w2.regionId = 1001;
	    w2.regionName = "Serra Gaúcha";
	    w2.wineryId = 10001;
	    w2.wineryName = "Casa Perini";
	    w2.website = "http://www.vinicolaperini.com.br";
	    w2.vintages = List.of(2016, 2015, 2014, 2013, 2012);
	    list.add(w2);

	    // --- Wine 3 ---
	    ParsedWineRow w3 = new ParsedWineRow();
	    w3.wineId = 100003;
	    w3.wineName = "Cabernet Sauvignon";
	    w3.type = "Red";
	    w3.elaborate = "Varietal/100%";
	    w3.grapes = List.of("Cabernet Sauvignon");
	    w3.harmonize = List.of("Beef", "Lamb", "Poultry");
	    w3.abv = 12.0;
	    w3.body = "Full-bodied";
	    w3.acidity = "High";
	    w3.code = "BR";
	    w3.country = "Brazil";
	    w3.regionId = 1001;
	    w3.regionName = "Serra Gaúcha";
	    w3.wineryId = 10002;
	    w3.wineryName = "Castellamare";
	    w3.website = "https://www.emporiocastellamare.com.br";
	    w3.vintages = List.of(2021, 2020, 2019, 2018, 2017);
	    list.add(w3);

	    // --- Wine 4 ---
	    ParsedWineRow w4 = new ParsedWineRow();
	    w4.wineId = 100004;
	    w4.wineName = "Virtus Moscato";
	    w4.type = "White";
	    w4.elaborate = "Varietal/100%";
	    w4.grapes = List.of("Muscat/Moscato");
	    w4.harmonize = List.of("Sweet Dessert");
	    w4.abv = 12.0;
	    w4.body = "Medium-bodied";
	    w4.acidity = "Medium";
	    w4.code = "BR";
	    w4.country = "Brazil";
	    w4.regionId = 1001;
	    w4.regionName = "Serra Gaúcha";
	    w4.wineryId = 10003;
	    w4.wineryName = "Monte Paschoal";
	    w4.website = "http://www.montepaschoal.com.br";
	    w4.vintages = List.of(2021, 2020, 2019, 2018, 2017);
	    list.add(w4);

	    // --- Wine 5 ---
	    ParsedWineRow w5 = new ParsedWineRow();
	    w5.wineId = 100005;
	    w5.wineName = "Maison de Ville Cabernet-Merlot";
	    w5.type = "Red";
	    w5.elaborate = "Assemblage/Bordeaux Red Blend";
	    w5.grapes = List.of("Cabernet Sauvignon", "Merlot");
	    w5.harmonize = List.of("Beef", "Lamb", "Game Meat", "Poultry");
	    w5.abv = 11.0;
	    w5.body = "Full-bodied";
	    w5.acidity = "Medium";
	    w5.code = "BR";
	    w5.country = "Brazil";
	    w5.regionId = 1001;
	    w5.regionName = "Serra Gaúcha";
	    w5.wineryId = 10000;
	    w5.wineryName = "Aurora";
	    w5.website = "http://www.vinicolaaurora.com.br";
	    w5.vintages = List.of(2021, 2020, 2019, 2018, 2017);
	    list.add(w5);

	    return list;
	}

	public List<ParsedWineRow> getWines() {
		return getSampleWines();
	}
	
	public static void applyFilters(FilterCriteria criteria) throws SQLException {
		// TODO: Implement filter logic here
		List<ParsedWineRow> filterWines = dbManager.search(criteria);
		uiManager.getResultsPanel().updateResults(filterWines);
	}	
}
