package it.compit.fenice.enums;

	    public enum UnitaAmministrativaEnum {
	    	GENERICO(0,"GENERICO"), 
	    	POLICLINICO_CT(1,"POLICLINICO_CT"), 
	    	BBCCAA_PA(2,"BBCCAA_PA"), 
	    	ERSU_PA(3,"ERSU_PA"),
	    	ERSU_CT(4,"ERSU_CT"),
	    	TEST(5,"CSWSERVIZI"),
	    	CDS(6,"CDS"),
	    	ERSU_ME(7,"ERSU_ME"),
	    	SCUOLA(8,"SCUOLA");
	        
	    	private final int valore;
	    	private final String denominazione;

	        private UnitaAmministrativaEnum(int valore , String denominazione) {
	                this.denominazione = denominazione;
	                this.valore=valore;
	        }

			public String getDenominazione() {
				return denominazione;
			} 
			
			
			public int getValore() {
				return valore;
			}

			public static UnitaAmministrativaEnum findByValue(int value){
				for (UnitaAmministrativaEnum iter : UnitaAmministrativaEnum.values()) {
					if (iter.getValore()==value) {
						return iter;
					}
				}
				return null;
			}
}
