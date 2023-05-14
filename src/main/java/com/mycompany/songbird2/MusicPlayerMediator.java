/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.songbird2;

import javax.swing.JButton;

/**
 *
 * @author HP
 */
public class MusicPlayerMediator implements Mediator{
    private JButton playButton;
    private JButton pauseButton;

    public MusicPlayerMediator(JButton playButton, JButton pauseButton) {
        this.playButton = playButton;
        this.pauseButton = pauseButton;
    }
    @Override
    public void play() {
        playButton.setEnabled(false);
        pauseButton.setEnabled(true);
    }

    @Override
    public void pause() {
        playButton.setEnabled(true);
        pauseButton.setEnabled(false);
    }
    
    
}
