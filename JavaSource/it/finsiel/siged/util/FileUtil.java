/*
 * Created on 30-nov-2004
 *
 * 
 */
package it.finsiel.siged.util;

import it.compit.fenice.mvc.presentation.actionform.documentale.EditorForm;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.FileConstants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.dao.digitalsignature.VerificaFirma;
import it.finsiel.siged.exception.CRLNonAggiornataException;
import it.finsiel.siged.exception.CertificatoNonValidoException;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.exception.FirmaNonValidaException;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.presentation.actionform.UploaderForm;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;

public final class FileUtil {

	static Logger logger = Logger.getLogger(FileUtil.class.getName());

	private static final int TEMP_DIR_ATTEMPTS = 10000;

	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static void stringToDom(String xmlSource, String tempPath)
			throws IOException {
		FileWriter fw = new FileWriter(tempPath);
		fw.write(xmlSource);
		fw.close();
	}

	public static File createTempDir() {
		File baseDir = new File(System.getProperty("java.io.tmpdir"));
		String baseName = System.currentTimeMillis() + "-";

		for (int counter = 0; counter < TEMP_DIR_ATTEMPTS; counter++) {
			File tempDir = new File(baseDir, baseName + counter);
			if (tempDir.mkdir()) {
				return tempDir;
			}
		}
		throw new IllegalStateException("Failed to create directory within "
				+ TEMP_DIR_ATTEMPTS + " attempts (tried " + baseName + "0 to "
				+ baseName + (TEMP_DIR_ATTEMPTS - 1) + ')');
	}

	public static DocumentoVO uploadDocumento(EditorForm form,
			HttpServletRequest request, ActionMessages errors) {
		InputStream is = new ByteArrayInputStream(form.getTesto().getBytes());
		DocumentoVO documento = new DocumentoVO();
		String fileName = form.getNomeFile() + ".pdf";
		String contentType = "application/pdf";
		logger.info(contentType);
		String tempFilePath = PdfUtil.creaPDFdaHtml(fileName, is, request,
				errors);
		String impronta = FileUtil.calcolaDigest(tempFilePath, errors);
		File file = new File(tempFilePath);
		int size = Long.valueOf(file.length()).intValue();
		if (!"".equals(fileName) && fileName.length() > 100) {
			errors.add("documento", new ActionMessage("error.nomefile.lungo",
					"", ""));
		} else if (size > 0 && !"".equals(fileName)) {
			if (errors.isEmpty()) {
				try {
					VerificaFirma
							.verificaFileFirmato(tempFilePath, contentType);
				} catch (DataException e) {
					errors.add("allegati", new ActionMessage(
							"database.cannot.load"));
				} catch (CertificatoNonValidoException e) {
					errors.add(
							"allegati",
							new ActionMessage(
									"errore.verificafirma.doc.non_valido", e
											.getMessage()));
				} catch (FirmaNonValidaException e) {
					errors.add(
							"allegati",
							new ActionMessage(
									"errore.verificafirma.doc.non_valido", e
											.getMessage()));
				} catch (CRLNonAggiornataException e) {
					errors.add("allegati", new ActionMessage(
							"errore.verificafirma.crl_non_aggiornata"));
				}
				String username = ((Utente) request.getSession().getAttribute(
						Constants.UTENTE_KEY)).getValueObject().getUsername();
				documento.setMustCreateNew(true);
				documento.setDescrizione(null);
				documento.setFileName(fileName);
				documento.setPath(tempFilePath);
				documento.setImpronta(impronta);
				documento.setSize(size);
				documento.setContentType(contentType);
				documento
						.setRowCreatedTime(new Date(System.currentTimeMillis()));
				documento
						.setRowUpdatedTime(new Date(System.currentTimeMillis()));
				documento.setRowCreatedUser(username);
				documento.setRowUpdatedUser(username);
			}
		} else {
			errors.add("documento", new ActionMessage("campo.obbligatorio",
					"File", ""));
		}
		return documento;
	}

