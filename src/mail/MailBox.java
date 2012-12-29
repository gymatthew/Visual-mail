package mail;

import info.Participant;
import info.UserInfo;
import info.MailInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Address;
import javax.mail.internet.InternetAddress;

public class MailBox {

	public final static String INBOX = "INBOX";
	public final static String OUTBOX = "OUTBOX";

	public ArrayList<MailInfo> ReadMail(final String type) {

		Properties props = new Properties();
		// 存储接收邮件服务器使用的协议
		props.setProperty(UserInfo.PROTOCOL_KEY, UserInfo.getInstance().getProtocol());
		// 设置接收邮件服务器的地址
		props.setProperty(UserInfo.HOST_KEY, UserInfo.getInstance().getHost());
		// 新建一个邮件会话.
		Session session = Session.getInstance(props);
		// 如果需要查看接收邮件的详细信息，需要设置Debug标志
		session.setDebug(false);

		ArrayList<MailInfo> mailList = new ArrayList<MailInfo>();

		try {
			Store store = session.getStore("pop3");
			store.connect("pop3.126.com", UserInfo.getInstance().getUserName(),
					UserInfo.getInstance().getPassword());

			// 获取邮件服务器的收件箱
			Folder folder = store.getFolder(type);
			// 以只读权限打开收件箱
			folder.open(Folder.READ_ONLY);

			// 获取收件箱中的邮件，也可以使用getMessage(int 邮件的编号)来获取具体某一封邮件
			Message message[] = folder.getMessages();
			for (int i = 0, n = message.length; i < n; i++) {
				MailInfo mailInfo = parseMessage(message[i]);
				if (mailInfo != null)
					mailList.add(mailInfo);
			}
			// 关闭连接
			folder.close(false);
			store.close();

		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		return mailList;
	}

	protected MailInfo parseMessage(Message message) {
		MailInfo mailInfo = new MailInfo();
		try {
			mailInfo.setSubject(message.getSubject());
			mailInfo.setDate(message.getSentDate());

			InternetAddress fromInterAddr = (InternetAddress) (message
					.getFrom())[0];
			Participant from = new Participant(fromInterAddr.getPersonal(),
					fromInterAddr.getAddress());
			mailInfo.setFrom(from);

			Address[] recipientsAddr = message.getAllRecipients();
			Participant[] recip = new Participant[recipientsAddr.length];
			for (int i = 0, n = recipientsAddr.length; i < n; i++) {
				InternetAddress recipientsInterAddr = (InternetAddress) recipientsAddr[i];
				recip[i] = new Participant(recipientsInterAddr.getPersonal(),
						recipientsInterAddr.getAddress());
			}
			mailInfo.setRecipients(recip);

			mailInfo.setContent(parseMessageContent(message));

		} catch (MessagingException e) {
			e.printStackTrace();
			return null;
		}
		return mailInfo;
	}

	protected String parseMessageContent(Part part) {

		String content = "";

		try {
			if (part.isMimeType("text/plain")) {
				content += (String) part.getContent();
//			} else if (part.isMimeType("text/html")) {
//				content += ((String) part.getContent());
			} else if (part.isMimeType("multipart/*")) {
				Multipart multipart = (Multipart) part.getContent();
				int count = multipart.getCount();
				for (int i = 0; i < count; i++) {
					content += parseMessageContent(multipart.getBodyPart(i));
					if (!content.equals(""))
						break;
				}
			} else if (part.isMimeType("message/rfc822")) {
				content += parseMessageContent((Part)part.getContent());
			}

		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return content;
	}

	public void testReadMail() {
		UserInfo.getInstance().setUserName("visualmail@126.com");
		UserInfo.getInstance().setPassword("123457");
		ArrayList<MailInfo> mailInfoList = this.ReadMail(MailBox.INBOX);
		for (int i = 0; i < mailInfoList.size(); i++) {
			MailInfo mailInfo = mailInfoList.get(i);
			System.out.println("################### new mail #########");
			System.out.println(mailInfo.getSubject());
			System.out.println(mailInfo.getContent());
			System.out.println(mailInfo.getDate());
			System.out.println(mailInfo.getFrom().getName() + " " + mailInfo.getFrom().getAdress());
			for(int j = 0; j < mailInfo.getRecipients().length; j++) {
				System.out.println(mailInfo.getRecipients()[j].getName() + " " + mailInfo.getRecipients()[j].getAdress());
			}
		}
	}

	public static void main(String[] argvs) {
		MailBox mailBox = new MailBox();
		mailBox.testReadMail();
	}

}
