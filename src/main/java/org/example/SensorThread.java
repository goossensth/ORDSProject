package org.example;

import org.jfree.data.time.TimeSeriesCollection;

import static org.example.gui.acceleroList;
import static org.example.gui.gyroList;

public class SensorThread extends Thread {
    private int actualValue;

    private final boolean isX;
    private final boolean isY;
    private final boolean isZ;

    private final TimeSeriesCollection xGyro;
    private final TimeSeriesCollection yGyro;
    private final TimeSeriesCollection zGyro;
    private final TimeSeriesCollection xAccelerate;
    private final TimeSeriesCollection yAccelerate;
    private final TimeSeriesCollection zAccelerate;

    public SensorThread(int initialValue,
                        TimeSeriesCollection xGyro, TimeSeriesCollection yGyro, TimeSeriesCollection zGyro,
                        TimeSeriesCollection xAccelerate, TimeSeriesCollection yAccelerate, TimeSeriesCollection zAccelerate, boolean isX, boolean isY, boolean isZ) {
        this.actualValue = initialValue;

        this.xGyro = xGyro;
        this.yGyro = yGyro;
        this.zGyro = zGyro;
        this.xAccelerate = xAccelerate;
        this.yAccelerate = yAccelerate;
        this.zAccelerate = zAccelerate;
        this.isZ = isZ;
        this.isY = isY;
        this.isX = isX;
    }

    @Override
    public void run() {
        try {

            for (int i = 0; i < 60; i++) {
                actualValue += 1;
                xGyro.removeAllSeries();
                yGyro.removeAllSeries();
                zGyro.removeAllSeries();
                xAccelerate.removeAllSeries();
                yAccelerate.removeAllSeries();
                zAccelerate.removeAllSeries();
                if(isX){
                    xAccelerate.addSeries(acceleroList.get(0).createCopy(actualValue, actualValue + 9));
                    xGyro.addSeries(gyroList.get(0).createCopy(actualValue, actualValue + 9));
                }
                if(isY){
                    yAccelerate.addSeries(acceleroList.get(1).createCopy(actualValue, actualValue + 9));
                    yGyro.addSeries(gyroList.get(1).createCopy(actualValue, actualValue + 9));
                }
                if(isZ){
                    zAccelerate.addSeries(acceleroList.get(2).createCopy(actualValue, actualValue + 9));
                    zGyro.addSeries(gyroList.get(2).createCopy(actualValue, actualValue + 9));


                }
                Thread.sleep(500);
            }
        } catch (CloneNotSupportedException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
