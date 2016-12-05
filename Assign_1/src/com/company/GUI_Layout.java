package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.List;
import javax.swing.ButtonGroup;

public class GUI_Layout implements ActionListener {

    JFrame frame;
    JPanel backPanel;
    JPanel rightPanel;
    JPanel leftPanel;

    //left panel for tabbed panel
    JTabbedPane tabbedPane;

    //tabbed panel #1
    JPanel radPanel;
    JPanel userPanel;
    JPanel passwordPanel;
    JPanel butPanel;
    ButtonGroup bg1;
    JRadioButton jRadioButton1;
    JRadioButton jRadioButton2;
    JRadioButton jRadioButton3;
    JLabel userLabel;
    JLabel passwordLabel;
    JTextField userField;
    JTextField passwordField;
    JButton saveButton;
    JButton clearButton;

    //tabbed panel #2
    JPanel loadPanel;
    JButton loadButton;
    JButton clearButton2;

    //right panel for text area
    JTextArea textArea;
    JFileChooser fc;
    File file;
    String text;
    List<String> lines = new ArrayList<>( 1000 );

    public static void main(String[] args) {

        new GUI_Layout();


    }

    public GUI_Layout() {
        //super("GUI_Layout");

        //1. Create the frame.
        frame = new JFrame("GUI Demo");
        backPanel = new JPanel();

        //2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,500);

        //3. Create components and put them in the frame.
        //...create Labels...
        //left side
        leftPanel = new JPanel();
        tabbedPane = new JTabbedPane();
        //tabbed panel #1
        radPanel = new JPanel();
        userPanel = new JPanel();
        passwordPanel = new JPanel();
        butPanel = new JPanel();
        bg1 = new ButtonGroup();
        jRadioButton1 = new JRadioButton("Radio Button 1");
        jRadioButton2 = new JRadioButton("Radio Button 2");
        jRadioButton3 = new JRadioButton("Radio Button 3");
        userLabel = new JLabel("User:");
        passwordLabel = new JLabel("Password");
        userField = new JTextField(15);
        passwordField = new JTextField(15);
        saveButton = new JButton("Save");
        clearButton = new JButton("Clear");
        //tabbed panel #2
        loadPanel = new JPanel();
        loadButton = new JButton("load file");
        clearButton2 = new JButton ("clear file");

        //right side
        rightPanel = new JPanel();
        textArea = new JTextArea(30,50);


        rightPanel.setSize(400,500);
        leftPanel.setSize(400,500);

        //tabbed panel #1
        JComponent panel1 = makeTextPanel("");
        tabbedPane.addTab("Tab 1", null, panel1,
                "Does nothing");

        GridLayout gridLeftPane = new GridLayout(3,0);
        panel1.setLayout(gridLeftPane);
        Dimension panelD = new Dimension(250,100);
        radPanel.setPreferredSize(panelD);
        panel1.add(radPanel);
        userPanel.setPreferredSize(panelD);
        panel1.add(userPanel);
        passwordPanel.setPreferredSize(panelD);
        panel1.add(passwordPanel);
        butPanel.setPreferredSize(panelD);
        panel1.add(butPanel);

        clearButton.addActionListener(this);
        bg1.add(jRadioButton1);
        bg1.add(jRadioButton2);
        bg1.add(jRadioButton3);
        radPanel.add(jRadioButton1);
        radPanel.add(jRadioButton2);
        radPanel.add(jRadioButton3);
        userPanel.add(userLabel);
        userPanel.add(userField);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        butPanel.add(saveButton);
        butPanel.add(clearButton);

        //tabbed panel #2
        JComponent panel2 = makeTextPanel("");
        tabbedPane.addTab("Tab 2", null, panel2);
        loadButton.addActionListener(this);
        loadButton.setBounds(0,0,50,50);
        clearButton2.addActionListener(this);
        loadPanel.add(loadButton);
        loadPanel.add(clearButton2);
        panel2.add(loadPanel);

        //tabbed panel #3
        JComponent panel3 = makeTextPanel(
                "Panel #3 (has a preferred size of 410 x 50).");
        panel3.setPreferredSize(new Dimension(400, 400));
        tabbedPane.addTab("Tab 3", null, panel3,
                "Does nothing at all");



        rightPanel.add(new JScrollPane(textArea));
        leftPanel.add(tabbedPane);

        GridLayout grid = new GridLayout(0,2);
        backPanel.setLayout(grid);

        backPanel.add(leftPanel);
        backPanel.add(rightPanel);
        frame.getContentPane().add(backPanel);


        //4. Size the frame.
        frame.pack();

        /* 5. Show it. */
        frame.setVisible(true);
    }

    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == clearButton) {
            userField.setText("");
            passwordField.setText(null); //or use this
        }
        if (e.getSource() == clearButton2) {
            textArea.setText(null);
        }

        if (e.getSource() == loadButton) {
            fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(textArea);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
            }
            textArea.setText(null);
            new SwingWorker<Void, String>() {
                @Override
                protected Void doInBackground() throws Exception {

                    try (InputStream is = new FileInputStream(file)) {
                        byte[] content = new byte[2048];  //changes amount of data loaded at a time
                        int bytesRead = -1;
                        while ((bytesRead = is.read(content)) != -1) {
                            text = new String(content);
                            lines.add(text);
                            publish(text);  //calls @override process
                            Thread.sleep(0, 1); //to see it loading each 'content' at 1 nano second intervals
                        }
                    }
                    return null;  //can be retrieved by calling get()
                }

                //runs on EDT, allowed to update gui
                @Override
                protected void process(List<String> text) {
                    super.process(text);
                    textArea.append(String.valueOf(text));
                }
            }.execute();
        }
    }
}
