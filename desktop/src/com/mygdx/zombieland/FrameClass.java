package com.mygdx.zombieland;

import com.sun.tools.javadoc.main.Start;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class FrameClass {

    private static final int frameWidth = 800;
    private static final int frameHeight = 600;

    void createFrame() {
        Frame frame = new Frame();
        frame.setSize(frameWidth, frameHeight);
        frame.setVisible(true);

        Button startBtn = new Button("Start game");

        frame.add(startBtn);

        startBtn.addActionListener(new StartButtonListener(frame));

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                System.exit(0);
            }
        });
    }

    static class StartButtonListener implements ActionListener {
        private Frame frame;

        public StartButtonListener(Frame frame){
            this.frame = frame;
        }
        public void actionPerformed(ActionEvent e) {
            frame.dispose();
            ApplicationClass application = new ApplicationClass();
            application.startApplication();
        }
    }

}