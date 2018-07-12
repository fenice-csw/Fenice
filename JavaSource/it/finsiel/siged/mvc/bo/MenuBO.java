package it.finsiel.siged.mvc.bo;

import it.finsiel.siged.model.organizzazione.Menu;
import it.finsiel.siged.mvc.presentation.helper.MenuView;

import java.util.Collection;
import java.util.Iterator;

public class MenuBO {

	private static int[] menuAvvocatoGenerale = { 1, 2, 6, 9, 16, 27, 33, 37,
			55, 54, 53, 52, 59, 58, 57, 56, 68, 69, 70, 71, 66, 67, 76, 77, 78,
			79, 72, 73, 74, 75, 85, 84, 87, 86, 81, 80, 83, 82, 88, 102, 103,
			100, 101, 99, 96, 110, 111, 108, 109, 106, 107, 104, 105, 116, 115,
			114, 113, 112, 127, 125, 120 };
	
	public static final int MENU_PROTOCOLLO_USCITA=7;
	
	public static final String UNIQUE_NAME_NOTIFICA_ESITO_COMMITTENTE="result_notification_client";

	public static void aggiungiMenu(String prefix, Menu menu,
			Collection<MenuView> menuLista) {
		for (Iterator<Menu> i = menu.getChildren().iterator(); i.hasNext();) {
			Menu child =  i.next();
			if (child.getChildren().size() == 0) {
				MenuView menuView = new MenuView();
				menuView.setId(child.getValueObject().getId().intValue());
				menuView.setDescrizione(prefix
						+ child.getValueObject().getTitle());
				menuLista.add(menuView);
				
			} else {
				aggiungiMenu(prefix + child.getValueObject().getTitle() + "/",
						child, menuLista);
			}
		}

	}

	public static int[] getMenuAvvocatoGenerale() {
		return menuAvvocatoGenerale;
	}
	
}