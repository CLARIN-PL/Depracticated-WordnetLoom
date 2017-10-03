/*
    Copyright (C) 2011 Łukasz Jastrzębski, Paweł Koczan, Michał Marcińczuk,
                       Bartosz Broda, Maciej Piasecki, Adam Musiał,
                       Radosław Ramocki, Michał Stanek
    Part of the WordnetLoom

    This program is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the Free
Software Foundation; either version 3 of the License, or (at your option)
any later version.

    This program is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
or FITNESS FOR A PARTICULAR PURPOSE. 

    See the LICENSE and COPYING files for more details.
*/

package pl.edu.pwr.wordnetloom.plugins.owner;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import pl.edu.pwr.wordnetloom.plugins.owner.data.SessionData;
import pl.edu.pwr.wordnetloom.plugins.owner.frames.OwnerFrame;
import pl.edu.pwr.wordnetloom.systems.misc.ActionWrapper;
import pl.edu.pwr.wordnetloom.utils.Labels;
import pl.edu.pwr.wordnetloom.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.systems.ui.MenuItemExt;
import pl.edu.pwr.wordnetloom.utils.Messages;
import pl.edu.pwr.wordnetloom.workbench.abstracts.AbstractService;
import pl.edu.pwr.wordnetloom.workbench.interfaces.Workbench;

/**
 * serwis zajmujący sie obsluga danych uzytkownika
 * @author Max
 */
public class OwnerService extends AbstractService {

	private static final String PARAM_OWNER = "Owner";
	private static final String PARAM_PROJECT = "Project";

	/**
	 * konstruktor serwisu
	 * @param workbench - srodowisko uruchomieniowe
	 */
	public OwnerService(Workbench workbench) {
		super(workbench);
	}

	/**
	 * gdy serwis jest uruchamiany
	 */
	public void onStart() {

		if (workbench.getParam(PARAM_OWNER)==null || workbench.getParam(PARAM_PROJECT)==null) {
			// brak danych o właścicielu, wyświetlenie okienka do wprowadzenia danych
			SessionData data = OwnerFrame.showModal(workbench);
			if (data.owner==null)
				System.exit(0); // nie będziemy działać dalej
			else{
				workbench.setParam(PARAM_OWNER,data.owner);
				workbench.updateOwner();
			}
		}
	}

	/**
	 * instalacja akcji
	 */
	public void installMenuItems() {
		JMenu other = workbench.getMenu(Labels.OTHER);
		if (other==null) return;
		JMenuItem removeItem=new MenuItemExt(Labels.REMOVE_USER_DATA,KeyEvent.VK_U,new ActionWrapper(this,"menuClick_removeUserData"));
		// dodanie na samym koncu
		other.insert(removeItem,other.getItemCount());
	}

	public boolean onClose() { return true; }
	public void installViews() {/***/}

	/**
	 * usuniecie danych uzytkownika
	 */
	public void menuClick_removeUserData() {
		if (DialogBox.showYesNo(Messages.QUESTION_REMOVE_USER_DATA)==DialogBox.YES) {
			workbench.removeParam(PARAM_OWNER); // usuniecie wpisu w danych
			workbench.setVisible(false);    // wylaczenie programu
		}
	}
}
