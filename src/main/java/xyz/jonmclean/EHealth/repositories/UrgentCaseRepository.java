package xyz.jonmclean.EHealth.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import xyz.jonmclean.EHealth.models.UrgentCase;

public interface UrgentCaseRepository extends CrudRepository<UrgentCase, Long>{
	
	public Optional<UrgentCase> findByPatientId(long patientId);
	public List<UrgentCase> findAllByPatientId(long patientId);
	
	public List<UrgentCase> findByResolved(boolean resolved);
	
}
