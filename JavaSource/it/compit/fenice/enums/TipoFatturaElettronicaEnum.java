package it.compit.fenice.enums;

public enum TipoFatturaElettronicaEnum {

		FA_1_1("/xsl/fatturapa_v1.1.xsl"),
		FA_1_2("/xsl/fatturapa_v1.2.xsl"),
		SE("/xsl/SE_v1.0.xsl"),
		DT("/xsl/DT_v1.0.xsl"),
		NS("/xsl/NS_v1.0.xsl"),
		EC("/xsl/EC_v1.0.xsl");
		
		private TipoFatturaElettronicaEnum(String path){
			this.path = path;
		}
		
		private String path;
		
		public String getPath(){
			return path;
		};
	
}
