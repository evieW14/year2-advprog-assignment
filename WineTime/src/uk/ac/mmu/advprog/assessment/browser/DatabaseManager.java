package uk.ac.mmu.advprog.assessment.browser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uk.ac.mmu.advprog.assessment.importer.ParsedWineRow;
import uk.ac.mmu.advprog.assessment.shared.DBConnection;

public class DatabaseManager {
	private Connection connection;

	public DatabaseManager(DBConnection dbConnection) throws SQLException {
		this.connection = dbConnection.getConnection();
	}

	// Map a ResultSet row to a ParsedWineRow object
		private ParsedWineRow mapRow(ResultSet rs) throws SQLException {
			ParsedWineRow wine = new ParsedWineRow();
			wine.wineId = rs.getInt("wine_id");
			wine.wineName = rs.getString("wine_name");
			wine.type = rs.getString("type");
			wine.elaborate = rs.getString("blend_type");
			wine.abv = rs.getDouble("ABV");
			wine.acidity = rs.getString("acidity");
			wine.body = rs.getString("body");
			wine.country = rs.getString("country");
			wine.regionId = rs.getInt("region_id");
			wine.regionName = rs.getString("region_name");
			wine.wineryId = rs.getInt("winery_id");
			wine.wineryName = rs.getString("winery_name");
			wine.website = rs.getString("website");

			wine.grapes = new ArrayList<>();
			wine.harmonize = new ArrayList<>();
			wine.vintages = new ArrayList<>();
			return wine;
		}

		// Search for wines based on filter criteria
		public List<ParsedWineRow> search (FilterCriteria criteria) throws SQLException {
			String sql = """
		            SELECT w.id AS wine_id, w.name AS wine_name, w.type, w.blend_type, w.ABV, w.acidity, w.body, wr.id AS winery_id, wr.name AS winery_name, wr.website, r.id AS region_id, r.name AS region_name, r.country
		            FROM wine w
		            JOIN winery wr ON w.winery_id = wr.id
		            JOIN region r ON wr.region_id = r.id
		            WHERE 1=1
		        """;

			List<Object> params = new ArrayList<>();

		    if(!criteria.name.isEmpty()) {
		    	sql += " AND w.name LIKE ?";
		    	params.add("%" + criteria.name + "%");
		    }

		    if(!criteria.winery.isEmpty()) {
		    	sql += " AND wr.name LIKE ?";
		    	params.add("%" + criteria.winery + "%");
		    }

		    if(!criteria.type.equals("Any")) {
		    	sql += " AND w.type = ?";
		    	params.add(criteria.type);
		    }

		    if(!criteria.country.isEmpty()) {
		    	sql += " AND r.country LIKE ?";
		    	params.add("%" + criteria.country + "%");
		    }

		    if(!criteria.blend.equals("Any")) {
		    	sql += " AND w.blend_type = ?";
		    	params.add(criteria.blend);
		    }

		    if(!criteria.body.equals("Any")) {
		    	sql += " AND w.body = ?";
		    	params.add(criteria.body);
		    }

		    if(!criteria.acidity.equals("Any")) {
		    	sql += " AND w.acidity = ?";
		    	params.add(criteria.acidity);
		    }

		    if(!criteria.grape.isEmpty()) {
		    	sql += " AND w.id IN (" +
		    			"SELECT wg.wine_id FROM wine_grape wg " +
		    			"JOIN grape g ON wg.grape_id = g.id " +
		    			"WHERE g.name LIKE ?)";
		    	params.add("%" + criteria.grape + "%");
		    }

		    if (criteria.abv != null && criteria.abvOperator != null) {
		    	String op = criteria.abvOperator.equals("<") ? "<" : ">";
		    	sql += " AND w.ABV " + op + " ?";
		    	params.add(criteria.abv);
		    }

		    PreparedStatement stmt = connection.prepareStatement(sql);

		    for (int i = 0; i < params.size(); i++) {
		        stmt.setObject(i + 1, params.get(i));
		    }

		    ResultSet rs = stmt.executeQuery();

			List<ParsedWineRow> filteredList = new ArrayList<>();

		    while (rs.next()) {
		    	filteredList.add(mapRow(rs));
		    }

		    return filteredList;
		}

		public void loadFullDetails(ParsedWineRow wine) throws SQLException {
			// Grapes
			try (PreparedStatement stmt = connection.prepareStatement(
					"SELECT g.name FROM grape g " +
					"JOIN wine_grape wg ON g.id = wg.grape_id " +
					"WHERE wg.wine_id = ?")) {
				stmt.setInt(1,wine.wineId);
				ResultSet rs = stmt.executeQuery();
				wine.grapes = new ArrayList<>();
				while (rs.next()) {
					wine.grapes.add(rs.getString("name"));
				}
			}

			// Vintages
			try (PreparedStatement stmt = connection.prepareStatement(
					"SELECT year FROM wine_vintage WHERE wine_id = ? ORDER BY year DESC")) {
				stmt.setInt(1,wine.wineId);
				ResultSet rs = stmt.executeQuery();
				wine.vintages = new ArrayList<>();
				while (rs.next()) {
					wine.vintages.add(rs.getInt("year"));
				}
			}

			// Food pairings
			try (PreparedStatement stmt = connection.prepareStatement(
					"SELECT p.food FROM pairing p " +
					"JOIN wine_pairing wp ON p.id = wp.pairing_id " +
					"WHERE wp.wine_id = ?")) {
				stmt.setInt(1,wine.wineId);
				ResultSet rs = stmt.executeQuery();
				wine.harmonize = new ArrayList<>();
				while (rs.next()) {
					wine.harmonize.add(rs.getString("food"));
				}
			}
		}

}
