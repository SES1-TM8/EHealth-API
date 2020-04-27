package xyz.jonmclean.EHealth.user;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import xyz.jonmclean.EHealth.models.User;

public interface UserRepository extends CrudRepository<User, Long> {
	
	public Optional<User> findByEmailAddress(String emailAddress);
	
}
