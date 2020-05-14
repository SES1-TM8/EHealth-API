package xyz.jonmclean.EHealth.models.message;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(indexes = {@Index(columnList = "userId")})
public class MessageGroupMember {
	
	@Id
	@GeneratedValue
	long messageGroupMemberId;
	@Column
	long userId;
	@JoinColumn(name = "messageGroupId")
	@ManyToOne
	MessageGroup messageGroup;
	Date timestamp = new Date(System.currentTimeMillis());
	
	public MessageGroupMember() {}
	
	public MessageGroupMember(long messageGroupMemberId, long userId, MessageGroup messageGroup, Date timestamp) {
		this.messageGroupMemberId = messageGroupMemberId;
		this.userId = userId;
		this.messageGroup = messageGroup;
		this.timestamp = timestamp;
	}
	
	public MessageGroupMember(long userId, MessageGroup messageGroup) {
		this.userId = userId;
		this.messageGroup = messageGroup;
	}

	public long getMessageGroupMemberId() {
		return messageGroupMemberId;
	}

	public void setMessageGroupMemberId(long messageGroupMemberId) {
		this.messageGroupMemberId = messageGroupMemberId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public MessageGroup getMessageGroup() {
		return messageGroup;
	}

	public void setMessageGroup(MessageGroup messageGroup) {
		this.messageGroup = messageGroup;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
