package xyz.jonmclean.EHealth.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import xyz.jonmclean.EHealth.models.InOfficeStatus;

public interface InOfficeRepository extends CrudRepository<InOfficeStatus, Long>{
	
	public Optional<InOfficeStatus> findByDoctorId(long doctorId);
	
}
