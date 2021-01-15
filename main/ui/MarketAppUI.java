package ui;

import model.Market;

import javax.swing.*;
import java.awt.*;

//Builds a Graphical User Interface for the Market App
public class MarketAppUI {

    /*
     * MODIFIES: this
     * EFFECTS: Constructs the frame, sets its default properties, finally sets a default panel of it.
     */

    public MarketAppUI() {

        JFrame frame = new JFrame("Online Market");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 700);

        JPanel panel = new DrawMarketPanel();

        frame.setContentPane(panel);
        frame.setVisible(true);
        frame.setResizable(false);
    }

}

