/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.songbird2;

/**
 *
 * @author HP
 */
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class WavFileImporter extends JFrame {

    private final JButton importButton;
    private final JTextArea fileDisplayArea;

    public WavFileImporter() {
        super("WAV File Importer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
            File targetDirectory = new File("C:\\WavFiles");

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

    public static void main(String[] args) {
        new WavFileImporter();
    }
}

