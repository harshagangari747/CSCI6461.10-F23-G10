package simulatorG10;

import java.awt.Color;
import java.awt.Dimension;
import java.io.Console;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

//Class to print the actions performed to better aid understanding
public class OutputConsole {
	private static JFrame outputConsole;
	private static JTextArea outputText;
	private static JPanel outputJPanel;
	private static StringBuffer textBuffer;
	private static OutputConsole consoleObj;
	
	/*
	 * Returns instance of this class
	 * follows singleton pattern*/
	public static OutputConsole GetOutputConsoleObj()
	{
		if(consoleObj == null)
			return consoleObj = new  OutputConsole();
		return  consoleObj;
	}
	
	/*
	 * Class constructor
	 * */
	private OutputConsole() {
        SwingUtilities.invokeLater(() -> {
            outputConsole = new JFrame("Debugging Console");
            outputText = new JTextArea(25,50);
            textBuffer = new StringBuffer();
            outputJPanel = new JPanel();
            outputJPanel.add(new JScrollPane(outputText));
            outputText.setEnabled(false);
            outputText.setForeground(Color.white);
            outputText.setBackground(Color.black);
            outputConsole.getContentPane().add(outputJPanel);
            outputConsole.setSize(new Dimension(500,500));
            outputConsole.pack(); // Automatically adjust size
            outputConsole.setLocationRelativeTo(null); // Center on the screen
            outputConsole.setVisible(true);
            outputConsole.setResizable(false);
        });
    }
	
	/*
	 * Updates the textarea with the instruction actions
	 * */
	public void WriteToOutputConsole(String message, Color color)
	{
		textBuffer.append(message+"\n");
		outputText.setSelectedTextColor(color);
		outputText.setText(textBuffer.toString());
	}

}
