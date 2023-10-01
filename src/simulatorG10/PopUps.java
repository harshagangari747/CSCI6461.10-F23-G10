package simulatorG10;

import javax.swing.JFrame;
import javax.swing.JLabel;

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
	private JLabel messageLbl;
	private JScrollPane scrollPane;
	private JLabel textArea;
	private static final String helpTextFilePath = "SupportFiles\\HelpText.txt";

	public PopUps() {
		popUpFrame = new JFrame("Message");
		popUpFrame.setSize(new Dimension(800, 250));
		popUpFrame.getContentPane().setLayout(null);
		popUpFrame.setVisible(true);

	}

	// Set the popup window with the appropriate message
	private void SetText(String message) {
		messageLbl.setText(message);

	}

	// Creating a window to show the popup message
	public void ShowPop(String message) {

		messageLbl = new JLabel();
		messageLbl.setHorizontalAlignment(SwingConstants.CENTER);
		messageLbl.setBounds(10, 11, 764, 189);
		popUpFrame.getContentPane().add(messageLbl);

		SetText(message);
	}

	/*
	 * Method to popup help area for the user - currently disabled for
	 * project 1
	 * */
	public void ShowHelp() throws Exception {
		File helpFile = new FileHandler().GetTextFromFile(helpTextFilePath);
		StringBuffer helptext = new StringBuffer();
		BufferedReader bufferedReader = new BufferedReader(new FileReader(helpFile));
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			helptext.append(line);
		}
		bufferedReader.close();
		SetupScrollPane();
		textArea.setText(helptext.toString());
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
