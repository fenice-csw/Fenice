package it.compit.fenice.ftpHelper;

import it.sauronsoftware.ftp4j.FTPDataTransferListener;

import org.apache.log4j.Logger;

public class TransferListener implements FTPDataTransferListener {
	
	static Logger logger = Logger.getLogger(TransferListener.class.getName());
	
	public boolean isTrasferred=false;

	public void started() {
		logger.info("TransferListener: started");	
	}

	public void transferred(int length) {
		isTrasferred=false;
	}

	public void completed() {
		logger.info("TransferListener: completed");
		isTrasferred=true;
	}

	public void aborted() {
		logger.info("TransferListener: aborted");
		isTrasferred=false;
	}

	public void failed() {
		logger.info("TransferListener: failed");
		isTrasferred=false;
	}

}