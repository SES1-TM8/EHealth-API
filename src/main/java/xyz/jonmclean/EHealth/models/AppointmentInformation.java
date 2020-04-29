package xyz.jonmclean.EHealth.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AppointmentInformation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long id;
	
	@Column(nullable = false)
	public String description;
	
	@Column(nullable = true)
	public List<Long> imageIds = new ArrayList<Long>();
	
	@Column(nullable = false, unique = true)
	public long appointmentId;
	
	public AppointmentInformation() {}

	public AppointmentInformation(String description, List<Long> imageIds, long appointmentId) {
		this.description = description;
		this.imageIds = imageIds;
		this.appointmentId = appointmentId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Long> getImageIds() {
		return imageIds;
	}

	public void setImageIds(List<Long> imageIds) {
		this.imageIds = imageIds;
	}

	public long getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(long appointmentId) {
		this.appointmentId = appointmentId;
	}
}
