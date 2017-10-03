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

package pl.edu.pwr.wordnetloom.systems.tooltips;

import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;

import pl.edu.pwr.wordnetloom.utils.Labels;
import pl.edu.pwr.wordnetloom.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.systems.models.GenericListModel;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

/**
 * List with tooltips and popup menu
 * @author Max
 */
public class ToolTipList extends JList {

	private static final long serialVersionUID = -6298109817976295515L;
	
	/** czas, po którym zniknie wyświetlone okienko po najechaniu myszką na element listy.
	 * Jeżeli chcemy wyświetlić prawie nieskończone okienko przypisujemy wartość Integer.MAX_VALUE*/
	private final int TOOLTIP_DISSMISS_DELAY_IN_MILLIS = Integer.MAX_VALUE;
	
	private ToolTipGeneratorInterface toolTipsGenerator = null;
	final JPopupMenu popupMenu;

	private LoadingCache<Object, String> cache;

	private LoadingCache<Object, String> getCache(){
		return cache;
	}

	class AsyncCacheLoader extends CacheLoader<Object, String> implements FutureCallback<Object>{

		private ListeningExecutorService lex;
		private Component component;

		public AsyncCacheLoader(Component component){
			ThreadPoolExecutor pool = new ThreadPoolExecutor(
					1, 1, Long.MAX_VALUE, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>()
			);
			pool.allowCoreThreadTimeOut(false);
			pool.prestartAllCoreThreads();
			lex = MoreExecutors.listeningDecorator(pool);
			this.component = component;
		}

		@Override
		public String load(final Object o) throws Exception {

			ListenableFuture<String> future = lex.submit(new Callable<String>() {

				@Override
				public String call() throws Exception {
					String result = toolTipsGenerator.getToolTipText(o);

					getCache().invalidate(o);
					getCache().put(o, result);

					return result;
				}
			});
			Futures.addCallback(future, this);

			return Labels.LOADING;
		}

		@Override
		public void onSuccess(final Object o) {

			/**
			 * naprawione odświeżanie się tooltipów po załadowaniu
			 */

			final Point locationOnScreen = MouseInfo.getPointerInfo().getLocation();
			final Point locationOnComponent = new Point(locationOnScreen);
			SwingUtilities.convertPointFromScreen(locationOnComponent,component);
			if(component.contains(locationOnComponent)){
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						ToolTipManager.sharedInstance().mouseMoved(
								new MouseEvent(component, -1, System.currentTimeMillis(),
										0, locationOnComponent.x, locationOnComponent.y,
										locationOnScreen.x, locationOnScreen.y, 0, false, 0));
					}
				});
			}
		}

		@Override
		public void onFailure(Throwable thrown) {
		}

	}

	/**
	 * @param workbench
	 * @param model
	 * @param toolTipsGenerator
	 * @param notepadSupport
	 */
	public ToolTipList(final Workbench workbench,
			final GenericListModel<?> model,
			final ToolTipGeneratorInterface toolTipsGenerator,
			final boolean notepadSupport) {
		super(model);

		/* customise me when needed */
		this.cache = CacheBuilder.newBuilder()
				.maximumSize(200)
				.expireAfterWrite(10, TimeUnit.SECONDS)
				.build(new AsyncCacheLoader(this));

		this.toolTipsGenerator = toolTipsGenerator;
		
		// ustawienie czasu widoczności dla okienek pojawiających się po najechaniu myszką na elementy listy z jednostkami i synsetami
		ToolTipManager.sharedInstance().setDismissDelay(TOOLTIP_DISSMISS_DELAY_IN_MILLIS);

		popupMenu = new JPopupMenu();

		// Add popup menu displayer
		this.addMouseListener(new MouseListener() {

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					// reselect object if only one is selected
					int index = locationToIndex(e.getPoint());
					if (index != -1 && getSelectedIndices().length == 1) {
						setSelectedIndex(index);
					}
					
					popupMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}

			public void mousePressed(MouseEvent e) {
				/***/
			}

			public void mouseExited(MouseEvent e) {
				/***/
			}

			public void mouseEntered(MouseEvent e) {
				/***/
			}

			public void mouseClicked(MouseEvent e) {
				/***/
			}

		});
	}

	/**
	 * @param workbench
	 * @param model
	 * @param toolTipsGenerator
	 */
	public ToolTipList(Workbench workbench, final GenericListModel<?> model,
			ToolTipGeneratorInterface toolTipsGenerator) {
		this(workbench, model, toolTipsGenerator, false);
	}

	/**
	 * Add menu item to standard popup menu
	 * @param item
	 */
	public void addPopupItem(JComponent item) {
		if (popupMenu.getComponentCount() == 1) {
			popupMenu.insert(item, 0);
			popupMenu.insert(new JPopupMenu.Separator(), 1);
		} else {
			popupMenu.add(item);
		}
	}

	/**
	 * Add separator to standard popup menu
	 */
	public void addPopupItemSeparator() {
		addPopupItem(new JPopupMenu.Separator());
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#getToolTipText(java.awt.event.MouseEvent)
	 */
	@Override
	public String getToolTipText(MouseEvent event) {
		// fast check
		if(!toolTipsGenerator.hasEnabledTooltips()){
			return null;
		}

		Point point = event.getPoint();
		int index = locationToIndex(point); // odczytanie indeksu
		
		GenericListModel<?> model = (GenericListModel<?>) getModel(); // odczytanie modelu
		
		if (toolTipsGenerator != null && model != null && index != -1 && this.getCellBounds(index, index).contains(point)) { // czy jest cos na liscie
			final Object item = model.getObjectAt(index);
			if(item == null)
				return null;

			String get = cache.getIfPresent(item);

			if(get == null) {
				try {
					get = cache.get(item);
				} catch (ExecutionException ignore) {}
				return get;
			}
			return get;
		}
		return super.getToolTipText(event); // standardowy tooltip
	}
}
