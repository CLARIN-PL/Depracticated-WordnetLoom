
package pl.edu.pwr.wordnetloom.client.systems.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import org.jdesktop.swingx.JXBusyLabel;

public class BusyGlassPane extends JComponent {

        private static final long serialVersionUID = -6906257101036014488L;
        private final JXBusyLabel busy_label = new JXBusyLabel(new Dimension(70, 70));

        public BusyGlassPane() {
            initializeComponents();
        }
        
        private void initializeComponents(){
            
            this.setBackground(new Color(0f, 0f, 0f, 0.1f));
            this.setLayout(new GridBagLayout());
            busy_label.getBusyPainter().setPoints(20);
            busy_label.getBusyPainter().setTrailLength(9);
            busy_label.getBusyPainter().setHighlightColor(new Color(44, 61, 146).darker());
            busy_label.getBusyPainter().setBaseColor(new Color(168, 204, 241).brighter());
            busy_label.setBusy(true);
            
            this.add(busy_label, new GridBagConstraints());

        }

        @Override
        protected void paintComponent(Graphics g) {
            // enables anti-aliasing
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            // gets the current clipping area
            Rectangle clip = g.getClipBounds();

            // sets a 65% translucent composite
            AlphaComposite alpha = AlphaComposite.SrcOver.derive(0.65f);
            Composite composite = g2.getComposite();
            g2.setComposite(alpha);

            // fills the background
            g2.setColor(getBackground());
            g2.fillRect(clip.x, clip.y, clip.width, clip.height);

            g2.setComposite(composite);
        }
    }