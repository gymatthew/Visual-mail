package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import info.MailInfo;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Star extends JButton {

	private static final long serialVersionUID = 1L;
	private static final String STAR_IMAGE_PATH = "images/star.png";

	protected MailInfo mailInfo;

	protected ImageIcon image;

	public Star(final MailInfo mailInfo) {
		this.mailInfo = mailInfo;
		image = new ImageIcon(Star.class.getClassLoader().getResource(STAR_IMAGE_PATH));
		this.setIcon(image);
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
		this.setSize(image.getIconWidth(), image.getIconHeight());

		String toolTipText = "<html>[Auther] " + mailInfo.getFrom().getName()
				+ "<br>" + "[Subject] " + mailInfo.getSubject() + "<br>"
				+ "[Date] " + mailInfo.getDate().toString() + "</html>";
		this.setToolTipText(toolTipText);
		
		this.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new EmailWindow(mailInfo);
			}
		});
	}

	public MailInfo getMailInfo() {
		return mailInfo;
	}

	public void setMailInfo(MailInfo mailInfo) {
		this.mailInfo = mailInfo;
	}
	
	
}
