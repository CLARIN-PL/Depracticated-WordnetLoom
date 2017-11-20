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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * klasa będąca wrapperam na ActioListener oraz SimpleListenerInterface pozwala
 * używać innych metod z klasy, a nie tylko tej nazwanej "actionPerformed" czy
 * "doAction" metody nie maja parametrow, a wiec argumenty wywolania sa tracone
 * <p>
 * przykład: testButton.addActionListener(new
 * ActionWrapper(this,"testButton_click")); zamiast:
 * testButton.addActionListener(this); gdzie: testButton_click to nazwa metody,
 * ktora ma zostac uzyta
 *
 * @author Max
 */
// cała ta klasa to jeden wielki syf do skasowania, za dużo porętnej refleksji która będzie tylko przyciemniać kod.
public class ActionWrapper implements ActionListener, SimpleListenerInterface, Loggable {

    private Method method;
    private Object owner;
    private Object[] args = new Object[0];
    private final String methodName;

    public ActionWrapper(Object owner, String methodName) {
        this.owner = owner;
        this.methodName = methodName;
        try {
            method = owner.getClass().getMethod(methodName, new Class[0]);
        } catch (NoSuchMethodException | SecurityException e) {
            logger().error("Trying to call method", e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        try {
            method.invoke(owner, args); // wywołanie metody zastępczej
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            System.err.println("Problem invoking method: " + methodName + " in object: " + owner);
            logger().error("Trying to call method", e);
        }
    }

    @Override
    public void doAction(Object object, int tag) {
        try {
            method.invoke(owner, args); // wywołanie metody zastępczej
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            logger().error("Trying to call method", e);
        }
    }
}
