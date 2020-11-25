package com.bsbls.table;

import com.bsbls.home.gui.test.GuiTester;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        GuiTester.test(frame -> {

            WaitTable table = new WaitTable();
            JLayer<? extends JComponent> layer = table.getLayer();

            //headers for the table
            String[] columns = new String[] {
                    "Id", "Name", "Hourly Rate", "Part Time"
            };

            //actual data for the table in a 2d array
            Object[][] data = new Object[][] {
                    {1, "John", 40.0, false },
                    {2, "Rambo", 70.0, false },
                    {3, "Zorro", 60.0, true },
            };
            table.setModel(new DefaultTableModel(data, columns));

            JButton okButton = new JButton("OK");
            JButton cancelButton = new JButton("Cancel");

            okButton.addActionListener(e-> table.start());

            cancelButton.addActionListener(e -> table.stop());


            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());


            JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            southPanel.add(okButton);
            southPanel.add(cancelButton);




            panel.add(layer, BorderLayout.CENTER);
            panel.add(southPanel, BorderLayout.SOUTH);


            return panel;
        });


    }
}
