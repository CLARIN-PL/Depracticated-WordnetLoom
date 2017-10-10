package pl.edu.pwr.wordnetloom.systems.ui;

import javax.swing.*;
import java.awt.*;

public class ToastMessage extends JDialog {

    public static final int SHORT_MESSAGE = 2000;
    public static final int LONG_MESSGE = 7000;

    /** Czas wyświetlania informacji wyrażony w milisekundach */
    private int durationInMillis;
    private JLabel toastLabel;

    public void setTextColor(Color color)
    {
        toastLabel.setForeground(color);
    }

    public ToastMessage(String toastText, int durationInMillis, Component owner){
        this.durationInMillis = durationInMillis;
        init(toastText);
        setPosition(owner);
    }

    public ToastMessage(String toastText, int durationInMillis, Point location)
    {
        this.durationInMillis = durationInMillis;
        init(toastText);
        setPosition(location);
    }

    public ToastMessage(final String toastText, final int durationInMillis, final Component owner, final Point locationOffset)
    {
        this.durationInMillis = durationInMillis;
        init(toastText);
        setPosition(owner, locationOffset);
    }

    private void setPosition(Point position)
    {
        setLocation(position);
    }

    private void setPosition(Component owner)
    {
        setLocationRelativeTo(owner);
    }

    /** Metoda ustawia okienko w pozycji oddalonej o odpowiednia odległość od penwego komponentu
     *
     * @param owner komponent, względem którego będzie ustawiana pozycja
     * @param offsetPosition przesunięcie pozycji
     */
    private void setPosition(Component owner, Point offsetPosition)
    {
        setLocationRelativeTo(owner);
        Point currentLocation = getLocation();
        currentLocation.x += offsetPosition.x;
        currentLocation.y += offsetPosition.y;
        setLocation(currentLocation);
    }

    private void init(final String toastText)
    {
        setUndecorated(true);
        getRootPane().setOpaque(false);
        getContentPane().setLayout(new BorderLayout(0,0));

        JPanel panel = new JPanel();
        panel.setBackground(Color.GRAY);
        getContentPane().add(panel, BorderLayout.CENTER);

        toastLabel = new JLabel("");
        toastLabel.setText(toastText);
        toastLabel.setFont(new Font("Dialog", Font.BOLD, 10));

        setBounds(100, 100, toastLabel.getPreferredSize().width+20, 31);

        setAlwaysOnTop(true);
        panel.add(toastLabel);
        setVisible(false);
    }

    /** Pokazuje okienko z informacją, która zniknie po określonym czasie */
    public void showMessage()
    {
        setVisible(true);
        new Thread(() -> {
            try {
                Thread.sleep(durationInMillis);
                dispose();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
