package dev.lanoxkodo;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 * @author LanoxKodo
 * 
 *	PasswordGenerator is a simple Java application to do what the name
 *	suggests, minimal GUI design, not aiming for bloat.
 * 
 *	By default, the program will generate passwords using the characters
 *	from [a-z][A-Z][0-9] ranges. The program allows the user to select
 *	some special characters as options for adding into the password
 *	generation logic.
 * 
 *	Program has a copy feature for ease of copying to the clipboard.
 */

@SuppressWarnings("serial")
public class PasswordGenerator extends JFrame implements ActionListener {

	List<String> optionalItems = Arrays.asList("`", "~", "!", "@", "#", "$", "%", "^", "&", "*", "+", "-", "=", "_", ":", ".", ",", "\\/",
		"[]", "()", "{}");
	List<JCheckBox> optionalCheckBoxes = new ArrayList<>();

	String selectedOptionalCharacters = "";

	JPanel pane = new JPanel(new GridBagLayout());
	JLabel optionalItemsLabel, passwordSectionLabel;
	JButton generateButton, copyButton;
	JTextArea passwordField = new JTextArea(6, 34);
	JScrollPane scrollField = new JScrollPane(pane);
	JTextField passwordLength = new JTextField(4);

	Font sulB = new Font("Segoe UI Light", Font.BOLD, 24);
	Color midGrey = Color.decode("#737373");
	Color lightGrey = Color.decode("#a6a6a6");
	Color lightRed = Color.decode("#ff6666");
	Color lightGreen = Color.decode("#85e085");

	Border border = BorderFactory.createLineBorder(midGrey);

	public static void main(String args[]) throws IOException
	{
		new PasswordGenerator();
	}

	private PasswordGenerator()
	{
		initApplication();
	}

	private ArrayList<Character> convertToCharArray(String input)
	{
		ArrayList<Character> charArray = new ArrayList<>();
		for (Character c : input.toCharArray())
		{
			charArray.add(c);
		}
		return charArray;
	}

	private void createPassword(int length)
	{
		String lowerCase = "abcdefghijklmnopqrstuvwxyz";
		String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String digits = "0123456789";

		ArrayList<Character> possibleCharacters = new ArrayList<>();
		possibleCharacters.addAll(convertToCharArray(lowerCase));
		possibleCharacters.addAll(convertToCharArray(upperCase));
		possibleCharacters.addAll(convertToCharArray(digits));
		if (!selectedOptionalCharacters.isEmpty()) possibleCharacters.addAll(convertToCharArray(selectedOptionalCharacters));

		SecureRandom random = new SecureRandom();
		random.setSeed(System.currentTimeMillis());
		Collections.shuffle(possibleCharacters, random);

		StringBuilder password = new StringBuilder();
		for (int a = 0; a < length; a++)
		{
			password.append(possibleCharacters.get(random.nextInt(possibleCharacters.size())));
		}

		passwordField.append(password.toString());
		selectedOptionalCharacters = "";
	}

	public void actionPerformed(ActionEvent action)
	{
		if (action.getSource() == copyButton) copy();
		if (action.getSource() == generateButton)
		{
			copyButton.setBackground(midGrey);
			copyButton.setText("Copy password");
			passwordField.setText("");
			checkInputs();
		}
	}

	private void checkInputs()
	{
		int desiredLength = 0;

		try
		{
			desiredLength = Integer.parseInt(passwordLength.getText());
		}
		catch (NumberFormatException e)
		{}

		if (desiredLength > 0)
		{
			for (JCheckBox checkBox : optionalCheckBoxes)
			{
				if (checkBox.isSelected()) selectedOptionalCharacters += checkBox.getText();
			}

			passwordLength.setBackground(lightGreen);
			createPassword(desiredLength);
		}
		else passwordLength.setBackground(lightRed);
	}

	private void copy()
	{
		if (!passwordField.getText().isEmpty())
		{
			StringSelection toCopy = new StringSelection(passwordField.getText());
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(toCopy, null);
			copyButton.setText("Copied to clipboard!");
			copyButton.setBackground(lightGreen);
		}
		else
		{
			copyButton.setText("No password to copy.");
			copyButton.setBackground(lightRed);
		}
	}

	private void initCheckBox(String label, GridBagConstraints grid)
	{
		JCheckBox checkBox = new JCheckBox(label);
		checkBox.setFont(sulB);
		checkBox.setBackground(lightGrey);
		checkBox.setFocusPainted(false);
		pane.add(checkBox, grid);
		optionalCheckBoxes.add(checkBox);
	}

	private void initApplication()
	{
		setLayout(new GridBagLayout());

		GridBagConstraints grid = new GridBagConstraints();
		grid.gridx = 0;
		grid.gridy = 0;
		grid.anchor = GridBagConstraints.WEST;

		optionalItemsLabel = new JLabel("Optional characters that can be used for password generation: ");
		optionalItemsLabel.setFont(sulB);
		pane.add(optionalItemsLabel, grid);

		int columnVal = 1;
		grid.insets = new Insets(0, 0, 0, 0);
		for (int iteration = 1; iteration <= optionalItems.size(); iteration++)
		{
			grid.gridy++;
			initCheckBox(optionalItems.get(iteration - 1), grid);

			if (iteration % 3 == 0)
			{
				grid.gridy = 0;
				grid.insets = new Insets(0, (65 * columnVal), 0, 0);
				columnVal++;
			}
		}

		grid.insets = new Insets(100, 0, 0, 0);
		grid.gridy = 6;
		passwordSectionLabel = new JLabel("Password length:");
		passwordSectionLabel.setFont(sulB);
		pane.add(passwordSectionLabel, grid);

		grid.insets = new Insets(100, 260, 0, 0);
		passwordLength.setFont(sulB);
		passwordLength.setBackground(midGrey);
		passwordLength.setBorder(border);
		pane.add(passwordLength, grid);

		grid.anchor = GridBagConstraints.EAST;
		copyButton = new JButton();
		copyButton.setFont(sulB);
		copyButton.setBackground(midGrey);
		copyButton.setText("Copy password");
		copyButton.setBorder(border);
		copyButton.setFocusPainted(false);
		pane.add(copyButton, grid);

		grid.anchor = GridBagConstraints.WEST;
		grid.insets = new Insets(5, 0, 0, 0);
		grid.gridy++;
		grid.gridx = 0;
		passwordField.setFont(sulB);
		passwordField.setBackground(midGrey);
		passwordField.setLineWrap(true);
		passwordField.setWrapStyleWord(true);

		scrollField = new JScrollPane(passwordField);
		scrollField.setFont(sulB);
		scrollField.setBackground(midGrey);
		scrollField.setBorder(border);
		scrollField.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		pane.add(scrollField, grid);

		grid.gridy++;
		generateButton = new JButton();
		grid.insets = new Insets(70, 0, 0, 0);
		generateButton.setFont(sulB);
		generateButton.setBackground(Color.decode("#AD94D3"));
		generateButton.setText("Generate unique password sequence");
		generateButton.setFocusPainted(false);
		grid.fill = GridBagConstraints.HORIZONTAL;
		grid.anchor = GridBagConstraints.SOUTH;
		pane.add(generateButton, grid);

		generateButton.addActionListener(this);
		copyButton.addActionListener(this);

		add(pane);
		pack();
		setTitle("Password Generator - (https://github.com/LanoxKodo/PasswordGenerator)");
		pane.setBackground(lightGrey);
		getContentPane().setBackground(lightGrey);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}