package xyz.jonmclean.EHealth.repositories;

import org.springframework.data.repository.CrudRepository;

import xyz.jonmclean.EHealth.models.Prescription;

public interface PrescriptionRepository extends CrudRepository<Prescription, Long> {
}
