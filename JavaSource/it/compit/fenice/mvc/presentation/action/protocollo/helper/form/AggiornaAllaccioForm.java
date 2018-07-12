package it.compit.fenice.mvc.presentation.action.protocollo.helper.form;

import it.compit.fenice.model.protocollo.PostaInterna;
import it.compit.fenice.mvc.presentation.actionform.protocollo.VisualizzaProtocolloForm;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.model.protocollo.Protocollo;
import it.finsiel.siged.model.protocollo.ProtocolloIngresso;
import it.finsiel.siged.model.protocollo.ProtocolloUscita;
import it.finsiel.siged.mvc.business.TitoliDestinatarioDelegate;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.presentation.helper.DestinatarioView;
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.AllaccioVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DestinatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloProcedimentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.util.DateUtil;
import it.flosslab.mvc.business.MittentiDelegate;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

public class AggiornaAllaccioForm {

	
	public static void aggiornaPosta(Protocollo protocollo, VisualizzaProtocolloForm form,
			HttpSession session) {
		aggiorna(protocollo, form, session);
		PostaInterna postaInterna = (PostaInterna) protocollo;
		aggiornaMittentePostaForm(postaInterna, form);
		aggiornaDestinatariPostaForm(postaInterna, form);
	}
	
	public static void aggiornaUscita(Protocollo protocollo, VisualizzaProtocolloForm form,
            HttpSession session) {
    	aggiorna(protocollo, form, session);
        ProtocolloUscita protocolloUscita = (ProtocolloUscita) protocollo;
        aggiornaMittenteUscitaForm(protocolloUscita, form);
        aggiornaDestinatariUscitaForm(protocolloUscita, form);
    }
	
	public static void aggiornaIngresso(Protocollo protocollo, VisualizzaProtocolloForm form,
			HttpSession session) {
		aggiorna(protocollo, form, session);
		ProtocolloIngresso protocolloIngresso = (ProtocolloIngresso) protocollo;
		aggiornaMittenteIngressoForm(protocolloIngresso, form);
		aggiornaAssegnatariIngressoForm(protocolloIngresso, form);
	}
	
	 public static void aggiorna(Protocollo protocollo, VisualizzaProtocolloForm form,
	            HttpSession session) {
	        ProtocolloVO protocolloVO = protocollo.getProtocollo();
	        Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
	        form.inizializzaForm();
	        form.setAutore(protocolloVO.getRowCreatedUser());
	        aggiornaDatiGeneraliForm(protocollo.getProtocollo(), form);
	        aggiornaAllacciForm(protocollo, form);
	        aggiornaAllegatiForm(protocollo, form);
	        form.setDocumentoPrincipale(protocollo.getDocumentoPrincipale());
	        aggiornaAnnotazioniForm(protocolloVO, form);
	        aggiornaFascicoloForm(protocollo, form, utente);
	        aggiornaProcedimentoForm(protocollo, form, utente);
	    }
	    

