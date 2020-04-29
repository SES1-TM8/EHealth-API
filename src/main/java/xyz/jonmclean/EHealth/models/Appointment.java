package xyz.jonmclean.EHealth.models;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Appointment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long appointmentId;
	
	@Column
	public long patientId;
	
	@Column
	public long doctorId;
	
	@Column
	public Date start;
	
	public Appointment() {}

	public Appointment(long patientId, long doctorId, Date start) {
		this.patientId = patientId;
		this.doctorId = doctorId;
		this.start = start;
	}

	public long getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(long appointmentId) {
		this.appointmentId = appointmentId;
	}

	public long getPatientId() {
		return patientId;
	}

	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}

	public long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(long doctorId) {
		this.doctorId = doctorId;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}
}
