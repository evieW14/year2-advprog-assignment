package uk.ac.mmu.advprog.assessment.browser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;

import uk.ac.mmu.advprog.assessment.importer.ParsedWineRow;

public class ResultsPanel extends JPanel {
	private WineApp wineApp;
	private JTable tblResults;
	private WineTableModel model;

	public ResultsPanel(WineApp wineApp) {
		this.wineApp = wineApp;
		setLayout(new BorderLayout());

		model = new WineTableModel(wineApp.getWines());
		tblResults = new JTable(model);

		// Table Styles
		tblResults.setRowHeight(28);
		tblResults.setShowGrid(true);
		tblResults.setFont(new Font("Grandstander", Font.PLAIN, 14));
		tblResults.setGridColor(Color.LIGHT_GRAY);
		tblResults.setAutoCreateRowSorter(true);

		// Header
		JTableHeader header = tblResults.getTableHeader();
		header.setFont(new Font("Grandstander", Font.BOLD, 16));
		header.setBackground(new java.awt.Color(255, 136, 90));
		header.setForeground(java.awt.Color.WHITE);

		// Row Selection - load and show full wine details on click
		tblResults.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int viewRow = tblResults.getSelectedRow();
				if (viewRow >= 0) {
					int modelRow = tblResults.convertRowIndexToModel(viewRow);
					ParsedWineRow selectedWine = model.getWineAt(modelRow);
					WineApp.showDetail(selectedWine);
				}
			}

		});

		add(new JScrollPane(tblResults), BorderLayout.CENTER);
	}

	public void updateResults(List<ParsedWineRow> wines) {
		model.setWines(wines);
	}

}