	public static DocumentoVO uploadDocumento(String fileName, InputStream is,
			HttpServletRequest request, ActionMessages errors) {
		DocumentoVO documento = new DocumentoVO();
		String contentType = "application/pdf";
		logger.info(contentType);
		String tempFilePath = PdfUtil.creaPDFdaHtml(fileName, is, request,
				errors);
		String impronta = FileUtil.calcolaDigest(tempFilePath, errors);
		File file = new File(tempFilePath);
		int size = Long.valueOf(file.length()).intValue();
		if (!"".equals(fileName) && fileName.length() > 100) {
			errors.add("documento", new ActionMessage("error.nomefile.lungo",
					"", ""));
		} else if (size > 0 && !"".equals(fileName)) {
			if (errors.isEmpty()) {
				try {
					VerificaFirma
							.verificaFileFirmato(tempFilePath, contentType);
				} catch (DataException e) {
					errors.add("allegati", new ActionMessage(
							"database.cannot.load"));
				} catch (CertificatoNonValidoException e) {
					errors.add(
							"allegati",
							new ActionMessage(
									"errore.verificafirma.doc.non_valido", e
											.getMessage()));
				} catch (FirmaNonValidaException e) {
					errors.add(
							"allegati",
							new ActionMessage(
									"errore.verificafirma.doc.non_valido", e
											.getMessage()));
				} catch (CRLNonAggiornataException e) {
					errors.add("allegati", new ActionMessage(
							"errore.verificafirma.crl_non_aggiornata"));
				}
				String username = ((Utente) request.getSession().getAttribute(
						Constants.UTENTE_KEY)).getValueObject().getUsername();
				documento.setMustCreateNew(true);
				documento.setDescrizione(null);
				documento.setFileName(fileName);
				documento.setPath(tempFilePath);
				documento.setImpronta(impronta);
				documento.setSize(size);
				documento.setContentType(contentType);
				documento
						.setRowCreatedTime(new Date(System.currentTimeMillis()));
				documento
						.setRowUpdatedTime(new Date(System.currentTimeMillis()));
				documento.setRowCreatedUser(username);
				documento.setRowUpdatedUser(username);
			}
		} else {
			errors.add("documento", new ActionMessage("campo.obbligatorio",
					"File", ""));
		}
		return documento;
	}

	public static String leggiFormFile(UploaderForm form,
			HttpServletRequest request, ActionMessages errors) {
		FormFile file = form.getFormFileUpload();
		String fileName = file.getFileName();
		String pathFileTemporaneo = null;
		String folder = null;
		try {
			folder = ServletUtil.getTempUserPath(request.getSession());
			InputStream stream = new BufferedInputStream(file.getInputStream(),
					FileConstants.BUFFER_SIZE);
			File tempFile = File.createTempFile("temp_", ".upload", new File(
					folder));
			OutputStream bos = new BufferedOutputStream(new FileOutputStream(
					tempFile), FileConstants.BUFFER_SIZE);
			int bytesRead = 0;
			byte buffer[] = new byte[FileConstants.BUFFER_SIZE];
			while ((bytesRead = stream.read(buffer)) != -1) {
				bos.write(buffer, 0, bytesRead);
				bos.flush();
			}
			bos.close();
			pathFileTemporaneo = tempFile.getAbsolutePath();
			stream.close();
		} catch (FileNotFoundException fnfe) {
			logger.debug("Error uploading file to:" + folder + " - file name:"
					+ fileName);
			logger.error(fnfe.getMessage(), fnfe);
			errors.add("fileFormUpload", new ActionMessage(
					"upload.error.filenotfound"));
		} catch (IOException ioe) {
			logger.debug("Error uploading file to:" + folder + " - file name:"
					+ fileName);
			logger.error(ioe.getMessage(), ioe);
			errors.add("fileFormUpload", new ActionMessage("upload.error.io"));
		}
		return pathFileTemporaneo;
	}

	public static String leggiFormFileP7M(UploaderForm form,
			HttpServletRequest request, ActionMessages errors) {
		FormFile file = form.getFormFileUpload();
		String fileName = file.getFileName();
		String pathFileTemporaneo = null;
		String folder = null;
		try {
			folder = ServletUtil.getTempUserPath(request.getSession());
			InputStream stream = new BufferedInputStream(file.getInputStream(),
					FileConstants.BUFFER_SIZE);
			File tempFileP7M = File.createTempFile("temp_", ".upload",
					new File(folder));
			OutputStream bos = new BufferedOutputStream(new FileOutputStream(
					tempFileP7M), FileConstants.BUFFER_SIZE);
			int bytesRead = 0;
			byte buffer[] = new byte[FileConstants.BUFFER_SIZE];
			while ((bytesRead = stream.read(buffer)) != -1) {
				bos.write(buffer, 0, bytesRead);
				bos.flush();
			}
			bos.close();
			stream.close();

			File tempFile = File.createTempFile("temp_", ".upload", new File(
					folder));
			VerificaFirma.saveContentFromP7M(tempFileP7M.getAbsolutePath(),
					tempFile.getAbsolutePath());
			pathFileTemporaneo = tempFile.getAbsolutePath();
			deleteFile(tempFileP7M.getAbsolutePath());
		} catch (FileNotFoundException fnfe) {
			logger.debug("Error uploading file to:" + folder + " - file name:"
					+ fileName);
			logger.error(fnfe.getMessage(), fnfe);
			errors.add("fileFormUpload", new ActionMessage(
					"upload.error.filenotfound"));
		} catch (IOException ioe) {
			logger.debug("Error uploading file to:" + folder + " - file name:"
					+ fileName);
			logger.error(ioe.getMessage(), ioe);
			errors.add("fileFormUpload", new ActionMessage("upload.error.io"));
		}
		return pathFileTemporaneo;
	}

