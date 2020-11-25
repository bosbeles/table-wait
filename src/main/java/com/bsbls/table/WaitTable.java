package com.bsbls.table;

import javax.swing.*;

public class WaitTable extends JTable {


    private final JLayer<? extends JComponent> layer;
    private final WaitLayerUI<JScrollPane> layerUI;

    public WaitTable() {
        layerUI = new WaitLayerUI<>();
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(this);
        this.setFillsViewportHeight(true);
        layer = new JLayer<JScrollPane>(scrollPane, layerUI);
    }

    public void start() {
        layerUI.start();
    }

    public void stop() {
        layerUI.stop();
    }

    public JLayer<? extends JComponent> getLayer() {
        return layer;
    }
}
