package it.compit.fenice.ftpHelper;

import it.compit.fenice.mvc.presentation.actionform.ammtrasparente.DocumentoAmmTrasparenteForm;
import it.compit.fenice.mvc.presentation.actionform.repertori.DocumentoRepertorioForm;
import it.compit.fenice.mvc.presentation.helper.DocumentoAmmTrasparenteView;
import it.compit.fenice.mvc.presentation.helper.DocumentoRepertorioView;
import it.compit.fenice.mvc.vo.ammtrasparente.AmmTrasparenteVO;
import it.compit.fenice.mvc.vo.ammtrasparente.DocumentoAmmTrasparenteVO;
import it.compit.fenice.mvc.vo.repertori.DocumentoRepertorioVO;
import it.compit.fenice.mvc.vo.repertori.RepertorioVO;
import it.sauronsoftware.ftp4j.FTPClient;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

public class FtpHelper {

	FTPClient client;

	static Logger logger = Logger.getLogger(FtpHelper.class.getName());

	public FtpHelper() {
		client = new FTPClient();
	}

	public boolean connect(String host, String username, String password,
			int port) {
		
		try {
			client.setSecurity(FTPClient.SECURITY_FTP);
			client.setPassive(true);
			client.setType(FTPClient.TYPE_AUTO);
			client.connect(host, port);
			client.login(username, password);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return false;
		}
		logger.info("FtpHelper: connected");
		return true;
	}

	public void disconnect() {
		try {
			client.disconnect(true);
		} catch (Exception e) {
		}
	}

	public void uploadFile(String file) {
		try {
			client.upload(new java.io.File(file), new TransferListener());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public boolean uploadFileOutputStream(InputStream is, String file) {
		try {
			
			client.upload(file, is, 0, 0, new TransferListener());
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			closeIS(is);
		}
		return true;
	}

	public static void closeIS(InputStream is) {
		try {
			if (is != null)
				is.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public void creaStruttura(DocumentoRepertorioVO vo) {
		try {
			if (!esisteDirectory(String.valueOf(vo.getRepId()))) {
				client.createDirectory(client.currentDirectory() + "/"
						+ vo.getRepId());
				client.createDirectory(client.currentDirectory() + "/"
						+ vo.getRepId() + "/" + vo.getDocRepertorioId());
			} else {
				client.createDirectory(client.currentDirectory() + "/"
						+ vo.getRepId() + "/" + vo.getDocRepertorioId());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	public void creaStruttura(DocumentoRepertorioForm form) {
		try {
			if (!esisteDirectory(String.valueOf(form.getRepertorioId()))) {
				client.createDirectory(client.currentDirectory() + "/"
						+ form.getRepertorioId());
				client.createDirectory(client.currentDirectory() + "/"
						+ form.getRepertorioId() + "/" + form.getDocRepertorioId());
			} else {
				client.createDirectory(client.currentDirectory() + "/"
						+ form.getRepertorioId() + "/" + form.getDocRepertorioId());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public Collection<RepertorioVO> checkDirectoryRepertorio(Collection<RepertorioVO> list) {
		List<RepertorioVO> result = new ArrayList<RepertorioVO>();
		try {
			for (Object o : list) {
					RepertorioVO vo = (RepertorioVO) o;
					if (esisteDirectory(String.valueOf(vo.getRepertorioId())))
						result.add(vo);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	public Collection<DocumentoRepertorioView> checkDirectoryDocumentoRepertorio(Collection<DocumentoRepertorioView> list) {
		List<DocumentoRepertorioView> result = new ArrayList<DocumentoRepertorioView>();
		try {
			for (Object o : list) {
					DocumentoRepertorioView view = (DocumentoRepertorioView) o;
					if (esisteDirectory(String.valueOf(view
							.getDocRepertorioId())))
						result.add(view);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}
	
	public boolean esisteDirectory(String dir) {
		try {
			for (String file : client.listNames())
				if (file.equals(dir))
					return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	public void changeDir(String dir) {
		try {
			client.changeDirectory(dir);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	};

	public void goInRoot() {
		try {
			client.changeDirectoryUp();
			client.changeDirectoryUp();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	};
	
	public void creaStruttura(DocumentoAmmTrasparenteVO vo) {
		try {
			if (!esisteDirectory(String.valueOf(vo.getSezId()))) {
				client.createDirectory(client.currentDirectory() + "/"
						+ vo.getSezId());
				client.createDirectory(client.currentDirectory() + "/"
						+ vo.getSezId() + "/" + vo.getDocSezioneId());
			} else {
				client.createDirectory(client.currentDirectory() + "/"
						+ vo.getSezId() + "/" + vo.getDocSezioneId());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	public void creaStruttura(DocumentoAmmTrasparenteForm form) {
		try {
			if (!esisteDirectory(String.valueOf(form.getSezioneId()))) {
				client.createDirectory(client.currentDirectory() + "/"
						+ form.getSezioneId());
				client.createDirectory(client.currentDirectory() + "/"
						+ form.getSezioneId() + "/" + form.getDocSezioneId());
			} else {
				client.createDirectory(client.currentDirectory() + "/"
						+ form.getSezioneId() + "/" + form.getDocSezioneId());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public Collection<AmmTrasparenteVO> checkDirectoryAmmTrasparente(Collection<AmmTrasparenteVO> list) {
		List<AmmTrasparenteVO> result = new ArrayList<AmmTrasparenteVO>();
		try {
			for (Object o : list) {
				AmmTrasparenteVO vo = (AmmTrasparenteVO) o;
					if (esisteDirectory(String.valueOf(vo.getSezioneId())))
						result.add(vo);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	public Collection<DocumentoAmmTrasparenteView> checkDirectoryDocumentoAmmTrasparente(Collection<DocumentoAmmTrasparenteView> list) {
		List<DocumentoAmmTrasparenteView> result = new ArrayList<DocumentoAmmTrasparenteView>();
		try {
			for (Object o : list) {
				DocumentoAmmTrasparenteView view = (DocumentoAmmTrasparenteView) o;
					if (esisteDirectory(String.valueOf(view.getDocSezioneId())))
						result.add(view);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}
}