	public static String leggiFormFilePrincipale(UploaderForm form,
			HttpServletRequest request, ActionMessages errors) {
		FormFile file = form.getFilePrincipaleUpload();
		String fileName = file.getFileName();
		String pathFileTemporaneo = null;
		String folder = null;
		try {
			folder = ServletUtil.getTempUserPath(request.getSession());
			InputStream stream = new BufferedInputStream(file.getInputStream(),
					FileConstants.BUFFER_SIZE);
			File tempFile = File.createTempFile("temp_", ".upload", new File(
					folder));
			OutputStream bos = new BufferedOutputStream(new FileOutputStream(
					tempFile), FileConstants.BUFFER_SIZE);
			int bytesRead = 0;
			byte buffer[] = new byte[FileConstants.BUFFER_SIZE];
			while ((bytesRead = stream.read(buffer)) != -1) {
				bos.write(buffer, 0, bytesRead);
				bos.flush();
			}
			bos.close();
			pathFileTemporaneo = tempFile.getAbsolutePath();
			stream.close();
		} catch (FileNotFoundException fnfe) {
			logger.debug("Error uploading file to:" + folder + " - file name:"
					+ fileName);
			logger.error(fnfe.getMessage(), fnfe);
			errors.add("filePrincipaleUpload", new ActionMessage(
					"upload.error.filenotfound"));
		} catch (IOException ioe) {
			logger.debug("Error uploading file to:" + folder + " - file name:"
					+ fileName);
			logger.error(ioe.getMessage(), ioe);
			errors.add("filePrincipaleUpload", new ActionMessage(
					"upload.error.io"));
		} catch (Exception ioe) {
			logger.debug("Error uploading file to:" + folder + " - file name:"
					+ fileName);
			logger.error(ioe.getMessage(), ioe);
			errors.add("filePrincipaleUpload", new ActionMessage(
					"upload.error.io"));
		}
		return pathFileTemporaneo;
	}

	public static boolean writeFile(OutputStream os, String filePath) {
		InputStream is = null;
		try {
			is = new BufferedInputStream(new FileInputStream(filePath),
					FileConstants.BUFFER_SIZE);
			int bytesRead = 0;
			byte buffer[] = new byte[FileConstants.BUFFER_SIZE];
			while ((bytesRead = is.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead);
				os.flush();
			}
		} catch (FileNotFoundException e) {
			logger.error("", e);
			return false;
		} catch (IOException e) {
			logger.error("", e);
			return false;
		} finally {
			closeOS(os);
			closeIS(is);
		}
		return true;
	}

	public static boolean writeFile(InputStream is, String destPath) {
		OutputStream os = null;
		try {

			os = new BufferedOutputStream(new FileOutputStream(destPath),
					FileConstants.BUFFER_SIZE);
			int bytesRead = 0;
			byte buffer[] = new byte[FileConstants.BUFFER_SIZE];
			while ((bytesRead = is.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead);
				os.flush();
			}
		} catch (FileNotFoundException e) {
			logger.error("", e);
			return false;
		} catch (IOException e) {
			logger.error("", e);
			return false;
		} finally {
			closeOS(os);
		}
		return true;
	}

	public static boolean writeFile(InputStream is, OutputStream os) {

		try {
			int bytesRead = 0;
			byte buffer[] = new byte[FileConstants.BUFFER_SIZE];
			while ((bytesRead = is.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead);
				os.flush();
			}
		} catch (IOException e) {
			logger.error("", e);
			return false;
		}
		return true;
	}

	public static byte[] leggiFileAsBytes(String path) {
		InputStream is = null;
		ByteArrayOutputStream os = null;
		byte[] array = new byte[0];
		try {
			os = new ByteArrayOutputStream();
			is = new BufferedInputStream(new FileInputStream(path),
					FileConstants.BUFFER_SIZE);
			int bytesRead = 0;
			byte buffer[] = new byte[FileConstants.BUFFER_SIZE];

			while ((bytesRead = is.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead);
				os.flush();
			}
			array = os.toByteArray();
		} catch (FileNotFoundException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		} finally {
			closeIS(is);
			closeOS(os);
		}
		return array;
	}
	
