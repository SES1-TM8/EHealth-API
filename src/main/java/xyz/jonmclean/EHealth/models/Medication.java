package xyz.jonmclean.EHealth.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Medication {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long medicationId;
	
	@Column(nullable = false)
	public String name;
	
	@Column(nullable = false)
	public String inputType;
	
	@Column
	public double dosage;
	
	@Column 
	public String dosageUnit;
	
	@Column
	public String scheduleListing;
	
	public Medication() {}
	
	public Medication(String name, String inputType, double dosage, String dosageUnit, String schedule) {
		this.name = name;
		this.inputType = inputType;
		this.dosage = dosage;
		this.dosageUnit = dosageUnit;
		this.scheduleListing = schedule;
	}

	public long getMedicationId() {
		return medicationId;
	}

	public void setMedicationId(long medicationId) {
		this.medicationId = medicationId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public double getDosage() {
		return dosage;
	}

	public void setDosage(double dosage) {
		this.dosage = dosage;
	}

	public String getDosageUnit() {
		return dosageUnit;
	}

	public void setDosageUnit(String dosageUnit) {
		this.dosageUnit = dosageUnit;
	}

	public String getScheduleListing() {
		return scheduleListing;
	}

	public void setScheduleListing(String scheduleListing) {
		this.scheduleListing = scheduleListing;
	}
}
