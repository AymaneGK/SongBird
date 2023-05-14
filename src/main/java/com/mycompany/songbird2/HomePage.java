/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.songbird2;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import java.awt.*;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.sound.sampled.*;
import javax.swing.table.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import uk.co.caprica.vlcj.*;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.windows.Win32FullScreenStrategy;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

//import org.jcodec.api.JCodecException;


//import org.jcodec.scale.AWTUtil;

;

/**
 *
 * @author HP
 */
public class HomePage extends javax.swing.JFrame {
private Clip audioClip;
private PlayCommand play;
private PauseCommand pause;
private long pausePosition;
private String playingSong="no song is playing";
private String selectedSong="selected";
private int selectid;
private int playid;
private MyCollection col;
private MyCollection queue;
private Iterator<String> itera;
private ArrayList<String> history=new ArrayList<String>();
private int hid = 0;
private JFrame frame;
    private JPanel panel;
    private JButton playButton;
    private JButton pauseButton;
    private JLabel videoLabel;
private JFrame visualiser;
    private String visual = "off";
    private Mediator mediator = new MusicPlayerMediator(playButton, pauseButton);
    private Thread timer;

    /**
     * Creates new form HomePage
     */
    public HomePage() {
        initComponents();
        showsong();
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(270);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(50);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (audioClip != null && audioClip.isOpen()) {
                    audioClip.stop();
                    audioClip.close();
                }
                e.getWindow().dispose();
            }
        });
        jPanel2.setVisible(false);
        //setPauseB(jButton2);
        //setPlayB(jButton3);
        Mediator mediatorr = new MusicPlayerMediator(jButton3, jButton2);
        setMediator(mediatorr);
        
        
    }

    public void setAudioClip(Clip audioClip) {
        this.audioClip = audioClip;
    }
    
    public void setCol(MyCollection c) {
        this.col = c;
    }
    public Clip getAudioClip(){
        return this.audioClip;
    }
    public void setPlayCommand(PlayCommand p){
        this.play = p;
    }
    public void setPauseCommand(PauseCommand p){
        this.pause = p;
    }
    public void setPlayB(JButton b) {
        this.playButton = b;
    }
    public void setPauseB(JButton b) {
        this.pauseButton = b;
    }
    public void setMediator(Mediator m){
        this.mediator = m;
    }

    
    public static ArrayList<String> listFiles1() {
        String folderPath = "C:\\music";
        File folder = new File(folderPath);
        ArrayList<String> fileList = new ArrayList<>();
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        fileList.add(file.getName());
                    }
                }
            }
        }
        return fileList;
    }
    public void setTimer(Thread d){
        this.timer = d;
    }
    private void Progress(String song) {
        
        Thread progressThread = new Thread(() -> {
        String fsong = song;
        while (fsong.equals(song)) {
        // Get the current position and length of the song
        int currentPosition = getCurrentPosition();
        //System.out.println("song is right now at: "+currentPosition);
        int songLength = getSongLength();
        
        

        // Update the label with the progress
        String progress = formatTime(currentPosition) + "/" + formatTime(songLength);
        SwingUtilities.invokeLater(() -> jLabel5.setText(progress));
        //if(currentPosition>=songLength){
            //flag = false;
        //}

        // Sleep for a short period of time (e.g. 500 ms) before updating again
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            // Handle exception
        }
    }
});
progressThread.start();

    }

    private String formatTime(int timeInSeconds) {
    int minutes = timeInSeconds / 60;
    int seconds = timeInSeconds % 60;
    return String.format("%02d:%02d", minutes, seconds);
}

    public int getCurrentPosition() {
    if (this.audioClip != null && this.audioClip.isRunning()) {
        //System.out.print("time is : "+(int)this.audioClip.getMicrosecondPosition() / 1000000);
        return (int)this.audioClip.getMicrosecondPosition() / 1000000;
    }
    return 0;
}

    private int getSongLength() {
        if (this.audioClip.isRunning()) {
        //System.out.print("time is : "+(int)this.audioClip.getMicrosecondPosition() / 1000000);
        return (int)this.audioClip.getMicrosecondLength() / 1000000;
        }
        return 0;
    }
    


    public class WavFileImporter extends JFrame {

    private final JButton importButton;
    private final JTextArea fileDisplayArea;

    public WavFileImporter() {
        super("WAV File Importer");
        addWindowListener(new WindowAdapter() {
    @Override
    public void windowClosing(WindowEvent e) {
        // Perform some code before closing the JFrame
        System.out.println("Closing JFrame...");
        HomePage p = new HomePage();
        p.setVisible(true);
    }
});

        importButton = new JButton("Import WAV Files");
        fileDisplayArea = new JTextArea(10, 30);

        JPanel contentPane = new JPanel();
        contentPane.add(importButton);
        contentPane.add(new JScrollPane(fileDisplayArea));

        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                importWavFiles();
            }
        });

        setContentPane(contentPane);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void importWavFiles() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "WAV Files", "wav");
        fileChooser.setFileFilter(filter);
        int returnVal = fileChooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = fileChooser.getSelectedFiles();
            File targetDirectory = new File("C:\\music");

            if (!targetDirectory.exists()) {
                targetDirectory.mkdir();
            }

            for (File selectedFile : selectedFiles) {
                try {
                    Files.copy(selectedFile.toPath(),
                            new File(targetDirectory.getAbsolutePath() +
                                    "\\" + selectedFile.getName())
                                    .toPath(),
                            StandardCopyOption.REPLACE_EXISTING);
                    fileDisplayArea.append("Imported: " +
                            selectedFile.getAbsolutePath() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

    public static String MusicLength(String filename) {
    String filePath = "C:\\music\\" + filename;
    File audioFile = new File(filePath);
    String durationString = null;
    try {
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
        AudioFormat format = audioStream.getFormat();
        long audioFileLength = audioFile.length();
        float frameRate = format.getFrameRate();
        long durationInSeconds = (long) (audioFileLength / (frameRate * format.getFrameSize()));
        long minutes = durationInSeconds / 60;
        long seconds = durationInSeconds % 60;
        durationString = String.format("%d:%02d", minutes, seconds);
        System.out.println("Audio file length: " + durationString);
        
    } catch (Exception e) {
        e.printStackTrace();
    }
    return durationString;
}

    public void showsong(){
        MyCollection all = new MyCollection(listFiles1());
        setCol(all);
        //System.out.println("TEST1");
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
         Object[] row = new Object[2];
         //System.out.println("TEST2");
         Iterator<String> iter = all.iterator();
         while(iter.hasNext()){
             String song = iter.next();
             row[0] = song;
             row[1] = MusicLength(song);
             model.addRow(row);
         }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        BottomPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        SidePanel = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        TopPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));

        BottomPanel.setBackground(new java.awt.Color(51, 51, 51));
        BottomPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(51, 51, 51)));

        jLabel2.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Nothing is playing");

        jButton1.setBackground(new java.awt.Color(51, 51, 51));
        jButton1.setIcon(new javax.swing.ImageIcon("C:\\Users\\HP\\Desktop\\icons\\fast-forward.png")); // NOI18N
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(51, 51, 51));
        jButton2.setIcon(new javax.swing.ImageIcon("C:\\Users\\HP\\Desktop\\icons\\pause (1).png")); // NOI18N
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(51, 51, 51));
        jButton3.setIcon(new javax.swing.ImageIcon("C:\\Users\\HP\\Desktop\\icons\\play-button.png")); // NOI18N
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(51, 51, 51));
        jButton5.setIcon(new javax.swing.ImageIcon("C:\\Users\\HP\\Desktop\\icons\\rewind (1).png")); // NOI18N
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton5MouseClicked(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon("C:\\Users\\HP\\Downloads\\virtual.png")); // NOI18N
        jButton4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton4MouseClicked(evt);
            }
        });
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout BottomPanelLayout = new javax.swing.GroupLayout(BottomPanel);
        BottomPanel.setLayout(BottomPanelLayout);
        BottomPanelLayout.setHorizontalGroup(
            BottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BottomPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(BottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(BottomPanelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel5)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3)
                .addGap(12, 12, 12)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4)
                .addGap(15, 15, 15))
        );
        BottomPanelLayout.setVerticalGroup(
            BottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BottomPanelLayout.createSequentialGroup()
                .addGroup(BottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BottomPanelLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(BottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton4)
                            .addGroup(BottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton1)
                                .addComponent(jButton2)
                                .addComponent(jButton3)
                                .addComponent(jButton5))))
                    .addGroup(BottomPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        SidePanel.setBackground(new java.awt.Color(51, 51, 51));
        SidePanel.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(51, 51, 51)));
        SidePanel.setForeground(new java.awt.Color(102, 102, 102));

        jButton6.setText("Import files");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel3.setIcon(new javax.swing.ImageIcon("C:\\Users\\HP\\Downloads\\visual.gif")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Song Title", "Length", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(2).setResizable(false);
        }

        javax.swing.GroupLayout SidePanelLayout = new javax.swing.GroupLayout(SidePanel);
        SidePanel.setLayout(SidePanelLayout);
        SidePanelLayout.setHorizontalGroup(
            SidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SidePanelLayout.createSequentialGroup()
                .addGroup(SidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(SidePanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(SidePanelLayout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                .addContainerGap())
        );
        SidePanelLayout.setVerticalGroup(
            SidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SidePanelLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jButton6)
                .addGap(46, 46, 46)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SidePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        TopPanel.setBackground(new java.awt.Color(51, 51, 51));
        TopPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(51, 51, 51)));

        jLabel1.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("SongBird");

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setIcon(new javax.swing.ImageIcon("C:\\Users\\HP\\Downloads\\bird (1).png")); // NOI18N

        javax.swing.GroupLayout TopPanelLayout = new javax.swing.GroupLayout(TopPanel);
        TopPanel.setLayout(TopPanelLayout);
        TopPanelLayout.setHorizontalGroup(
            TopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TopPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        TopPanelLayout.setVerticalGroup(
            TopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TopPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(TopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BottomPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(SidePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(TopPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(TopPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(SidePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(BottomPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        mediator.pause();
        int i = jTable1.getSelectedRow();
        selectid = i;
        TableModel tm =jTable1.getModel();
        String songname =tm.getValueAt(i, 0).toString();
        //PlayCommand p = new PlayCommand(songname);
        //setPlayCommand(p);
        //p.execute();
        String filePath = "C:\\music\\" + songname;
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            setAudioClip((Clip) AudioSystem.getLine(info));
            this.audioClip.open(audioStream);
            /*this.audioClip.addLineListener(new LineListener() {
                public void update(LineEvent evt) {
                    if (evt.getType() == LineEvent.Type.STOP) {
                        audioClip.close();
                    }
                }
            });*/
            //this.audioClip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
        PauseCommand p1 = new PauseCommand(this.getAudioClip());
        setPauseCommand(p1);
        PlayCommand p2 = new PlayCommand(this.getAudioClip());
        setPlayCommand(p2);*/
        ////////////////////////////////////
        this.selectedSong = songname;
        System.out.println("Song Name: "+songname);
        System.out.println("-----------------------------------------");
        System.out.println("Selected Song: "+this.selectedSong);
        System.out.println("Playing Song: "+this.playingSong);
        System.out.println("S: "+selectid);
            System.out.println("P: "+playid);
        System.out.println("------------------------------------------");
        
        
        //p2.setPausePosition(this.pause.getPausePosition());
    }
//GEN-LAST:event_jTable1MouseClicked

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        //pause button
        if(this.visual.equals("on")){
            jButton4.doClick();
        }
        pausePosition = audioClip.getMicrosecondPosition();
        System.out.println("Pause pos: "+pausePosition);
        this.pause.execute();
        mediator.pause();
        this.play.setPausePosition(pausePosition);
        System.out.println("Pause position on play is "+this.play.getPausePosition());
        //pausePosition = 0;
        //p2.setPausePosition(this.pause.getPausePosition());
        
    }//GEN-LAST:event_jButton2MouseClicked

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        //play button
        
    if (this.selectedSong.equals(this.playingSong)){
        pausePosition = audioClip.getMicrosecondPosition();
        System.out.println("it is the same song");
        mediator.play();
        System.out.println("time paused at : "+this.pausePosition);
        this.play.setPausePosition(pausePosition);
        this.play.execute();
    }
    else{
        
        System.out.println("else");
        if(this.playingSong.equals("no song is playing")){
            //first time playing
            mediator.pause();
            System.out.println("no song is playing");
            /////////////////////////////////////
            int i = jTable1.getSelectedRow();
            TableModel tm =jTable1.getModel();
            tm.setValueAt("playing", i, 2);
            //////////////////////////////////////
            pausePosition = 0;
            System.out.println("its not the same song 1 ");
            this.playingSong = this.selectedSong; 
            PauseCommand p1 = new PauseCommand(this.getAudioClip());
            setPauseCommand(p1);
            PlayCommand p2 = new PlayCommand(this.getAudioClip());
            setPlayCommand(p2);
            this.play.execute();
            mediator.play();
            history.add(playingSong);
            hid++;
            jLabel2.setText(playingSong);
            Progress(playingSong);
        }
        else{
            System.out.println("Changing the song to ");
            if(this.visual.equals("on")){
            jButton4.doClick();
            jButton4.doClick();
        }
            
            mediator.pause();
            this.pause.execute();
            //int selectedid = jTable1.getSelectedRow();
            TableModel tm =jTable1.getModel();
            System.out.println("S: "+selectid);
            System.out.println("P: "+playid);
            //history.add(playingSong);
            //hid++;
            tm.setValueAt("playing", selectid, 2);
            tm.setValueAt("", playid, 2);
            
            /////////////////////////////
            this.pausePosition = 0;
            System.out.println("its not the same song 2");
            this.playingSong = this.selectedSong; 
            ///////////////////////////////////
            String filePath = "C:\\music\\" + selectedSong;
            try {
                File audioFile = new File(filePath);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                AudioFormat format = audioStream.getFormat();
                DataLine.Info info = new DataLine.Info(Clip.class, format);
                setAudioClip((Clip) AudioSystem.getLine(info));
                this.audioClip.open(audioStream);
                PauseCommand p1 = new PauseCommand(this.getAudioClip());
                setPauseCommand(p1);
                PlayCommand p2 = new PlayCommand(this.getAudioClip());
                setPlayCommand(p2);
                this.play.execute();
                mediator.play();
                if(this.visual.equals("on")){
                    jButton4.doClick();
                    jButton4.doClick();
                }
                history.add(selectedSong);
                jLabel2.setText(selectedSong);
                hid++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Progress(playingSong);
    playid = selectid;
    
    ArrayList<String> newList = new ArrayList<String>(col.getArray().subList(playid+1, col.getArray().size()));
    MyCollection q = new MyCollection(newList);
    Iterator<String> iter = q.iterator();
    itera =iter;
    jLabel2.setText(playingSong);
    }
    }//GEN-LAST:event_jButton3MouseClicked

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        //next button
        //System.out.println("Bazinga");
        audioClip.stop();
        
        if(this.visual.equals("on")){
            jButton4.doClick();
            jButton4.doClick();
        }
        TableModel tm =jTable1.getModel();
            System.out.println("S: "+selectid);
            System.out.println("P: "+playid);
            
        this.pausePosition = 0;
        if(itera.hasNext()){
            ////
             tm.setValueAt("", 0, 2);
             tm.setValueAt("", playid, 2);
            tm.setValueAt("playing", playid+1, 2);
            this.mediator.play();
            this.pause.execute();
            ///////////////////////////
            NextCommand Next = new NextCommand(this.itera);
            Next.execute();
            ///////////////////////////
            setAudioClip(Next.audioClip);
                PauseCommand p1 = new PauseCommand(this.getAudioClip());
                setPauseCommand(p1);
                this.pause.execute();
                PlayCommand p2 = new PlayCommand(this.getAudioClip());
                setPlayCommand(p2);
                //this.play.execute();
                
                history.add(Next.NextSong);
                hid++;
                jLabel2.setText(Next.NextSong);
            
        playid++;
        }
        else {
            tm.setValueAt("", playid, 2);
            playid=0;
            tm.setValueAt("playing", 0, 2);
            
            
            
            if(this.visual.equals("on")){
            disposeV();
            Visualiser(jPanel2);
        }
            //plays from the start
            
            MyCollection all = new MyCollection(listFiles1());
            Iterator<String> iter1 = all.iterator();
            itera = iter1;
            ////////////////////////////////////////////////
            NextCommand Next = new NextCommand(this.itera);
            Next.execute();
            ////////////////////////////////////////////////
            setAudioClip(Next.audioClip);
            PauseCommand p1 = new PauseCommand(this.getAudioClip());
            setPauseCommand(p1);
            PlayCommand p2 = new PlayCommand(this.getAudioClip());
            setPlayCommand(p2);
                //this.play.execute();
                
                history.add(Next.NextSong);
                hid++;
                jLabel2.setText(Next.NextSong);
        //playid++;
        }
    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseClicked
        // TODO add your handling code here:
        //GO BACK
        if(!playingSong.equals("no song is playing")){
            if(hid>1){
            
            if(this.visual.equals("on")){
               jButton4.doClick();
               jButton4.doClick();
        }
        this.pause.execute();
        String previous = history.get(hid-1-1);
        hid--;
        System.out.println(previous);
        jLabel2.setText(previous);
        TableModel tm =jTable1.getModel();
        tm.setValueAt("", playid, 2);
        tm.setValueAt("playing", playid-1, 2);
        ///////////////////////////////////////////////////
        PreviousCommand Prev = new PreviousCommand(previous);
            Prev.execute();
        //////////////////////////////////////////////////
        setAudioClip(Prev.audioClip);
        
        
        setPauseCommand(new PauseCommand(this.getAudioClip()));
        
        setPlayCommand(new PlayCommand(this.getAudioClip()));
    ArrayList<String> newList = new ArrayList<String>(col.getArray().subList(playid, col.getArray().size()));
    MyCollection q = new MyCollection(newList);
    Iterator<String> iter = q.iterator();
    itera =iter; 
                
        }
        else{
                mediator.pause();
            this.pause.execute();
            System.out.println("there is no song before this");
        }
        
        }
        else{
            System.out.println("no song was playing");
        }
        
    }//GEN-LAST:event_jButton5MouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        //System.setProperty("jna.library.path", "C:/Users/HP/Documents/NetBeansProjects/SongBird2/lib");
        if(this.visual.equals("on")){
            disposeV();   
            this.visual = "off";
        }
        else{
            this.visual = "on";
            Visualiser(jPanel2);
        }
        
        
    ///////////////////////////////

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseClicked
        // TODO add your handling code here:   
    }//GEN-LAST:event_jButton4MouseClicked

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        new WavFileImporter();
        
    }//GEN-LAST:event_jButton6ActionPerformed


    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HomePage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BottomPanel;
    private javax.swing.JPanel SidePanel;
    private javax.swing.JPanel TopPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

    private void Visualiser(JPanel panel) {
        
        panel.setVisible(false);
        jLabel3.setIcon(new javax.swing.ImageIcon("C:\\Users\\HP\\Downloads\\visual.gif"));
        panel.revalidate();
        panel.repaint();
        panel.setVisible(true);
}


    private void disposeV() {
        
        jPanel2.setVisible(false);
    }
}
