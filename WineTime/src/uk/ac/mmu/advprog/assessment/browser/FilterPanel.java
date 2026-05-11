package uk.ac.mmu.advprog.assessment.browser;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FilterPanel extends JPanel {

	// All ComboBox options are hardcoded but were retrieved from the dataset
	private JTextField nameField;
	private JTextField wineryField;
	private JComboBox<String> typeBox;
	private JTextField countryField;
	private JComboBox<String> blendBox;
	private JComboBox<String> bodyBox;
	private JComboBox<String> acidityBox;
	private JTextField grapeField;
	private JTextField abvField;
	private JComboBox<String> abvOperatorBox;

	private JButton searchButton;

	public FilterPanel(WineApp wineApp) {

		setLayout(new BorderLayout());
		add(new JLabel("Search Filters"), BorderLayout.NORTH);

		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(container, BorderLayout.CENTER);

		// Row 1
		JPanel row1 = new JPanel(new GridLayout(1, 4, 10, 10));
		row1.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

		nameField = new JTextField();
		wineryField = new JTextField();
		typeBox = new JComboBox<>(
				new String[] { "Any", "Dessert", "Dessert/Port", "Red", "Rosé", "Sparkling", "White" });

		row1.add(fieldBlock("Name", nameField));
		row1.add(fieldBlock("Winery", wineryField));
		row1.add(fieldBlock("Type", typeBox));

		container.add(row1);

		// Row 2
		JPanel row2 = new JPanel(new GridLayout(1, 4, 10, 10));
		row2.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

		countryField = new JTextField();
		blendBox = new JComboBox<>(
				new String[] { "Any", "Assemblage/Blend", "Assemblage/Bordeaux Red Blend", "Assemblage/Port Blend",
						"Assemblage/Portuguese Red Blend", "Assemblage/Portuguese White Blend", "Varietal/100%" });
		bodyBox = new JComboBox<>(new String[] { "Any", "Full-bodied", "Light-bodied", "Medium-bodied",
				"Very full-bodied", "Very light-bodied" });

		row2.add(fieldBlock("Country", countryField));
		row2.add(fieldBlock("Blend", blendBox));
		row2.add(fieldBlock("Body", bodyBox));
		container.add(row2);

		// Row 3
		JPanel row3 = new JPanel(new GridLayout(1, 4, 10, 10));
		row3.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

		acidityBox = new JComboBox<>(new String[] { "Any", "Low", "Medium", "High" });
		grapeField = new JTextField();

		abvOperatorBox = new JComboBox<>(new String[] { ">", "<" });
		abvField = new JTextField();
		JPanel abvBlock = new JPanel(new BorderLayout(4, 0));
		abvBlock.add(new JLabel("ABV"), BorderLayout.NORTH);
		JPanel abvInputs = new JPanel(new GridLayout(1, 2, 4, 0));
		abvInputs.add(abvOperatorBox);
		abvInputs.add(abvField);
		abvBlock.add(abvInputs, BorderLayout.CENTER);

		row3.add(fieldBlock("Acidity", acidityBox));
		row3.add(fieldBlock("Grape", grapeField));
		row3.add(abvBlock);
		container.add(row3);

		// Search with filters
		searchButton = new JButton("Search");
		searchButton.addActionListener(e -> {
			FilterCriteria criteria = buildCriteria();
			try {
				WineApp.applyFilters(criteria);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		});

		add(searchButton, BorderLayout.SOUTH);
	}

	private JPanel fieldBlock(String label, JComponent input) {
		JPanel p = new JPanel(new BorderLayout());
		p.add(new JLabel(label), BorderLayout.NORTH);
		p.add(input, BorderLayout.CENTER);
		return p;
	}

	private FilterCriteria buildCriteria() {
		FilterCriteria criteria = new FilterCriteria();
		criteria.name = nameField.getText().trim();
		criteria.winery = wineryField.getText().trim();
		criteria.type = (String) typeBox.getSelectedItem();
		criteria.country = countryField.getText().trim();
		criteria.blend = (String) blendBox.getSelectedItem();
		criteria.body = (String) bodyBox.getSelectedItem();
		criteria.acidity = (String) acidityBox.getSelectedItem();
		criteria.grape = grapeField.getText().trim();

		String abvText = abvField.getText().trim();
		if (!abvText.isEmpty()) {
			try {
				criteria.abv = Double.parseDouble(abvText);
				criteria.abvOperator = (String) abvOperatorBox.getSelectedItem();
			} catch (NumberFormatException e) {
				System.out.println("Invalid ABV value: " + abvText);
			}
		}

		return criteria;
	}
}
