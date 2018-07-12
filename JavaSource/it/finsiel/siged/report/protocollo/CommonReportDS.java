/*
 * Created on 12-gen-2005
 *
 * 
 */
package it.finsiel.siged.report.protocollo;

import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;

import java.lang.reflect.Method;
import java.util.Collection;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.apache.log4j.Logger;


public class CommonReportDS implements JRDataSource {

    static Logger logger = Logger.getLogger(CommonReportDS.class.getName());

    private Object[] data = null;

    private int index = -1;
    
    //ReportProtocolloView

    private Class c = ReportProtocolloView.class;

    public CommonReportDS(Collection d, Class c) {
        super();
        this.data = d.toArray();
        this.c = c;
    }

    public CommonReportDS(Collection d) {
        super();
        this.data = d.toArray();
    }

	/*
     * (non-Javadoc)
     * 
     * @see net.sf.jasperreports.engine.JRDataSource#next()
     */
    public boolean next() throws JRException {
        index++;
        return (index < data.length);
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sf.jasperreports.engine.JRDataSource#getFieldValue(net.sf.jasperreports.engine.JRField)
     */
    public Object getFieldValue(JRField field) throws JRException {
        Object value = null;
        String fieldName = field.getName();
        try {
            Method method = this.c.getMethod("get" + fieldName, null);
            value = method.invoke(data[index], null);
        } catch (NoSuchMethodException e1) {
            logger.warn("Field not found in DataSource:" + fieldName);
            value = "Field Not Found";
        } catch (Exception e1) {
            logger.warn("Error invoking method: get[" + fieldName + "]");
            value = "Field Not Found";
        }
        return value;
    }

}
