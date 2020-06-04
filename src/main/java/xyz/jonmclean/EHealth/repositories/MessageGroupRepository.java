package xyz.jonmclean.EHealth.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import xyz.jonmclean.EHealth.models.message.MessageGroup;

public interface MessageGroupRepository extends CrudRepository<MessageGroup, Long>{
	
	public Optional<MessageGroup> findByAppointmentId(long appointmentId);
	
}
