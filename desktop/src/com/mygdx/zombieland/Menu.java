package com.mygdx.zombieland;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class Menu {

    private static Menu instance;
    private static final int FRAME_WIDTH = 1066;
    private static final int FRAME_HEIGHT = 630;
    private final JFrame gameFrame =  new JFrame();
    private final int START_BUTTON_X = 600;
    private final int START_BUTTON_Y = 200;
    private final int QUIT_BUTTON_X = 600;
    private final int QUIT_BUTTON_Y = 500;
    private final int RULES_BUTTON_X = 600;
    private final int RULES_BUTTON_Y = 300;
    private final int SETTING_BUTTON_X = 600;
    private final int SETTING_BUTTON_Y = 400;
    private Menu(){
        gameFrame.setLayout(null);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton startButton = new JButton();
        JButton quitButton = new JButton();
        JButton rulesButton = new JButton();
        JButton settingButton = new JButton();

        ImageIcon startButtonIconInactive = new ImageIcon("assets/Menu/start_inactive.png");
        ImageIcon startButtonIconActive = new ImageIcon("assets/Menu/start_active.png");

        ImageIcon quitButtonIconInactive = new ImageIcon("assets/Menu/quit_inactive.png");
        ImageIcon quitButtonIconActive = new ImageIcon("assets/Menu/quit_active.png");

        ImageIcon rulesButtonIconInactive = new ImageIcon("assets/Menu/rules_inactive.png");
        ImageIcon rulesButtonIconActive = new ImageIcon("assets/Menu/rules_active.png");

        ImageIcon settingButtonIconInactive = new ImageIcon("assets/Menu/setting_inactive.png");
        ImageIcon settingButtonIconActive = new ImageIcon("assets/Menu/setting_active.png");

        startButton.setIcon(startButtonIconInactive);
        startButton.setBounds(START_BUTTON_X, START_BUTTON_Y,startButtonIconInactive.getIconWidth(),startButtonIconInactive.getIconHeight());

        quitButton.setIcon(quitButtonIconInactive);
        quitButton.setBounds(QUIT_BUTTON_X, QUIT_BUTTON_Y,quitButtonIconInactive.getIconWidth(),quitButtonIconInactive.getIconHeight());

        rulesButton.setIcon(rulesButtonIconInactive);
        rulesButton.setBounds(RULES_BUTTON_X, RULES_BUTTON_Y,rulesButtonIconInactive.getIconWidth(),rulesButtonIconInactive.getIconHeight());

        settingButton.setIcon(settingButtonIconInactive);
        settingButton.setBounds(SETTING_BUTTON_X, SETTING_BUTTON_Y, settingButtonIconInactive.getIconWidth(),settingButtonIconInactive.getIconHeight());


        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                startButton.setIcon(startButtonIconActive);
                startButton.setBounds(START_BUTTON_X, START_BUTTON_Y,startButtonIconActive.getIconWidth(),startButtonIconActive.getIconHeight());

            }

            @Override
            public void mouseExited(MouseEvent e) {
                startButton.setIcon(startButtonIconInactive);
                startButton.setBounds(START_BUTTON_X, START_BUTTON_Y,startButtonIconInactive.getIconWidth(),startButtonIconInactive.getIconHeight());

            }
        });


        quitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                quitButton.setIcon(quitButtonIconActive);
                quitButton.setBounds(QUIT_BUTTON_X, QUIT_BUTTON_Y,quitButtonIconInactive.getIconWidth(),quitButtonIconInactive.getIconHeight());

            }

            @Override
            public void mouseExited(MouseEvent e) {
                quitButton.setIcon(quitButtonIconInactive);
                quitButton.setBounds(QUIT_BUTTON_X, QUIT_BUTTON_Y,quitButtonIconInactive.getIconWidth(),quitButtonIconInactive.getIconHeight());

            }
        });

        rulesButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                rulesButton.setIcon(rulesButtonIconActive);
                rulesButton.setBounds(RULES_BUTTON_X, RULES_BUTTON_Y,rulesButtonIconInactive.getIconWidth(),rulesButtonIconInactive.getIconHeight());

            }

            @Override
            public void mouseExited(MouseEvent e) {
                rulesButton.setIcon(rulesButtonIconInactive);
                rulesButton.setBounds(RULES_BUTTON_X, RULES_BUTTON_Y,rulesButtonIconInactive.getIconWidth(),rulesButtonIconInactive.getIconHeight());
            }
        });

        settingButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                settingButton.setIcon(settingButtonIconActive);
                settingButton.setBounds(SETTING_BUTTON_X, SETTING_BUTTON_Y, settingButtonIconInactive.getIconWidth(),settingButtonIconInactive.getIconHeight());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                settingButton.setIcon(settingButtonIconInactive);
                settingButton.setBounds(SETTING_BUTTON_X, SETTING_BUTTON_Y, settingButtonIconInactive.getIconWidth(),settingButtonIconInactive.getIconHeight());
            }
        });



        startButton.addActionListener(new StartButtonListener());

        quitButton.addActionListener(e -> System.exit(0));

        rulesButton.addActionListener(e -> {
            Rules.getInstance();
            instance.setGameFrameVisible(false);

        });

        settingButton.addActionListener(e->{
            Setting.getInstance();
            instance.setGameFrameVisible(false);
        });
        gameFrame.add(startButton);
        gameFrame.add(quitButton);
        gameFrame.add(rulesButton);
        gameFrame.add(settingButton);

        gameFrame.setSize(FRAME_WIDTH,FRAME_HEIGHT);

        Container c = gameFrame.getContentPane();
        JLabel label = new JLabel();
        label.setIcon(new ImageIcon("assets/Menu/background.png"));
        Dimension size = label.getPreferredSize();
        label.setBounds(0, 0, size.width, size.height);
        c.add(label);


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Calculate the center position
        int centerX = (screenSize.width - gameFrame.getWidth()) / 2;
        int centerY = (screenSize.height - gameFrame.getHeight()) / 2;
        gameFrame.setLocation(centerX, centerY);
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
            Application applicationClass = Application.getInstance();
            applicationClass.setGameVisible(true);
            applicationClass.startApplication();

        }
    }

    public void setGameFrameVisible(boolean state){
        this.gameFrame.setVisible(state);
    }


}