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

package pl.edu.pwr.wordnetloom.plugins.viwordnet.views;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.commons.collections15.Predicate;

import pl.edu.pwr.wordnetloom.plugins.viwordnet.structure.ViwnEdge;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.structure.ViwnEdgeCand;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.visualization.decorators.ViwnSatelliteEdgeTransformer;
import pl.edu.pwr.wordnetloom.workbench.abstracts.AbstractViewUI;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.structure.ViwnNode;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.visualization.decorators.RootNodeLabeller;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.visualization.decorators.ViwnSatelliteNodeSizeTransformer;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.visualization.decorators.ViwnSatelliteNodeStrokeTransformer;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.visualization.decorators.ViwnSatelliteNodeToolTip;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.visualization.renderers.ViwnVertexFillColor;
import se.datadosen.component.RiverLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.LayoutScalingControl;
import edu.uci.ics.jung.visualization.control.SatelliteVisualizationViewer;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;

/**
 * @author amusial
 * 
 */
public class ViwnSatelliteGraphViewUI extends AbstractViewUI {

	protected VisualizationViewer<ViwnNode, ViwnEdge> satellite;

	protected VisualizationViewer<ViwnNode, ViwnEdge> vv;

	protected ViwnGraphViewUI graphUI;

	protected ScalingControl satelliteScaler = new LayoutScalingControl();

	protected JPanel rootPanel;

	private final static int WIDTH = 250;
	private final static int HEIGHT = 200;
	/**
	 * @param graphUI
	 *            ViwnGraphViewUI
	 */
	public ViwnSatelliteGraphViewUI(ViwnGraphViewUI graphUI) {
		set(graphUI);
	}

	/**
	 * after that constructor ui is not ready
	 * */
	protected ViwnSatelliteGraphViewUI() {
		/**/
	}

	/**
	 * @param vgvui
	 *            ViwnGraphViewUI
	 * */
	public void set(ViwnGraphViewUI vgvui) {

		this.graphUI = vgvui;

		this.vv = graphUI.getVisualizationViewer();

		satellite = new SatelliteVisualizationViewer<ViwnNode, ViwnEdge>(vv,
				new Dimension(WIDTH, HEIGHT));
		satellite.getRenderContext().setEdgeDrawPaintTransformer(
				new PickableEdgePaintTransformer<ViwnEdge>(satellite
						.getPickedEdgeState(), Color.black, Color.cyan));

		satellite.getRenderContext().setVertexFillPaintTransformer(
				new ViwnVertexFillColor(vv.getPickedVertexState(), graphUI
						.getRootNode()));
		satellite.getRenderContext().setEdgeShapeTransformer(
				new EdgeShape.Line<ViwnNode, ViwnEdge>());
		
		// rescale
		satellite.scaleToLayout(satelliteScaler);

	}

	@Override
	public JComponent getRootComponent() {
		return null;
	}

	@Override
	protected void initialize(JPanel content) {
		rootPanel = content;
		content.setLayout(new RiverLayout());

		// Create and add a panel for graph visualization.
		content.add(getSatelliteGraphViewer(), "hfill vfill");
	}

	private JPanel getSatelliteGraphViewer() {

		JPanel panel = new JPanel();
		panel.add(satellite, "hfill vfill");
		return panel;

	}

