package uk.ac.mmu.advprog.assessment.importer;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CsvImporter {
	Map<String, Integer> headerMap;

	public void run(Connection conn, DatabaseManager dbManager) throws SQLException {
		try (BufferedReader br = Files.newBufferedReader(Paths.get("data/XWines_Full_100K_wines.csv"))) {
			String headerLine = br.readLine();
			this.headerMap = buildHeaderMap(headerLine);

			// Start transaction
			conn.setAutoCommit(false);

			String line;
			int count = 0;
			long startTime = System.currentTimeMillis();
			while ((line = br.readLine()) != null) {
				ParsedWineRow row = processRow(line);
				if (row != null) {
					insertRow(row, dbManager);

				}

				count++;

				// Commit every 10000 lines to avoid long transactions and reduce memory usage
				if (count % 10000 == 0) {
					dbManager.executeBatches();
					conn.commit();
					long elapsedTime = System.currentTimeMillis() - startTime;
					System.out.println("Processed " + count + " lines in " + (elapsedTime / 1000) + " seconds");
				}
			}

			conn.commit(); // Final commit after processing all lines

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void runRatings(Connection conn, DatabaseManager dbManager) throws SQLException, IOException {
		try (BufferedReader br = Files.newBufferedReader(Paths.get("data/XWines_Full_21M_ratings.csv"))) {
			String headerLine = br.readLine();
			Map<String, Integer> headers = buildHeaderMap(headerLine);

			conn.setAutoCommit(false);

			String line;
			int count = 0;
			long startTime = System.currentTimeMillis();

			while ((line = br.readLine()) != null) {
				String[] fields = CsvParser.parseCsvLine(line);
				if (fields.length < 6)
					continue;

				try {
					int wineId = Integer.parseInt(fields[headers.get("WineId")]);
					String vintageStr = fields[headers.get("Vintage")].trim();
					int vintage = vintageStr.equalsIgnoreCase("N.V.") ? -1 : Integer.parseInt(vintageStr);
					double rating = Double.parseDouble(fields[headers.get("Rating")]);
					System.out.println("Parsing Rating row: " + wineId + vintage + rating);
				} catch (Exception e) {
					continue;
				}

				count++;
				if (count % 10000 == 0) {
					dbManager.executeRatings();
					conn.commit();
					long elapsed = System.currentTimeMillis() - startTime;
					long rowsPerSec = count / Math.max(elapsed / 1000, 1);
					System.out.printf("Ratings: %,d rows | %dm %02ds | ~%,d rows/sec%n", count, elapsed / 60000,
							(elapsed % 60000) / 1000, rowsPerSec);
				}
			}

			dbManager.executeRatings();
			conn.commit();

		} catch (Exception e) {
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
		// It will use the DatabaseManager to insert the necessary records into the
		// Wine, Region, and Winery tables

		// 1. REGION
		int regionId = dbManager.getOrInsertRegion(wineRow.regionId, wineRow.regionName, wineRow.country);

		// 2. WINERY
		dbManager.insertWinery(wineRow.wineryId, wineRow.wineryName, regionId, wineRow.website);

		// 3. WINE
		dbManager.insertWine(wineRow.wineId, wineRow.wineName, wineRow.type, wineRow.elaborate, wineRow.abv,
				wineRow.acidity, wineRow.body, wineRow.wineryId);

		// 4. VINTAGES
		for (Integer year : wineRow.vintages) {
			dbManager.insertWineVintage(wineRow.wineId, year);
		}

		// 5. GRAPES
		for (String grapeName : wineRow.grapes) {
			int grapeId = dbManager.getOrInsertGrape(grapeName);
			dbManager.insertWineGrape(wineRow.wineId, grapeId);
		}

		// 6. HARMONIZE (Pairings)
		for (String pairing : wineRow.harmonize) {
			int pairingId = dbManager.getOrInsertPairing(pairing);
			dbManager.insertWinePairing(wineRow.wineId, pairingId);
		}
	}

	public ParsedWineRow processRow(String line) {
		// This function will handle processing a single row from the CSV file
		// It will parse the fields, create the necessary objects, and insert them into
		// the database
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
