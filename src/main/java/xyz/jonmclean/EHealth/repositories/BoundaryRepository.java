package xyz.jonmclean.EHealth.repositories;

import org.springframework.data.repository.CrudRepository;

import xyz.jonmclean.EHealth.models.OfficeBoundary;

public interface BoundaryRepository extends CrudRepository<OfficeBoundary, Long>{
	
	public Iterable<OfficeBoundary> findAllByOwnerId(long ownerId);
	
}
