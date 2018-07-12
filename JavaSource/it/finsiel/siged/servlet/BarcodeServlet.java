/*
 * Created on 12-gen-2005
 *
 * 
 */
package it.finsiel.siged.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class BarcodeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	static Logger logger = Logger.getLogger(BarcodeServlet.class.getName());

    public void service(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String barcode_msg = (String) request.getParameter("barcode_msg");
        logger.info(barcode_msg);
        
        try {
        	/*
        	EAN13Bean bean = new EAN13Bean();
            int dpi = FileConstants.BARCODE_DPI;
            double height = FileConstants.BARCODE_HEIGHT;
            bean.setModuleWidth(UnitConv.in2mm(1.5f / dpi)); // makes the
            bean.setBarHeight(height);
            bean.setFontName("Arial Bold");// request.getParameter("font"));
            bean.setFontSize(2.48);// Double.parseDouble(request.getParameter("size")));
            bean.doQuietZone(true);
            bean.setQuietZone(5);
            bean.setChecksumMode(ChecksumMode.CP_ADD);// aggiunge l'ultima
            try {
                BitmapCanvasProvider canvas = new BitmapCanvasProvider(os,
                        "image/png", dpi, BufferedImage.TYPE_BYTE_BINARY, false);
                bean.generateBarcode(canvas, StringUtil
                        .formattaNumeroProtocollo(barcode_msg));
                canvas.finish();
            } finally {
                os.close();
            }
            */
        } catch (Exception e) {
        	e.printStackTrace();
            logger.error("", e);
        }
	
    }
}