	public static byte[] getBytesFromInputStream(InputStream is) 
    {
		ByteArrayOutputStream os = null;
		byte[] array = new byte[0];
        try
        {
        	os = new ByteArrayOutputStream();
            byte[] buffer = new byte[FileConstants.BUFFER_SIZE];
            for (int len; (len = is.read(buffer)) != -1;)
            	os.write(buffer, 0, len);
            os.flush();
            array = os.toByteArray();
        }catch (IOException e) {
			logger.error("", e);
		} finally {
			closeIS(is);
			closeOS(os);
		}
        return array;
    }

	public static void copyFile(File source, File destination) throws IOException {
		if (!source.exists()) {
			return;
		}
		if ((destination.getParentFile() != null)
				&& (!destination.getParentFile().exists())) {
			destination.getParentFile().mkdirs();
		}
		FileChannel srcChannel=null;
		FileChannel dstChannel=null;
		try {
			srcChannel = new FileInputStream(source).getChannel();
			dstChannel = new FileOutputStream(destination).getChannel();
			dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
		} catch (Exception ioe) {
			ioe.printStackTrace();
		}finally{
			srcChannel.close();
			dstChannel.close();
		}
	}

	public static void deltree(String directory) {
		deltree(new File(directory));
	}

	public static void deltree(File directory) {
		if (directory.exists() && directory.isDirectory()) {
			File[] fileArray = directory.listFiles();

			for (int i = 0; i < fileArray.length; i++) {
				if (fileArray[i].isDirectory()) {
					deltree(fileArray[i]);
				} else {
					fileArray[i].delete();
				}
			}

			directory.delete();
		}
	}

	public static String calcolaDigest(String filePath, ActionMessages errors) {
		String strImpronta = "";
		InputStream is = null;
		MessageDigest sha = null;

		try {
			is = new BufferedInputStream(new FileInputStream(filePath),
					FileConstants.BUFFER_SIZE);
			if (is != null) {
				sha = MessageDigest.getInstance(FileConstants.SHA);
				byte[] messaggio = new byte[FileConstants.BUFFER_SIZE];
				int len = 0;
				while ((len = is.read(messaggio)) != -1) {
					sha.update(messaggio, 0, len);
				}
				byte[] impronta = sha.digest();
				int size = impronta.length;
				StringBuffer buf = new StringBuffer();
				int unsignedValue = 0;
				String strUnsignedValue = null;
				for (int i = 0; i < size; i++) {
					unsignedValue = ((int) impronta[i]) & 0xff;
					strUnsignedValue = Integer.toHexString(unsignedValue);
					if (strUnsignedValue.length() == 1)
						buf.append("0");
					buf.append(strUnsignedValue);
				}
				strImpronta = buf.toString();
				logger.debug("Impronta calcolata:" + strImpronta);

			}
		} catch (IOException io) {
			logger.error("Errore nella generazione dell'impronta:", io);
			errors.add("fileFormUpload", new ActionMessage("errore_impronta"));
		} catch (Throwable t) {
			logger.error("Errore nella generazione dell'impronta:", t);
			errors.add("fileFormUpload", new ActionMessage("errore_impronta"));
		}finally{
			FileUtil.closeIS(is);
		}
		return strImpronta;
	}

	public static String calcolaDigest(String filePath, String algorithm) {
		String strImpronta = "";
		InputStream is = null;
		MessageDigest sha = null;
		try {
			is = new BufferedInputStream(new FileInputStream(filePath),
					FileConstants.BUFFER_SIZE);
			if (is != null) {
				sha = MessageDigest.getInstance(algorithm);
				byte[] messaggio = new byte[FileConstants.BUFFER_SIZE];
				int len = 0;
				while ((len = is.read(messaggio)) != -1) {
					sha.update(messaggio, 0, len);
				}
				byte[] impronta = sha.digest();
				int size = impronta.length;
				StringBuffer buf = new StringBuffer();
				int unsignedValue = 0;
				String strUnsignedValue = null;
				for (int i = 0; i < size; i++) {
					unsignedValue = ((int) impronta[i]) & 0xff;
					strUnsignedValue = Integer.toHexString(unsignedValue);
					if (strUnsignedValue.length() == 1)
						buf.append("0");
					buf.append(strUnsignedValue);
				}
				strImpronta = buf.toString();
				logger.debug("Impronta calcolata:" + strImpronta);
				is.close();
			}
		} catch (IOException io) {
			logger.error("Errore nella generazione dell'impronta:", io);

		} catch (Throwable t) {
			logger.error("Errore nella generazione dell'impronta:", t);
		}
		return strImpronta;
	}

