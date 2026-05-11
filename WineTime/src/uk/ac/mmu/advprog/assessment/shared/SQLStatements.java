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
	
	public static final String CREATE_RATING_TABLE = 
			"CREATE TABLE IF NOT EXISTS rating (" +
			"wine_id INTEGER," +
			"vintage INTEGER," +
			"rating_value REAL NOT NULL," +
			"FOREIGN KEY (wine_id) REFERENCES wine(id)" +
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
				CREATE_WINE_PAIRING_TABLE,
				CREATE_RATING_TABLE,
		};
	}
	
	// Create indices to speed up queries for common filtering fields
	public static final String CREATE_INDEX_WINDE_WINERY = 
			"CREATE INDEX IF NOT EXISTS idx_wine_winery_id ON wine(winery_id);";
	
	public static final String CREATE_INDEX_WINERY_REGION = 
			"CREATE INDEX IF NOT EXISTS idx_winery_region_id ON winery(region_id);";
	
	public static final String CREATE_INDEX_WINE_TYPE = 
			"CREATE INDEX IF NOT EXISTS idx_wine_type ON wine(type);";
	
	public static final String CREATE_INDEX_WINE_BODY = 
			"CREATE INDEX IF NOT EXISTS idx_wine_body ON wine(body);";
	
	public static final String CREATE_INDEX_WINE_ACIDITY =
			"CREATE INDEX IF NOT EXISTS idx_wine_acidity ON wine(acidity);";
	
	public static final String CREATE_INDEX_WINE_BLEND = 
			"CREATE INDEX IF NOT EXISTS idx_wine_blend ON wine(blend_type);";
	
	public static final String CREATE_INDEX_WINE_GRAPE = 
			"CREATE INDEX IF NOT EXISTS idx_wine_grape ON wine_grape(wine_id);";
	
	public static final String CREATE_INDEX_RATING_WINE = 
			"CREATE INDEX IF NOT EXISTS idx_rating_wine_id ON rating(wine_id)";
	
	public static final String CREATE_INDEX_RATING_VINTAGE = 
			"CREATE INDEX IF NOT EXISTS idx_rating_wine_vintage ON rating(wine_id, vintage)";
	
	public static String[] getCreateIndexStatements() {
		return new String[] {
				CREATE_INDEX_WINDE_WINERY,
				CREATE_INDEX_WINERY_REGION,
				CREATE_INDEX_WINE_TYPE,
				CREATE_INDEX_WINE_BODY,
				CREATE_INDEX_WINE_ACIDITY,
				CREATE_INDEX_WINE_BLEND,
				CREATE_INDEX_WINE_GRAPE,
				CREATE_INDEX_RATING_WINE,
				CREATE_INDEX_RATING_VINTAGE
		};
	}

}
