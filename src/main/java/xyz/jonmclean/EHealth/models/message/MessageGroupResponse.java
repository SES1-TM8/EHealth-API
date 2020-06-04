package xyz.jonmclean.EHealth.models.message;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import xyz.jonmclean.EHealth.models.response.UserResponse;

public class MessageGroupResponse {
	
	long messageGroupId;
	String name;
	List<UserResponse> members = new ArrayList<>();
	boolean direct;
	Date timestamp;
	long appointmentId;
	
	public MessageGroupResponse() {}
	
	public MessageGroupResponse(long messageGroupId, String name, List<UserResponse> members, boolean direct,
			Date timestamp, long appointmentId) {
		super();
		this.messageGroupId = messageGroupId;
		this.name = name;
		this.members = members;
		this.direct = direct;
		this.timestamp = timestamp;
		this.appointmentId = appointmentId;
	}
	
	public MessageGroupResponse(MessageGroup group) {
		this.messageGroupId = group.getMessageGroupId();
		this.name = group.getName();
		this.timestamp = group.getTimestamp();
		this.direct = group.isDirect();
		this.appointmentId = group.getAppointmentId();
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

	public List<UserResponse> getMembers() {
		return members;
	}

	public void setMembers(List<UserResponse> members) {
		this.members = members;
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

	public long getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(long appointmentId) {
		this.appointmentId = appointmentId;
	}
}
