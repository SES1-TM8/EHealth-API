package xyz.jonmclean.EHealth.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Prescription {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long prescriptionId;
	
	@Column(nullable = false)
	public long patientId;
	
	@Column(nullable = false)
	public long medicationId;
	
	@Column
	public double frequency;
	
	@Column
	public String frequencyUnit;
	
	@Column
	public String notes;
	
	@Column
	public long prescriberId;
	
	public Prescription() {}

	public long getPrescriptionId() {
		return prescriptionId;
	}

	public void setPrescriptionId(long prescriptionId) {
		this.prescriptionId = prescriptionId;
	}

	public long getPatientId() {
		return patientId;
	}

	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}

	public long getMedicationId() {
		return medicationId;
	}

	public void setMedicationId(long medicationId) {
		this.medicationId = medicationId;
	}

	public double getFrequency() {
		return frequency;
	}

	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}

	public String getFrequencyUnit() {
		return frequencyUnit;
	}

	public void setFrequencyUnit(String frequencyUnit) {
		this.frequencyUnit = frequencyUnit;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public long getPrescriberId() {
		return prescriberId;
	}

	public void setPrescriberId(long prescriberId) {
		this.prescriberId = prescriberId;
	}
}
