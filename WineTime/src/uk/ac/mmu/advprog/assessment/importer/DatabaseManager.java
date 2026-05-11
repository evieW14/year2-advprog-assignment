package uk.ac.mmu.advprog.assessment.importer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import uk.ac.mmu.advprog.assessment.shared.DBConnection;
import uk.ac.mmu.advprog.assessment.shared.SQLStatements;

public class DatabaseManager {
	private Connection connection;

	// Use HashMap as in-memory caches to avoid redundant database lookups for any repeated values
	// This is most common with the grape, pairings and region fields
	private final Set<Integer> wineryCache = new HashSet<>();
	private final Map<String, Integer> grapeCache = new HashMap<>();
	private final Map<String, Integer> pairingCache = new HashMap<>();
	private final Map<Integer, Integer> regionCache = new HashMap<>();

    // PreparedStatements
    private PreparedStatement insertRegion;
    private PreparedStatement selectRegionId;
    private PreparedStatement insertWinery;
    private PreparedStatement insertWine;
    private PreparedStatement insertWineVintage;
    private PreparedStatement insertGrape;
    private PreparedStatement selectGrapeId;
    private PreparedStatement insertWineGrape;
    private PreparedStatement insertPairing;
    private PreparedStatement selectPairingId;
    private PreparedStatement insertWinePairing;
    private PreparedStatement insertRating;
    

	public DatabaseManager(DBConnection dbConnection) throws SQLException {
		this.connection = dbConnection.getConnection();
		createDatabase();
		prepareStatements();
	}

