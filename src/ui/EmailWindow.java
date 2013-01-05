package ui;

import java.awt.Font;
import java.awt.GridLayout;

import info.MailInfo;
import info.Participant;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class EmailWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final String EMAIL_IMAGE_PATH = "images/email.jpg";
	private JLayeredPane layeredPane;
	private JPanel bkg_panel;
	private JPanel panel;
	private JLabel bkg;
	private JLabel subject;
	private JLabel from;
	private JLabel recipients;
	private JLabel date;
	private JLabel content;

	public EmailWindow(MailInfo mailInfo) {
		
		layeredPane = new JLayeredPane();
		
		ImageIcon image = new ImageIcon(EmailWindow.class.getClassLoader().getResource(EMAIL_IMAGE_PATH));
		bkg = new JLabel(image);
		bkg_panel = new JPanel();
		bkg_panel.add(bkg);
		bkg_panel.setBounds(0, 0, image.getIconWidth(), image.getIconHeight());
		
		layeredPane.add(bkg_panel, JLayeredPane.DEFAULT_LAYER);
		
		subject = new JLabel("  Subject:  " + mailInfo.getSubject());
		from = new JLabel("  From:  " + mailInfo.getFrom().getName() + "<"
				+ mailInfo.getFrom().getAdress() + ">");
		String recipients_label_text = "  To:  ";
		Participant[] participants = mailInfo.getRecipients();
		for (int i = 0, n = participants.length; i < n; i++) {
			recipients_label_text += participants[i].getName() + "<"
					+ participants[i].getAdress() + ">;";
		}
		recipients = new JLabel(recipients_label_text);
		date = new JLabel("  Date:  " + mailInfo.getDate());
		content = new JLabel("  Content:  " + mailInfo.getContent());
		
		Font font = new  Font("Serief", Font.BOLD, 16);
		subject.setFont(font);
		from.setFont(font);
		recipients.setFont(font);
		date.setFont(font);
		content.setFont(font);

		panel = new JPanel();
		panel.setLayout(new GridLayout(6, 1));
		panel.add(subject);
		panel.add(from);
		panel.add(recipients);
		panel.add(date);
		panel.add(content);
		panel.setSize(image.getIconWidth(), image.getIconHeight());
		panel.setOpaque(false);
		
		layeredPane.add(panel, JLayeredPane.MODAL_LAYER);
		
		this.add(layeredPane);
		this.setVisible(true);
		this.setSize(image.getIconWidth(), image.getIconHeight());
		this.setLocationRelativeTo(null);
	}

}
