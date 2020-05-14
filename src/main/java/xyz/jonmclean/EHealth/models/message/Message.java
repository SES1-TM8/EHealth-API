package xyz.jonmclean.EHealth.models.message;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(indexes = { @Index(columnList="messageGroupId") })
public class Message {
	
	@Id
	@GeneratedValue
	long messageId;
	long userId;
	long messageGroupId;
	@Column(length = 2048)
	String content;
	Date timestamp = new Date(System.currentTimeMillis());
	
	public Message() {
		super();
	}

	public Message(long userId, long messageGroupId, String content) {
		super();
		this.userId = userId;
		this.messageGroupId = messageGroupId;
		this.content = content;
	}

	public Message(long messageId, long userId, long messageGroupId, String content, Date timestamp) {
		super();
		this.messageId = messageId;
		this.userId = userId;
		this.messageGroupId = messageGroupId;
		this.content = content;
		this.timestamp = timestamp;
	}

	public long getMessageId() {
		return messageId;
	}

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getMessageGroupId() {
		return messageGroupId;
	}

	public void setMessageGroupId(long messageGroupId) {
		this.messageGroupId = messageGroupId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	
}
