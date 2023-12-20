package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class GetDataFromORDS {

    public static void ParseToTimeSeries(int upper, List<TimeSeries> gyroList, List<TimeSeries> valuesList) {
        ObjectMapper mapper = new ObjectMapper();

        ArrayNode arrayNode;

        String url = "http://10.20.0.47:8080/ords/testavance/motionAnalyse/model/" + (upper-60) + "/" +upper+"?limit=120";
        try {
            arrayNode = (ArrayNode) mapper.readTree(new URL(url)).get("items");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        TimeSeries seriesX = new TimeSeries("Données axe X");
        TimeSeries seriesY = new TimeSeries("Données axe Y");
        TimeSeries seriesZ = new TimeSeries("Données axe Z");
        TimeSeries gyroX = new TimeSeries("Données gyro X");
        TimeSeries gyroY = new TimeSeries("Données gyro Y");
        TimeSeries gyroZ = new TimeSeries("Données gyro Z");
        Second current = new Second();
        for (JsonNode jsonNode : arrayNode) {

            seriesX.addOrUpdate(current, jsonNode.get("accx").numberValue());
            seriesY.addOrUpdate(current, jsonNode.get("accy").numberValue());
            seriesZ.addOrUpdate(current, jsonNode.get("accz").numberValue());
            gyroX.addOrUpdate(current, jsonNode.get("gyrox").numberValue());
            gyroY.addOrUpdate(current, jsonNode.get("gyroy").numberValue());
            gyroZ.addOrUpdate(current, jsonNode.get("gyroz").numberValue());
            current = (Second) current.next();
            //System.out.println("accx " + jsonNode.get("accx") +"    accy    "+ jsonNode.get("accy") +"  accz    "+ jsonNode.get("accz") +"  gyroX   "+ jsonNode.get("gyrox")+"  gyroZ   "+jsonNode.get("gyroy")+"   gyroZ   "+jsonNode.get("gyroz"));
        }
        valuesList.add(seriesX);
        valuesList.add(seriesY);
        valuesList.add(seriesZ);

        gyroList.add(gyroX);
        gyroList.add(gyroY);
        gyroList.add(gyroZ);
        }
    }
