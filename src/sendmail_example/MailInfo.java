package sendmail_example;

/**
 * Module: MailInfo.java Description: ���巢���ʼ��������ֶε�javabean Company: Author: ptp
 * Date: Mar 1, 2012
 */
public class MailInfo {
	private String host;// �ʼ�������������IP
	private String from;// ������
	private String to;// �ռ���
	private String cc;// ������
	private String username;// �������û���
	private String password;// ����������
	private String title;// �ʼ�������
	private String content;// �ʼ�������

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}