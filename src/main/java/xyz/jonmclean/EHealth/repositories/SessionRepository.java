package xyz.jonmclean.EHealth.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import xyz.jonmclean.EHealth.models.Session;

@Repository
public interface SessionRepository extends CrudRepository<Session, Long> {
	
	public Optional<Session> findByToken(String token);
	public List<Session> findAllByUserId(long userId);
	
}
