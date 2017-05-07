package speciesClasssification;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Tiago on 5/7/2017.
 */
public class MainView {
    private JLabel reconhecimentoDeEspeciesFlorestaisLabel;
    private JButton listarEspeciesButton;
    private JButton reconhecerEspecieButton;
    private JButton treinarRedeButton;
    private JButton cadastrarEspeciesButton;
    private JButton adcionarFotosButton;
    private JPanel panelMain;

    public MainView() {
        reconhecerEspecieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        listarEspeciesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        treinarRedeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        cadastrarEspeciesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        adcionarFotosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
    }

    public static void main (String args[]){
        JFrame frame = new JFrame("ManView");
        frame.setContentPane(new MainView().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
