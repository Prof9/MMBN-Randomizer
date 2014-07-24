/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rand.gui;

import java.awt.CardLayout;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import javax.swing.JFileChooser;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import mmbn.bn6.BN6RandomizerContext;
import rand.ByteStream;

/**
 *
 * @author
 */
public class MainFrame extends javax.swing.JFrame {
    private final Random rng;
    private final FileNameExtensionFilter gbaFileFilter;
    private final DocumentListener goButtonEnableListener;
    private boolean isRunning;

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        this.rng = new Random();
        this.gbaFileFilter = new FileNameExtensionFilter("Game Boy Advance ROMs (*.gba)", "gba");
        this.goButtonEnableListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkCanGo();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                checkCanGo();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                checkCanGo();
            }
        };
        
        this.isRunning = false;
        
        initComponents();
        setRandomSeed();
        
        inFileChooser.setFileFilter(gbaFileFilter);
        outFileChooser.setFileFilter(gbaFileFilter);
        
        inFileTextField.getDocument().addDocumentListener(this.goButtonEnableListener);
        outFileTextField.getDocument().addDocumentListener(this.goButtonEnableListener);
        seedTextField.getDocument().addDocumentListener(this.goButtonEnableListener);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        inFileChooser = new javax.swing.JFileChooser();
        outFileChooser = new javax.swing.JFileChooser();
        tabbedPane = new javax.swing.JTabbedPane();
        basicPanel = new javax.swing.JPanel();
        inFileLabel = new javax.swing.JLabel();
        inFileTextField = new javax.swing.JTextField();
        inFileButton = new javax.swing.JButton();
        outFileLabel = new javax.swing.JLabel();
        outFileTextField = new javax.swing.JTextField();
        outFileButton = new javax.swing.JButton();
        seedLabel = new javax.swing.JLabel();
        seedTextField = new javax.swing.JTextField();
        seedButton = new javax.swing.JButton();
        goButton = new javax.swing.JButton();
        cardPanel = new javax.swing.JPanel();
        explanationPanel = new javax.swing.JPanel();
        explanationLabel = new javax.swing.JLabel();
        statusPanel = new javax.swing.JPanel();
        statusScrollPane = new javax.swing.JScrollPane();
        statusTextArea = new javax.swing.JTextArea();
        statusProgressBar = new javax.swing.JProgressBar();
        statusCloseButton = new javax.swing.JButton();

        outFileChooser.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MMBN Randomizer");
        setResizable(false);

        basicPanel.setOpaque(false);

        inFileLabel.setText("Input ROM:");

        inFileButton.setText("Choose");
        inFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inFileButtonActionPerformed(evt);
            }
        });

        outFileLabel.setText("Output ROM:");

        outFileButton.setText("Choose");
        outFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                outFileButtonActionPerformed(evt);
            }
        });

        seedLabel.setText("Seed:");

        seedButton.setText("Random");
        seedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seedButtonActionPerformed(evt);
            }
        });

        goButton.setText("GO!");
        goButton.setEnabled(false);
        goButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goButtonActionPerformed(evt);
            }
        });

        cardPanel.setOpaque(false);
        cardPanel.setLayout(new java.awt.CardLayout());

        explanationPanel.setOpaque(false);

        explanationLabel.setText("<html>\n<style>\nol {\n\tmargin: 0 0 0 20pt;\n}\n</style>\n<strong>What to do:</strong>\n<ol>\n\t<li>Choose the input ROM to randomize.</li>\n\t<li>Choose where to save the randomized output ROM.</li>\n\t<li>Browse through the options using the tabs above to customize the randomization to your liking.</li>\n\t<li>Optionally, choose a seed to initialize the RNG with. See below for more info.</li>\n\t<li>Hit \"GO!\" to run the randomizer!</li>\n</ol>\n<br>\n<strong>Choosing a seed:</strong><br>\nA \"seed\" is the initial value of a random number generator (RNG). Two RNGs initialized with the same seed will generate the same series of numbers, so any particular seed will always produce the same randomized ROM. You can choose a seed by setting the Seed field to an integer number (between -2147483648 and 2147483647) or text string. The behaviour of seeds may vary slightly between different versions of the MMBN Randomizer.<br>\n<br>\n<strong>Save file compatibility:</strong><br>\nSave files produced by randomized MMBN ROMs may not work correctly when transferred to \"normal\" ROMs or randomized ROMs with a different seed, and vice versa. Make sure you memorize your seed, so that you can re-patch your clean ROM with a newer version of the MMBN Randomizer and continue your game where you left off.\n</html>");
        explanationLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        explanationLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout explanationPanelLayout = new javax.swing.GroupLayout(explanationPanel);
        explanationPanel.setLayout(explanationPanelLayout);
        explanationPanelLayout.setHorizontalGroup(
            explanationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(explanationPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(explanationLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE))
        );
        explanationPanelLayout.setVerticalGroup(
            explanationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(explanationLabel)
        );

        cardPanel.add(explanationPanel, "explanationCard");

        statusPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Status:"));
        statusPanel.setOpaque(false);

        statusTextArea.setEditable(false);
        statusTextArea.setColumns(20);
        statusTextArea.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        statusTextArea.setRows(5);
        statusScrollPane.setViewportView(statusTextArea);

        statusCloseButton.setText("Close");
        statusCloseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statusCloseButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusScrollPane)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusCloseButton))
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusPanelLayout.createSequentialGroup()
                .addComponent(statusScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statusProgressBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statusCloseButton, javax.swing.GroupLayout.Alignment.TRAILING)))
        );

        cardPanel.add(statusPanel, "statusCard");

        javax.swing.GroupLayout basicPanelLayout = new javax.swing.GroupLayout(basicPanel);
        basicPanel.setLayout(basicPanelLayout);
        basicPanelLayout.setHorizontalGroup(
            basicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(basicPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(basicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(basicPanelLayout.createSequentialGroup()
                        .addComponent(cardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(basicPanelLayout.createSequentialGroup()
                        .addGroup(basicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(inFileLabel)
                            .addComponent(outFileLabel)
                            .addComponent(seedLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(basicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(outFileTextField)
                            .addComponent(inFileTextField)
                            .addComponent(seedTextField))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(basicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(seedButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(outFileButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(inFileButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(goButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        basicPanelLayout.setVerticalGroup(
            basicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(basicPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(basicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(basicPanelLayout.createSequentialGroup()
                        .addGroup(basicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(inFileButton)
                            .addComponent(inFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(inFileLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(basicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(outFileButton)
                            .addComponent(outFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(outFileLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(basicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(seedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(seedLabel)
                            .addComponent(seedButton)))
                    .addComponent(goButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab("Basic", basicPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void statusCloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statusCloseButtonActionPerformed
        ((CardLayout)cardPanel.getLayout()).show(cardPanel, "explanationCard");
    }//GEN-LAST:event_statusCloseButtonActionPerformed

    private void seedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seedButtonActionPerformed
        setRandomSeed();
    }//GEN-LAST:event_seedButtonActionPerformed

    private void inFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inFileButtonActionPerformed
        if (inFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String inPath = inFileChooser.getSelectedFile().toString();
            
            int insertIndex = inPath.lastIndexOf('.');
            if (insertIndex < 0) {
                insertIndex = inPath.length();
            }
            String outPath = new StringBuilder(inPath).insert(insertIndex, "-Randomized").toString();
            
            inFileTextField.setText(inPath);
            outFileChooser.setSelectedFile(new File(outPath));
            outFileTextField.setText(outPath);
        }
    }//GEN-LAST:event_inFileButtonActionPerformed

    private void outFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_outFileButtonActionPerformed
        if (outFileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String outPath = outFileChooser.getSelectedFile().toString();
            if (outFileChooser.getFileFilter() == this.gbaFileFilter) {
                if (outPath.lastIndexOf('.') < 0) {
                    outPath += ".gba";
                    outFileChooser.setSelectedFile(new File(outPath));
                }
            }
            outFileTextField.setText(outPath);
        }
    }//GEN-LAST:event_outFileButtonActionPerformed

    private void goButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goButtonActionPerformed
        goButton.setEnabled(false);
        runRandomizer(
                inFileTextField.getText(),
                outFileTextField.getText(),
                getSeed(seedTextField.getText())
        );
        checkCanGo();
    }//GEN-LAST:event_goButtonActionPerformed
    
    private void setRandomSeed() {
        seedTextField.setText(Integer.toString(rng.nextInt()));
    }
    
    private void checkCanGo() {
        goButton.setEnabled(
                !this.isRunning &&
                inFileTextField.getText().length() > 0 &&
                outFileTextField.getText().length() > 0 &&
                seedTextField.getText().length() > 0
        );
    }
    
    private int getSeed(String seedString) {
        try {
            return Integer.parseInt(seedString);
        } catch (NumberFormatException ex) {
            return seedString.hashCode();
        }
    }
    
    private void runRandomizer(String inPath, String outPath, int seed) {
        this.isRunning = true;
        ((CardLayout)cardPanel.getLayout()).show(cardPanel, "statusCard");
        
        // Load input ROM.
        ByteStream rom;
        try {
            Path in = Paths.get(inPath);
            rom = new ByteStream(in, 0x08000000);
        }
        catch (IOException ex) {
            appendStatus("FATAL ERROR: Could not read input ROM.");
            this.isRunning = false;
            return;
        }
        
        // Run randomizer.
        appendStatus("Starting...");
        Random seededRng = new Random(seed);
        BN6RandomizerContext context = new BN6RandomizerContext(seededRng);
        context.randomize(rom);
        
        // Write output ROM.
        try {
            Files.write(Paths.get(outPath), rom.toBytes());
        }
        catch (IOException ex) {
            appendStatus("FATAL ERROR: Could not write output ROM.");
            this.isRunning = false;
            return;
        }
        
        appendStatus("Done!");
        this.isRunning = false;
    }
    
    private void clearStatus() {
        statusTextArea.setText(null);
    }
    
    private void appendStatus(String status) {
        statusTextArea.append(System.lineSeparator() + status);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel basicPanel;
    private javax.swing.JPanel cardPanel;
    private javax.swing.JLabel explanationLabel;
    private javax.swing.JPanel explanationPanel;
    private javax.swing.JButton goButton;
    private javax.swing.JButton inFileButton;
    private javax.swing.JFileChooser inFileChooser;
    private javax.swing.JLabel inFileLabel;
    private javax.swing.JTextField inFileTextField;
    private javax.swing.JButton outFileButton;
    private javax.swing.JFileChooser outFileChooser;
    private javax.swing.JLabel outFileLabel;
    private javax.swing.JTextField outFileTextField;
    private javax.swing.JButton seedButton;
    private javax.swing.JLabel seedLabel;
    private javax.swing.JTextField seedTextField;
    private javax.swing.JButton statusCloseButton;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JProgressBar statusProgressBar;
    private javax.swing.JScrollPane statusScrollPane;
    private javax.swing.JTextArea statusTextArea;
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables
}