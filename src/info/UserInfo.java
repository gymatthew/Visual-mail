package info;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Properties;

public class UserInfo {

	public static final String PROTOCOL_KEY = "mail.store.protocol";
	public static final String HOST_KEY = "mail.pop3.host";
	protected static final String FILE_PATH = "resources/host.properties";
	private static final String PROTOCOL_END = ".protocol";
	private static final String HOST_END = ".host";

	private String userName = null;
	private String password = null;
	private static UserInfo instance = new UserInfo();
	private String protocol = null;
	private String host = null;

	private UserInfo() {
	}

	public static UserInfo getInstance() {
		return instance;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String user_name) {
		this.userName = user_name;
		int index = userName.indexOf("@");
		if (index == -1)
			return;
		String pre = userName.substring(index + 1);
		Properties props = new Properties();
		try {	
			InputStream in = new BufferedInputStream(
					UserInfo.class.getClassLoader().getResourceAsStream(FILE_PATH));
			props.load(in);
			this.protocol = props.getProperty(pre + PROTOCOL_END);
			this.host = props.getProperty(pre + HOST_END);
			// this.protocol = "pop3";
			// this.host = "pop3.126.com";
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getHost() {
		return host;
	}
}
