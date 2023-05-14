/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.songbird2;

import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;

/**
 *
 * @author HP
 */
public class PauseCommand implements Command {
    private Clip audioClip;
    private long pausePosition;
    
    public PauseCommand(Clip audioClip) {
        this.audioClip = audioClip;
    }
    
    public void execute() {
    if (audioClip.isRunning()) {
        pausePosition = audioClip.getMicrosecondPosition();
        audioClip.stop();
        System.out.println("STOPPED BUT THEN");
        
        // Create a new thread to display the message dialog
        //audioClip.start();
    }
}


    
    public long getPausePosition() {
        return pausePosition;
    }
}
