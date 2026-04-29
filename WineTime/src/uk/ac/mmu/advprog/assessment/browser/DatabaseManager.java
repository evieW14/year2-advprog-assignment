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
	};
	
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
			
			// TODO: Query other tables to populate
			wine.grapes = new ArrayList<>();
			wine.harmonize = new ArrayList<>();
			wine.vintages = new ArrayList<>();
			return wine;
		}
		
		// Search for wines based on filter criteria 
		public List<ParsedWineRow> search (FilterCriteria criteria) throws SQLException {
			String sql = """
		            SELECT *
		            FROM wines
		            JOIN wineries USING (winery_id)
		            JOIN regions USING (region_id)
		            WHERE 1=1
		        """;
		    	    
			List<Object> params = new ArrayList<>();
			
		    if(!criteria.name.isEmpty()) {
		    	sql += " AND wine_name LIKE ?";
		    	params.add("%" + criteria.name + "%");
		    }
		    
		    if(!criteria.winery.isEmpty()) {
		    	sql += " AND winery_name LIKE ?";
		    	params.add("%" + criteria.winery + "%");
		    }
		    
		    if(!criteria.type.equals("Any")) {
		    	sql += " AND type = ?";
		    	params.add(criteria.type);
		    }
		    
		    if(!criteria.country.isEmpty()) {
		    	sql += " AND country LIKE ?";
		    	params.add("%" + criteria.country + "%");
		    }
		    
		    if(!criteria.blend.equals("Any")) {
		    	sql += " AND blend_type = ?";
		    	params.add(criteria.blend);
		    }
		    
		    if(!criteria.body.equals("Any")) {
		    	sql += " AND body = ?";
		    	params.add(criteria.body);
		    }
		    
		    if(!criteria.acidity.equals("Any")) {
		    	sql += " AND acidity = ?";
		    	params.add(criteria.acidity);
		    }
		    
		    if(!criteria.grape.isEmpty()) {
		    	sql += " AND wine_id IN (SELECT wine_id FROM wine_grape JOIN grape USING (grape_id) WHERE grape_name LIKE ?)";
		    	params.add("%" + criteria.grape + "%");
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
}