	    private static void aggiornaDatiGeneraliForm(ProtocolloVO protocollo,
	    		VisualizzaProtocolloForm form) {
	        Integer id = protocollo.getId();
	        if (id != null) {
	            form.setProtocolloId(id.intValue());
	            form.setAooId(protocollo.getAooId());
	            form.setDataRegistrazione(DateUtil.formattaDataOra(protocollo
	                    .getDataRegistrazione().getTime()));
	            form.setUfficioProtocollatoreId(protocollo
	                    .getUfficioProtocollatoreId());
	            form.setUtenteProtocollatoreId(protocollo.getCaricaProtocollatoreId());
	           
	            form.setStato(protocollo.getStatoProtocollo());
	        } else {
	            form.setProtocolloId(0);
	        }
	        
	        form.setFlagTipo(protocollo.getFlagTipo());
	        form.setNumero(protocollo.getNumProtocollo());
	        form.setNumProtocolloEmergenza(protocollo.getNumProtocolloEmergenza());
	        form.setStato(protocollo.getStatoProtocollo());
	        form.setNumeroProtocollo(protocollo.getNumProtocollo() + "");
	        form.setTipoDocumentoId(protocollo.getTipoDocumentoId());
	        Date dataDoc = protocollo.getDataDocumento();
	        form.setDataDocumento(dataDoc == null ? null : DateUtil
	                .formattaData(dataDoc.getTime()));
	        Date dataRic = protocollo.getDataRicezione();
	        form.setDataRicezione(dataRic == null ? null : DateUtil
	                .formattaData(dataRic.getTime()));
	        form.setRiservato(protocollo.isRiservato());
	        form.setDocumentoVisibile(!protocollo.isRiservato());
	        form.setOggetto(protocollo.getOggetto());
	        form.setChiaveAnnotazione(protocollo.getChiaveAnnotazione());
	        form.setPosizioneAnnotazione(protocollo.getPosizioneAnnotazione());
	        form.setDescrizioneAnnotazione(protocollo.getDescrizioneAnnotazione());
	        Date dataAnnullamento = protocollo.getDataAnnullamento();
	        form.setDataAnnullamento(dataAnnullamento == null ? null : DateUtil
	                .formattaData(dataAnnullamento.getTime()));
	        form.setProvvedimentoAnnullamento(protocollo
	                .getProvvedimentoAnnullamento());
	        form.setNotaAnnullamento(protocollo.getNotaAnnullamento());
	        form.setVersione(protocollo.getVersione());
	    }

	    private static void aggiornaAllacciForm(Protocollo protocollo, VisualizzaProtocolloForm form) {
	        for (Iterator<AllaccioVO> i = protocollo.getAllacci().iterator(); i.hasNext();) {
	            AllaccioVO allaccio = i.next();
	            form.allacciaProtocollo(allaccio);
	        }
	    }

	    private static void aggiornaAllegatiForm(Protocollo protocollo, VisualizzaProtocolloForm form) {
	        for (Iterator<DocumentoVO> i = protocollo.getAllegati().values().iterator(); i
	                .hasNext();) {
	            DocumentoVO documentoVO =  i.next();
	            form.allegaDocumento(documentoVO);
	        }
	    }

	    private static void aggiornaAnnotazioniForm(ProtocolloVO protocollo,
	    		VisualizzaProtocolloForm form) {
	        form.setDescrizioneAnnotazione(protocollo.getDescrizioneAnnotazione());
	        form.setPosizioneAnnotazione(protocollo.getPosizioneAnnotazione());
	        form.setChiaveAnnotazione(protocollo.getChiaveAnnotazione());
	    }

	    private static void aggiornaFascicoloForm(Protocollo protocollo,
	    		VisualizzaProtocolloForm form, Utente utente) {
	        if (protocollo != null && protocollo.getFascicoli() != null) {
	            for (Iterator<FascicoloVO> i = protocollo.getFascicoli().iterator(); i.hasNext();) {
	                FascicoloVO fascicolo = (FascicoloVO) i.next();
	                form.aggiungiFascicolo(fascicolo);
	            }
	        }
	    }

	    private static void aggiornaProcedimentoForm(Protocollo protocollo,
	    		VisualizzaProtocolloForm form, Utente utente) {
	        if (protocollo != null) {
	            Collection<ProtocolloProcedimentoVO> procedimenti = protocollo.getProcedimenti();
	            if (procedimenti != null) {
	                for (Iterator<ProtocolloProcedimentoVO> i = procedimenti.iterator(); i.hasNext();) {
	                    ProtocolloProcedimentoVO procedimento =i
	                            .next();
	                    form.aggiungiProcedimento(procedimento);
	                }
	            }
	        }
	    }

	    private static void aggiornaMittentePostaForm(PostaInterna protocollo,
	    		VisualizzaProtocolloForm form) {
			AssegnatarioVO mittente = protocollo.getMittente();
			if (mittente != null) {
				Organizzazione org = Organizzazione.getInstance();
				Ufficio uff = org.getUfficio(mittente.getUfficioAssegnatarioId());
				Utente ute = org.getUtente(mittente.getUtenteAssegnatarioId());
				form.setMittenteUscita(newMittente(uff, ute));
			}
		}