	public static String cambiaEstensioneFile(String filename, String estensione) {
		if (filename != null && filename.lastIndexOf(".") > 0)
			return filename.substring(0, filename.lastIndexOf(".")) + "."
					+ estensione;
		else
			return filename + "." + estensione;
	}

	public static String deleteEstensioneFile(String filename) {
		if (filename != null && filename.lastIndexOf(".") > 0)
			return filename.substring(0, filename.lastIndexOf("."));
		else
			return filename;
	}

	public static String deleteEstensioneFileFatturaElettronica(String filename) {
		if (filename != null && filename.toLowerCase().lastIndexOf(".xml.p7m") > 0)
			return filename.substring(0, filename.toLowerCase().lastIndexOf(".xml.p7m"));
		else if (filename != null && filename.toLowerCase().lastIndexOf(".xml") > 0)
			return filename.substring(0, filename.toLowerCase().lastIndexOf(".xml"));
		else if (filename != null && filename.toLowerCase().lastIndexOf(".p7m") > 0)
			return filename.substring(0, filename.toLowerCase().lastIndexOf(".p7m"));
		else
			return filename;
	}

	public static boolean deleteFile(String pathToFile) {
		if (pathToFile == null)
			return true;
		File f = new File(pathToFile);
		if (!f.exists())
			return false;
		else
			return f.delete();
	}

	public static void closeIS(InputStream is) {
		try {
			if (is != null)
				is.close();
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	public static void closeOS(OutputStream os) {
		try {
			if (os != null)
				os.close();
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	public static boolean validateXML(String file) {

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			dbf.setValidating(true);
			dbf.setAttribute(
					"http://java.sun.com/xml/jaxp/properties/schemaLanguage",
					"http://www.w3.org/2001/XMLSchema");
			dbf.setAttribute(
					"http://java.sun.com/xml/jaxp/properties/schemaSource",
					"http://www.example.com/Report.xsd");
			return true;
		} catch (Exception de) {
			de.printStackTrace();
			return false;
		}

	}

	public static int gestionePathDoc(String dirDoc) {
		int returnValues = ReturnValues.UNKNOWN;
		File dir = new File(dirDoc);
		if (!dir.isDirectory()) {
			try {
				if (dir.mkdirs()) {
					returnValues = ReturnValues.SAVED;
				}

				else {
					returnValues = ReturnValues.INVALID;

				}
			} catch (RuntimeException e) {
				logger.error("Errore nella creazione del path documenti della Aoo");
				e.printStackTrace();
			}
		} else {
			returnValues = ReturnValues.FOUND;
		}
		return returnValues;
	}
	
	public static int createForCurrentYear(String aooDir, String currentYearDir) {
		int returnValues = ReturnValues.UNKNOWN;
		File dir = new File(aooDir);
		if(dir.exists() && dir.isDirectory()) {
			try {
				File aooDirForCurrentYear = new File(dir, currentYearDir);
				if(!aooDirForCurrentYear.exists()) {
					if(aooDirForCurrentYear.mkdir()) {
						returnValues = ReturnValues.SAVED;
					} else {
						returnValues = ReturnValues.INVALID;
					}
				} else {
					returnValues = ReturnValues.FOUND;
				}
			} catch (RuntimeException e) {
				logger.error("Errore nella creazione del path documenti della Aoo");
				e.printStackTrace();
			}
		} else {
			returnValues = ReturnValues.INVALID;
		}
		return returnValues;
	}

	public static Collection<File> getFilePathDoc(String dirDoc) {
		ArrayList<File> listaFile = new ArrayList<File>();
		File dir = new File(dirDoc);
		String[] files = dir.list();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				String filename = files[i];
				File f = new File(dirDoc, filename);
				listaFile.add(f);
			}
		}
		return listaFile;
	}

	public static String getFatturaElettronicaType (String fileName, String path) throws IOException{
		if (fileName.contains("_DT_"))
			return "DT";
		else if (fileName.contains("_EC_"))
			return "EC";
		else if (fileName.contains("_SE_"))
			return "SE";
		else if (fileName.contains("_NS_"))
			return "NS";
		else{
			if(isFatturaElettronica1dot1(path))
				return "FA_1_1";
			else 
				return "FA_1_2";
			
		}
	}
	
	public static boolean isFatturaElettronica1dot1(String filePath) throws IOException {
		String searchQuery = "http://www.fatturapa.gov.it/sdi/fatturapa/v1.1";
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains(searchQuery)) {
					br.close();
					return true;
				}
			}
		br.close();
		return false;
	}

}