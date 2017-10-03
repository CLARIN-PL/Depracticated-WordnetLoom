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

package pl.edu.pwr.wordnetloom.service;

import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import pl.edu.pwr.wordnetloom.dao.DAOBean;
import pl.edu.pwr.wordnetloom.dao.ExtGraphExtensionDAOLocal;
import pl.edu.pwr.wordnetloom.model.ExtGraphExtension;

/**
 * klasa zarządza rozszerzeniami grafów kandydatów
 * 
 * @author lburdka
 */
@Stateless
public class ExtGraphExtensionServiceBean extends DAOBean implements ExtGraphExtensionServiceRemote {
	
	@EJB private ExtGraphExtensionDAOLocal extGraphExtension;
	
	@Override
	public void dbSave(Collection<ExtGraphExtension> exts){
		extGraphExtension.dbSave(exts);
	}
	
	@Override
	public Collection<ExtGraphExtension> dbFullGet() {
		return extGraphExtension.dbFullGet();
	}

	@Override
	public Collection<ExtGraphExtension> dbFullGet(Long[] extgraph_ids) {
		return extGraphExtension.dbFullGet(extgraph_ids);
	}
	
	@Override
	public Collection<ExtGraphExtension> dbFullGet(String word) {
		return extGraphExtension.dbFullGet(word);
	}
	
	@Override
	public Collection<ExtGraphExtension> dbFullGet(String word, int packageno) {
		return extGraphExtension.dbFullGet(word, packageno);
	}
	
	@Override
	public List<ExtGraphExtension> dbGetRelation(List<ExtGraphExtension> exts){
		return extGraphExtension.dbGetRelation(exts);
	}
	
}
