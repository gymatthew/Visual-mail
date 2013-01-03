package ui;

import info.MailInfo;
import info.UserInfo;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import mail.MailBox;

public class LoginWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private final static int GAP = 5;
	private final static int WIDTH = 300;
	private final static int HEIGHT = 250;
	private final static int LOCATION_X = 500;
	private final static int LOCATION_Y = 300;

	protected JPanel main_panel;
	protected JPanel button_panel;
	protected JLabel name_label;
	protected JLabel passwnd_label;
	protected JTextField name_textfield;
	protected JPasswordField passwnd_textfield;
	protected JButton ok_button;
	protected JButton clear_button;

	public LoginWindow() {

		main_panel = new JPanel();
		button_panel = new JPanel();
		name_label = new JLabel("Please enter your email:", JLabel.CENTER);
		passwnd_label = new JLabel("Please enter your Password:", JLabel.CENTER);
		name_textfield = new JTextField(30);
		name_textfield.setHorizontalAlignment(JTextField.CENTER);
		passwnd_textfield = new JPasswordField(30);
		passwnd_textfield.setHorizontalAlignment(JPasswordField.CENTER);
		ok_button = new JButton("Ok");
		clear_button = new JButton("Clear");

		main_panel.add(name_label);
		main_panel.add(name_textfield);
		main_panel.add(passwnd_label);
		main_panel.add(passwnd_textfield);
		main_panel.add(button_panel);
		button_panel.add(ok_button);
		button_panel.add(clear_button);
		main_panel.setLayout(new GridLayout(5, 1, GAP, GAP));
		this.add(main_panel);

		ok_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!check()) {
					return;
				}
				accountLogin();
			}

		});

		clear_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				name_textfield.setText("");
				passwnd_textfield.setText("");
			}
		});

		this.setVisible(true);
		this.setSize(WIDTH, HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(LOCATION_X, LOCATION_Y);
	}

	protected void accountLogin() {
		UserInfo.getInstance().setUserName(name_textfield.getText());
		UserInfo.getInstance().setPassword(new String(passwnd_textfield.getPassword()));
		ArrayList<MailInfo> mailInfoList = MailBox.getInstance().ReadMail(
				MailBox.INBOX);
		if (mailInfoList == null) {
			JOptionPane.showMessageDialog(null, "Email or password is incorrect!",
					"error", JOptionPane.ERROR_MESSAGE);
		} else {
			new ContentWindow(mailInfoList);
			dispose();
		}
	}

	public boolean check() {
		String name = name_textfield.getText();
		String passwnd = new String(passwnd_textfield.getPassword());
		if (name == null || name.equals("")) {
			JOptionPane.showMessageDialog(null, "Email cannot be null!",
					"error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (passwnd == null || passwnd.equals("")) {
			JOptionPane.showMessageDialog(null, "Password cannot be null!",
					"error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (!name.contains("@") || name.length() < 3) {
			JOptionPane.showMessageDialog(null, "Email format is incorrect!",
					"error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		new LoginWindow();
	}
}
