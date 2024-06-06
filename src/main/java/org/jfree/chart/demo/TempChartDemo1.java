/* =========================
 * TimeSeriesChartDemo1.java
 * =========================
 *
 * (C) Copyright 2003-2017, by Object Refinery Limited.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
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
 */

package org.jfree.chart.demo;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Random;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.ui.UIUtils;
import org.jfree.data.general.SeriesChangeEvent;
import org.jfree.data.general.SeriesChangeListener;
import org.jfree.data.time.*;
import org.jfree.data.xy.XYDataset;

/**
 * An example of a time series chart create using JFreeChart.  For the most
 * part, default settings are used, except that the renderer is modified to
 * show filled shapes (as well as lines) at each data point.
 */
public class TempChartDemo1 extends Frame {

  private static final long serialVersionUID = 1L;

  static class TimeSeriesChartCanvas extends Canvas {
    private final JFreeChart chart;
    private ChartRenderingInfo info;

    TimeSeriesChartCanvas(TimeSeries timeSeries) {
      TimeSeriesCollection dataset = new TimeSeriesCollection(timeSeries);
      timeSeries.addChangeListener(new SeriesChangeListener() {
        @Override
        public void seriesChanged(SeriesChangeEvent seriesChangeEvent) {
          repaint();
        }
      });
      chart = ChartFactory.createTimeSeriesChart("Climate Control", "Fecha", "Temperatura Cº", dataset, true, true, false);

      info = new ChartRenderingInfo();

      XYPlot plot = (XYPlot) chart.getPlot();
      plot.setBackgroundPaint(Color.white);
      plot.setDomainGridlinePaint(Color.gray);
      plot.setRangeGridlinePaint(Color.gray);
      DateAxis axis = (DateAxis) plot.getDomainAxis();
      axis.setAutoRange(true);
      axis.setLowerMargin(0.0);
      axis.setUpperMargin(0.0);
    }

    @Override
    public void paint(Graphics g) {
      Graphics2D g2d = (Graphics2D) g;
      Dimension size = getSize();
      Rectangle2D chartArea = new Rectangle2D.Double(0, 0, size.width, size.height);
      chart.draw(g2d, chartArea, info);
    }

    public void updateChart() {
      repaint();
    }
  }

  static class RandomTemperatureGenerator {
    int countMinute = 0;
    int countHour = 0;
    int countDay = 0;
    int countMonth = 0;
    int countYear = 2001;
    TimeSeries timeSeries;
    Thread thread;

    RandomTemperatureGenerator(TimeSeries timeSeries) {
      this.timeSeries = timeSeries;
    }

    public void generate() {
      Double temperature = ((new Random().nextDouble() + 0.1) * 10) + 20;
      timeSeries.add(new Minute(countMinute % 60, countHour % 24, (countDay % 31) + 1, (countMonth % 12) + 1, countYear), temperature);
      countMinute += 60;
      if (countMinute % 60 == 0) {
        countHour++;
        if (countHour % 24 == 0) {
          countDay++;
          if (countDay % 31 == 0) {
            countMonth++;
            if (countMonth % 12 == 0) {
              countYear++;
            }
          }
        }
      }
    }

    enum Live {
      START,
      STOP,
    }

    public void stream(Live state) {
      if (thread != null && state == Live.STOP) {
        thread.interrupt();
        thread = null;
      }

      if (thread == null && state == Live.START) {
        thread = new Thread(new Runnable() {
          boolean isRunning = true;

          @Override
          public void run() {
            while (!Thread.currentThread().isInterrupted() && isRunning) {
              generate();
              try {
                Thread.sleep(500);
              } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                isRunning = false;
                System.out.println("Thread Id (" + Thread.currentThread().getId() + ") : Interrupted");
              }
            }
          }
        });
        thread.start();
      }
    }

  }

  /**
   * A demonstration application showing how to create a simple time series
   * chart.  This example uses Hour data.
   *
   * @param title  the frame title.
   */
  public TempChartDemo1(String title) {
    super(title);
    final TimeSeries s1 = new TimeSeries("Temperatura");
    // Create a Canvas to draw the chart on
    TimeSeriesChartCanvas canvas = new TimeSeriesChartCanvas(s1);

    // Set preferred size of the canvas
    canvas.setPreferredSize(new Dimension(500, 270));
    add(canvas, BorderLayout.CENTER);
    final RandomTemperatureGenerator randomData = new RandomTemperatureGenerator(s1);
    Button addRandomData = new Button("Más datos");
    addRandomData.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        randomData.generate();
      }
    });

    Checkbox addContinousRandomData = new Checkbox("Simulate Live Data");
    addContinousRandomData.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent event) {
        randomData.stream(event.getStateChange() == ItemEvent.SELECTED ? RandomTemperatureGenerator.Live.START : RandomTemperatureGenerator.Live.STOP);
      }
    });

    add(addContinousRandomData, BorderLayout.SOUTH);


    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
  }

  /**
   * Starting point for the demonstration application.
   *
   * @param args  ignored.
   */
  public static void main(String[] args) {

    TempChartDemo1 demo = new TempChartDemo1(
        "Temperature Chart Demo 1");
    demo.pack();
    UIUtils.centerFrameOnScreen(demo);
    demo.setVisible(true);

  }

}