		public static AssegnatarioView newMittente(Ufficio ufficio, Utente utente) {
			UfficioVO uffVO = ufficio.getValueObject();
			AssegnatarioView mittente = new AssegnatarioView();
			mittente.setUfficioId(uffVO.getId().intValue());
			mittente.setDescrizioneUfficio(ufficio.getPath());
			mittente.setNomeUfficio(uffVO.getDescription());
			if (utente != null) {
				UtenteVO uteVO = utente.getValueObject();
				CaricaVO carica=utente.getCaricaUfficioVO(uffVO.getId().intValue());
				uteVO.setCarica(carica.getNome());
				mittente.setUtenteId(uteVO.getId().intValue());
				mittente.setNomeUtente(uteVO.getCaricaFullName());
			}
			return mittente;
		}

		private static void aggiornaDestinatariPostaForm(PostaInterna protocollo,
				VisualizzaProtocolloForm form) {
			Organizzazione org = Organizzazione.getInstance();
			for (Iterator<AssegnatarioVO> i = protocollo.getDestinatari().iterator(); i.hasNext();) {
				AssegnatarioVO assegnatario = i.next();
				AssegnatarioView ass = new AssegnatarioView();
				int uffId = assegnatario.getUfficioAssegnatarioId();
				ass.setUfficioId(uffId);
				Ufficio uff = org.getUfficio(uffId);
				if (uff != null) {
					ass.setNomeUfficio(uff.getValueObject().getDescription());
				}
				if (assegnatario.getCaricaAssegnatarioId() != 0) {
					int caricaId = assegnatario.getCaricaAssegnatarioId();
					CaricaVO carica = org.getCarica(caricaId);
					ass.setUtenteId(carica.getUtenteId());
					Utente ute = org.getUtente(carica.getUtenteId());
					ute.getValueObject().setCarica(carica.getNome());
					if (ute != null) {
						ass.setNomeUtente(ute.getValueObject().getCaricaFullName());
					}
				}
				ass.setStato(assegnatario.getStatoAssegnazione());
				form.aggiungiAssegnatario(ass);
				if (assegnatario.isCompetente()) 
					form.setAssegnatarioCompetente(ass.getKey());
			}

		}
	    
	    
	 private static void aggiornaMittenteUscitaForm(ProtocolloUscita protocollo,
	    		VisualizzaProtocolloForm form) {
	        AssegnatarioVO mittente = protocollo.getMittente();
	        if (mittente != null && !"R".equals(protocollo.getProtocollo().getFlagTipo())) {
	            Organizzazione org = Organizzazione.getInstance();
	            Ufficio uff = org.getUfficio(mittente.getUfficioAssegnatarioId());
	            Utente ute = org.getUtente(mittente.getUtenteAssegnatarioId());
	            form.setMittenteUscita(newMittente(uff, ute));
	        }
	    }
	    
