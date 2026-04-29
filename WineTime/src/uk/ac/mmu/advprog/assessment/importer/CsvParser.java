package uk.ac.mmu.advprog.assessment.importer;

import java.util.ArrayList;
import java.util.List;

public class CsvParser {

	public static String[] parseCsvLine(String line) {
		// This function will handle parsing a line from the CSV file
		// It will handle fields in double quotes, escaped quotes, and plain unquoted fields
		List<String> fields = new ArrayList<>();
		StringBuilder current = new StringBuilder();

		boolean inQuotes = false;
		int i = 0;

		while (i < line.length()) {
			char currentChar = line.charAt(i);

			if (currentChar == '"') {
				if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
					current.append('"');
					i += 2; // Skip the escaped quote
					continue;
				}
				inQuotes = !inQuotes; // Toggle the inQuotes flag
				i++;
				continue;
			}

			if(currentChar == ',' && !inQuotes) {
				fields.add(current.toString());
				current.setLength(0); // Clear the current field
				i++;
				continue;
			}

			current.append(currentChar);
			i++;
		}

		fields.add(current.toString()); // Add the last field
		return fields.toArray(new String[0]);
	}

	public static List<String> parseList(String rawList) {
		// This function will handle parsing a list of items from a CSV field
		// It will split on commas, but also handle quoted items that may contain commas
		// This is for the Grape and Harmonize (Blend) columns
		List<String> result = new ArrayList<>();

		if (rawList == null) {
			return result;
		}

		rawList = rawList.trim();

		// Check if it is a list
		if (!rawList.startsWith("[") || !rawList.endsWith("]")) {
			result.add(rawList.trim().replace("'", ""));
			return result;
		}

		String inner = rawList.substring(1, rawList.length() - 1).trim();

		StringBuilder current = new StringBuilder();
		boolean inQuotes = false;

		for (int i=0; i < inner.length(); i++) {
			char currentChar = inner.charAt(i);

			if (currentChar == '\'') {
				inQuotes = !inQuotes; // Toggle the inQuotes flag
				continue;
			}

			if (currentChar == ',' && !inQuotes) {
				result.add(current.toString().trim());
				current.setLength(0); // Clear the current item
				continue;
			}

			current.append(currentChar);
		}

		if (current.length() > 0) {
			result.add(current.toString().trim());
		}

		return result;
	}

	public static List<Integer> parseVintage(String rawVintageList) {
		// This function will handle parsing the vintage field, which may be a year or "N.V."
		// If it's "N.V.", we will return -1 to indicate no vintage

		List<String> vintageStrings = parseList(rawVintageList);
		List<Integer> vintages = new ArrayList<>();

		for (String t : vintageStrings) {
			t = t.replace(",","").trim(); // Remove any commas and trim whitespace

			try {
				vintages.add(Integer.parseInt(t));
			} catch (NumberFormatException ignored) {}

			if (t.equalsIgnoreCase("N.V.")) {
				// Use -1 to represent N.V.
				vintages.add(-1);
			}
		}

		return vintages;
	}
}
