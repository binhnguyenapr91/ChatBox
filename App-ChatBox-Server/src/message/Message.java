package message;

import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;
	private String type;
	private String sender;
	private String content;
	private String recipient;
	
	public Message(String type, String sender, String content,String recipient) {
		this.type = type;
		this.sender = sender;
		this.content = content;
		this.recipient =recipient;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	@Override
	public String toString() {
		return "{type='"+type+"', sender='"+sender+"', content='"+content+"', recipient='"+recipient+"'}";
	}
	
	
}
