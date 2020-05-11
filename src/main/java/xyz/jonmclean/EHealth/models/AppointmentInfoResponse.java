package xyz.jonmclean.EHealth.models;

import java.util.List;

import xyz.jonmclean.EHealth.image.models.S3Upload;

public class AppointmentInfoResponse {
	
	public long appointmentInfoId;
	public Appointment appointment;
	public String description;
	public List<S3Upload> uploads;
	
	public AppointmentInfoResponse() {}
	
	public AppointmentInfoResponse(long appointmentInfoId, Appointment appointment, String description,
			List<S3Upload> uploads) {
		this.appointmentInfoId = appointmentInfoId;
		this.appointment = appointment;
		this.description = description;
		this.uploads = uploads;
	}
	public long getAppointmentInfoId() {
		return appointmentInfoId;
	}
	public void setAppointmentInfoId(long appointmentInfoId) {
		this.appointmentInfoId = appointmentInfoId;
	}
	public Appointment getAppointment() {
		return appointment;
	}
	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
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
	
	
	
}
