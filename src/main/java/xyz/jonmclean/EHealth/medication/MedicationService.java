package xyz.jonmclean.EHealth.medication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.jonmclean.EHealth.models.Medication;
import xyz.jonmclean.EHealth.models.exceptions.MedicationNotFoundException;
import xyz.jonmclean.EHealth.repositories.MedicationRepository;

@Controller
public class MedicationService {
	
	@Autowired
	public MedicationRepository medicationRepo;
	
	@PostMapping("/medication/add")
	@ResponseBody
	public Medication create(@RequestParam("medicationName") String name, @RequestParam("input") String input, @RequestParam("dosage") double dosage, @RequestParam("dosageUnit") String dosageUnit, @RequestParam("schedule") String schedule) {
		Medication medication = new Medication();
		medication.setName(name);
		medication.setDosage(dosage);
		medication.setDosageUnit(dosageUnit);
		medication.setInputType(input);
		medication.setScheduleListing(schedule);
		
		medicationRepo.save(medication);
		
		return medication;
	}
	
	@GetMapping("/medication/all")
	@ResponseBody
	public List<Medication> get() {
		Iterable<Medication> iterableMedications = medicationRepo.findAll();
		List<Medication> medications = new ArrayList<Medication>();
		iterableMedications.forEach(medications::add);
		return medications;
	}
	
	@GetMapping("/medication/{id}")
	@ResponseBody
	public Medication get(@PathVariable long medicationId) throws MedicationNotFoundException {
		Optional<Medication> optional = medicationRepo.findById(medicationId);
		
		if(!optional.isPresent()) {
			throw new MedicationNotFoundException();
		}
		
		return optional.get();
	}
	
}
