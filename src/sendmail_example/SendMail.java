package sendmail_example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * Module: SendMail.java Description: �����ʼ� Company: Author: ptp Date: Mar 1,
 * 2012
 */

public class SendMail {

	private static final Log log = LogFactory.getLog(SendMail.class);

	// �����ļ�xml��·��
	private final static String XML_PATH = "src" + File.separator + "sendmail_example"
			+ File.separator + "SendMail.xml";

	// xml�ļ����ֶζ�Ӧ��javabean����
	private static MailInfo mailInfo = new MailInfo();

	/**
	 * <p>
	 * Title: readXML
	 * </p>
	 * <p>
	 * Description:��ȡxml�ļ�,���ļ��������ַ�����ʽ���
	 * </p>
	 * 
	 * @param xmlPath
	 *            �����ļ���·��
	 * @return
	 * @throws Exception
	 */
	private String readXML(String xmlPath) throws Exception {
		log.debug("xmlPath=" + xmlPath);

		String fileContent = "";
		File file = new File(xmlPath);
		if (file.isFile() && file.exists()) {
			try {
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), "UTF-8");
				BufferedReader reader = new BufferedReader(read);
				String line;
				try {
					while ((line = reader.readLine()) != null) {
						fileContent += line;
					}
					reader.close();
					read.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			throw new Exception("configĿ¼�µ������ļ�SendMail.xml������,����");
		}
		log.debug("xml=" + fileContent);
		return fileContent;
	}

