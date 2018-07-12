/*
 * Created on 13-mag-2005
 *

 */
package it.finsiel.siged.dao.digitalsignature;

import it.finsiel.siged.constant.FileConstants;
import it.finsiel.siged.constant.ProtocolConstants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.exception.CRLNonAggiornataException;
import it.finsiel.siged.exception.CertificatoNonValidoException;
import it.finsiel.siged.exception.ConversionException;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.exception.FirmaNonValidaException;
import it.finsiel.siged.mvc.business.FirmaDigitaleDelegate;
import it.finsiel.siged.mvc.vo.firma.CaVO;
import it.finsiel.siged.mvc.vo.firma.CrlUrlVO;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.FileUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CRLException;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.security.auth.x500.X500Principal;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DERUTCTime;
import org.bouncycastle.asn1.cms.Attribute;
import org.bouncycastle.asn1.cms.CMSAttributes;
import org.bouncycastle.asn1.cms.Time;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerId;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.util.encoders.Base64;

public class VerificaFirma {

	static Logger logger = Logger.getLogger(VerificaFirma.class.getName());

	public static X509CRL getCRL(byte[] bytes) throws DataException {
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			X509CRL crl = (X509CRL) cf.generateCRL(new ByteArrayInputStream(
					bytes));
			return crl;
		} catch (CertificateException e) {
			throw new DataException(
					"Errore durante l'elaborazione della CRL.\n"
							+ e.getMessage());
		} catch (CRLException e) {
			throw new DataException(
					"Errore durante l'elaborazione della CRL.\n"
							+ e.getMessage());
		}
	}

	/**
	 * Restituisce un oggetto contenente i dati relativi ad una CA incluse
	 * eventuali punti di distribuzione delle CRL.
	 */

	public static CaVO getCaFromCertificate(String cerFile)
			throws ConversionException {
		InputStream inStream = null;
		try {
			inStream = new BufferedInputStream(new FileInputStream(cerFile),
					FileConstants.BUFFER_SIZE);
			return getCaFromCertificate(inStream);
		} catch (Exception e) {
			throw new ConversionException(
					"Errore nell'elaborazione del certificato\nDettaglio Errore:"
							+ e.getMessage());
		} finally {
			FileUtil.closeIS(inStream);
		}
	}

	/**
	 * Restituisce un oggetto contenente i dati relativi ad una CA incluse
	 * eventuali punti di distribuzione delle CRL.
	 */
	public static CaVO getCaFromCertificate(InputStream inStream)
			throws ConversionException {
		CaVO ca = new CaVO();
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			X509Certificate cert = (X509Certificate) cf
					.generateCertificate(inStream);
			inStream.close();
			cert.checkValidity();
			logger.info(cert.getIssuerX500Principal().getName());
			ca.setIssuerCN(cert.getIssuerX500Principal().getName(
					X500Principal.RFC2253));
			ca.setValidoDal(cert.getNotBefore());
			ca.setValidoAl(cert.getNotAfter());
			String[] urls = getCRLDistributionPoints(cert);
			HashMap<String, CrlUrlVO> caCrl = new HashMap<String, CrlUrlVO>(1);
			for (int i = 0; urls != null && i < urls.length; i++) {
				if (urls[i] == null)
					continue;
				CrlUrlVO crl = new CrlUrlVO();
				crl.setUrl(urls[i]);
				crl.setTipo(getTipoProtocollo(crl.getUrl()));
				caCrl.put(crl.getUrl(), crl);
			}
			ca.setCrlUrls(caCrl);
			ca.setReturnValue(ReturnValues.VALID);
		} catch (IOException e) {
			throw new ConversionException(e);
		} catch (CertificateExpiredException e) {
			throw new ConversionException(
					"Certificato non valido perche' scaduto.");
		} catch (CertificateNotYetValidException e) {
			throw new ConversionException("Certificato non ancora valido.");
		} catch (CertificateException e) {
			throw new ConversionException(e);
		}
		return ca;
	}

	/**
	 * Salva il contenuto in chiaro, ovvero senza "headers" aggiuntivi. Tali
	 * headers sono i dati relativi la firma elettronica.
	 */

	public static void saveContentFromP7M(String p7mFile, String destFile) {
		try {
			OutputStream os = new BufferedOutputStream(new FileOutputStream(destFile), FileConstants.BUFFER_SIZE);
			InputStream p7mIs = new BufferedInputStream(new FileInputStream(p7mFile), FileConstants.BUFFER_SIZE);
			CMSSignedData cms = getCMSSignedData(p7mIs);
			//CMSSignedData cms= new CMSSignedData(decode(p7mIs));
			cms.getSignedContent().write(os);
			p7mIs.close();
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (CMSException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void saveContentFromP7MInputStream(InputStream p7mIs,
			String destFile) {
		try {
			OutputStream os = new BufferedOutputStream(new FileOutputStream(
					destFile), FileConstants.BUFFER_SIZE);
			CMSSignedData cms = getCMSSignedData(p7mIs);
			cms.getSignedContent().write(os);
			p7mIs.close();
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (CMSException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getTipoProtocollo(String url) {
		String tipo = null;
		if (url != null && url.indexOf("://") > 0) {
			tipo = url.substring(0, url.indexOf("://"));
			if (!ProtocolConstants.HTTP.equalsIgnoreCase(tipo)
					&& !ProtocolConstants.HTTPS.equalsIgnoreCase(tipo)
					&& !ProtocolConstants.LDAP.equalsIgnoreCase(tipo))
				tipo = null;
		}
		return tipo;
	}

	public static boolean verificaP7M(String pathToP7M) throws DataException,
			CertificatoNonValidoException, FirmaNonValidaException,
			CRLNonAggiornataException {
		InputStream is = null;
		try {
			is = new BufferedInputStream(new FileInputStream(pathToP7M),FileConstants.BUFFER_SIZE);
			return verificaP7M(is);
		} catch (FileNotFoundException e) {
			throw new DataException("File non trovato." + pathToP7M);
		} finally {
			FileUtil.closeIS(is);
		}

	}

	public static void verificaFileFirmato(String tempFilePath,
			String contentType) throws DataException,
			CertificatoNonValidoException, FirmaNonValidaException,
			CRLNonAggiornataException {
		if (FileConstants.MIME_TYPE_XP7M.equalsIgnoreCase(contentType)
				|| FileConstants.MIME_TYPE_P7M.equalsIgnoreCase(contentType)) {
			verificaP7M(tempFilePath);
		}
	}

	/**
	 * Verifica "forte" di un file firmato di tipo P7M. Controlli eseguiti: - Il
	 * file ha almeno un Signer - Il file ha almeno un certificato per ogni
	 * Signer trovato - Verifica 'debole' per ogni firma apposta sul file:
	 * controlla che il "message digest" ottenuto dal file e' stato codificato
	 * (ecco cos'� una firma!) utilizzando la chiave primaria corrispondente a
	 * quella pubblica (inclusa nel file). Verifica di tipo: SHA-1 a chiavi
	 * simmetriche.
	 */

	public static boolean verificaP7M(InputStream p7mIs) throws DataException,
			CertificatoNonValidoException, FirmaNonValidaException,
			CRLNonAggiornataException {
		try {
			CMSSignedData cms = getCMSSignedData(p7mIs);
			//CMSSignedData cms= new CMSSignedData(decode(p7mIs));
			CertStore certs = cms.getCertificatesAndCRLs("Collection", "BC");
			SignerInformationStore signers = cms.getSignerInfos();

			Collection c = signers.getSigners();
			Iterator it = c.iterator();
			
			if (!c.isEmpty()) {
				while (it.hasNext()) {
					SignerInformation signerInfo = (SignerInformation) it
							.next();
					SignerId signerID = signerInfo.getSID();
					String issuerDN = signerID.getIssuerAsString();
					String certSerialNumber = signerID.getSerialNumber()
							.toString();

					Date dataFirma = null;
					if (signerInfo.getSignedAttributes() != null) {
						Attribute a = signerInfo.getSignedAttributes().get(
								CMSAttributes.signingTime);
						Enumeration enumeration = a.getAttrValues()
								.getObjects();
						if (enumeration.hasMoreElements()) {
							DERUTCTime time = (DERUTCTime) enumeration
									.nextElement();
							dataFirma = Time.getInstance(time.toASN1Object())
									.getDate();
						}
					}
					Collection certCollection = certs.getCertificates(signerID);
					Iterator certIter = certCollection.iterator();
					if (!certCollection.isEmpty()) {
						while (certIter.hasNext()) {
							X509Certificate x509Cert = (X509Certificate) certIter
									.next();
							logger.info("Rilasciato Da x500:"
									+ x509Cert.getIssuerX500Principal()
											.getName(X500Principal.RFC2253));
							if (!signerInfo.verify(x509Cert.getPublicKey(),"BC")) {
								throw new FirmaNonValidaException("Firma non valida.");
							} else { 
								Date dataRevoca = FirmaDigitaleDelegate
										.getInstance().getDataSeRevocato(
												issuerDN, certSerialNumber);
								if (dataRevoca != null) {
									if (dataFirma == null) {
										dataFirma = new Date(System.currentTimeMillis());
									}
									if (dataRevoca.after(dataFirma)) {
										throw new CertificatoNonValidoException(
												"Firma Valida ma il Certificato e' stato Revocato in data: "
												+ DateUtil.formattaData(dataRevoca.getTime()));
									}
								}
							}
						}
					} else {
						throw new CertificatoNonValidoException(
								"Nessun certificato trovato per il firmatario.");
					}
				}
			} else {
				throw new FirmaNonValidaException(
						"Nessuna firma trovata all'interno del file.");
			}
			return true;
		} catch (NoSuchAlgorithmException e) {
			throw new FirmaNonValidaException(
					"Algoritimo di codifica della firma non supportato.\n"
							+ e.getMessage());
		} catch (NoSuchProviderException e) {
			throw new FirmaNonValidaException(
					"Provider di Certificato non trovato." + e.getMessage());
		} catch (CertStoreException e) {
			throw new FirmaNonValidaException(
					"Errore nella lettura dei certificati dal file.");
		} catch (CMSException e) {
			throw new FirmaNonValidaException(
					"Errore nella lettura del contenuto firmato.\nPKCS#7 non valido.");
		}
	}

	private static String[] getCRLDistributionPoints(X509Certificate cert)
			throws IOException {

		byte[] extBytes = cert
				.getExtensionValue(X509Extensions.CRLDistributionPoints.getId());
		if (extBytes == null || extBytes.length == 0) {
			logger.info("Nessuna CRL trovato per: " + cert.getIssuerDN());
			return new String[0];
		}
		ASN1InputStream ansiIs = new ASN1InputStream(new ByteArrayInputStream(
				extBytes));
		DERObject derObj = ansiIs.readObject();
		DEROctetString dos = (DEROctetString) derObj;
		byte[] ansiOctets = dos.getOctets();
		ASN1InputStream ansiIsOc = new ASN1InputStream(
				new ByteArrayInputStream(ansiOctets));
		DERObject derObj2 = ansiIsOc.readObject();
		Vector urls = getDERValue(derObj2);
		if (urls == null)
			return null;
		return (String[]) urls.toArray(new String[0]);
	} 

	private static Vector getDERValue(DERObject derObj) {
		if (derObj instanceof DERSequence) {
			Vector ret = new Vector();
			DERSequence seq = (DERSequence) derObj;
			Enumeration en = seq.getObjects();
			while (en.hasMoreElements()) {
				DERObject nestedObj = (DERObject) en.nextElement();
				Vector appo = getDERValue(nestedObj);
				if (appo != null)
					ret.addAll(appo);
			}
			return ret;
		}
		if (derObj instanceof DERTaggedObject) {
			DERTaggedObject derTag = (DERTaggedObject) derObj;
			if (derTag.isExplicit() && !derTag.isEmpty()) {
				DERObject nestedObj = derTag.getObject();
				Vector ret = getDERValue(nestedObj);
				return ret;
			} else {
				DEROctetString derOct = (DEROctetString) derTag.getObject();
				String val = new String(derOct.getOctets());
				Vector ret = new Vector();
				ret.add(val);
				return ret;
			}
		}
		return null;
	}
	
	public static byte[] decode(byte[] buffer) {
		return Base64.decode(buffer);
	}
	
	public static CMSSignedData getCMSSignedData(InputStream is) throws CMSException{
		byte[] array=FileUtil.getBytesFromInputStream(is);
		if(org.apache.commons.codec.binary.Base64.isBase64(array))
			return new CMSSignedData(decode(array));
		else
			return new CMSSignedData(array);
	}
	

}
