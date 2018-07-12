package it.compit.fenice.mvc.vo.cds;

import it.finsiel.siged.mvc.vo.VersioneVO;

public class UtenteCdsVO extends VersioneVO {

		private static final long serialVersionUID = 1L;

	    private int utenteId;

	    private int ufficioId;

	    private int caricaId;

		public UtenteCdsVO() {

		}

		public int getUtenteId() {
			return utenteId;
		}

		public int getUfficioId() {
			return ufficioId;
		}

		public int getCaricaId() {
			return caricaId;
		}

		public void setUtenteId(int utenteId) {
			this.utenteId = utenteId;
		}

		public void setUfficioId(int ufficioId) {
			this.ufficioId = ufficioId;
		}

		public void setCaricaId(int caricaId) {
			this.caricaId = caricaId;
		}
	    
}
