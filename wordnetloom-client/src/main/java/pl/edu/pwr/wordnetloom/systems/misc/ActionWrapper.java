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

package pl.edu.pwr.wordnetloom.systems.misc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import pl.edu.pwr.wordnetloom.systems.listeners.SimpleListenerInterface;

/**
 * klasa będąca wrapperam na ActioListener oraz SimpleListenerInterface pozwala
 * używać innych metod z klasy, a nie tylko tej nazwanej "actionPerformed" czy "doAction"
 * metody nie maja parametrow, a wiec argumenty wywolania sa tracone 
 *
 * przykład: testButton.addActionListener(new ActionWrapper(this,"testButton_click"));
 * zamiast:  testButton.addActionListener(this);
 * gdzie:    testButton_click to nazwa metody, ktora ma zostac uzyta
 * @author Max
 *
 */
// cała ta klasa to jeden wielki syf do skasowania, za dużo porętnej refleksji która będzie tylko przyciemniać kod.
public class ActionWrapper implements ActionListener, SimpleListenerInterface {
	private Method method=null; // metoda do wywołania
	private Object owner=null;  // obiekt do którego należy metoda
	private Object[] args=new Object[0]; // tablica z argumentami, aby nie tworzyc jej za każdym razem
	private final String	methodName;
	
	/**
	 * konstruktor
	 * @param owner - obiekt z którego pochodzi metoda zastępująca actionListener
	 * @param methodName - nazwa metody zastępująca actionListener
	 */
	public ActionWrapper(Object owner,String methodName) {
		this.owner=owner;
		this.methodName = methodName;
		try { // odczytanie metody zastępczej
			this.method = owner.getClass().getMethod(methodName, new Class[0]);
		} catch (Exception e) {
			Logger.getLogger(ActionWrapper.class).log(Level.ERROR, "Trying to call method" + e);
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		try {
			method.invoke(owner,args); // wywołanie metody zastępczej
		} catch (Exception e) {
			System.err.println("Problem invoking method: " + methodName + " in object: " + owner);
			Logger.getLogger(ActionWrapper.class).log(Level.ERROR, "Trying to call method" + e);
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see SimpleListenerInterface#doAction(java.lang.Object, int)
	 */
	public void doAction(Object object, int tag) {
		try {
			method.invoke(owner,args); // wywołanie metody zastępczej
		} catch (Exception e) {
			System.err.println("Problem invoking method: " + methodName + " in object: " + owner);
			Logger.getLogger(ActionWrapper.class).log(Level.ERROR, "Trying to call method" + e);
		}
	}
}
