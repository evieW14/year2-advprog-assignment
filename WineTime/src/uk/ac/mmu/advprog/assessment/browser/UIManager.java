package uk.ac.mmu.advprog.assessment.browser;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class UIManager extends JPanel {
    private TopBarPanel topBar;
    private FilterPanel filterPanel;
    private ResultsPanel resultsPanel;

    public UIManager(WineApp wineApp) {
        setLayout(new BorderLayout());

        topBar = new TopBarPanel();
        filterPanel = new FilterPanel(wineApp);
        resultsPanel = new ResultsPanel(wineApp);

        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));

        topContainer.add(topBar);
        topContainer.add(filterPanel);

        add(topContainer, BorderLayout.NORTH);
        add(resultsPanel, BorderLayout.CENTER);
    }

    public TopBarPanel getTopBar() {
        return topBar;
    }
    
    public FilterPanel getFilterPanel() {
		return filterPanel;
	}
    
    public ResultsPanel getResultsPanel() {
		return resultsPanel;
	}
}
