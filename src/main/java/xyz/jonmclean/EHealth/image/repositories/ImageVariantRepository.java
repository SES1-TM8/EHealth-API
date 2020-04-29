package xyz.jonmclean.EHealth.image.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import xyz.jonmclean.EHealth.image.models.ImageVariant;

@Repository
public interface ImageVariantRepository extends CrudRepository<ImageVariant, Long> {
}

