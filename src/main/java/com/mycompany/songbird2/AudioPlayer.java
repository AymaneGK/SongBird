/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.songbird2;

import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

/**
 *
 * @author HP
 */
public class AudioPlayer {
    public static void main(String[] args) throws Exception {
    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("C:\\music\\audio.mp3"));
        AudioFormat format = audioInputStream.getFormat();
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
        line.open(format);
        line.start();
        int bufferSize = (int) format.getSampleRate() * format.getFrameSize();
        byte[] buffer = new byte[bufferSize];
        int count;
        while ((count = audioInputStream.read(buffer, 0, buffer.length)) != -1) {
            if (count > 0) {
                line.write(buffer, 0, count);
            }
        }
        line.drain();
        line.close();
        audioInputStream.close();
    }
}
