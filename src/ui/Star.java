package ui;

import info.MailInfo;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Star extends JButton {

	private static final long serialVersionUID = 1L;
	private static final String STAR_IMAGE_PATH = "images\\star.png";

	protected MailInfo mailInfo;

	protected ImageIcon image;

	public Star(MailInfo mailInfo) {
		this.mailInfo = mailInfo;
		image = new ImageIcon(STAR_IMAGE_PATH);
		this.setIcon(image);
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
		this.setSize(image.getIconWidth(), image.getIconHeight());
	}

	public MailInfo getMailInfo() {
		return mailInfo;
	}

	public void setMailInfo(MailInfo mailInfo) {
		this.mailInfo = mailInfo;
	}

}
