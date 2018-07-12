package it.finsiel.siged.mvc.vo.lookup;

import it.finsiel.siged.mvc.vo.VersioneVO;

public class AmministrazionePartecipanteVO extends VersioneVO{
	
	private static final long serialVersionUID = 1L;

		private int idx;
	
	    private int tipoProcedimentoId;

	    private String denominazione;

	    private int rubricaId;

	    
		public int getIdx() {
			return idx;
		}

		public void setIdx(int idx) {
			this.idx = idx;
		}

		public int getTipoProcedimentoId() {
			return tipoProcedimentoId;
		}

		public void setTipoProcedimentoId(int tipoProcedimentoId) {
			this.tipoProcedimentoId = tipoProcedimentoId;
		}

		public String getDenominazione() {
			return denominazione;
		}

		public void setDenominazione(String denominazione) {
			this.denominazione = denominazione;
		}

		public int getRubricaId() {
			return rubricaId;
		}

		public void setRubricaId(int rubricaId) {
			this.rubricaId = rubricaId;
		}


}
