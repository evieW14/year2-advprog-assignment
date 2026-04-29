package uk.ac.mmu.advprog.assessment.browser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import uk.ac.mmu.advprog.assessment.importer.ParsedWineRow;

public class WineDetailPanel extends JPanel{

	private JLabel titleLabel;
	private JLabel typeLabel;
	private JLabel wineryLabel;
	private JLabel countryLabel;
	private JLabel regionLabel;
	private JLabel abvLabel;
	private JLabel bodyLabel;
	private JLabel acidityLabel;
	private JLabel blendLabel;
	private JLabel websiteLabel;
	private JTextArea grapesArea;
	private JTextArea pairingsArea;
	private JTextArea vintagesArea;

	public WineDetailPanel() {
		setLayout(new BorderLayout());
		setBackground(new Color(250, 245, 240));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Header
		JLabel header = new JLabel("Wine Details");
		header.setFont(new Font("Grandstander", Font.BOLD, 16));
		header.setForeground(new Color(255, 136, 90));
		header.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
		add(header, BorderLayout.NORTH);

		// Scrollable content area
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.setBackground(new Color(250, 245, 240));

		titleLabel = makeValueLabel("-", Font.BOLD, 15);
		typeLabel = makeValueLabel("-");
		wineryLabel = makeValueLabel("-");
		countryLabel = makeValueLabel("-");
		regionLabel = makeValueLabel("-");
		abvLabel = makeValueLabel("-");
		bodyLabel = makeValueLabel("-");
		acidityLabel = makeValueLabel("-");
		blendLabel = makeValueLabel("-");
		websiteLabel = makeValueLabel("-");
		grapesArea = makeTextArea();
		pairingsArea = makeTextArea();
		vintagesArea = makeTextArea();

		content.add(makeRow("Name", titleLabel));
		content.add(makeRow("Type", typeLabel));
		content.add(makeRow("Winery", wineryLabel));
		content.add(makeRow("Country", countryLabel));
		content.add(makeRow("Region", regionLabel));
		content.add(makeRow("ABV", abvLabel));
		content.add(makeRow("Body", bodyLabel));
		content.add(makeRow("Acidity", acidityLabel));
		content.add(makeRow("Blend Type", blendLabel));
		content.add(makeRow("Website", websiteLabel));
		content.add(makeTextRow("Grapes", grapesArea));
		content.add(makeTextRow("Food Pairings", pairingsArea));
		content.add(makeTextRow("Vintages", vintagesArea));

		add(new JScrollPane(content), BorderLayout.CENTER);
	}

	public void showWine(ParsedWineRow wine) {
		titleLabel.setText(wine.wineName);
		typeLabel.setText(wine.type);
		wineryLabel.setText(wine.wineryName);
		countryLabel.setText(wine.country);
		regionLabel.setText(wine.regionName);
		abvLabel.setText(String.valueOf(wine.abv) + "%");
		bodyLabel.setText(wine.body);
		acidityLabel.setText(wine.acidity);
		blendLabel.setText(wine.elaborate);
		websiteLabel.setText(wine.website);
		grapesArea.setText(formatList(wine.grapes));
		pairingsArea.setText(formatList(wine.harmonize));
		vintagesArea.setText(formatVintages(wine.vintages));
	}

	public void clear() {
		titleLabel.setText("-");
		typeLabel.setText("-");
		wineryLabel.setText("-");
		countryLabel.setText("-");
		regionLabel.setText("-");
		abvLabel.setText("-");
		bodyLabel.setText("-");
		acidityLabel.setText("-");
		blendLabel.setText("-");
		websiteLabel.setText("-");
		grapesArea.setText("");
		pairingsArea.setText("");
		vintagesArea.setText("");
	}

	// Helper methods to create UI components
	private JLabel makeValueLabel(String text) { return makeValueLabel(text, Font.PLAIN, 13); }

	private JLabel makeValueLabel(String text, int style, int size) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("Grandstander", style, size));
		return label;
	}

	private JTextArea makeTextArea() {
		JTextArea area = new JTextArea("-");
		area.setFont(new Font("Grandstander", Font.PLAIN, 13));
		area.setLineWrap(true);
		area.setWrapStyleWord(true);
		area.setEditable(false);
		area.setBackground(new Color(250, 245, 240));
		return area;
	}

	private JPanel makeRow(String label, JLabel value) {
		JPanel row = new JPanel(new BorderLayout(6, 0));
		row.setBackground(new Color(250, 245, 240));
		row.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
		JLabel key = new JLabel(label + ": ");
		key.setFont(new Font("Grandstander", Font.BOLD, 12));
		key.setForeground(new Color(120, 80, 60));
		row.add(key, BorderLayout.WEST);
		row.add(value, BorderLayout.CENTER);
		return row;
	}

	private JPanel makeTextRow(String label, JTextArea value) {
		JPanel row = new JPanel(new BorderLayout(6, 0));
		row.setBackground(new Color(250, 245, 240));
		row.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
		JLabel key = new JLabel(label + ": ");
		key.setFont(new Font("Grandstander", Font.BOLD, 12));
		key.setForeground(new Color(120, 80, 60));
		row.add(key, BorderLayout.WEST);
		row.add(value, BorderLayout.CENTER);
		return row;
	}

	private String formatList(List<String> items) {
		if (items == null || items.isEmpty()) {
			return "-";
		}
		return items.stream().collect(Collectors.joining(", "));
	}

	private String formatVintages(List<Integer> vintages) {
		if (vintages == null || vintages.isEmpty()) {
			return "-";
		}
		return vintages.stream()
				.map(y -> y == -1 ? "N.V." : String.valueOf(y))
				.collect(Collectors.joining(", "));
	}

}
