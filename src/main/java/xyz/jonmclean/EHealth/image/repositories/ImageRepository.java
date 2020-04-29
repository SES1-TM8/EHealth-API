package xyz.jonmclean.EHealth.image.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import xyz.jonmclean.EHealth.image.models.Image;

@Repository
public interface ImageRepository extends CrudRepository<Image, Long> {	
}
