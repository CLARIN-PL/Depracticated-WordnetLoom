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

import java.lang.reflect.Method;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import pl.edu.pwr.wordnetloom.systems.listeners.SimpleListenerInterface;

/**
 * klasa będąca wrapperam na SimpleListenerInterface pozwala
 * używać innych metod z klasy, a nie tylko tej nazwanej "doAction" 
 *
 * @author Max
 *
 */
public class SimpleListenerWrapper implements SimpleListenerInterface {
	private Method method=null; // metoda do wywołania
	private Object owner=null;  // obiekt do którego należy metoda
	private Object[] args=new Object[2]; // tablica z argumentami, aby nie tworzyc jej za każdym razem
	
	/**
	 * konstruktor
	 * @param owner - obiekt z którego pochodzi metoda zastępująca actionListener
	 * @param methodName - nazwa metody zastępująca actionListener
	 */
	public SimpleListenerWrapper(Object owner,String methodName) {
		this.owner=owner;
		try { // odczytanie metody zastępczej
			this.method = owner.getClass().getMethod(methodName, new Class[]{Object.class,Integer.class});
		} catch (Exception e) {
			Logger.getLogger(SimpleListenerWrapper.class).log(Level.ERROR, "Trying to call method" + e);
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see SimpleListenerInterface#doAction(java.lang.Object, int)
	 */
	public void doAction(Object object, int tag) {
		try {
			args[0]=object;
			args[1]=new Integer(tag);
			method.invoke(owner,args); // wywołanie metody zastępczej
		} catch (Exception e) {
			Logger.getLogger(SimpleListenerWrapper.class).log(Level.ERROR, "Trying to call doAction" + e);
		}
	}
}
