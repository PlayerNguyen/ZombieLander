package com.mygdx.zombieland;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public final class Menu {

    private static Menu instance;
    private static final int FRAME_WIDTH = 1066;
    private static final int FRAME_HEIGHT = 630;
    private JFrame gameFrame =  new JFrame();
    private final int startButtonX = 600;
    private final int startButtonY = 300;

    private Menu(){
        gameFrame.setLayout(null);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton startButton = new JButton();
        ImageIcon startButtonIconInactive = new ImageIcon("assets/Menu/start_inactive.png");
        ImageIcon startButtonIconActive = new ImageIcon("assets/Menu/start_active.png");

        startButton.setIcon(startButtonIconInactive);
        startButton.setBounds(startButtonX,startButtonY,startButtonIconInactive.getIconWidth(),startButtonIconInactive.getIconHeight());

        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                startButton.setIcon(startButtonIconActive);
                startButton.setBounds(startButtonX,startButtonY,startButtonIconActive.getIconWidth(),startButtonIconActive.getIconHeight());

            }

            @Override
            public void mouseExited(MouseEvent e) {
                startButton.setIcon(startButtonIconInactive);
                startButton.setBounds(startButtonX,startButtonY,startButtonIconInactive.getIconWidth(),startButtonIconInactive.getIconHeight());

            }
        });



        startButton.addActionListener(new StartButtonListener());

        gameFrame.add(startButton);
        gameFrame.setSize(FRAME_WIDTH,FRAME_HEIGHT);

        Container c = gameFrame.getContentPane();
        JLabel label = new JLabel();
        label.setIcon(new ImageIcon("assets/Menu/My project.png"));
        Dimension size = label.getPreferredSize();
        label.setBounds(0, 0, size.width, size.height);
        c.add(label);
        gameFrame.setVisible(true);
        gameFrame.setResizable(false);
    }

    public static Menu getInstance() {
        if(instance == null){
            instance = new Menu();
        }
        return instance;
    }

    static class StartButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Menu menu = Menu.getInstance();
            menu.setGameFrameVisible(false);
            ApplicationClass applicationClass = ApplicationClass.getInstance();
            applicationClass.setGameVisible(true);
            applicationClass.startApplication();

        }
    }

    public void setGameFrameVisible(boolean state){
        this.gameFrame.setVisible(state);
    }

}