	public void createDatabase() {
		try (Statement stmt = connection.createStatement()) {
			// Added these PRAGMA commands 
			stmt.executeUpdate("PRAGMA journal_mode=WAL");
			stmt.executeUpdate("PRAGMA synchronous=NORMAL");
			for (String sql :  SQLStatements.getCreateDatabaseStatements()) {
				stmt.executeUpdate(sql);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createIndices() {
		try (Statement stmt = connection.createStatement()) {
			for (String sql :  SQLStatements.getCreateIndexStatements()) {
				stmt.executeUpdate(sql);
			}
			System.out.println("Indices created.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void prepareStatements() throws SQLException {
		// This function can be used to prepare any SQL statements that will be reused during the import process
        insertRegion = connection.prepareStatement("INSERT OR IGNORE INTO region(id, name, country) VALUES (?, ?, ?)");
        selectRegionId = connection.prepareStatement("SELECT id FROM region WHERE id = ?");
        insertWinery = connection.prepareStatement("INSERT OR IGNORE INTO winery(id, name, region_id, website) VALUES (?, ?, ?, ?)");
        insertWine = connection.prepareStatement("INSERT OR IGNORE INTO wine(id, name, type, blend_type, ABV, acidity, body, winery_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        insertWineVintage = connection.prepareStatement("INSERT OR IGNORE INTO wine_vintage(wine_id, year) VALUES (?, ?)");
        insertGrape = connection.prepareStatement("INSERT OR IGNORE INTO grape(name) VALUES (?)");
        selectGrapeId = connection.prepareStatement("SELECT id FROM grape WHERE name = ?");
        insertWineGrape = connection.prepareStatement("INSERT OR IGNORE INTO wine_grape(wine_id, grape_id) VALUES (?, ?)");
        insertPairing = connection.prepareStatement("INSERT OR IGNORE INTO pairing(food) VALUES (?)");
        selectPairingId = connection.prepareStatement("SELECT id FROM pairing WHERE food = ?");
        insertWinePairing = connection.prepareStatement("INSERT OR IGNORE INTO wine_pairing(wine_id, pairing_id) VALUES (?, ?)");
        insertRating = connection.prepareStatement("INSERT INTO rating(wine_id, vintage, rating_value) VALUES (?, ?, ?)");
	}

	// Check if region exists, if not insert it, then return the region id
	public int getOrInsertRegion(int id, String name, String country) throws SQLException {
		if (regionCache.containsKey(id)) return regionCache.get(id);
		
		insertRegion.setInt(1, id);
		insertRegion.setString(2, name);
		insertRegion.setString(3, country);

		insertRegion.executeUpdate();

		selectRegionId.setInt(1, id);
		ResultSet rs = selectRegionId.executeQuery();

		if (rs.next()) {
			int regionId = rs.getInt("id");
			regionCache.put(id, regionId);
			return regionId;
		} else {
			throw new SQLException("Failed to insert or retrieve region with name: " + name);
		}

	}

	// Check if winery exists, if not insert it
	public void insertWinery(int id, String name, int regiondId, String website) throws SQLException {
		if (wineryCache.contains(id)) return;
		insertWinery.setInt(1, id);
		insertWinery.setString(2, name);
		insertWinery.setInt(3, regiondId);
		insertWinery.setString(4, website);

		insertWinery.executeUpdate();
		wineryCache.add(id);
	}

	//Check if wine exists, if not insert it
	public void insertWine(int id, String name, String type, String blend_type, double ABV, String acidity, String body, int winery_id) throws SQLException {
		insertWine.setInt(1, id);
		insertWine.setString(2, name);
		insertWine.setString(3, type);
		insertWine.setString(4, blend_type);
		insertWine.setDouble(5, ABV);
		insertWine.setString(6, acidity);
		insertWine.setString(7, body);
		insertWine.setInt(8, winery_id);

		insertWine.addBatch();
	}

	// Check if vintage exists for the wine, if not insert it
	public void insertWineVintage(int wineId, int year) throws SQLException {
		insertWineVintage.setInt(1, wineId);
		insertWineVintage.setInt(2, year);

		insertWineVintage.addBatch();
	}

	// Check if grape exists, if not insert it, then return the grape id
	public int getOrInsertGrape(String name) throws SQLException {
		if (grapeCache.containsKey(name)) return grapeCache.get(name);
		
		insertGrape.setString(1, name);
		insertGrape.executeUpdate();

		selectGrapeId.setString(1, name);
		ResultSet rs = selectGrapeId.executeQuery();

		if (rs.next()) {
			int grapeId = rs.getInt("id");
			grapeCache.put(name, grapeId);
			return grapeId;
		} else {
			throw new SQLException("Failed to insert or retrieve grape with name: " + name);
		}
	}

	// Check if wine-grape pair exists, if not insert it
	public void insertWineGrape(int wineId, int grapeId) throws SQLException {
		insertWineGrape.setInt(1, wineId);
		insertWineGrape.setInt(2, grapeId);

		insertWineGrape.addBatch();
	}

	// Check if pairing exists, if not insert it, then return the pairing id
	public int getOrInsertPairing(String food) throws SQLException {
		if (pairingCache.containsKey(food)) return pairingCache.get(food);
		
		insertPairing.setString(1, food);
		insertPairing.executeUpdate();

		selectPairingId.setString(1, food);
		ResultSet rs = selectPairingId.executeQuery();
		
		if (rs.next()) {
			int pairingId = rs.getInt("id");
			pairingCache.put(food, pairingId);
			return pairingId;
		} else {
			throw new SQLException("Failed to insert or retrieve pairing with food: " + food);
		}
	}

	// Check if wine-pairing pair exists, if not insert it
	public void insertWinePairing(int wineId, int pairingId) throws SQLException {
		insertWinePairing.setInt(1, wineId);
		insertWinePairing.setInt(2, pairingId);

		insertWinePairing.addBatch();
	}
	
	// Insert rating
	public void insertRating(int wineId, int vintage, double ratingValue) throws SQLException {
		insertRating.setInt(1, wineId);
		insertRating.setInt(2, vintage);
		insertRating.setDouble(3, ratingValue);
		
		insertWinePairing.addBatch();
	}
	
	// Execute all batched inserts
	public void executeBatches() throws SQLException {
		insertWine.executeBatch();
		insertWineVintage.executeBatch();
		insertWineGrape.executeBatch();
		insertWinePairing.executeBatch();
	}
	
	// Execute Ratings batches
	public void executeRatings() throws SQLException {
		insertRating.executeBatch();
	}
}
