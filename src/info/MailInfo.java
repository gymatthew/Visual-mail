package info;

import java.util.Date;

public class MailInfo {
	
	private String subject;
	private Participant from;
	private Participant[] recipients;
	private String content;
	private Date date;
	
	public MailInfo() {
		
	}
	
	public MailInfo(String subject, Participant from, Participant[] recipients,
			String content, Date date) {
		super();
		this.subject = subject;
		this.from = from;
		this.recipients = recipients;
		this.content = content;
		this.date = date;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Participant getFrom() {
		return from;
	}

	public void setFrom(Participant from) {
		this.from = from;
	}

	public Participant[] getRecipients() {
		return recipients;
	}

	public void setRecipients(Participant[] recipients) {
		this.recipients = recipients;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
