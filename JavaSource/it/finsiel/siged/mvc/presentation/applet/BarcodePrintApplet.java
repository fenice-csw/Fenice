
package it.finsiel.siged.mvc.presentation.applet;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.RepaintManager;


public class BarcodePrintApplet extends Applet implements Printable,
        ActionListener {

    private static GridBagConstraints cons = new GridBagConstraints();

    private JTextField msgField;

    private String testoRiga1;

    private String testoRiga3;

    private String testoRiga4;

    private String testoRiga5;
    
    private double deltaXMM = 0;

    private double deltaYMM = 0;

    private double leftMarginMM = 0;

    private double topMarginMM = 0;

    private double paperWidthMM = 50;

    private double paperHeightMM = 30;

    private String stampaSuFoglioA4;

    private double rotazione = 0;

    private JButton print;


    private JComboBox printers;

    private PrintService[] printerList;

    public void print() {
        try {
            if (printers.getItemCount() < 1) {
                JOptionPane.showMessageDialog(this,
                        "Nessuna Stampante istallata!", "",
                        JOptionPane.WARNING_MESSAGE);
                return;
            } else if (printers.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this,
                        "Selezionare la stampante.", "",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            PrintService printer = getPrinterService((String) printers
                    .getSelectedItem());
            if (printer == null) {
                JOptionPane.showMessageDialog(this,
                        "Impossibile trovare la stampante.", "",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            PrinterJob j = PrinterJob.getPrinterJob();
            j.setPrintService(printer);
            PageFormat pf = new PageFormat();
            Paper paper = pf.getPaper();
            if ("S".equals(stampaSuFoglioA4)) {
               
                paper.setSize(8.264 * 72, 11.694 * 72);
                paper.setImageableArea(leftMarginMM + deltaXMM, topMarginMM
                        + deltaYMM, (((paperWidthMM) / 10) / 2.54) * 72,
                        (((paperHeightMM) / 10) / 2.54) * 72);

            } else {
                paper.setSize(((paperWidthMM + leftMarginMM) / 10) / 2.54 * 72,
                        ((paperHeightMM + topMarginMM) / 10) / 2.54 * 72);
                paper.setImageableArea((leftMarginMM / 10) / 2.54 * 72,
                        (topMarginMM / 10) / 2.54 * 72,
                        (((paperWidthMM) / 10) / 2.54) * 72,
                        (((paperHeightMM) / 10) / 2.54) * 72);

            }
            pf.setPaper(paper);
            j.setPrintable(this, pf);
            j.print();

        } catch (Exception ex) {
        	ex.printStackTrace();
            ex.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
            
        }
    }

    public int print(Graphics g, PageFormat pf, int pageIndex) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform baktrans = g2d.getTransform();
        if (pageIndex > 0) {
            return (NO_SUCH_PAGE);
        } else {
            try {
                disableDoubleBuffering(this);
                g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                        RenderingHints.VALUE_FRACTIONALMETRICS_ON);

                g2d.setColor(Color.black);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                if (rotazione != 0)
                    g2d.rotate(Math.toRadians(rotazione), paperWidthMM,
                            paperHeightMM);

                g2d.setFont(new Font("Arial", Font.PLAIN, 8));
                g2d.drawString(testoRiga1 == null ? "" : testoRiga1, (float) pf
                        .getImageableX(), (float) pf.getImageableY() + 12f);

                g2d.setFont(new Font("Arial", Font.PLAIN, 8));
                g2d.drawString("Prot. ", (float) pf.getImageableX(), (float) pf
                        .getImageableY() + 24f);
                
                g2d.setFont(new Font("Arial", Font.BOLD, 8));
                g2d.drawString(testoRiga3 == null ? "" : testoRiga3,
                                (float) pf.getImageableX() + 20, (float) pf
                                        .getImageableY() + 24f);

                g2d.setFont(new Font("Arial", Font.PLAIN, 8));
                g2d.drawString("Numero:", (float) pf.getImageableX(),
                        (float) pf.getImageableY() + 36f);
                
                g2d.setFont(new Font("Arial", Font.BOLD, 12));
                g2d.drawString(testoRiga4 == null ? "" : testoRiga4,
                                (float) pf.getImageableX() + 35, (float) pf
                                        .getImageableY() + 36f);
                
                g2d.setFont(new Font("Arial", Font.PLAIN, 8));
                g2d.drawString("Classifica: ", (float) pf.getImageableX(), (float) pf.getImageableY() + 48f);
                g2d.setFont(new Font("Arial", Font.BOLD, 8));
                g2d.drawString(testoRiga5 == null ? "" : testoRiga5, (float) pf.getImageableX() + 40, (float) pf.getImageableY() + 48f);
                g2d.rotate(Math.toRadians(90), 0, 0);
            } catch (Exception e) {
                g.setColor(Color.red);
                g.drawLine((int) pf.getImageableX(), (int) pf.getImageableY(),
                        1, getHeight());
                g.drawLine(0, (int) pf.getImageableHeight(), (int) pf
                        .getImageableWidth(), 0);
                e.printStackTrace();
            } finally {

                g2d.setTransform(baktrans);
            }
            enableDoubleBuffering(this);
            return (PAGE_EXISTS);
        }
    }

    public static void disableDoubleBuffering(Component c) {
        RepaintManager currentManager = RepaintManager.currentManager(c);
        currentManager.setDoubleBufferingEnabled(false);
    }

    public static void enableDoubleBuffering(Component c) {
        RepaintManager currentManager = RepaintManager.currentManager(c);
        currentManager.setDoubleBufferingEnabled(true);
    }

    public PrintService getPrinterService(String ps) {

        PrintService p = null;
        for (int i = 0; printerList != null && i < printerList.length; i++) {
            if (printerList[i].getName().equalsIgnoreCase(ps)) {
                p = printerList[i];
                break;
            }
        }
        return p;
    }

    public void initComponents() {
        msgField = new JTextField();
        print = new JButton("Stampa Etichetta");
        printers = new JComboBox();
    }

    public JPanel buildControlPanel() {
        GridBagLayout layout = new GridBagLayout();
        JPanel panel = new JPanel();
        panel.setLayout(layout);
        printers.setBackground(Color.white);
        add(panel, new JLabel("Selezionare la stampante:"), 0, 2, 4, 1, 0, 0,
                GridBagConstraints.BOTH, GridBagConstraints.EAST);
        add(panel, printers, 0, 3, 4, 1, 1, 0, GridBagConstraints.BOTH,
                GridBagConstraints.CENTER);
        print.setBackground(Color.LIGHT_GRAY);

        add(panel, print, 0, 4, 4, 2, 1, 0, GridBagConstraints.NONE,
                GridBagConstraints.CENTER);

        panel.setBackground(Color.WHITE);
        return panel;
    }

    public void init() {
        try {
            if (leftMarginMM <= 0) {
                leftMarginMM = Double
                        .parseDouble(getParameter("MargineSinistro"));// 22
                topMarginMM = Double
                        .parseDouble(getParameter("MargineSuperiore"));// 2
                paperWidthMM = Double
                        .parseDouble(getParameter("LarghezzaEtichetta"));// 50
                paperHeightMM = Double
                        .parseDouble(getParameter("AltezzaEtichetta"));// 35

                if (getParameter("rotazione") != null
                        && !"".equals(getParameter("rotazione"))) {
                    rotazione = Double.parseDouble(getParameter("rotazione"));// 0
                }
                // Parametri per stampa fu foglio A4
                deltaXMM = Double.parseDouble(getParameter("deltaXMM"));
                deltaYMM = Double.parseDouble(getParameter("deltaYMM"));
                stampaSuFoglioA4 = getParameter("stampaSuFoglioA4");

                testoRiga1 = getParameter("Riga_1");// ASL 3 - Regione Umbria";
                testoRiga3 = getParameter("Riga_3");// "Prot. Entrata Del:
                testoRiga4 = getParameter("Riga_4");// "Numero: 123456789012";
                testoRiga5 = getParameter("Riga_5");// "Classifica: XVII.6";
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage(), "eProt",
                    JOptionPane.ERROR_MESSAGE);

        }
        setBackground(Color.white);
        setLayout(new BorderLayout());
        initComponents();
        printerList = PrintServiceLookup.lookupPrintServices(null, null);
        PrintService def = PrintServiceLookup.lookupDefaultPrintService();
        for (int i = 0; printerList != null && i < printerList.length; i++) {
            printers.addItem(printerList[i].getName());
        }
        if (def != null && printers.getItemCount() > 0) {
            printers.setSelectedItem(def.getName());
        }

        msgField.setEnabled(false);
        print.addActionListener(this);
        add(BorderLayout.CENTER, buildControlPanel());
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == print) {
            print();
        }
    }

    public static void add(Container cont, Component comp, int x, int y,
            int width, int height, int weightx, int weighty, int fill,
            int anchor) {
        cons.gridx = x;
        cons.gridy = y;
        cons.gridwidth = width;
        cons.gridheight = height;
        cons.weightx = weightx;
        cons.weighty = weighty;
        cons.fill = fill;
        cons.anchor = anchor;
        cont.add(comp, cons);
    }

    public static String formattaNumeroProtocollo(String num) {
        String n = num;
        while (n.length() < 12) {
            n = "0" + n;
        }
        return n;
    }

    public static void main(String[] args) {
    }
}
