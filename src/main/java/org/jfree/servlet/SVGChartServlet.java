package org.jfree.servlet;

import org.jfree.chart.JFreeChart;
import org.jfree.graphics2d.svg.SVGGraphics2D;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;

import org.jfree.graphics2d.svg.demo.SVGBarChartDemo1;
import org.jfree.graphics2d.svg.demo.SVGChartWithAnnotationsDemo1;
import org.jfree.graphics2d.svg.demo.SVGPieChartDemo1;
import org.jfree.graphics2d.svg.demo.SVGTimeSeriesChartDemo1;

public class SVGChartServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setContentType("text/html");
    PrintWriter out = resp.getWriter();

    JFreeChart chart;
    SVGGraphics2D g2 = new SVGGraphics2D(600, 400);
    Rectangle r = new Rectangle(0, 0, 600, 400);

    String chartType = req.getParameter("chart");
    if (chartType == null) {
      chart = SVGBarChartDemo1.createChart(SVGBarChartDemo1.createDataset());
    } else {
      if (chartType.equals("bar")) {
        chart = SVGBarChartDemo1.createChart(SVGBarChartDemo1.createDataset());
      } else if (chartType.equals("pie")) {
        chart = SVGPieChartDemo1.createChart(SVGPieChartDemo1.createDataset());
        g2.setRenderingHint(JFreeChart.KEY_SUPPRESS_SHADOW_GENERATION, true);
      } else if (chartType.equals("time")) {
        chart = SVGTimeSeriesChartDemo1.createChart(SVGTimeSeriesChartDemo1.createDataset());
        g2.setRenderingHint(JFreeChart.KEY_SUPPRESS_SHADOW_GENERATION, true);
      } else if (chartType.equals("annotations")) {
        chart = SVGChartWithAnnotationsDemo1.createChart(SVGTimeSeriesChartDemo1.createDataset());
      } else {
        chart = SVGBarChartDemo1.createChart(SVGBarChartDemo1.createDataset());
      }
    }

    chart.draw(g2, r);

    out.println("<html><body>");

    out.println("<div style='display: flex; flex-direction: column; align-items: center;'>");
    out.println(g2.getSVGElement());
    out.println("</div>");

    out.println("<div style='display: flex; flex-direction: column; align-items: center;'>");
    out.println("<a href=\"/jfree-demos/svgchart?chart=bar\"> Bar Chart </a>");
    out.println("<a href=\"/jfree-demos/svgchart?chart=pie\"> Pie Chart </a>");
    out.println("<a href=\"/jfree-demos/svgchart?chart=annotations\"> Chart With Annotations </a>");
    out.println("<a href=\"/jfree-demos/svgchart?chart=time\"> Time Series Chart </a>");
    out.println("</div>");

    out.println("</body></html>");
  }
}
