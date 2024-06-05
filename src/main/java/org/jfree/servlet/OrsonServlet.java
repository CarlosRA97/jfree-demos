package org.jfree.servlet;

import com.orsoncharts.Chart3D;
import com.orsoncharts.data.xyz.XYZDataset;
import com.orsoncharts.demo.OrsonChartsDemo;
import com.orsoncharts.demo.XYZBarChart3DDemo1;
import com.orsoncharts.graphics3d.ViewPoint3D;
import org.jfree.graphics2d.svg.SVGGraphics2D;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.PrintWriter;

public class OrsonServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setContentType("text/html");
    PrintWriter out = resp.getWriter();

    SVGGraphics2D g2 = new SVGGraphics2D(OrsonChartsDemo.DEFAULT_CONTENT_SIZE.width, OrsonChartsDemo.DEFAULT_CONTENT_SIZE.height);
    XYZDataset dataset = XYZBarChart3DDemo1.createDataset();
    final Chart3D chart = XYZBarChart3DDemo1.createChart(dataset);
    Rectangle2D chartArea = new Rectangle2D.Double(0, 0, g2.getWidth(), g2.getHeight());
    chart.draw(g2, chartArea);

    out.println(g2.getSVGElement());
  }
}
