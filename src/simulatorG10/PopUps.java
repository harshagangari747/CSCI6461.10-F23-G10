package simulatorG10;


import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.*;

//Class to show any messages or errors in a popup
public class PopUps {
	private static JFrame popUpFrame;
	private static JLabel messageLbl;
	
	//Set the popup window with the appropriate message
	private void SetText(String message)
	{
		messageLbl.setText(message);
		
		
	}
	
	//Creating a window to show the popup message
	public void ShowPop(String message)
	{
		popUpFrame = new JFrame("Message");
		popUpFrame.setSize(800,250);
		
		messageLbl = new JLabel("Message here");
		messageLbl.setHorizontalAlignment(SwingConstants.CENTER);
		messageLbl.setBounds(10, 11, 764, 189);
		
		
		popUpFrame.getContentPane().setLayout(null);
		popUpFrame.getContentPane().add(messageLbl);
		popUpFrame.setVisible(true);
		
		SetText(message);
	}

}

