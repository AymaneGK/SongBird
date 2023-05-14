/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.songbird2;

import java.io.File;
import java.util.Iterator;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

/**
 *
 * @author HP
 */
public class PreviousCommand implements Command {
    public Iterator<String> itera;
    public Clip audioClip;
    public String Previous;
    
    public PreviousCommand(String p) {
        this.Previous = p;
    }
    
    public void execute() {
        //code for previous song
        
            String filePath = "C:\\music\\" + Previous;
            try {
                File audioFile = new File(filePath);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                AudioFormat format = audioStream.getFormat();
                DataLine.Info info = new DataLine.Info(Clip.class, format);
                Clip audioClipp = (Clip) AudioSystem.getLine(info);
                this.audioClip = audioClipp;
                //setAudioClip(audioClipp);
                this.audioClip.open(audioStream);
                this.audioClip.start();
                } catch (Exception e) {
                e.printStackTrace();
            }
            
    }
}
