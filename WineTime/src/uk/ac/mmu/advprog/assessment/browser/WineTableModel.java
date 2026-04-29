package uk.ac.mmu.advprog.assessment.browser;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import uk.ac.mmu.advprog.assessment.importer.ParsedWineRow;

public class WineTableModel extends AbstractTableModel{

	private final String[] columnNames = {"Name", "Type", "Winery", "Country", "ABV"};
	private List<ParsedWineRow> wines;

	public WineTableModel(List<ParsedWineRow> wines) {
		this.wines = wines;
	}

	@Override
	public Object getValueAt(int row, int col) {
		ParsedWineRow wine = wines.get(row);
		return switch (col) {
			case 0 -> wine.wineName;
			case 1 -> wine.type;
			case 2 -> wine.wineryName;
			case 3 -> wine.country;
			case 4 -> wine.abv;
			default -> null;
		};
	}

	public ParsedWineRow getWineAt(int row) {
		return wines.get(row);
	}

	@Override
	public int getRowCount() {
		return wines == null ? 0 : wines.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	public void setWines(List<ParsedWineRow> wines) {
	    this.wines = wines;
	    fireTableDataChanged();
	}

}
