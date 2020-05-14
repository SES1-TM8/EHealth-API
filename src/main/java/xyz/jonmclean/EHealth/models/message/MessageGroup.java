package xyz.jonmclean.EHealth.models.message;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class MessageGroup {

	@Id
	@GeneratedValue
	long messageGroupId;
	
	@Column(nullable = true)
	String name;
	@Column
	boolean direct;
	Date timestamp = new Date(System.currentTimeMillis());
	
	public MessageGroup() {}

	public MessageGroup(long messageGroupId, String name, boolean direct, Date timestamp) {
		this.messageGroupId = messageGroupId;
		this.name = name;
		this.direct = direct;
		this.timestamp = timestamp;
	}
	
	public MessageGroup(String name, boolean direct) {
		this.name = name;
		this.direct = direct;
	}

	public long getMessageGroupId() {
		return messageGroupId;
	}

	public void setMessageGroupId(long messageGroupId) {
		this.messageGroupId = messageGroupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDirect() {
		return direct;
	}

	public void setDirect(boolean direct) {
		this.direct = direct;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
