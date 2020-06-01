package xyz.jonmclean.EHealth.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import xyz.jonmclean.EHealth.models.DoctorHours;

public interface DoctorHoursRepository extends CrudRepository<DoctorHours, Long>{
	
	public Optional<DoctorHours> findByDoctorId(long doctorId);
	
}
