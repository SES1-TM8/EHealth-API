package xyz.jonmclean.EHealth.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import xyz.jonmclean.EHealth.models.Doctor;

public interface DoctorRepository extends CrudRepository<Doctor, Long>{
	
	public Optional<Doctor> findByUserId(long userId);
	
}
