package simulatorG10;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;

//Main class that contains the Front Panel User Interface
public class FrontPanel extends JFrame {

	// Class constructor calling another method to initialize UI components
	public FrontPanel() {
		InitializeFrameComponents();
	}

	// Declaring the essential components of the UI
	private static JFrame frame;
	private static JTextField oprInput;
	private static JTextField gprInput;
	private static JTextField ixrInput;
	private static JTextField addrInput;
	private static JButton runBtn;
	private static JLabel gpr0Lbl;
	private static JLabel gpr1Lbl;
	private static JLabel gpr2Lbl;
	private static JLabel gpr3Lbl;
	public static JLabel gpr0ValueLbl;
	public static JLabel gpr1ValueLbl;
	public static JLabel gpr2ValueLbl;
	public static JLabel gpr3ValueLbl;
	private static JTextField indexInput;
	private static JLabel oprInputLbl;
	private static JLabel gprInputLbl;
	private static JLabel ixrInputLbl;
	private static JLabel indexInputLbl;
	private static JLabel addrInputLbl;
	private static JButton fileLoadBtn;
	private static JLabel ixr1Lbl;
	private static JLabel ixr2Lbl;
	private static JLabel ixr3Lbl;
	public static JLabel ixr1ValueLbl;
	public static JLabel ixr2ValueLbl;
	public static JLabel ixr3ValueLbl;

	private static JButton gpr0LoadBtn;
	private static JButton gpr2LoadBtn;
	private static JButton gpr1LoadBtn;
	private static JButton gpr3LoadBtn;

	private static JButton ixr1LoadBtn;
	private static JButton ixr2LoadBtn;
	private static JButton ixr3LoadBtn;

	private static JButton singleStepBtn;
	private static JButton helpBtn;

	private static JLabel pcLbl;
	private static JLabel marLbl;
	private static JLabel mbrLbl;
	private static JLabel irLbl;
	private static JLabel mfrLbl;
	private static JLabel prvlgLbl;

	public static JLabel pcValueLbl;

	private static String gprText = Constants.default16Zeroes;
	private static String pcText = Constants.default12Zeroes;
	private static String ixrText = Constants.default16Zeroes;
	private static String marText = Constants.default12Zeroes;
	private static String mbrText = Constants.default16Zeroes;

	public static JLabel marValueLbl;
	public static JLabel mbrValueLbl;
	public static JLabel irValueLbl;
	public static JLabel mfrValueLbl;
	public static JLabel prvlgValueLbl;

	private static JButton pcLoadBtn;
	private static JButton marLoadBtn;
	private static JButton mbrLoadBtn;

	private static JButton loadBtn;
	private static JButton storeBtn;

	private static OutputConsole opConsoleObj;

	public static boolean conditionCode;

	private static JLabel ccLabel;
	private static JButton ccLoadBtn;
	public static JLabel ccValueLbl;
	private static String ccText;

	public static JTextArea keyboardArea;
	public static JTextArea printerArea;

	public static JLabel helpTextLabel;

	public static boolean faultTriggered;

