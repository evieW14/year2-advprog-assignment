package uk.ac.mmu.advprog.assessment.shared;

public class SQLStatements {
	public static final String CREATE_REGION_TABLE =
			"CREATE TABLE IF NOT EXISTS region (" +
			"id INTEGER PRIMARY KEY," +
			"name TEXT NOT NULL UNIQUE," +
			"country TEXT NOT NULL" +
			");";

	public static final String CREATE_WINERY_TABLE =
			"CREATE TABLE IF NOT EXISTS winery (" +
			"id INTEGER PRIMARY KEY," +
			"name TEXT NOT NULL UNIQUE," +
			"region_id INTEGER," +
			"website TEXT," +
			"FOREIGN KEY(region_id) REFERENCES region(id)" +
			");";

	public static final String CREATE_GRAPE_TABLE =
			"CREATE TABLE IF NOT EXISTS grape (" +
			"id INTEGER PRIMARY KEY AUTOINCREMENT," +
			"name TEXT NOT NULL UNIQUE" +
			");";

	public static final String CREATE_PAIRING_TABLE =
			"CREATE TABLE IF NOT EXISTS pairing (" +
			"id INTEGER PRIMARY KEY AUTOINCREMENT," +
			"food TEXT NOT NULL" +
			");";

	public static final String CREATE_WINE_TABLE =
			"CREATE TABLE IF NOT EXISTS wine (" +
			"id INTEGER PRIMARY KEY," +
			"name TEXT NOT NULL," +
			"type TEXT NOT NULL," +
			"blend_type TEXT," +
			"ABV REAL," +
			"acidity TEXT," +
			"body TEXT," +
			"winery_id INTEGER," +
			"FOREIGN KEY(winery_id) REFERENCES winery(id)" +
			");";

	// Some years may be N.V. so need to replace with -1 in the database
	public static final String CREATE_WINE_VINTAGE_TABLE =
			"CREATE TABLE IF NOT EXISTS wine_vintage (" +
			"wine_id INTEGER," +
			"year INTEGER," +
			"FOREIGN KEY(wine_id) REFERENCES wine(id)" +
			");";


	public static final String CREATE_WINE_GRAPE_TABLE =
			"CREATE TABLE IF NOT EXISTS wine_grape (" +
			"wine_id INTEGER," +
			"grape_id INTEGER," +
			"FOREIGN KEY(wine_id) REFERENCES wine(id)," +
			"FOREIGN KEY(grape_id) REFERENCES grape(id)" +
			");";

	public static final String CREATE_WINE_PAIRING_TABLE  =
			"CREATE TABLE IF NOT EXISTS wine_pairing (" +
			"wine_id INTEGER," +
			"pairing_id INTEGER," +
			"FOREIGN KEY(wine_id) REFERENCES wine(id)," +
			"FOREIGN KEY(pairing_id) REFERENCES pairing(id)" +
			");";

	public static String[] getCreateDatabaseStatements() {
		return new String[] {
				CREATE_REGION_TABLE,
				CREATE_WINERY_TABLE,
				CREATE_GRAPE_TABLE,
				CREATE_PAIRING_TABLE,
				CREATE_WINE_TABLE,
				CREATE_WINE_VINTAGE_TABLE,
				CREATE_WINE_GRAPE_TABLE,
				CREATE_WINE_PAIRING_TABLE
		};
	}

}
