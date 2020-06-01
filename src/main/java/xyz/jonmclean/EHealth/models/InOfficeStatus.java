package xyz.jonmclean.EHealth.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class InOfficeStatus {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	long id;
	
	@Column(unique = true)
	long doctorId;
	
	@Column
	boolean inOffice = false;
	
	public InOfficeStatus() {}

	public InOfficeStatus(long doctorId, boolean inOffice) {
		this.doctorId = doctorId;
		this.inOffice = inOffice;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(long doctorId) {
		this.doctorId = doctorId;
	}

	public boolean isInOffice() {
		return inOffice;
	}

	public void setInOffice(boolean inOffice) {
		this.inOffice = inOffice;
	}
}
