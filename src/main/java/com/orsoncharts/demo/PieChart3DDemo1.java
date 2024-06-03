/* ===================
 * Orson Charts - Demo
 * ===================
 * 
 * Copyright (c) 2013-2017, Object Refinery Limited.
 * All rights reserved.
 *
 * http://www.object-refinery.com/orsoncharts/index.html
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *   - Neither the name of the Object Refinery Limited nor the
 *     names of its contributors may be used to endorse or promote products
 *     derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL OBJECT REFINERY LIMITED BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * Note that the above terms apply to the demo source only, and not the 
 * Orson Charts library.
 * 
 */

package com.orsoncharts.demo;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import com.orsoncharts.Chart3DPanel;
import com.orsoncharts.Chart3D;
import com.orsoncharts.Chart3DFactory;
import com.orsoncharts.TitleAnchor;
import com.orsoncharts.data.PieDataset3D;
import com.orsoncharts.data.StandardPieDataset3D;
import com.orsoncharts.graphics3d.RenderedElement;
import com.orsoncharts.graphics3d.swing.DisplayPanel3D;
import com.orsoncharts.interaction.Chart3DMouseEvent;
import com.orsoncharts.interaction.Chart3DMouseListener;
import com.orsoncharts.legend.LegendAnchor;
import com.orsoncharts.util.Orientation;

/**
 * A demo showing a simple pie chart in 3D.
 */
@SuppressWarnings("serial")
public class PieChart3DDemo1 extends Frame {

    Chart3D chart;

    /**
     * Creates a new test app.
     *
     * @param title  the frame title.
     */
    public PieChart3DDemo1(String title) {
        super(title);
        PieDataset3D<String> dataset = createDataset();
        chart = createChart(dataset);
        Canvas canvas = new Canvas() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                Rectangle2D chartArea = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
                chart.draw((Graphics2D) g, chartArea);
            }
        };

        // Add an event listener to close the window
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // Set preferred size of the canvas
        canvas.setPreferredSize(OrsonChartsDemo.DEFAULT_CONTENT_SIZE);
        add(canvas, BorderLayout.CENTER);
    }

    /**
     * Returns a panel containing the content for the demo.  This method is
     * used across all the individual demo applications to allow aggregation 
     * into a single "umbrella" demo (OrsonChartsDemo).
     * 
     * @return A panel containing the content for the demo.
     */
    public static JPanel createDemoPanel() {
        final DemoPanel content = new DemoPanel(new BorderLayout());
        content.setPreferredSize(OrsonChartsDemo.DEFAULT_CONTENT_SIZE);
        PieDataset3D<String> dataset = createDataset();
        Chart3D chart = createChart(dataset);
        Chart3DPanel chartPanel = new Chart3DPanel(chart);
        chartPanel.setMargin(0.05);
        chartPanel.addChartMouseListener(new Chart3DMouseListener() {

            @Override
            public void chartMouseClicked(Chart3DMouseEvent event) {
                RenderedElement element = event.getElement();
                if (element != null) {
                    JOptionPane.showMessageDialog(content, 
                            Chart3D.renderedElementToString(event.getElement()));
                }
            }

            @Override
            public void chartMouseMoved(Chart3DMouseEvent event) {
                
            }
        });
        content.setChartPanel(chartPanel);
        content.add(new DisplayPanel3D(chartPanel));
        chartPanel.zoomToFit(OrsonChartsDemo.DEFAULT_CONTENT_SIZE);
        return content;
    }
    
    /**
     * Creates a pie chart based on the supplied dataset.
     * 
     * @param dataset  the dataset.
     * 
     * @return A pie chart. 
     */
    private static Chart3D createChart(PieDataset3D<String> dataset) {
        Chart3D chart = Chart3DFactory.createPieChart(
                "New Zealand Exports 2012", 
                "http://www.stats.govt.nz/browse_for_stats/snapshots-of-nz/nz-in-profile-2013.aspx", 
                dataset);
        chart.setTitleAnchor(TitleAnchor.TOP_LEFT);
        chart.setLegendPosition(LegendAnchor.BOTTOM_CENTER,
                Orientation.HORIZONTAL);
        return chart;
    }
    
    /**
     * Creates a sample dataset (hard-coded for the purpose of keeping the
     * demo self-contained - in practice you would normally read your data
     * from a file, database or other source).
     * 
     * @return A sample dataset.
     */
    private static PieDataset3D<String> createDataset() {
        StandardPieDataset3D<String> dataset = new StandardPieDataset3D<String>();
        dataset.add("Milk Products", 11625);
        dataset.add("Meat", 5114);
        dataset.add("Wood/Logs", 3060);
        dataset.add("Crude Oil", 2023);
        dataset.add("Machinery", 1865);
        dataset.add("Fruit", 1587);
        dataset.add("Fish", 1367);
        dataset.add("Wine", 1177);
        dataset.add("Other", 18870);
        return dataset; 
    }

    /**
     * Starting point for the app.
     *
     * @param args  command line arguments (ignored).
     */
    public static void main(String[] args) {
        PieChart3DDemo1 app = new PieChart3DDemo1(
                "OrsonCharts: PieChart3DDemo1.java");
        app.pack();
        app.setVisible(true);
    }

}