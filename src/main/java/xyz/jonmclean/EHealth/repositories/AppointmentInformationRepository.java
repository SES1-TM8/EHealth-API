package xyz.jonmclean.EHealth.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import xyz.jonmclean.EHealth.models.AppointmentInformation;

public interface AppointmentInformationRepository extends CrudRepository<AppointmentInformation, Long>{
	public Optional<AppointmentInformation> findByAppointmentId(long appointmentId);
}
