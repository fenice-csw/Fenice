package it.compit.fenice.mvc.presentation.helper;

public class TitolarioView {

	 	private int id;
	 
	    private String codice;

	    private String descrizione;

	    private String uffici;

		public int getId() {
			return id;
		}

		public TitolarioView() {
	    }
		
		public void setId(int id) {
			this.id = id;
		}

		public String getCodice() {
			return codice;
		}

		public String getDescrizione() {
			return descrizione;
		}

		public String getDescrizioneTitolario() {
			return codice+" - "+descrizione;
		}
		
		public String getUffici() {
			return uffici;
		}

		public void setCodice(String codice) {
			this.codice = codice;
		}

		public void setDescrizione(String descrizione) {
			this.descrizione = descrizione;
		}

		public void setUffici(String uffici) {
			this.uffici = uffici;
		}
	    
	    
	
}
