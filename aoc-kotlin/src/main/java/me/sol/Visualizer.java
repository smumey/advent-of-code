package me.sol;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Visualizer {
    public static void main(String[] args) {
        // Creating instance of frame with the label
        var frame = new Frame("Example 2");

        var textArea = new TextArea();
        textArea.setColumns(40);
        textArea.setRows(40);
        textArea.setText("Hello!!!!");
        textArea.setMinimumSize(new Dimension(80, 80));
        textArea.setVisible(true);

        // Adding button to the frame
        frame.add(textArea);

        // setting size, layout and visibility of frame
        frame.setSize(300, 300);
//        frame.setLayout(new (5, 5));
        frame.setVisible(true);

        // Using WindowListener for closing the window
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

}
