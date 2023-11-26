package simulatorG10;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.function.LongSupplier;

import javax.sound.sampled.Line;
import javax.swing.*;

//Class to show any messages or errors in a popup
public class PopUps {
    private JFrame popUpFrame;
    private JTextArea messageLbl;
    private JScrollPane scrollPane;
    private JLabel textArea;
    private static final String helpTextFilePath = "SupportFiles\\HelpText.txt";

    public PopUps() {
        popUpFrame = new JFrame("Message");
        popUpFrame.setSize(new Dimension(800, 250));
        popUpFrame.getContentPane().setLayout(null);
        popUpFrame.setVisible(true);

        // Create a single JTextArea instance for all messages
        messageLbl = new JTextArea(800, 500);
        messageLbl.setEnabled(false);
        messageLbl.setBounds(10, 11, 764, 189);
        messageLbl.setForeground(Color.white);
        messageLbl.setBackground(Color.black);
        popUpFrame.getContentPane().add(messageLbl);
    }

    // Set the popup window with the appropriate message
    public void SetText(String message) {
        // Clear existing text before adding new content
        messageLbl.setText("");
        messageLbl.append(message);
    }

    // Creating a window to show the popup message
    public void ShowPop(String message) {
        SetText(message);
    }

    /*
     * Method to popup help area for the user - currently disabled for
     * project 1
     * */
    public void ShowHelp() throws Exception {
        // Read help text from file
        File helpFile = new FileHandler().GetTextFromFile(helpTextFilePath);
        StringBuffer helptext = new StringBuffer();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(helpFile));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            helptext.append(line);
        }
        bufferedReader.close();

        // Ensure scroll pane and label are created
        if (scrollPane == null) {
            SetupScrollPane();
        }

        // Set help text to the label
        textArea.setText(helptext.toString());

        // Make help window visible
        popUpFrame.setVisible(true);
    }

    /*
     * Method to show help pop up - currently disabled
     * */
    private void SetupScrollPane() {
        textArea = new JLabel();
        scrollPane = new JScrollPane(textArea);
        popUpFrame.getContentPane().add(scrollPane);
    }
}
