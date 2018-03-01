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
package pl.edu.pwr.wordnetloom.client.systems.misc;

import pl.edu.pwr.wordnetloom.client.systems.listeners.SimpleListenerInterface;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Loggable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * klasa będąca wrapperam na SimpleListenerInterface pozwala używać innych metod
 * z klasy, a nie tylko tej nazwanej "doAction"
 *
 * @author Max
 */
public class SimpleListenerWrapper implements SimpleListenerInterface, Loggable {

    private Method method;
    private Object owner;
    private Object[] args = new Object[2];

    public SimpleListenerWrapper(Object owner, String methodName) {
        this.owner = owner;
        try {
            method = owner.getClass().getMethod(methodName, Object.class, Integer.class);
        } catch (NoSuchMethodException | SecurityException e) {
            logger().error("Trying to call method", e);
        }
    }

    @Override
    public void doAction(Object object, int tag) {
        try {
            args[0] = object;
            args[1] = tag;
            method.invoke(owner, args); // wywołanie metody zastępczej
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            logger().error("Trying to call doAction", e);
        }
    }
}
