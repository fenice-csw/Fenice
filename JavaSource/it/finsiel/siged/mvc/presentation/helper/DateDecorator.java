/*
 * Created on 25-gen-2005
 *
 * 
 */
package it.finsiel.siged.mvc.presentation.helper;

/**
 * @author Almaviva sud
 * 
 */

import java.util.Date;

import org.apache.commons.lang.time.FastDateFormat;
import org.displaytag.decorator.ColumnDecorator;

public class DateDecorator implements ColumnDecorator {

    public DateDecorator() {
        dateFormat = FastDateFormat.getInstance("dd/MM/yyyy");
    }

    public final String decorate(Object columnValue) {
        if (columnValue != null) {
            Date date = (Date) columnValue;
            return dateFormat.format(date);
        } else
            return "";
    }

    private FastDateFormat dateFormat;
}