	// Main method performing some tasks before the user can interact with the UI
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InitializeFrameComponents();
					SetDefaultValues();
					SetActions();
					opConsoleObj = OutputConsole.GetOutputConsoleObj();
				} catch (Exception e) {
					ShowDialog(e.getLocalizedMessage());
				}
			}
		});
	}

	/*
	 * Method to set actions for each of the buttons when they are clicked
	 */
	private static void SetActions() {
		/*
		 * ActionListener To perform file load operation when user clicked on the IPL
		 * button
		 */
		fileLoadBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileHandler newfileFileHandler = new FileHandler();
				try {
					opConsoleObj.WriteToOutputConsole("Interpreting Json File", Color.MAGENTA);
					newfileFileHandler.LoadFile(FileTypes.JSONFile);
					newfileFileHandler.ReadJsonFile();
					opConsoleObj.WriteToOutputConsole("Comprehending the inputfile. Translation in process...",
							Color.MAGENTA);
					newfileFileHandler.LoadFile(FileTypes.IPLFile);
					opConsoleObj.WriteToOutputConsole("Conversion Succeeded!. Now reading translated file",
							Color.GREEN);
					ArrayList<String> inputFileArrayList = newfileFileHandler.ReadTextFile();
					Translator translateObj = new Translator();
					LinkedHashMap<String, String> getHexInputValues = (LinkedHashMap<String, String>) translateObj
							.TranslateIntoHexCode(inputFileArrayList);
					newfileFileHandler.LoadFile(FileTypes.HexFile);
					newfileFileHandler.WriteToHexFile(getHexInputValues);
					newfileFileHandler.ConvertTheFile();
				} catch (Exception ex) {
					ShowDialog(ex.getMessage());
				}

			}
		});

		/*
		 * ActionListener To run the program in the loaded file when user clicked on the
		 * run button
		 */
		runBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Simulator step = new Simulator();
				int memSize = -1;
				while (memSize < Memory.memory.size()) {
					try {
						singleStepBtn.doClick();
						if (!Simulator.haltTriggered) {
							memSize++;
						} else {
							Simulator.haltTriggered = true;
							memSize = Memory.memory.size();
						}
					} catch (Exception e1) {
						ShowDialog(e1.getLocalizedMessage());
					}
				}
			}
		});

		/*
		 * ActionListener To Load gpr0 Value when user clicked on the LD button next to
		 * gpr0
		 */
		gpr0LoadBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					gprText = LoadRegister(Registers.GPR0);
					gpr0ValueLbl.setText(gprText);
					opConsoleObj.WriteToOutputConsole("Loaded GPR0 with " + gprText, Color.WHITE);

				} catch (Exception e1) {
					gpr0ValueLbl.setText(Constants.default16Zeroes);
					ShowDialog(e1.getMessage());
				}
			}
		});

		/*
		 * ActionListener To Load gpr1 value when user clicked on the LD button next to
		 * gpr1
		 */
		gpr1LoadBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					gprText = LoadRegister(Registers.GPR1);
					gpr1ValueLbl.setText(gprText);
					opConsoleObj.WriteToOutputConsole("Loaded GPR1 with " + gprText, Color.white);
				} catch (Exception e1) {
					gpr1ValueLbl.setText(Constants.default16Zeroes);
					ShowDialog(e1.getMessage());
				}
			}
		});

		/*
		 * ActionListener To Load gpr2 Value when user clicked on the LD button next to
		 * gpr2
		 */
		gpr2LoadBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					gprText = LoadRegister(Registers.GPR2);
					gpr2ValueLbl.setText(gprText);
					opConsoleObj.WriteToOutputConsole("Loaded GPR2 with " + gprText, Color.WHITE);
				} catch (Exception e1) {
					gpr2ValueLbl.setText(Constants.default16Zeroes);
					ShowDialog(e1.getMessage());
				}
			}
		});

		/*
		 * ActionListener To Load gpr3 Value when user clicked on the LD button next to
		 * gpr3
		 */
		gpr3LoadBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					gprText = LoadRegister(Registers.GPR3);
					gpr3ValueLbl.setText(gprText);
					opConsoleObj.WriteToOutputConsole("Loaded GPR3 with " + gprText, Color.WHITE);
				} catch (Exception e1) {
					gpr3ValueLbl.setText(Constants.default16Zeroes);
					ShowDialog(e1.getMessage());
				}
			}
		});

		/*
		 * ActionListener To show help instructions to run the simulator when user
		 * clicked on the Help button
		 */
		helpBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					new PopUps().ShowHelp();
				} catch (Exception e1) {
					ShowDialog(e1.getLocalizedMessage());
				}
			}
		});

		/*
		 * ActionListener To Load PC Value when user clicked on the LD button next to PC
		 */
		pcLoadBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					pcText = LoadRegister(Registers.PC);
					pcValueLbl.setText(pcText);
					opConsoleObj.WriteToOutputConsole("Loaded PC with " + pcText, Color.WHITE);
				} catch (Exception ex) {
					pcValueLbl.setText(Constants.default12Zeroes);
					ShowDialog(ex.getMessage());

				}
			}
		});

		/*
		 * ActionListener To Load Index 1 when user clicked on the LD button next to
		 * IXR1
		 */
		ixr1LoadBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ixrText = LoadRegister(Registers.IXR1);
					ixr1ValueLbl.setText(ixrText);
					opConsoleObj.WriteToOutputConsole("Loaded IXR1 with " + ixrText, Color.WHITE);
				} catch (Exception ex) {
					pcValueLbl.setText(Constants.default16Zeroes);
					ShowDialog(ex.getMessage());
				}
			}
		});

		/*
		 * ActionListener To Load Index 2 when user clicked on the LD button next to
		 * IXR2
		 */

		ixr2LoadBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ixrText = LoadRegister(Registers.IXR2);
					ixr2ValueLbl.setText(ixrText);
					opConsoleObj.WriteToOutputConsole("Loaded IXR2 with " + ixrText, Color.WHITE);
				} catch (Exception ex) {
					pcValueLbl.setText(Constants.default16Zeroes);
					ShowDialog(ex.getMessage());
				}
			}
		});

		/*
		 * ActionListener To Load Index 3 when user clicked on the LD button next to
		 * IXR3
		 */

		ixr3LoadBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ixrText = LoadRegister(Registers.IXR3);
					ixr3ValueLbl.setText(ixrText);
					opConsoleObj.WriteToOutputConsole("Loaded IXR3 with " + ixrText, Color.WHITE);
				} catch (Exception ex) {
					pcValueLbl.setText(Constants.default16Zeroes);
					ShowDialog(ex.getMessage());
				}
			}
		});

		/*
		 * ActionListener To Load Index 2 when user clicked on the LD button next to
		 * IXR2
		 */
		marLoadBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					marText = LoadRegister(Registers.MAR);
					marValueLbl.setText(marText);
					opConsoleObj.WriteToOutputConsole("Set MAR to " + marText, Color.WHITE);
				} catch (Exception ex) {
					marValueLbl.setText(Constants.default12Zeroes);
					ShowDialog(ex.getMessage());
				}
			}
		});

		/*
		 * ActionListener To Load MBR value when user clicked on the LD button next to
		 * MBR
		 */
		mbrLoadBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					mbrText = LoadRegister(Registers.MBR);
					mbrValueLbl.setText(mbrText);
					opConsoleObj.WriteToOutputConsole("Set MBR to " + mbrText, Color.WHITE);
				} catch (Exception ex) {
					mbrValueLbl.setText(Constants.default16Zeroes);
					ShowDialog(ex.getMessage());
				}
			}
		});

		/*
		 * ActionListener To Load MBR value fetched from memory for the address
		 * specified in MAR when user clicked on the Load button
		 */
		loadBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					mbrText = LoadRegisterFromMemory();
					mbrValueLbl.setText(mbrText);
					opConsoleObj.WriteToOutputConsole("Set MBR to " + mbrText, Color.WHITE);
				} catch (Exception ex) {
					mbrValueLbl.setText(Constants.default16Zeroes);
					ShowDialog(ex.getMessage());
				}
			}
		});

		/*
		 * ActionListener To Store MBR value in to the Memory[MAR] when user clicked on
		 * the Store button
		 */
		storeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Memory.StoreIntoMemory(marText, mbrText);
				} catch (Exception e2) {
					ShowDialog(e2.getLocalizedMessage());
				}
			}
		});

		/*
		 * ActionListener To Run program step by step (instruction by instruction) when
		 * user clicked on the Step button
		 */
		singleStepBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Simulator step = new Simulator();
				try {
					if (!Simulator.haltTriggered) {
						step.StepIntoTheInstruction();
					}
				} catch (Exception e1) {
					ShowDialog(e1.getLocalizedMessage());
				}
			}
		});

		/* CC bit-yet to complete in Project phase 3 */
		ccLoadBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ccText = LoadRegister(Registers.CC);
					ccValueLbl.setText(ccText);
					opConsoleObj.WriteToOutputConsole("Loaded CC with " + ccText, Color.WHITE);

				} catch (Exception e2) {
					ccValueLbl.setText(Constants.default4Zeroes);
					ShowDialog(e2.getLocalizedMessage());
				}
			}
		});

	}

	/*
	 * Method to initialize the frame components i.e (creating objects for all the
	 * components)
	 */
	private static void InitializeFrameComponents() {
		frame = new JFrame("Group10");
		frame.getContentPane().setBackground(SystemColor.activeCaption);
		frame.setSize(840, 560);
		frame.getContentPane().setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Labels
		gpr0Lbl = new JLabel("GPR0");
		gpr0Lbl.setHorizontalAlignment(SwingConstants.LEFT);
		gpr0Lbl.setForeground(Color.black);
		gpr0Lbl.setBounds(86, 40, 33, 14);

		gpr0ValueLbl = new JLabel(Constants.default16Zeroes);
		gpr0ValueLbl.setBounds(130, 40, 259, 20);
		gpr0ValueLbl.setForeground(SystemColor.activeCaptionText);
		gpr0ValueLbl.setFont(new Font("Calibri", Font.BOLD, 18));

		gpr1Lbl = new JLabel("GPR1");
		gpr1Lbl.setForeground(Color.black);
		gpr1Lbl.setBounds(86, 70, 33, 14);

		gpr1ValueLbl = new JLabel(Constants.default16Zeroes);
		gpr1ValueLbl.setBounds(129, 70, 260, 20);
		gpr1ValueLbl.setForeground(SystemColor.activeCaptionText);
		gpr1ValueLbl.setFont(new Font("Calibri", Font.BOLD, 18));

		gpr2Lbl = new JLabel("GPR2");
		gpr2Lbl.setBounds(86, 100, 33, 14);
		gpr2Lbl.setForeground(Color.black);

		gpr2ValueLbl = new JLabel(Constants.default16Zeroes);
		gpr2ValueLbl.setBounds(129, 100, 260, 20);
		gpr2ValueLbl.setForeground(SystemColor.activeCaptionText);
		gpr2ValueLbl.setFont(new Font("Calibri", Font.BOLD, 18));

		gpr3Lbl = new JLabel("GPR3");
		gpr3Lbl.setBounds(86, 130, 33, 14);
		gpr3Lbl.setForeground(Color.black);

		gpr3ValueLbl = new JLabel(Constants.default16Zeroes);
		gpr3ValueLbl.setBounds(128, 130, 261, 20);
		gpr3ValueLbl.setForeground(SystemColor.activeCaptionText);
		gpr3ValueLbl.setFont(new Font("Calibri", Font.BOLD, 18));

		ixr1Lbl = new JLabel("IXR 1");
		ixr1Lbl.setBounds(86, 165, 46, 14);
		ixr1Lbl.setForeground(Color.black);

		ixr2Lbl = new JLabel("IXR 2");
		ixr2Lbl.setBounds(86, 195, 46, 14);
		ixr2Lbl.setForeground(Color.black);

		ixr3Lbl = new JLabel("IXR 3");
		ixr3Lbl.setBounds(86, 225, 46, 14);
		ixr3Lbl.setForeground(Color.black);

		ixr1ValueLbl = new JLabel(Constants.default16Zeroes);
		ixr1ValueLbl.setBounds(129, 165, 260, 20);
		ixr1ValueLbl.setForeground(SystemColor.activeCaptionText);
		ixr1ValueLbl.setFont(new Font("Calibri", Font.BOLD, 18));

		ixr2ValueLbl = new JLabel(Constants.default16Zeroes);
		ixr2ValueLbl.setBounds(129, 195, 260, 20);
		ixr2ValueLbl.setForeground(SystemColor.activeCaptionText);
		ixr2ValueLbl.setFont(new Font("Calibri", Font.BOLD, 18));

		ixr3ValueLbl = new JLabel(Constants.default16Zeroes);
		ixr3ValueLbl.setBounds(129, 225, 260, 20);
		ixr3ValueLbl.setForeground(SystemColor.activeCaptionText);
		ixr3ValueLbl.setFont(new Font("Calibri", Font.BOLD, 18));

		oprInput = new JTextField();
		oprInput.setBounds(126, 265, 86, 20);
		oprInput.setColumns(10);

		oprInputLbl = new JLabel("Operation");
		oprInputLbl.setBounds(146, 290, 66, 14);

		gprInput = new JTextField();
		gprInput.setBounds(222, 265, 40, 20);
		gprInput.setColumns(2);

		gprInputLbl = new JLabel("GPR");
		gprInputLbl.setBounds(232, 290, 26, 14);

		ixrInput = new JTextField();
		ixrInput.setBounds(272, 265, 38, 20);
		ixrInput.setColumns(10);

		ixrInputLbl = new JLabel("IXR");
		ixrInputLbl.setBounds(285, 290, 28, 14);

		indexInput = new JTextField();
		indexInput.setBounds(320, 265, 18, 20);
		indexInput.setColumns(10);

		indexInputLbl = new JLabel("I");
		indexInputLbl.setBounds(327, 290, 4, 14);

		addrInputLbl = new JLabel("Address");
		addrInputLbl.setBounds(371, 290, 63, 14);

		addrInput = new JTextField();
		addrInput.setBounds(348, 265, 86, 20);
		addrInput.setColumns(10);

		pcLbl = new JLabel("PC");
		pcLbl.setBounds(475, 40, 46, 14);

		pcValueLbl = new JLabel(Constants.default12Zeroes);
		pcValueLbl.setBounds(545, 40, 198, 20);
		pcValueLbl.setFont(new Font("Calibri", Font.BOLD, 18));

		marLbl = new JLabel("MAR");
		marLbl.setBounds(475, 70, 46, 14);

		marValueLbl = new JLabel(Constants.default12Zeroes);
		marValueLbl.setBounds(545, 70, 198, 20);
		marValueLbl.setFont(new Font("Calibri", Font.BOLD, 18));

		mbrLbl = new JLabel("MBR");
		mbrLbl.setBounds(475, 106, 46, 14);

		mbrValueLbl = new JLabel(Constants.default16Zeroes);
		mbrValueLbl.setBounds(545, 100, 241, 20);
		mbrValueLbl.setFont(new Font("Calibri", Font.BOLD, 18));

		irLbl = new JLabel("IR");
		irLbl.setBounds(475, 136, 46, 14);

		irValueLbl = new JLabel(Constants.default16Zeroes);
		irValueLbl.setBounds(545, 130, 241, 20);
		irValueLbl.setFont(new Font("Calibri", Font.BOLD, 18));

		mfrLbl = new JLabel("MFR");
		mfrLbl.setBounds(475, 160, 46, 14);

		mfrValueLbl = new JLabel(Constants.default4Zeroes);
		mfrValueLbl.setBounds(545, 160, 241, 20);
		mfrValueLbl.setFont(new Font("Calibri", Font.BOLD, 18));

		prvlgLbl = new JLabel("Privliged");
		prvlgLbl.setBounds(475, 190, 58, 14);

		prvlgValueLbl = new JLabel(Constants.defaultSingleZero);
		prvlgValueLbl.setBounds(545, 190, 46, 20);
		prvlgValueLbl.setFont(new Font("Calibri", Font.BOLD, 18));

		// Buttons
		gpr0LoadBtn = new JButton("LD");
		gpr0LoadBtn.setFont(new Font("Tahoma", Font.PLAIN, 9));
		gpr0LoadBtn.setBounds(371, 40, 18, 14);
		gpr0LoadBtn.setMargin(new Insets(0, 0, 0, -3));

		gpr1LoadBtn = new JButton("LD");
		gpr1LoadBtn.setFont(new Font("Tahoma", Font.PLAIN, 9));
		gpr1LoadBtn.setBounds(371, 70, 18, 14);
		gpr1LoadBtn.setMargin(new Insets(0, 0, 0, -3));

		gpr2LoadBtn = new JButton("LD");
		gpr2LoadBtn.setFont(new Font("Tahoma", Font.PLAIN, 9));
		gpr2LoadBtn.setBounds(371, 100, 18, 14);
		gpr2LoadBtn.setMargin(new Insets(0, 0, 0, -3));

		gpr3LoadBtn = new JButton("LD");
		gpr3LoadBtn.setFont(new Font("Tahoma", Font.PLAIN, 9));
		gpr3LoadBtn.setBounds(371, 130, 18, 14);
		gpr3LoadBtn.setMargin(new Insets(0, 0, 0, -3));

		ixr1LoadBtn = new JButton("LD");
		ixr1LoadBtn.setFont(new Font("Tahoma", Font.PLAIN, 9));
		ixr1LoadBtn.setBounds(371, 165, 18, 14);
		ixr1LoadBtn.setMargin(new Insets(0, 0, 0, -3));

		ixr2LoadBtn = new JButton("LD");

		ixr2LoadBtn.setFont(new Font("Tahoma", Font.PLAIN, 9));
		ixr2LoadBtn.setBounds(371, 195, 18, 14);
		ixr2LoadBtn.setMargin(new Insets(0, 0, 0, -3));

		ixr3LoadBtn = new JButton("LD");

		ixr3LoadBtn.setFont(new Font("Tahoma", Font.PLAIN, 9));
		ixr3LoadBtn.setBounds(371, 225, 18, 14);
		ixr3LoadBtn.setMargin(new Insets(0, 0, 0, -3));

		runBtn = new JButton("Run");
		runBtn.setBounds(611, 333, 70, 25);
		runBtn.setEnabled(true);

		fileLoadBtn = new JButton("IPL");
		fileLoadBtn.setBounds(451, 333, 70, 25);

		pcLoadBtn = new JButton("LD");

		pcLoadBtn.setFont(new Font("Tahoma", Font.PLAIN, 9));
		pcLoadBtn.setMargin(new Insets(0, 0, 0, -3));
		pcLoadBtn.setBounds(778, 40, 18, 14);

		marLoadBtn = new JButton("LD");

		marLoadBtn.setMargin(new Insets(0, 0, 0, -3));
		marLoadBtn.setFont(new Font("Tahoma", Font.PLAIN, 9));
		marLoadBtn.setBounds(778, 70, 18, 14);

		mbrLoadBtn = new JButton("LD");

		mbrLoadBtn.setMargin(new Insets(0, 0, 0, -3));
		mbrLoadBtn.setFont(new Font("Tahoma", Font.PLAIN, 9));
		mbrLoadBtn.setBounds(778, 100, 18, 14);

		helpBtn = new JButton("Help");

		helpBtn.setMargin(new Insets(0, 0, 0, -3));
		helpBtn.setFont(new Font("Yu Gothic UI", Font.BOLD, 12));
		helpBtn.setBounds(743, 387, 40, 23);
		helpBtn.setEnabled(false);

// 		  Add To Frame Here

		frame.getContentPane().add(gpr0Lbl);
		frame.getContentPane().add(gpr0ValueLbl);

		frame.getContentPane().add(gpr1Lbl);
		frame.getContentPane().add(gpr1ValueLbl);

		frame.getContentPane().add(gpr2Lbl);
		frame.getContentPane().add(gpr2ValueLbl);

		frame.getContentPane().add(gpr3Lbl);
		frame.getContentPane().add(gpr3ValueLbl);

		frame.getContentPane().add(fileLoadBtn);
		frame.getContentPane().add(runBtn);

		frame.getContentPane().add(oprInput);
		frame.getContentPane().add(gprInput);
		frame.getContentPane().add(ixrInput);
		frame.getContentPane().add(indexInput);
		frame.getContentPane().add(addrInput);

		frame.getContentPane().add(oprInputLbl);
		frame.getContentPane().add(gprInputLbl);
		frame.getContentPane().add(ixrInputLbl);
		frame.getContentPane().add(indexInputLbl);
		frame.getContentPane().add(addrInputLbl);

		frame.getContentPane().add(ixr1Lbl);
		frame.getContentPane().add(ixr1ValueLbl);

		frame.getContentPane().add(ixr2Lbl);
		frame.getContentPane().add(ixr2ValueLbl);

		frame.getContentPane().add(ixr3Lbl);
		frame.getContentPane().add(ixr3ValueLbl);

		frame.getContentPane().add(gpr0LoadBtn);
		frame.getContentPane().add(gpr1LoadBtn);
		frame.getContentPane().add(gpr2LoadBtn);
		frame.getContentPane().add(gpr3LoadBtn);
		frame.getContentPane().add(ixr1LoadBtn);
		frame.getContentPane().add(ixr2LoadBtn);
		frame.getContentPane().add(ixr3LoadBtn);

		frame.getContentPane().add(pcLbl);
		frame.getContentPane().add(marLbl);
		frame.getContentPane().add(mbrLbl);
		frame.getContentPane().add(irLbl);
		frame.getContentPane().add(mfrLbl);
		frame.getContentPane().add(prvlgLbl);

		frame.getContentPane().add(pcValueLbl);
		frame.getContentPane().add(marValueLbl);
		frame.getContentPane().add(mbrValueLbl);
		frame.getContentPane().add(irValueLbl);
		frame.getContentPane().add(mfrValueLbl);
		frame.getContentPane().add(prvlgValueLbl);

		frame.getContentPane().add(pcLoadBtn);
		frame.getContentPane().add(marLoadBtn);
		frame.getContentPane().add(mbrLoadBtn);

		frame.getContentPane().add(helpBtn);

		singleStepBtn = new JButton("Step");
		singleStepBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		singleStepBtn.setBounds(531, 333, 70, 25);
		frame.getContentPane().add(singleStepBtn);

		loadBtn = new JButton("Load");

		loadBtn.setBounds(451, 263, 70, 25);
		frame.getContentPane().add(loadBtn);

		storeBtn = new JButton("Store");

		storeBtn.setBounds(531, 263, 70, 25);
		frame.getContentPane().add(storeBtn);

		ccLabel = new JLabel("CC");
		ccLabel.setBounds(475, 225, 46, 14);
		frame.getContentPane().add(ccLabel);

		ccValueLbl = new JLabel("0000");
		ccValueLbl.setFont(new Font("Calibri", Font.BOLD, 18));
		ccValueLbl.setBounds(545, 223, 86, 20);
		frame.getContentPane().add(ccValueLbl);

		ccLoadBtn = new JButton("LD");
		ccLoadBtn.setFont(new Font("Tahoma", Font.PLAIN, 9));

		ccLoadBtn.setMargin(new Insets(0, 0, 0, -3));
		ccLoadBtn.setBounds(778, 225, 18, 14);
		frame.getContentPane().add(ccLoadBtn);

		keyboardArea = new JTextArea();
		keyboardArea.setBounds(26, 333, 208, 115);
		frame.getContentPane().add(keyboardArea);

		printerArea = new JTextArea();
		printerArea.setBounds(244, 333, 190, 115);
		printerArea.setEditable(false);
		frame.getContentPane().add(printerArea);

		helpTextLabel = new JLabel("");
		helpTextLabel.setBounds(451, 392, 274, 18);
		frame.getContentPane().add(helpTextLabel);

		frame.setVisible(true);
		frame.setResizable(false);

	}

	/*
	 * Set default values to the registers based on their address capacity
	 */
	private static void SetDefaultValues() {
		gpr0ValueLbl.setText(Constants.default16Zeroes);
		gpr1ValueLbl.setText(Constants.default16Zeroes);
		gpr2ValueLbl.setText(Constants.default16Zeroes);
		gpr3ValueLbl.setText(Constants.default16Zeroes);
		ixr1ValueLbl.setText(Constants.default16Zeroes);
		ixr2ValueLbl.setText(Constants.default16Zeroes);
		ixr3ValueLbl.setText(Constants.default16Zeroes);
		pcValueLbl.setText(Constants.default12Zeroes);
		marValueLbl.setText(Constants.default12Zeroes);
		mbrValueLbl.setText(Constants.default16Zeroes);
		irValueLbl.setText(Constants.default16Zeroes);
		mfrValueLbl.setText(Constants.default4Zeroes);
		prvlgValueLbl.setText(Constants.defaultSingleZero);
	}

	private static void ShowDialog(String message) {
		new PopUps().ShowPop(message);
	}

	private static void ShowHelp() {
		try {
			new PopUps().ShowHelp();
		} catch (Exception e) {
			ShowDialog(e.getLocalizedMessage());
		}
	}

	// Loads the specified register by reading the user input
	public static String LoadRegister(Registers registerName) throws Exception {
		try {
			UserInputReader reader = new UserInputReader(oprInput.getText(), gprInput.getText(), ixrInput.getText(),
					indexInput.getText(), addrInput.getText());
			return reader.GetValueForSpecificRegister(registerName);
		} catch (Exception e) {
			throw new Exception(e.getLocalizedMessage());
		}
	}

	// Gets the address value from the Memory at the location
	public static String LoadRegisterFromMemory() throws Exception {
		try {
			return Memory.GetFromMemory(marText);
		} catch (Exception e) {
			throw new Exception("Address " + marText + " not found");
		}
	}

	/*
	 * Set different registers with the value passed to the parameter "value"
	 */
	public static void SetRegister(Registers register, String value) throws Exception {
		String outputText = "Set " + register + " to value :" + value;
		switch (register) {
		case GPR0: {
			gpr0ValueLbl.setText(value);
			opConsoleObj.WriteToOutputConsole(outputText, null);
		}
			break;
		case GPR1: {
			gpr1ValueLbl.setText(value);
			opConsoleObj.WriteToOutputConsole(outputText, null);
		}
			break;
		case GPR2: {
			gpr2ValueLbl.setText(value);
			opConsoleObj.WriteToOutputConsole(outputText, null);
		}
			break;
		case GPR3: {
			gpr3ValueLbl.setText(value);
			opConsoleObj.WriteToOutputConsole(outputText, null);
		}
			break;
		case IXR1: {
			ixr1ValueLbl.setText(value);
			opConsoleObj.WriteToOutputConsole(outputText, null);
		}
			break;
		case IXR2: {
			ixr2ValueLbl.setText(value);
			opConsoleObj.WriteToOutputConsole(outputText, null);
		}
		case IXR3: {
			ixr3ValueLbl.setText(value);
			opConsoleObj.WriteToOutputConsole(outputText, null);
		}
		case PC: {
			pcValueLbl.setText(value);
		}
			break;
		case MAR: {
			marValueLbl.setText(value);
		}
			break;
		case MBR: {
			mbrValueLbl.setText(value);
		}
			break;
		case IR: {
			irValueLbl.setText(value);
		}
			break;

		default:
			throw new IllegalArgumentException("Unexpected value: " + register);
		}
	}

	public static void SetCCRegister(int position, boolean flag) {
		StringBuffer prevCCLable = new StringBuffer(ccValueLbl.getText());
		prevCCLable.replace(position - 1, position, flag ? "1" : "0");
		ccValueLbl.setText(prevCCLable.toString());
		opConsoleObj.WriteToOutputConsole("Set CC[" + position + "] to " + flag, null);

	}

	public static void SetPrinterText(String value) {
		FrontPanel.printerArea.setText(value);
	}

	public static String GetKeyboardInput() {
		return FrontPanel.keyboardArea.getText();
	}
	
	
}
