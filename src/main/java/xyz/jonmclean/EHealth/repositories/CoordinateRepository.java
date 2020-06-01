package xyz.jonmclean.EHealth.repositories;

import org.springframework.data.repository.CrudRepository;

import xyz.jonmclean.EHealth.models.MapCoordinate;
import xyz.jonmclean.EHealth.models.OfficeBoundary;

public interface CoordinateRepository extends CrudRepository<MapCoordinate, Long>{
	public void deleteByBoundary(OfficeBoundary boundary);
}