	private static void aggiornaMittenteIngressoForm(ProtocolloIngresso protocollo,
			VisualizzaProtocolloForm form) {
		form.getMittenteIngresso().setTipo(protocollo.getProtocollo().getFlagTipoMittente());
		SoggettoVO mittente = null;
		if ("F".equals(protocollo.getProtocollo().getFlagTipoMittente())) {
			mittente = new SoggettoVO('F');
			mittente.setCognome(protocollo.getProtocollo().getCognomeMittente());
			mittente.setNome(protocollo.getProtocollo().getNomeMittente());
		} else if ("M".equals(protocollo.getProtocollo().getFlagTipoMittente())) {

			mittente = new SoggettoVO('M');
			MittentiDelegate delegate = MittentiDelegate.getInstance();
			try {
				form.getMittentiIngresso().clear();
				form.getMittentiIngresso()
						.addAll(
								delegate.getMittenti(protocollo.getProtocollo()
										.getId()));
				form.getStoriaMittentiIngresso().addAll(
						delegate.getStoriaMittenti(protocollo.getProtocollo()
								.getId(), protocollo.getProtocollo()
								.getVersione()));
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (DataException e) {
				e.printStackTrace();
			}

		}
		else {
			mittente = new SoggettoVO('G');
			mittente.setDescrizioneDitta(protocollo.getProtocollo()
					.getDenominazioneMittente());
		}

		form.setNumProtocolloMittente(protocollo.getProtocollo()
				.getNumProtocolloMittente());
		mittente.getIndirizzo().setToponimo(
				protocollo.getProtocollo().getMittenteIndirizzo());
		mittente.getIndirizzo().setComune(
				protocollo.getProtocollo().getMittenteComune());
		mittente.getIndirizzo().setProvinciaId(
				protocollo.getProtocollo().getMittenteProvinciaId());
		mittente.getIndirizzo().setCap(
				protocollo.getProtocollo().getMittenteCap());
		form.setMittenteIngresso(mittente);
	}

	private static void aggiornaDestinatariUscitaForm(ProtocolloUscita protocollo,
			VisualizzaProtocolloForm form) {
		form.resetDestinatari();
        for (Iterator<DestinatarioVO> i = protocollo.getDestinatari().iterator(); i.hasNext();) {
            DestinatarioVO destinatario =  i.next();
            DestinatarioView dest = new DestinatarioView();
            dest.setTitoloId(destinatario.getTitoloId());
            if (destinatario.getTitoloId() > 0) {
                dest.setTitoloDestinatario(TitoliDestinatarioDelegate
                        .getInstance().getTitoloDestinatario(
                                destinatario.getTitoloId()).getDescription());
            }
            dest.setDestinatario(destinatario.getDestinatario());
            dest
                    .setFlagTipoDestinatario(destinatario
                            .getFlagTipoDestinatario());
            if (destinatario.getCitta() != null) {
                dest.setCitta(destinatario.getCitta());
            }

            dest.setEmail(destinatario.getEmail());
            if (destinatario.getCodicePostale() != null) {
                dest.setCapDestinatario(destinatario.getCodicePostale());
            }
            if (destinatario.getIndirizzo() != null) {
                dest.setIndirizzo(destinatario.getIndirizzo());
            }
            dest.setMezzoSpedizioneId(destinatario.getMezzoSpedizioneId());
            dest.setMezzoDesc(destinatario.getMezzoDesc());

            Date dataSped = destinatario.getDataSpedizione();
            if (dataSped != null) {
                dest.setDataSpedizione(DateUtil
                        .formattaData(dataSped.getTime()));
            }
            dest.setFlagConoscenza(destinatario.getFlagConoscenza());
            dest.setFlagPresso(destinatario.getFlagPresso());
            dest.setNote(destinatario.getNote());
            form.aggiungiDestinatario(dest);
        }
    }
	
	private static void aggiornaAssegnatariIngressoForm(ProtocolloIngresso protocollo,
			VisualizzaProtocolloForm form) {
		Organizzazione org = Organizzazione.getInstance();
		for (Iterator<AssegnatarioVO> i = protocollo.getAssegnatari().iterator(); i.hasNext();) {
			AssegnatarioVO assegnatario = i.next();
			AssegnatarioView ass = new AssegnatarioView();
			int uffId = assegnatario.getUfficioAssegnatarioId();
			ass.setUfficioId(uffId);
			Ufficio uff = org.getUfficio(uffId);
			if (uff != null) {
				ass.setNomeUfficio(uff.getValueObject().getDescription());
			}
			if (assegnatario.getCaricaAssegnatarioId() != 0) {
				int caricaId = assegnatario.getCaricaAssegnatarioId();
				CaricaVO carica = org.getCarica(caricaId);
				ass.setUtenteId(carica.getUtenteId());
				Utente ute = org.getUtente(carica.getUtenteId());
				if (ute != null) {
					ute.getValueObject().setCarica(carica.getNome());
					if (carica.isAttivo())
						ass.setNomeUtente(ute.getValueObject().getCaricaFullName());
					else
						ass.setNomeUtente(ute.getValueObject().getCaricaFullNameNonAttivo());
				} else
					ass.setNomeUtente(carica.getNome());
			}
			ass.setStato(assegnatario.getStatoAssegnazione());
			form.aggiungiAssegnatario(ass);
			if (assegnatario.isCompetente()) 
				form.setAssegnatarioCompetente(ass.getKey());
			

		}

	}




	

}
