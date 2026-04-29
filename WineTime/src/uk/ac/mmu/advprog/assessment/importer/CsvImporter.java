package uk.ac.mmu.advprog.assessment.importer;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CsvImporter {
	Map<String, Integer> headerMap;

	public void run(Connection conn, DatabaseManager dbManager) throws SQLException {
    	try (BufferedReader br = Files.newBufferedReader(Paths.get("data/wines.csv"))) {
    		String headerLine = br.readLine();
			this.headerMap = buildHeaderMap(headerLine);


			// Start transaction
			conn.setAutoCommit(false);

			String line;
			int count = 0;
			while ((line = br.readLine()) != null) {
				ParsedWineRow row = processRow(line);
				if (row != null) {
					insertRow(row, dbManager);

				}

				count ++;

				// Commit every 1000 lines to avoid long transactions and reduce memory usage
				if (count % 5 == 0) {
					System.out.println("Processed " + count + " lines...");
					conn.commit();
				}
			}

			conn.commit(); // Final commit after processing all lines

    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
	}

	public Map<String, Integer> buildHeaderMap(String headerLine) {
		String[] headers = CsvParser.parseCsvLine(headerLine);
		Map<String, Integer> headerMap = new HashMap<>();

		for (int i = 0; i < headers.length; i++) {
			headerMap.put(headers[i], i);
		}

		return headerMap;
	}

	public void insertRow(ParsedWineRow wineRow, DatabaseManager dbManager) throws SQLException {
		// This function will handle inserting the parsed wine row into the database
		// It will use the DatabaseManager to insert the necessary records into the Wine, Region, and Winery tables

		//1. REGION
		int regionId = dbManager.getOrInsertRegion(
				wineRow.regionId,
				wineRow.regionName,
				wineRow.country
				);

		//2. WINERY
		dbManager.insertWinery(
				wineRow.wineryId,
				wineRow.wineryName,
				regionId,
				wineRow.website
				);

		//3. WINE
		dbManager.insertWine(
				wineRow.wineId,
				wineRow.wineName,
				wineRow.type,
				wineRow.elaborate,
				wineRow.abv,
				wineRow.acidity,
				wineRow.body,
				wineRow.wineryId
				);

		//4. VINTAGES
		for (Integer year : wineRow.vintages) {
			dbManager.insertWineVintage(wineRow.wineId, year);
		}

		//5. GRAPES
		for (String grapeName : wineRow.grapes) {
			int grapeId = dbManager.getOrInsertGrape(grapeName);
			dbManager.insertWineGrape(wineRow.wineId, grapeId);
		}

		//6. HARMONIZE (Pairings)
		for (String pairing : wineRow.harmonize) {
			int pairingId = dbManager.getOrInsertPairing(pairing);
			dbManager.insertWinePairing(wineRow.wineId, pairingId);
		}
	}

	public ParsedWineRow processRow(String line) {
		// This function will handle processing a single row from the CSV file
		// It will parse the fields, create the necessary objects, and insert them into the database
		String[] fields = CsvParser.parseCsvLine(line);

		if (fields.length != headerMap.size()) {
			System.err.println("Warning: Skipping line with incorrect number of fields: " + line);
			return null;
		}

		try {

			ParsedWineRow wineRow = new ParsedWineRow();

			wineRow.wineId = Integer.parseInt(fields[headerMap.get("WineID")]);
			wineRow.wineName = fields[headerMap.get("WineName")];
			wineRow.type = fields[headerMap.get("Type")];
			wineRow.elaborate = fields[headerMap.get("Elaborate")];

			wineRow.grapes = CsvParser.parseList(fields[headerMap.get("Grapes")]);
			wineRow.harmonize = CsvParser.parseList(fields[headerMap.get("Harmonize")]);

			wineRow.abv = Double.parseDouble(fields[headerMap.get("ABV")]);

			wineRow.body = fields[headerMap.get("Body")];
			wineRow.acidity = fields[headerMap.get("Acidity")];
			wineRow.code = fields[headerMap.get("Code")];
			wineRow.country = fields[headerMap.get("Country")];

			wineRow.regionId = Integer.parseInt(fields[headerMap.get("RegionID")]);

			wineRow.regionName = fields[headerMap.get("RegionName")];

			wineRow.wineryId = Integer.parseInt(fields[headerMap.get("WineryID")]);

			wineRow.wineryName = fields[headerMap.get("WineryName")];
			wineRow.website = fields[headerMap.get("Website")];

			wineRow.vintages = CsvParser.parseVintage(fields[headerMap.get("Vintages")]);

			return wineRow;

		} catch (Exception e) {
			System.err.println("Error processing line: " + line);
			e.printStackTrace();
		}

		return null;
	}
}
