package simulatorG10;


import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.*;


public class PopUps {
	private static JFrame popUpFrame;
	private static JLabel messageLbl;
	private void SetText(String message)
	{
		messageLbl.setText(message);
		
		
	}
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

