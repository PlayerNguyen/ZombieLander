package com.mygdx.zombieland;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class Setting {
    private static Setting instance;
    private static final int FRAME_WIDTH = 1066;
    private static final int FRAME_HEIGHT = 630;
    private final JFrame gameFrame =  new JFrame();
    private final int RETURN_BUTTON_X = 333;
    private final int RETURN_BUTTON_Y = 500;

    private Setting(){
        gameFrame.setLayout(null);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton returnButton = new JButton();

        ImageIcon returnButtonIconInactive = new ImageIcon("assets/Menu/return_inactive.png");
        ImageIcon returnButtonIconActive = new ImageIcon("assets/Menu/return_active.png");


        returnButton.setIcon(returnButtonIconInactive);
        returnButton.setBounds(RETURN_BUTTON_X, RETURN_BUTTON_Y,returnButtonIconInactive.getIconWidth(),returnButtonIconInactive.getIconHeight());

        returnButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                returnButton.setIcon(returnButtonIconActive);
                returnButton.setBounds(RETURN_BUTTON_X, RETURN_BUTTON_Y,returnButtonIconInactive.getIconWidth(),returnButtonIconInactive.getIconHeight());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                returnButton.setIcon(returnButtonIconInactive);
                returnButton.setBounds(RETURN_BUTTON_X, RETURN_BUTTON_Y,returnButtonIconInactive.getIconWidth(),returnButtonIconInactive.getIconHeight());
            }
        });

        returnButton.addActionListener(e -> {
            instance.setGameFrameVisible(false);
            Menu menu = Menu.getInstance();
            menu.setGameFrameVisible(true);
        });

        gameFrame.add(returnButton);

        gameFrame.setSize(FRAME_WIDTH,FRAME_HEIGHT);

        Container c = gameFrame.getContentPane();
        JLabel label = new JLabel();
        label.setIcon(new ImageIcon("assets/Menu/setting.jpg"));
        Dimension size = label.getPreferredSize();
        label.setBounds(0, 0, size.width, size.height);
        c.add(label);
        gameFrame.setVisible(true);
        gameFrame.setResizable(false);
    }

    public static Setting getInstance() {
        if(instance == null){
            instance = new Setting();
        }
        else{
            instance.setGameFrameVisible(true);
        }
        return instance;
    }

    public void setGameFrameVisible(boolean state){
        this.gameFrame.setVisible(state);
    }

}