package com.satilianius.views;


import com.satilianius.adapters.Adapter;
import com.satilianius.models.Note;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class ChartView {
    private Adapter adapter;
    private JFrame mainForm;
    private ChartPanel chartPanel;

    public ChartView(Adapter adapter) {
        setAdapter(adapter);
    }

    public void setAdapter(Adapter adapter) {
        if (adapter != null) {
            this.adapter = adapter;
        }
        else throw new NullPointerException("Adapter should not be null");
    }

    /**
     * Draws the main window of the program. Includes chart area and control panel to choose currencies.
     */
    public void initialiseWindow(Note note) {
        mainForm = new JFrame();
        fillFrame(mainForm, note);

        mainForm.setSize(1300,700);
        mainForm.setPreferredSize(new Dimension(1300,700));
        mainForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainForm.setTitle("NoteChart");
        mainForm.setResizable(true);
        mainForm.setLocationRelativeTo(null);
        SwingUtilities.invokeLater( () -> mainForm.setVisible(true));
    }

    /**
     * Creates a JPanel with wo comboboxes. Gets the list of values from the database.
     * @return JPanel with control elements.
     */
    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

//        controlPanel.add(new Label("From: "));
//        JComboBox<String> fromCurrenciesComboBox = new JComboBox<>(currencies);
//        fromCurrenciesComboBox.setSelectedItem(DEFAULT_FROM);
//        controlPanel.add(fromCurrenciesComboBox);
//
//        controlPanel.add(new Label("To: "));
//        JComboBox<String> toCurrenciesComboBox = new JComboBox<>(currencies);
//        toCurrenciesComboBox.setSelectedItem(DEFAULT_TO);
//        controlPanel.add(toCurrenciesComboBox);
//
//        //Changes the chart when user selects another pair of currencies.
//        ItemListener listener = e -> {
//            if (e.getStateChange() == ItemEvent.SELECTED) {
//                renderData(
//                        (String) fromCurrenciesComboBox.getSelectedItem(),
//                        (String) toCurrenciesComboBox.getSelectedItem());
//            }
//        };
//        fromCurrenciesComboBox.addItemListener(listener);
//        toCurrenciesComboBox.addItemListener(listener);

        return controlPanel;
    }

//    /**
//     * Redraws the chart.
//     * To avoid ambiguity, "1 currency from costs x currency to"
//     * @param from currency code of a currency which is being exchanged
//     * @param to currency code of a currency to which the exchange is happening.
//     */
//    private void renderData(String from, String to) {
//        if (from == null || to == null)
//            return;
//
//        // Restrict choosing the same currency in both comboBoxes.
//        if (from.equals(to)) {
//            JOptionPane.showMessageDialog(mainForm, "Pair " + from + " " + to + " is invalid");
//            return;
//        }
//        mainForm.getContentPane().remove(chartPanel);
//        JFreeChart chart = getjFreeChart(from, to);
//        // If there is no data for the chosen currency in the database
//        if (chart == null){
//            JOptionPane.showMessageDialog(mainForm, "No data for the pair " + from + " to " + to);
//            return;
//        }
//        chartPanel = new ChartPanel(chart);
//        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
//
//        mainForm.getContentPane().add(chartPanel, BorderLayout.CENTER);
//        mainForm.revalidate();
//    }

    /**
     * Adds layout and panels on the main window.
     * @param frame JFrame to which the panels should be added.
     */
    private void fillFrame(JFrame frame, Note note) {
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(createControlPanel(), BorderLayout.SOUTH);

        chartPanel = new ChartPanel(getjFreeChart(note));
        frame.getContentPane().add(chartPanel, BorderLayout.CENTER);
    }

    private JFreeChart getjFreeChart(Note note) {

        XYDataset dataSet =  adapter.getNoteDataSet(note);
        if (dataSet == null){
            return null;
        }
        JFreeChart mainChart = ChartFactory.createTimeSeriesChart(
                "Note history",
                "Date" ,
                "Rate" ,
                dataSet,
                true , true , false);
        final XYPlot plot = mainChart.getXYPlot( );
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd"));

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
        renderer.setSeriesPaint( 0 , Color.RED );
        renderer.setSeriesStroke( 0 , new BasicStroke( 1.0f ) );

        plot.setRenderer( renderer );
        return mainChart;
    }
}
