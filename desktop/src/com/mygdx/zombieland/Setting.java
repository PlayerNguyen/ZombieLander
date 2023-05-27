package com.mygdx.zombieland;

import com.mygdx.zombieland.setting.GameSetting;

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
    private static final JFrame gameFrame =  new JFrame();
    private final int RETURN_BUTTON_X = 333;
    private final int RETURN_BUTTON_Y = 500;
    private final int MUSIC_PLUS_BUTTON_X = 597;
    private final int MUSIC_PLUS_BUTTON_Y = 212;
    private final int MUSIC_MINUS_BUTTON_X = 469;
    private final int MUSIC_MINUS_BUTTON_Y = 212;
    private final int SFX_PLUS_BUTTON_X = 597;
    private final int SFX_PLUS_BUTTON_Y = 372;
    private final int SFX_MINUS_BUTTON_X = 469;
    private final int SFX_MINUS_BUTTON_Y = 372;
    private final GameSetting gameSetting = GameSetting.getInstance();

    private Setting(){
        gameFrame.setLayout(null);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel music = new JLabel(String.valueOf((int)(gameSetting.getMusicSoundLevel()*5)));
        music.setFont(new Font("Ariel", Font.BOLD, 50));
        music.setForeground(Color.YELLOW);
        Dimension musicSize = music.getPreferredSize();
        music.setBounds((FRAME_WIDTH-musicSize.width)/2, 220, musicSize.width, musicSize.height);

        JLabel sfx = new JLabel(String.valueOf((int)(gameSetting.getVfxSoundLevel()*5)));
        sfx.setFont(new Font("Ariel", Font.BOLD, 50));
        sfx.setForeground(Color.YELLOW);
        Dimension sfxSize = music.getPreferredSize();
        sfx.setBounds((FRAME_WIDTH-sfxSize.width)/2, 380, sfxSize.width, sfxSize.height);

        JButton returnButton = new JButton();
        JButton musicPlusButton = new JButton();
        JButton musicMinusButton = new JButton();
        JButton sfxPlusButton = new JButton();
        JButton sfxMinusButton = new JButton();

        ImageIcon returnButtonIconInactive = new ImageIcon("assets/Menu/return_inactive.png");
        ImageIcon returnButtonIconActive = new ImageIcon("assets/Menu/return_active.png");
        ImageIcon musicPlusButtonIconInactive = new ImageIcon("assets/Menu/plus_inactive.png");
        ImageIcon musicPlusButtonIconActive = new ImageIcon("assets/Menu/plus_active.png");
        ImageIcon musicMinusButtonIconInactive = new ImageIcon("assets/Menu/minus_inactive.png");
        ImageIcon musicMinusButtonIconActive = new ImageIcon("assets/Menu/minus_active.png");
        ImageIcon sfxPlusButtonIconInactive = new ImageIcon("assets/Menu/plus_inactive.png");
        ImageIcon sfxPlusButtonIconActive = new ImageIcon("assets/Menu/plus_active.png");
        ImageIcon sfxMinusButtonIconInactive = new ImageIcon("assets/Menu/minus_inactive.png");
        ImageIcon sfxMinusButtonIconActive = new ImageIcon("assets/Menu/minus_active.png");

        returnButton.setIcon(returnButtonIconInactive);
        returnButton.setBounds(RETURN_BUTTON_X, RETURN_BUTTON_Y,returnButtonIconInactive.getIconWidth(),returnButtonIconInactive.getIconHeight());

        musicPlusButton.setIcon(musicPlusButtonIconInactive);
        musicPlusButton.setBounds(MUSIC_PLUS_BUTTON_X, MUSIC_PLUS_BUTTON_Y, musicPlusButtonIconInactive.getIconWidth(),musicPlusButtonIconInactive.getIconHeight());

        musicMinusButton.setIcon(musicMinusButtonIconInactive);
        musicMinusButton.setBounds(MUSIC_MINUS_BUTTON_X-musicMinusButtonIconInactive.getIconWidth(), MUSIC_MINUS_BUTTON_Y, musicPlusButtonIconInactive.getIconWidth(),musicPlusButtonIconInactive.getIconHeight());

        sfxPlusButton.setIcon(sfxPlusButtonIconInactive);
        sfxPlusButton.setBounds(SFX_PLUS_BUTTON_X, SFX_PLUS_BUTTON_Y, sfxPlusButtonIconInactive.getIconWidth(), sfxPlusButtonIconInactive.getIconHeight());

        sfxMinusButton.setIcon(sfxMinusButtonIconInactive);
        sfxMinusButton.setBounds(SFX_MINUS_BUTTON_X-sfxMinusButtonIconInactive.getIconWidth(), SFX_MINUS_BUTTON_Y, sfxMinusButtonIconInactive.getIconWidth(),sfxMinusButtonIconInactive.getIconHeight());


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

        musicPlusButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                musicPlusButton.setIcon(musicPlusButtonIconActive);
                musicPlusButton.setBounds(MUSIC_PLUS_BUTTON_X, MUSIC_PLUS_BUTTON_Y, musicPlusButtonIconInactive.getIconWidth(),musicPlusButtonIconInactive.getIconHeight());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                musicPlusButton.setIcon(musicPlusButtonIconInactive);
                musicPlusButton.setBounds(MUSIC_PLUS_BUTTON_X, MUSIC_PLUS_BUTTON_Y, musicPlusButtonIconInactive.getIconWidth(),musicPlusButtonIconInactive.getIconHeight());
            }
        });

        musicMinusButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                musicMinusButton.setIcon(musicMinusButtonIconActive);
                musicMinusButton.setBounds(MUSIC_MINUS_BUTTON_X-musicMinusButtonIconInactive.getIconWidth(), MUSIC_MINUS_BUTTON_Y, musicPlusButtonIconInactive.getIconWidth(),musicPlusButtonIconInactive.getIconHeight());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                musicMinusButton.setIcon(musicMinusButtonIconInactive);
                musicMinusButton.setBounds(MUSIC_MINUS_BUTTON_X-musicMinusButtonIconInactive.getIconWidth(), MUSIC_MINUS_BUTTON_Y, musicPlusButtonIconInactive.getIconWidth(),musicPlusButtonIconInactive.getIconHeight());

            }
        });


        sfxPlusButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                sfxPlusButton.setIcon(sfxPlusButtonIconActive);
                sfxPlusButton.setBounds(SFX_PLUS_BUTTON_X, SFX_PLUS_BUTTON_Y, sfxPlusButtonIconInactive.getIconWidth(), sfxPlusButtonIconInactive.getIconHeight());

            }

            @Override
            public void mouseExited(MouseEvent e) {
                sfxPlusButton.setIcon(sfxPlusButtonIconInactive);
                sfxPlusButton.setBounds(SFX_PLUS_BUTTON_X, SFX_PLUS_BUTTON_Y, sfxPlusButtonIconInactive.getIconWidth(), sfxPlusButtonIconInactive.getIconHeight());

            }
        });

        sfxMinusButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                sfxMinusButton.setIcon(sfxMinusButtonIconActive);
                sfxMinusButton.setBounds(SFX_MINUS_BUTTON_X-sfxMinusButtonIconInactive.getIconWidth(), SFX_MINUS_BUTTON_Y, sfxMinusButtonIconInactive.getIconWidth(),sfxMinusButtonIconInactive.getIconHeight());

            }

            @Override
            public void mouseExited(MouseEvent e) {
                sfxMinusButton.setIcon(sfxMinusButtonIconInactive);
                sfxMinusButton.setBounds(SFX_MINUS_BUTTON_X-sfxMinusButtonIconInactive.getIconWidth(), SFX_MINUS_BUTTON_Y, sfxMinusButtonIconInactive.getIconWidth(),sfxMinusButtonIconInactive.getIconHeight());

            }
        });

        returnButton.addActionListener(e -> {
            instance.setGameFrameVisible(false);
            Menu menu = Menu.getInstance();
            menu.setGameFrameVisible(true);
        });

        musicPlusButton.addActionListener(e -> {
            int level = (int)(gameSetting.getMusicSoundLevel()*5);
            if(level != 5){
                gameSetting.setMusicSoundLevel(gameSetting.getMusicSoundLevel()+0.2f);
                music.setText(String.valueOf((int)(gameSetting.getMusicSoundLevel()*5)));
            }
        });

        musicMinusButton.addActionListener(e -> {
            int level = (int)(gameSetting.getMusicSoundLevel()*5);
            if(level != 0){
                gameSetting.setMusicSoundLevel(gameSetting.getMusicSoundLevel()-0.2f);
                music.setText(String.valueOf((int)(gameSetting.getMusicSoundLevel()*5)));
            }
        });

        sfxPlusButton.addActionListener(e -> {
            int level = (int)(gameSetting.getVfxSoundLevel()*5);
            if(level != 5){
                gameSetting.setVfxSoundLevel(gameSetting.getVfxSoundLevel()+0.2f);
                sfx.setText(String.valueOf((int)(gameSetting.getVfxSoundLevel()*5)));
            }
        });

        sfxMinusButton.addActionListener(e -> {
            int level = (int)(gameSetting.getVfxSoundLevel()*5);
            if(level != 0){
                gameSetting.setVfxSoundLevel(gameSetting.getVfxSoundLevel()-0.2f);
                sfx.setText(String.valueOf((int)(gameSetting.getVfxSoundLevel()*5)));
            }
        });

        gameFrame.add(returnButton);
        gameFrame.add(musicPlusButton);
        gameFrame.add(musicMinusButton);
        gameFrame.add(sfxPlusButton);
        gameFrame.add(sfxMinusButton);
        gameFrame.setSize(FRAME_WIDTH,FRAME_HEIGHT);

        Container c = gameFrame.getContentPane();
        JLabel label = new JLabel();
        label.setIcon(new ImageIcon("assets/Menu/setting.png"));
        Dimension size = label.getPreferredSize();
        label.setBounds(0, 0, size.width, size.height);

        c.add(sfx);
        c.add(music);
        c.add(label);



        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // Calculate the center position
        int centerX = (screenSize.width - gameFrame.getWidth()) / 2;
        int centerY = (screenSize.height - gameFrame.getHeight()) / 2;
        gameFrame.setLocation(centerX, centerY);



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
        gameFrame.setVisible(state);
    }

}