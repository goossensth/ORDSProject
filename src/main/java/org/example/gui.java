package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


public class gui extends JFrame{
    public static final List<TimeSeries> acceleroList = new ArrayList<>();
    public static final List<TimeSeries> gyroList = new ArrayList<>();
    private final JLabel labelTimestamp = new JLabel("Timestamp :");
    private final JTextField timeValue = new JTextField("                  ");
    private final JButton goButton = new JButton("Démarer");
    private final JButton stopButton = new JButton("Stop");
    private final JCheckBox xBox = new JCheckBox("Afficher x", true);

    private final JCheckBox yBox = new JCheckBox("Afficher y", true);

    private final JCheckBox zBox = new JCheckBox("Afficher z", true);

    private final TimeSeriesCollection xGyro;
    private final TimeSeriesCollection yGyro;
    private final TimeSeriesCollection zGyro;
    private final TimeSeriesCollection xAccelero;
    private final TimeSeriesCollection yAccelero;
    private final TimeSeriesCollection zAccelero ;

    private final int actualValue;
    private SensorThread updater;

    public gui(String title) throws MalformedURLException, CloneNotSupportedException, InterruptedException {
        super(title);

        goButton.addActionListener(new goActionListener());
        stopButton.addActionListener(new stopActionListener());

        actualValue = 0;
        GetDataFromORDS.ParseToTimeSeries(3583286, gyroList, acceleroList);

        xAccelero = new TimeSeriesCollection(acceleroList.get(0).createCopy(actualValue,actualValue + 9));
        yAccelero = new TimeSeriesCollection(acceleroList.get(1).createCopy(actualValue,actualValue + 9));
        zAccelero = new TimeSeriesCollection(acceleroList.get(2).createCopy(actualValue,actualValue +9));

        xGyro = new TimeSeriesCollection(gyroList.get(0).createCopy(actualValue,actualValue + 9));
        yGyro = new TimeSeriesCollection(gyroList.get(1).createCopy(actualValue,actualValue + 9));
        zGyro = new TimeSeriesCollection(gyroList.get(2).createCopy(actualValue,actualValue +9));


        JPanel myPanel = new JPanel();
        ChartPanel acceleroChart = new ChartPanel(CreateChart(true));
        acceleroChart.setPreferredSize(new Dimension(800, 370));
        ChartPanel gyroChart = new ChartPanel(CreateChart(false));
        gyroChart.setPreferredSize(new Dimension(800, 370));
        myPanel.add(acceleroChart);
        myPanel.add(gyroChart);
        myPanel.add(goButton);
        myPanel.add(stopButton);
        myPanel.add(xBox);
        myPanel.add(yBox);
        myPanel.add(zBox);
        myPanel.add(labelTimestamp);
        myPanel.add(timeValue);

        setContentPane(myPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 900);

        setVisible(true);


    }

    private JFreeChart CreateChart(boolean acc) {

        JFreeChart chart;
        if (acc){
            chart = ChartFactory.createTimeSeriesChart(
                    "Graphique accéléromètre",
                    "Timestamp",
                    "Valeur",
                    xAccelero,
                    false,
                    true,
                    false
            );

            // Ajouter la deuxième série temporelle au graphique
            chart.getXYPlot().setDataset(1, yAccelero);
            chart.getXYPlot().setDataset(2, zAccelero);
        }
        else {
            chart = ChartFactory.createTimeSeriesChart(
                    "Graphique gyroscope",
                    "Timestamp",
                    "Valeur",
                    xGyro,
                    false,
                    true,
                    false
            );

            // Ajoutez la deuxième série temporelle au graphique
            chart.getXYPlot().setDataset(1, yGyro);
            chart.getXYPlot().setDataset(2, zGyro);
        }


        // Personnalisez l'axe des dates
        XYPlot plot = chart.getXYPlot();

        // Set the visible range to show only the first 10 data points
        XYLineAndShapeRenderer renderer0 = new XYLineAndShapeRenderer();
        XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer();
        XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer();
        plot.setRenderer(0, renderer0);
        plot.setRenderer(1, renderer1);
        plot.setRenderer(2, renderer2);
        plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(0, Color.red);
        plot.getRendererForDataset(plot.getDataset(1)).setSeriesPaint(0, Color.blue);
        plot.getRendererForDataset(plot.getDataset(2)).setSeriesPaint(0, Color.green);

        return chart;
    }
    class goActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
                updater = new SensorThread(actualValue, xGyro, yGyro, zGyro, xAccelero, yAccelero, zAccelero, xBox.isSelected(), yBox.isSelected(), zBox.isSelected());
                updater.start();
        }
    }
    class stopActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            updater.interrupt();

        }
    }
}

