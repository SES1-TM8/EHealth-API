package xyz.jonmclean.EHealth.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import xyz.jonmclean.EHealth.models.Patient;

public interface PatientRepository extends CrudRepository<Patient, Long> {
	
	public Optional<Patient> findByUserId(long userId);
	public Optional<Patient> findByMedicareNumber(String medicareNumber);
	public Optional<Patient> findByConcessionNumber(String concessionNumber);
	
}
