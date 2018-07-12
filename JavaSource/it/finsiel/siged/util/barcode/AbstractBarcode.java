package it.finsiel.siged.util.barcode;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;

import org.krysalis.barcode4j.BarcodeDimension;
import org.krysalis.barcode4j.BarcodeGenerator;
import org.krysalis.barcode4j.output.java2d.Java2DCanvasProvider;

public abstract class AbstractBarcode extends JComponent {

    private BarcodeGenerator bargen;

    private String msg;

    private Java2DCanvasProvider canvas;

    private BarcodeDimension bardim;

    private double leftMargin;

    private double topMargin;

    protected void updateBarcodeDimension() {
        if ((getBarcodeGenerator() != null) && (getMessage() != null)) {
            try {
                this.bardim = getBarcodeGenerator()
                        .calcDimensions(getMessage());
            } catch (IllegalArgumentException iae) {
                this.bardim = null;
            }
        } else {
            this.bardim = null;
        }
       
    }

    public void setBarcodeGenerator(BarcodeGenerator bargen) {
        this.bargen = bargen;
        updateBarcodeDimension();
        repaint();
    }

    public BarcodeGenerator getBarcodeGenerator() {
        return this.bargen;
    }

    public void setMessage(String msg) {
        if (!msg.equals(this.msg)) {
            this.msg = msg;
            updateBarcodeDimension();
            repaint();
        }
    }

    public String getMessage() {
        return this.msg;
    }

    public BarcodeDimension getBarcodeDimension() {
        return this.bardim;
    }

    protected abstract void transformAsNecessary(Graphics2D g2d);

    public void paint(Graphics g, double leftMargin, double topMargin) {
        this.leftMargin = leftMargin;
        this.topMargin = topMargin;
        paint(g);
    }

    /**
     * @see java.awt.Component#paint(java.awt.Graphics)
     */
    public void paint(Graphics g) {
     
        if (bargen == null || msg == null) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g;
        if (canvas == null) {
            canvas = new Java2DCanvasProvider(g2d);
        } else {
            canvas.setGraphics2D(g2d);
        }

        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        try {
            // AffineTransform baktrans = g2d.getTransform();
            try {
                g2d.setColor(Color.black);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setFont(new Font("Arial", 0, 7));

                g2d.drawString("ASL 3 - Regione Umbria",
                        (float) (leftMargin + 2), (float) (topMargin + 14));
                g2d.drawString("Prot. Entrata del 01/02/2005",
                        (float) (leftMargin + 2), (float) (topMargin + 24));
                g2d.drawString("N. 0000010", (float) (leftMargin + 2),
                        (float) (topMargin + 34));
                // now paint the barcode
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_OFF);
                g2d.translate(leftMargin, topMargin + 44);
                // g2d.setFont(new Font("EAN13",0,12));
                // g2d.drawString("123456789012",(float)(leftMargin+2),(float)(topMargin+20));
                System.out
                        .println("Margini: " + leftMargin + " - " + topMargin);
                getBarcodeGenerator().generateBarcode(canvas, getMessage());

            } finally {
                // g2d.setTransform(baktrans);
            }
        } catch (Exception e) {
            g.setColor(Color.red);
            g.drawLine(0, 0, getWidth(), getHeight());
            g.drawLine(0, getHeight(), getWidth(), 0);
            e.printStackTrace();
        }
    }
}