	/**
	 * refresh satellite view
	 * 
	 * TODO: do it better than recreating satellite, probably wont fix TODO:
	 * graph should be centered in the view, centered at the center of the view
	 * not at the root or selected node, probably done, need testing TODO: graph
	 * should be resized to fill full size of view, something is wrong when
	 * graph scale is >1, fixed by double scaling
	 */
	public void refreshViewUI() {
		// System.out.println("refresh satellite view UI");
		// remove old satellite panel
		rootPanel.removeAll();
		// to get dimension of parent
		rootPanel.repaint();

		// refresh satellite
		refreshSatellite();

		// scale
		// if layout was scaled, scale it to it original size
		if (vv.getRenderContext().getMultiLayerTransformer().getTransformer(
				Layer.LAYOUT).getScaleX() > 1D) {
			(new LayoutScalingControl()).scale(satellite,
					(1f / (float) satellite.getRenderContext()
							.getMultiLayerTransformer().getTransformer(
									Layer.LAYOUT).getScaleX()),
					new Point2D.Double());
		}
		// get view bounds
		Dimension vd = satellite.getPreferredSize();
		if (satellite.isShowing()) {
			vd = satellite.getSize();
		}
		// get graph layout size
		Dimension ld = satellite.getGraphLayout().getSize();
		// finally scale it if view bounds are different than graph layer bounds
		if (vd.equals(ld) == false) {
			float heightRatio = (float) (vd.getWidth() / ld.getWidth()), widthRatio = (float) (vd
					.getHeight() / ld.getHeight());

			satelliteScaler.scale(satellite,
					(heightRatio < widthRatio ? heightRatio : widthRatio),
					new Point2D.Double());
		}

		// center
		Point2D q = new Point2D.Double(satellite.getGraphLayout().getSize()
				.getWidth() / 2, satellite.getGraphLayout().getSize()
				.getHeight() / 2);
		Point2D lvc = satellite.getRenderContext().getMultiLayerTransformer()
				.inverseTransform(satellite.getCenter());
		// vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW).translate(lvc.getX()
		// - q.getX(), lvc.getY() - q.getY());
		satellite.getRenderContext().getMultiLayerTransformer().getTransformer(
				Layer.LAYOUT).translate(lvc.getX() - q.getX(),
				lvc.getY() - q.getY());

		// Create and add a panel for graph visualization.
		rootPanel.add(getSatelliteGraphViewer(), "hfill vfill");
		
		// force repaint of the view 
		rootPanel.getParent().repaint();
	}

	
	/**
	 * refresh satellite
	 * */
	private void refreshSatellite() {
		
		// refresh satellite view
		satellite = new SatelliteVisualizationViewer<ViwnNode, ViwnEdge>(vv,
				new Dimension(WIDTH, HEIGHT));
//		satellite = new SatelliteVisualizationViewer<ViwnNode, ViwnEdge>(vv,
//				rootPanel.getSize());
		
		// set vertex shape transformer
		satellite.getRenderContext().setVertexShapeTransformer(
				new ViwnSatelliteNodeSizeTransformer());
		
		// set vertex fill paint
		satellite.getRenderContext().setVertexFillPaintTransformer(
				new ViwnVertexFillColor(vv.getPickedVertexState(), graphUI
						.getRootNode()));
		
		// set vertex stroke (its vertex border) transformer
		satellite.getRenderContext().setVertexStrokeTransformer(
				new ViwnSatelliteNodeStrokeTransformer());
		
		// set vertex labeler to generate root node label
		satellite.getRenderContext().setVertexLabelTransformer(new RootNodeLabeller());
		
		// set edge stroke (set width) transformer
		satellite.getRenderContext().setEdgeStrokeTransformer(
				new ViwnSatelliteEdgeTransformer());
		
		// set edge draw
		satellite.getRenderContext().setEdgeDrawPaintTransformer(
				new PickableEdgePaintTransformer<ViwnEdge>(satellite
						.getPickedEdgeState(), Color.black, Color.cyan));
		
		// set edge shape to line
		satellite.getRenderContext().setEdgeShapeTransformer(
				new EdgeShape.Line<ViwnNode, ViwnEdge>());
		
		// set edge arrow predicate
		satellite.getRenderContext().setEdgeArrowPredicate(
				new ViwnSatelliteEdgeTransformer());
		
		satellite.getRenderContext().setEdgeIncludePredicate(new Predicate<Context<Graph<ViwnNode,ViwnEdge>,ViwnEdge>>() {
			public boolean evaluate(Context<Graph<ViwnNode, ViwnEdge>, ViwnEdge> context) {
				if (context.element instanceof ViwnEdgeCand) {
					ViwnEdgeCand cand = (ViwnEdgeCand)context.element;
					return !cand.isHidden();
				}
				return true;
			}
		});
		
		// set tool tips
		satellite.setVertexToolTipTransformer(new ViwnSatelliteNodeToolTip<ViwnEdge>());
		
		// add mouse listener to focus view at click point
		satellite.addMouseListener(new MouseListener() {
			public void mousePressed(MouseEvent e) {
				Point2D q = satellite.getRenderContext().getMultiLayerTransformer()
						.inverseTransform(new Point2D.Double(e.getX(),e.getY()));
				Point2D lvc = vv.getRenderContext().getMultiLayerTransformer()
						.inverseTransform(vv.getCenter());
				vv.getRenderContext().getMultiLayerTransformer().getTransformer(
						Layer.LAYOUT).translate(lvc.getX() - q.getX(),
						lvc.getY() - q.getY());
			}
			public void mouseReleased(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {}
		});
	}
	
	/**
	 * use for testing only
	 * 
	 * @param vv
	 *            visualization viewer for this satellite visualization viewer
	 */
	public static void ShowSatelliteView(
			VisualizationViewer<ViwnNode, ViwnEdge> vv) {

		SatelliteVisualizationViewer<ViwnNode, ViwnEdge> satellite = new SatelliteVisualizationViewer<ViwnNode, ViwnEdge>(
				vv, new Dimension(WIDTH, HEIGHT));
		satellite.getRenderContext().setEdgeDrawPaintTransformer(
				new PickableEdgePaintTransformer<ViwnEdge>(satellite
						.getPickedEdgeState(), Color.black, Color.cyan));
		satellite.getRenderContext().setVertexFillPaintTransformer(
				new PickableVertexPaintTransformer<ViwnNode>(satellite
						.getPickedVertexState(), Color.red, Color.yellow));

		ScalingControl satelliteScaler = new CrossoverScalingControl();
		satellite.scaleToLayout(satelliteScaler);

		JFrame frame = new JFrame();
		Container content = frame.getContentPane();
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		satellite.scaleToLayout(new CrossoverScalingControl());

		content.add(satellite);

		frame.setSize(WIDTH, HEIGHT);
		frame.setVisible(true);

	}

}