	/**
	 * <p>
	 * Title: parseXML
	 * </p>
	 * <p>
	 * Description:�����ʼ���ָ�����ռ��˺ͳ�����,ͬʱ����һЩ�򵥵�У���ж�,���������ֶΡ�type��ֵ
	 * </p>
	 * 
	 * @param xml
	 *            �����ļ�������
	 * @param type
	 *            �ʼ�����
	 */
	private void parseXML(String xml, String type) {
		type = type.toLowerCase();// ����type�ֶεĴ�Сд
		try {
			// ����XML�����doc����
			Document doc = DocumentHelper.parseText(xml);

			// �ж�type��ֵ�Ƿ���ȷ,type��ֵӦ�Ǳ��Ķ����ڵ��е�һ��
			String flag = doc.getRootElement().element(type) + "";
			if (null == flag || flag.equals("null"))
				throw new DocumentException(
						"�����typeֵ����,type��ֵӦ��SendMail.xml�����ж����ڵ��һ��,��鿴�ļ�Ȼ�����´���type��ֵ");

			// ������������
			Element hostEle = (Element) doc.selectSingleNode("/mail/" + type
					+ "/host");
			if (null == hostEle || "".equals(hostEle.getTextTrim())) {
				throw new DocumentException(
						"�ʼ�������������IP����Ϊ��,���������ļ�SendMail.xml");
			}
			mailInfo.setHost(hostEle == null ? "" : hostEle.getTextTrim());

			// ���÷�����
			Element fromEle = (Element) doc.selectSingleNode("/mail/" + type
					+ "/from");
			if (null == fromEle || "".equals(fromEle.getTextTrim())) {
				throw new DocumentException("�����˵�ַ����Ϊ��,���������ļ�SendMail.xml");
			}
			mailInfo.setFrom(fromEle == null ? "" : fromEle.getTextTrim());

			// �����ʼ�����
			Element titleEle = (Element) doc.selectSingleNode("/mail/" + type
					+ "/title");
			mailInfo.setTitle(titleEle == null ? "" : titleEle.getTextTrim());

			// �����ռ��ˣ��Զ���ռ��˵Ĵ�����ں���
			Element toEle = (Element) doc.selectSingleNode("/mail/" + type
					+ "/to");
			if (null == toEle || "".equals(toEle.getTextTrim())) {
				throw new DocumentException("�ռ��˵�ַ����Ϊ��,���������ļ�SendMail.xml");
			}
			mailInfo.setTo(toEle == null ? "" : toEle.getTextTrim());

			// ���ó��ͣ��Զ�������˵Ĵ�����ں���
			Element ccEle = (Element) doc.selectSingleNode("/mail/" + type
					+ "/cc");
			mailInfo.setCc(ccEle == null ? "" : ccEle.getTextTrim());

			// ���÷������û���
			Element nameEle = (Element) doc.selectSingleNode("/mail/" + type
					+ "/username");
			if (null == nameEle || "".equals(nameEle.getTextTrim())) {
				throw new DocumentException("�������û�������Ϊ��,���������ļ�SendMail.xml");
			}
			mailInfo.setUsername(nameEle == null ? "" : nameEle.getTextTrim());

			// ���÷���������
			Element passEle = (Element) doc.selectSingleNode("/mail/" + type
					+ "/password");
			if (null == passEle || "".equals(passEle.getTextTrim())) {
				throw new DocumentException("���������벻��Ϊ��,���������ļ�SendMail.xml");
			}
			mailInfo.setPassword(passEle == null ? "" : passEle.getTextTrim());

		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * Title: sendMailOfValidate
	 * </p>
	 * <p>
	 * Description:�����ʼ��ķ���,Authenticator����֤
	 * </p>
	 */
	private void sendMailOfValidate() {
		Properties props = System.getProperties();
		props.put("mail.smtp.host", mailInfo.getHost());// �����ʼ���������������IP
		props.put("mail.smtp.auth", "true");// ��Ȩ�ʼ�,mail.smtp.auth��������Ϊtrue

		String password = mailInfo.getPassword();// ����
		/*
		 * try { password=Encrypt.DoDecrypt(password);//������뾭�������ô˷�����������н��� }
		 * catch (NumberFormatException e1) { //�������δ��������,������벻���κδ��� }
		 */
		// ���뷢���˵��û���������,����MyAuthenticator����
		MyAuthenticator myauth = new MyAuthenticator(mailInfo.getUsername(),
				password);

		// ����props��myauth����,�����ʼ���Ȩ��session����
		Session session = Session.getDefaultInstance(props, myauth);

		// ��Session������ΪMimeMessage���췽���Ĳ������빹��message����
		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(mailInfo.getFrom()));// ������

			// �Զ���ռ��˵�������д���,�����ļ�SendMail.xml��ÿ���ռ���֮������ö��Ÿ�����
			if (mailInfo.getTo() != null && !"".equals(mailInfo.getTo())) {
				String to[] = mailInfo.getTo().split(",");
				for (int i = 0; i < to.length; i++) {
					message.addRecipient(Message.RecipientType.TO,
							new InternetAddress(to[i]));// �ռ���
				}
			}

			// �Զ�������˵�������д���,ÿ��������֮���ö��Ÿ�����
			if (mailInfo.getCc() != null && !"".equals(mailInfo.getCc())) {
				String cc[] = mailInfo.getCc().split(",");
				for (int j = 0; j < cc.length; j++) {
					message.addRecipient(Message.RecipientType.CC,
							new InternetAddress(cc[j]));// ����
				}
			}
			message.setSubject(mailInfo.getTitle());// ����

			message.setText(mailInfo.getContent());// ����

			Transport.send(message);// ���÷����ʼ��ķ���

			log.debug("�ʼ����ͳɹ�");
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * Title: sendMail
	 * </p>
	 * <p>
	 * Description:�ⲿ������õ����
	 * </p>
	 * 
	 * @param type
	 *            �ʼ�������,Ŀǰ�����֣���logmessage��checkmessage��ordermessage,
	 *            typeֻ�ܴ�������ֵ��һ��,���������κ�ֵ������
	 * @param content
	 *            �ʼ�������
	 * @throws Exception
	 */
	public void sendMail(String type, String content) throws Exception {
		log.debug("���type=" + type);
		log.debug("���content=" + content);
		if (null == type || "".equals(type) || null == content
				|| "".equals(content)) {
			throw new Exception("���������type��content�ֶε�ֵ������Ϊ�ջ�null");
		}

		String xml = readXML(XML_PATH);// ���xml�ַ���

		parseXML(xml, type);// ����xml�ַ������Ѷ�Ӧ�ֶε�ֵ���뵽mailInfo������

		mailInfo.setContent(content);// ���÷��͵�����

		sendMailOfValidate();// �����ʼ�

	}

	/**
	 * Ϊ�˷���ֱ����main��������
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {

		SendMail mail = new SendMail();

		// type����,���ݴ��ֶ�ȥ�����ļ�SendMail.xml��ƥ��,Ȼ�󷢵���Ӧ������
		String type = "logmessage";

		// �ʼ�������,ʵ�ʿ��������������ǰ̨������̨
		String content = "��ã�ʮ�� ��ӭʹ��JavaMail�������ʼ�����";

		// ���������е���ʱֻ�ܿ���sendMail����,Ϊ�˱����ڲ�ϸ��,�����ķ���������Ϊ˽�е�
		mail.sendMail(type, content);
		// �����Ŀ��û����־�ļ�,�����Ҵ�ӡһ�仰�������Լ������Ѿ��ɹ�����
		System.out.println("****success****");

	}
}