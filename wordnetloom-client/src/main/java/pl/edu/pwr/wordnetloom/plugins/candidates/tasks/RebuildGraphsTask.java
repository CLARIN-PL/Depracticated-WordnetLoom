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

package pl.edu.pwr.wordnetloom.plugins.candidates.tasks;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pl.edu.pwr.wordnetloom.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.systems.progress.AbstractProgressThread;

@Deprecated
// TODO: rebuild!
public class RebuildGraphsTask extends AbstractProgressThread {
	private PartOfSpeech pos = null;
	private int packageNo = -1;

	private ChangeListener cl;

	public RebuildGraphsTask(PartOfSpeech pos, int packageNo, ChangeListener cl)
	{
		super(null, "Przeliczanie paczki " + packageNo, null, true, true, false);
		this.pos = pos;
		this.packageNo = packageNo;
		this.cl = cl;
		super.start(false);
	}
	
	public void show() {
		progress.setCancel(false);
		progress.setCancelEnable(true);
		progress.setVisible(true);
	}
	
	@Override
	protected void mainProcess() {
		progress.setButtonLabel("Ukryj");
		progress.setGlobalProgressParams(0, 100);
		progress.setProgressParams(0, 100, "Tworzenie grafów");

		boolean finish = false;
		while (!finish) {
//			PkgLockDTO lock = RemoteUtils.pkgLockRemote.dbGet(new Long(packageNo), pos);
//			if (lock == null) {
//				finish = true;
//				break;
//			}
			
			int local = 0;
//			if (lock.getLProgress() != null)
//				local = lock.getLProgress().intValue();
			
			int global = 0;
//			if (lock.getGProgress() != null)
//				global = lock.getGProgress().intValue();

			progress.setProgressValue(local);
			progress.setGlobalProgressValue(global);
			
			if(progress.isCanceled()) {
				progress.setVisible(false);
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
		
		
		cl.stateChanged(new ChangeEvent(this));
	}
	
	public PartOfSpeech getPos() {
		return pos;
	}

	public int getPackageNo() {
		return packageNo;
	}
}
