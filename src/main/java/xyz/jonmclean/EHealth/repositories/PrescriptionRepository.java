package xyz.jonmclean.EHealth.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import xyz.jonmclean.EHealth.models.Prescription;

public interface PrescriptionRepository extends CrudRepository<Prescription, Long> {
	
	public List<Prescription> findAllByPatientId(long patientId);
	
}
