package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.listeners;

import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.ViWordNetService;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views.ViwnGraphView;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * <b>MouseGraphClickListener</b> class provide satellite view
 * (<i>ViWordNetService.satelliteView</i>) refresh after clicking at visualisation
 * view (<i>ViwnGraphViewUI.vv</i>) after any changes in structure of
 * <code>ViwnGraphViewUI</code> this could stop working properly, because of
 * many class castings and strong depending on ui hierarchy<br>
 * </br> Only mouseClicked event is implemented, event mouseEntered was
 * tried, but with clicks work is much more comfortable.
 */
public class MouseGraphClickListener implements MouseListener {

    private final ViWordNetService service;

    public MouseGraphClickListener(ViWordNetService service) {
        this.service = service;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (service != null) {
            try {
                JPanel jp = (JPanel) ((JPanel) e.getSource()).getParent().getParent();
                ViwnGraphView vgv = service.getGraphView(jp);
                service.setSatteliteOwner(vgv);
            } catch (ClassCastException cce) {
                System.out.println(cce.getMessage());
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }
}
