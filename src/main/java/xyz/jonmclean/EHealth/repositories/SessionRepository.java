package xyz.jonmclean.EHealth.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import xyz.jonmclean.EHealth.models.Session;
import xyz.jonmclean.EHealth.models.User;

public interface SessionRepository extends CrudRepository<Session, Long> {
	
	public Optional<Session> findByToken(String token);
	
}
