/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.songbird2;

import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

/**
 *
 * @author HP
 */
public class PlayCommand implements Command {
    private Clip audioClip;
    private long pausePosition;
    
    public PlayCommand(Clip audioClip) {
        this.audioClip = audioClip;
    }
    
    public void execute() {
    if (audioClip != null) {
        audioClip.setMicrosecondPosition(pausePosition);
        audioClip.start();
    }
    else{
        System.out.println("audio is null");
    }
}

    
    public void setPausePosition(long pausePosition) {
        this.pausePosition = pausePosition;
    }
    public long getPausePosition(){
        return this.pausePosition;
    }

    public void stop() {
        this.audioClip.stop();
    }
}
