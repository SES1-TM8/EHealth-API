package xyz.jonmclean.EHealth.repositories;

import org.springframework.data.repository.CrudRepository;

import xyz.jonmclean.EHealth.models.Medication;

public interface MedicationRepository extends CrudRepository<Medication, Long> {
}
