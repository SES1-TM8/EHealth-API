package xyz.jonmclean.EHealth.models.response;

import java.sql.Timestamp;
import java.util.List;

import xyz.jonmclean.EHealth.image.models.S3Upload;

public class UrgentCaseCallbackResponse {
	
	long urgentId;
	long patientId;
	String description;
	List<S3Upload> uploads;
	Timestamp openTime;
	boolean resolved;
	
	public UrgentCaseCallbackResponse() {}

	public UrgentCaseCallbackResponse(long urgentId, long patientId, String description, List<S3Upload> uploads,
			Timestamp openTime, boolean resolved) {
		this.urgentId = urgentId;
		this.patientId = patientId;
		this.description = description;
		this.uploads = uploads;
		this.openTime = openTime;
		this.resolved = resolved;
	}

	public long getUrgentId() {
		return urgentId;
	}

	public void setUrgentId(long urgentId) {
		this.urgentId = urgentId;
	}

	public long getPatientId() {
		return patientId;
	}

	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<S3Upload> getUploads() {
		return uploads;
	}

	public void setUploads(List<S3Upload> uploads) {
		this.uploads = uploads;
	}

	public Timestamp getOpenTime() {
		return openTime;
	}

	public void setOpenTime(Timestamp openTime) {
		this.openTime = openTime;
	}

	public boolean isResolved() {
		return resolved;
	}

	public void setResolved(boolean resolved) {
		this.resolved = resolved;
	}
}
