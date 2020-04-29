package xyz.jonmclean.EHealth.repositories;

import org.springframework.data.repository.CrudRepository;

import xyz.jonmclean.EHealth.models.Appointment;

public interface AppointmentRepository extends CrudRepository<Appointment, Long> {
	public Iterable<Appointment> findAllByPatientId(long patientId);
	public Iterable<Appointment> findAllByDoctorId(long doctorId);
}
