package xyz.jonmclean.EHealth.models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UrgentCase {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	long id;
	
	@Column
	String description;
	
	@ElementCollection
	List<Long> imageIds = new ArrayList<Long>();
	
	@Column
	long patientId;
	
	@Column
	boolean resolved = false;
	
	@Column
	Timestamp openTime;
	
	@Column(nullable = true)
	Timestamp closeTime;
	
	@Column(nullable = true)
	long resolvedById; // Doctor ID
	
	
	public UrgentCase() {}

	public UrgentCase(String description, List<Long> imageIds, long patientId, boolean resolved, Timestamp openTime,
			Timestamp closeTime, long resolvedById) {
		this.description = description;
		this.imageIds = imageIds;
		this.patientId = patientId;
		this.resolved = resolved;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.resolvedById = resolvedById;
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

	public long getPatientId() {
		return patientId;
	}

	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}

	public boolean isResolved() {
		return resolved;
	}

	public void setResolved(boolean resolved) {
		this.resolved = resolved;
	}

	public Timestamp getOpenTime() {
		return openTime;
	}

	public void setOpenTime(Timestamp openTime) {
		this.openTime = openTime;
	}

	public Timestamp getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Timestamp closeTime) {
		this.closeTime = closeTime;
	}

	public long getResolvedById() {
		return resolvedById;
	}

	public void setResolvedById(long resolvedById) {
		this.resolvedById = resolvedById;
	}
}
