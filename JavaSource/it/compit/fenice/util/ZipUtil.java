package it.compit.fenice.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

	    public static byte[] zipFiles(File directory, Collection<String> files) throws IOException {
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        ZipOutputStream zos = new ZipOutputStream(baos);
	        byte bytes[] = new byte[2048];

	        for (String fileName : files) {
	            FileInputStream fis = new FileInputStream(directory.getPath() + 
	                "/" + fileName);
	            BufferedInputStream bis = new BufferedInputStream(fis);

	            zos.putNextEntry(new ZipEntry(fileName));

	            int bytesRead;
	            while ((bytesRead = bis.read(bytes)) != -1) {
	                zos.write(bytes, 0, bytesRead);
	            }
	            zos.closeEntry();
	            bis.close();
	            fis.close();
	        }
	        zos.flush();
	        baos.flush();
	        zos.close();
	        baos.close();

	        return baos.toByteArray();
	    }
	    
	    public static File zipFiles(File fileZip, File directory, Collection<String> files) throws IOException {
	    	FileOutputStream faos=new FileOutputStream(fileZip);
	    	ZipOutputStream zos = new ZipOutputStream(faos);
	        byte bytes[] = new byte[2048];

	        for (String fileName : files) {
	            FileInputStream fis = new FileInputStream(directory.getPath() + 
	                "/" + fileName);
	            BufferedInputStream bis = new BufferedInputStream(fis);

	            zos.putNextEntry(new ZipEntry(fileName));

	            int bytesRead;
	            while ((bytesRead = bis.read(bytes)) != -1) {
	                zos.write(bytes, 0, bytesRead);
	            }
	            zos.closeEntry();
	            bis.close();
	            fis.close();
	        }
	        zos.flush();
	        faos.flush();
	        zos.close();
	        faos.close();

	        return fileZip;
	    }
	
}
