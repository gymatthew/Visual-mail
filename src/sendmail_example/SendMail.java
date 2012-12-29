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
 * Module: SendMail.java Description: 发送邮件 Company: Author: ptp Date: Mar 1,
 * 2012
 */

public class SendMail {

	private static final Log log = LogFactory.getLog(SendMail.class);

	// 配置文件xml的路径
	private final static String XML_PATH = "src" + File.separator + "sendmail_example"
			+ File.separator + "SendMail.xml";

	// xml文件中字段对应的javabean对象
	private static MailInfo mailInfo = new MailInfo();

	/**
	 * <p>
	 * Title: readXML
	 * </p>
	 * <p>
	 * Description:读取xml文件,把文件内容以字符串形式输出
	 * </p>
	 * 
	 * @param xmlPath
	 *            配置文件的路径
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
			throw new Exception("config目录下的配置文件SendMail.xml不存在,请检查");
		}
		log.debug("xml=" + fileContent);
		return fileContent;
	}

	/**
	 * <p>
	 * Title: parseXML
	 * </p>
	 * <p>
	 * Description:发送邮件给指定的收件人和抄送人,同时进行一些简单的校验判断,如必填项的字段、type的值
	 * </p>
	 * 
	 * @param xml
	 *            配置文件的内容
	 * @param type
	 *            邮件类型
	 */
	private void parseXML(String xml, String type) {
		type = type.toLowerCase();// 忽略type字段的大小写
		try {
			// 解析XML，获得doc对象
			Document doc = DocumentHelper.parseText(xml);

			// 判断type的值是否正确,type的值应是报文二级节点中的一个
			String flag = doc.getRootElement().element(type) + "";
			if (null == flag || flag.equals("null"))
				throw new DocumentException(
						"传入的type值不对,type的值应是SendMail.xml报文中二级节点的一个,请查看文件然后重新传入type的值");

			// 设置主机名称
			Element hostEle = (Element) doc.selectSingleNode("/mail/" + type
					+ "/host");
			if (null == hostEle || "".equals(hostEle.getTextTrim())) {
				throw new DocumentException(
						"邮件服务器域名或IP不能为空,请检查配置文件SendMail.xml");
			}
			mailInfo.setHost(hostEle == null ? "" : hostEle.getTextTrim());

			// 设置发件人
			Element fromEle = (Element) doc.selectSingleNode("/mail/" + type
					+ "/from");
			if (null == fromEle || "".equals(fromEle.getTextTrim())) {
				throw new DocumentException("发件人地址不能为空,请检查配置文件SendMail.xml");
			}
			mailInfo.setFrom(fromEle == null ? "" : fromEle.getTextTrim());

			// 设置邮件主题
			Element titleEle = (Element) doc.selectSingleNode("/mail/" + type
					+ "/title");
			mailInfo.setTitle(titleEle == null ? "" : titleEle.getTextTrim());

			// 设置收件人，对多个收件人的处理放在后面
			Element toEle = (Element) doc.selectSingleNode("/mail/" + type
					+ "/to");
			if (null == toEle || "".equals(toEle.getTextTrim())) {
				throw new DocumentException("收件人地址不能为空,请检查配置文件SendMail.xml");
			}
			mailInfo.setTo(toEle == null ? "" : toEle.getTextTrim());

			// 设置抄送，对多个抄送人的处理放在后面
			Element ccEle = (Element) doc.selectSingleNode("/mail/" + type
					+ "/cc");
			mailInfo.setCc(ccEle == null ? "" : ccEle.getTextTrim());

			// 设置发件人用户名
			Element nameEle = (Element) doc.selectSingleNode("/mail/" + type
					+ "/username");
			if (null == nameEle || "".equals(nameEle.getTextTrim())) {
				throw new DocumentException("发件人用户名不能为空,请检查配置文件SendMail.xml");
			}
			mailInfo.setUsername(nameEle == null ? "" : nameEle.getTextTrim());

			// 设置发件人密码
			Element passEle = (Element) doc.selectSingleNode("/mail/" + type
					+ "/password");
			if (null == passEle || "".equals(passEle.getTextTrim())) {
				throw new DocumentException("发件人密码不能为空,请检查配置文件SendMail.xml");
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
	 * Description:发送邮件的方法,Authenticator类验证
	 * </p>
	 */
	private void sendMailOfValidate() {
		Properties props = System.getProperties();
		props.put("mail.smtp.host", mailInfo.getHost());// 设置邮件服务器的域名或IP
		props.put("mail.smtp.auth", "true");// 授权邮件,mail.smtp.auth必须设置为true

		String password = mailInfo.getPassword();// 密码
		/*
		 * try { password=Encrypt.DoDecrypt(password);//如果密码经过加密用此方法对密码进行解密 }
		 * catch (NumberFormatException e1) { //如果密码未经过加密,则对密码不做任何处理 }
		 */
		// 传入发件人的用户名和密码,构造MyAuthenticator对象
		MyAuthenticator myauth = new MyAuthenticator(mailInfo.getUsername(),
				password);

		// 传入props、myauth对象,构造邮件授权的session对象
		Session session = Session.getDefaultInstance(props, myauth);

		// 将Session对象作为MimeMessage构造方法的参数传入构造message对象
		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(mailInfo.getFrom()));// 发件人

			// 对多个收件人的情况进行处理,配置文件SendMail.xml中每个收件人之间必须用逗号隔开的
			if (mailInfo.getTo() != null && !"".equals(mailInfo.getTo())) {
				String to[] = mailInfo.getTo().split(",");
				for (int i = 0; i < to.length; i++) {
					message.addRecipient(Message.RecipientType.TO,
							new InternetAddress(to[i]));// 收件人
				}
			}

			// 对多个抄送人的情况进行处理,每个抄送人之间用逗号隔开的
			if (mailInfo.getCc() != null && !"".equals(mailInfo.getCc())) {
				String cc[] = mailInfo.getCc().split(",");
				for (int j = 0; j < cc.length; j++) {
					message.addRecipient(Message.RecipientType.CC,
							new InternetAddress(cc[j]));// 抄送
				}
			}
			message.setSubject(mailInfo.getTitle());// 主题

			message.setText(mailInfo.getContent());// 内容

			Transport.send(message);// 调用发送邮件的方法

			log.debug("邮件发送成功");
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
	 * Description:外部程序调用的入口
	 * </p>
	 * 
	 * @param type
	 *            邮件的类型,目前有三种，即logmessage、checkmessage、ordermessage,
	 *            type只能传这三个值中一个,传其他的任何值都不行
	 * @param content
	 *            邮件的内容
	 * @throws Exception
	 */
	public void sendMail(String type, String content) throws Exception {
		log.debug("入参type=" + type);
		log.debug("入参content=" + content);
		if (null == type || "".equals(type) || null == content
				|| "".equals(content)) {
			throw new Exception("方法的入参type和content字段的值都不能为空或null");
		}

		String xml = readXML(XML_PATH);// 获得xml字符串

		parseXML(xml, type);// 解析xml字符串，把对应字段的值放入到mailInfo对象中

		mailInfo.setContent(content);// 设置发送的内容

		sendMailOfValidate();// 发送邮件

	}

	/**
	 * 为了方便直接用main方法测试
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {

		SendMail mail = new SendMail();

		// type类型,根据此字段去配置文件SendMail.xml中匹配,然后发到相应的邮箱
		String type = "logmessage";

		// 邮件的内容,实际开发中这个内容是前台传到后台
		String content = "你好，十分 欢迎使用JavaMail包开发邮件功能";

		// 在其他类中调用时只能看到sendMail方法,为了保护内部细节,其它的方法都声明为私有的
		mail.sendMail(type, content);
		// 这个项目中没有日志文件,所以我打印一句话来告诉自己程序已经成功运行
		System.out.println("****success****");

	}
}