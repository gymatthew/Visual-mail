package ui;

import info.MailInfo;
import info.UserInfo;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import mail.MailBox;

public class ContentWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final String BACKGROUND_IMAGE_PATH = "images\\background.jpg";
	private static final String TITLE = "WELCOME";

	private JLayeredPane layeredPane;
	private JPanel bkgPanel;
	private ImageIcon bkgImage;
	private JLabel bkgLabel;
	private ArrayList<Star> starList;

	public ContentWindow(ArrayList<MailInfo> mailInfoList) {
		init(mailInfoList);
	}

	protected void init(ArrayList<MailInfo> mailInfoList) {

		layeredPane = new JLayeredPane();
		bkgImage = new ImageIcon(BACKGROUND_IMAGE_PATH);
		bkgPanel = new JPanel();
		bkgLabel = new JLabel(bkgImage);
		
		this.setVisible(true);
		this.setLayeredPane(layeredPane);
		this.setSize(bkgImage.getIconWidth(), bkgImage.getIconHeight());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(getOwner());
		this.setResizable(false);
		this.setTitle(TITLE);
		
		
		bkgPanel.setBounds(0, 0, bkgImage.getIconWidth(),
				bkgImage.getIconHeight());
		bkgPanel.add(bkgLabel);

		layeredPane.add(bkgPanel, JLayeredPane.DEFAULT_LAYER);

		generateStars(mailInfoList);

		for (int i = 0, n = starList.size(); i < n; i++) {
			layeredPane.add(starList.get(i), JLayeredPane.MODAL_LAYER);
		}
	}

	protected void generateStars(ArrayList<MailInfo> mailInfoList) {
		Random random = new Random();
		starList = new ArrayList<Star>();
		for (int i = 0, n = mailInfoList.size(); i < n; i++) {
			Star star = new Star(mailInfoList.get(i));
			star.setLocation(random.nextInt(this.getWidth()),
					random.nextInt(this.getHeight()));
			starList.add(star);
		}
	}

	public static void main(String[] args) {
		UserInfo.getInstance().setUserName("visualmail@126.com");
		UserInfo.getInstance().setPassword("123457");
		ArrayList<MailInfo> mailInfoList = MailBox.getInstance().ReadMail(
				MailBox.INBOX);
		new ContentWindow(mailInfoList);
	}

}
