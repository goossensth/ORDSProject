package org.example;

import javax.swing.*;
import java.net.MalformedURLException;

public class Main {
    public static void main(String[] args) throws MalformedURLException, CloneNotSupportedException {
        SwingUtilities.invokeLater(() -> {
            try {
                new gui("Car data visualizer");
            } catch (MalformedURLException | CloneNotSupportedException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}