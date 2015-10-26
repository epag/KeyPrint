/*
 * ShhListen
 * @author Evan Pagryzinski
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;
import java.io.FileWriter;
import java.util.Scanner;

import javax.swing.*;

public class ShhListen extends JFrame implements KeyListener, ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static String name;
	int position = 0;
	float[] timeToPress = new float[100];
	float[] timeBetweenKeys = new float[100];
	char[] keyPressed = new char[100];
	float startTime = 0;
	float endTime = 0;
	float prevKey = 0;
	JTextArea displayArea;
	JTextField typingArea;
	static final String newline = System.getProperty("line.separator");

	public static void main(String[] args) {

		/* Use an appropriate Look and Feel */
		try {
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		/* Turn off metal's use of bold fonts */
		UIManager.put("swing.boldMetal", Boolean.FALSE);

		// Schedule a job for event dispatch thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		System.out.println("Please type your name");
		Scanner s = new Scanner(System.in);
		name = s.nextLine();
		s.close();
		// Create and set up the window.
		ShhListen frame = new ShhListen();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set up the content pane.
		frame.addComponentsToPane();

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	private void addComponentsToPane() {

		JButton button = new JButton("Clear");
		button.addActionListener(this);

		typingArea = new JTextField(20);
		typingArea.addKeyListener(this);

		// Uncomment this if you wish to turn off focus
		// traversal. The focus subsystem consumes
		// focus traversal keys, such as Tab and Shift Tab.
		// If you uncomment the following line of code, this
		// disables focus traversal and the Tab events will
		// become available to the key event listener.
		// typingArea.setFocusTraversalKeysEnabled(false);

		displayArea = new JTextArea();
		displayArea
				.append("PLease type the following setence and then hit ENTER: \nI am a program written at BoilerMake\n");
		displayArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(displayArea);
		scrollPane.setPreferredSize(new Dimension(375, 125));

		getContentPane().add(typingArea, BorderLayout.PAGE_START);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		getContentPane().add(button, BorderLayout.PAGE_END);
	}

	public ShhListen() {
	}

	public void writeCSVFile(String fileName) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(fileName);

			fw.append("Letter, Time pressed for, Time between presses");
			fw.append("\n");

			for (int x = 0; x < 100; x++) {
				fw.append(String.valueOf(x));
				fw.append(",");
				fw.append(String.valueOf(timeToPress[x]));
				fw.append(",");
				fw.append(String.valueOf(timeBetweenKeys[x]));
				fw.append('\n');
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				fw.flush();
				fw.close();
				String[] argz = new String[1];
				argz[0] = name;
				Regression.main(argz);
				Compare.main(argz);
			} catch (Exception e) {

			}
		}
	}

	/** Handle the key pressed event from the text field. */
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == 10) {
			writeCSVFile("dataReg.csv");
			System.exit(0);
		}
		keyPressed[position] = e.getKeyChar();
		startTime = System.nanoTime();
		timeBetweenKeys[position] = (startTime - prevKey) / 1000000;
		timeBetweenKeys[0] = 0;
		if (e.getKeyCode() != 8 && e.getKeyCode() != 16) {
			if (position != 0) {
				if (keyPressed[position - 1] == ' ') {
					//System.out.println("Between key SPACE" + " and "
					//		+ keyPressed[position] + " is "
					//		+ timeBetweenKeys[position]);
				} else if (keyPressed[position] == ' ') {
					//System.out.println("Between key "
					//		+ keyPressed[position - 1] + " and SPACE" + " is "
					//		+ timeBetweenKeys[position]);
				} else {
					//System.out.println("Between key "
					//		+ keyPressed[position - 1] + " and "
					//		+ keyPressed[position] + " is "
					//		+ timeBetweenKeys[position]);
				}

			}
			position++;
			displayInfo(e, "KEY PRESSED: ");
		}
		if (e.getKeyCode() == 8) {
			//System.out.println("CHARACTER DELETED");
			position--;
		}
	}

	/** Handle the key released event from the text field. */
	public void keyReleased(KeyEvent e) {
		prevKey = System.nanoTime();
		endTime = System.nanoTime();
		timeToPress[position - 1] = (endTime - startTime) / 1000000;
		if (e.getKeyCode() != 8 && e.getKeyCode() != 16) {
			if (keyPressed[position - 1] == ' ') {
				//System.out.println("You pressed SPACE" + " for "
				//		+ timeToPress[position - 1]);
				displayInfo(e, "KEY RELEASED: ");
			} else {
				//System.out.println("You pressed  " + keyPressed[position - 1]
				//		+ " for " + timeToPress[position - 1]);
				displayInfo(e, "KEY RELEASED: ");
			}
		}
	}

	/** Handle the button click. */
	public void actionPerformed(ActionEvent e) {
		// Clear the text components.
		displayArea.setText("");

		typingArea.setText("");

		// Return the focus to the typing area.
		typingArea.requestFocusInWindow();
	}

	/*
	 * We have to jump through some hoops to avoid trying to print non-printing
	 * characters such as Shift. (Not only do they not print, but if you put
	 * them in a String, the characters afterward won't show up in the text
	 * area.)
	 */
	private void displayInfo(KeyEvent e, String keyStatus) {

		// You should only rely on the key char if the event
		// is a key typed event.
		/*int id = e.getID();
		String keyString;
		if (e.getKeyCode() != 8 && e.getKeyCode() != 16 && e.getKeyChar() != 17) {
			if (id == KeyEvent.KEY_TYPED) {
				char c = e.getKeyChar();
				keyString = "key character = '" + c + "'";
			} else {
				int keyCode = e.getKeyCode();
				keyString = "key code = " + keyCode + " ("
						+ KeyEvent.getKeyText(keyCode) + ")";
			}

			// displayArea.append(keyStatus + newline
			// + "    " + keyString + newline
			// + "    " + System.nanoTime() + newline);
			// displayArea.setCaretPosition(displayArea.getDocument().getLength());
		} */
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}