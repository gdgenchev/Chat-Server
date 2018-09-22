package bg.chat.client.view.CustomComponents;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JLabel;

public class SelfClosingDialog {
    private JDialog dialog;
    private int ms;

    public SelfClosingDialog(String message, String title, int ms) {
        JOptionPane jop = new JOptionPane();
        jop.setMessageType(JOptionPane.PLAIN_MESSAGE);
        jop.setMessage(message);
        dialog = jop.createDialog(null, title);
        changeColor(dialog.getComponents());
        this.ms = ms;
    }

    private void changeColor(Component[] comp) {
        for (Component aComp : comp) {
            if (aComp instanceof Container) {
                changeColor(((Container) aComp).getComponents());
            }
            if (aComp instanceof JLabel) {
                aComp.setForeground(new Color(187, 0, 96));
            } else if (aComp instanceof JButton) {
                aComp.setBackground(new Color(187, 83, 132));
                aComp.setForeground(new Color(255, 255, 255));
            } else {
                aComp.setBackground(new Color(255, 231, 248));
            }
        }
    }

    public void showDialogAndClose() {
        new Thread(() -> {
            try {
                Thread.sleep(ms);
            } catch (Exception ignored) {

            }
            dialog.dispose();
        }).start();
        dialog.setVisible(true);
    }
}
