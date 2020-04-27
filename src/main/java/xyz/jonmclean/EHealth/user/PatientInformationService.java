package xyz.jonmclean.EHealth.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.jonmclean.EHealth.models.Patient;
import xyz.jonmclean.EHealth.models.User;
import xyz.jonmclean.EHealth.models.exceptions.UserNotFoundException;
import xyz.jonmclean.EHealth.models.exceptions.patient.PatientConcessionNumberInUseException;
import xyz.jonmclean.EHealth.models.exceptions.patient.PatientInfoAlreadyExistsException;
import xyz.jonmclean.EHealth.models.exceptions.patient.PatientMedicareNumberInUseException;
import xyz.jonmclean.EHealth.models.exceptions.patient.PatientNotFoundException;
import xyz.jonmclean.EHealth.repositories.PatientRepository;
import xyz.jonmclean.EHealth.repositories.UserRepository;

@Controller
public class PatientInformationService {
	
	@Autowired
	public UserRepository userRepo;
	
	@Autowired
	public PatientRepository patientRepo;
	
	@PostMapping("/user/patient/concession/add")
	@ResponseBody
	public Patient create(@RequestParam("userId") long userId, @RequestParam("medicareNumber") String medicareNumber, @RequestParam("concessionType") String concessionType, @RequestParam("concessionNumber") String concessionNumber) throws UserNotFoundException, PatientInfoAlreadyExistsException, PatientMedicareNumberInUseException, PatientConcessionNumberInUseException {
		Optional<User> optionalUser = userRepo.findById(userId);
		
		if(!optionalUser.isPresent()) {
			throw new UserNotFoundException();
		}
		
		User user = optionalUser.get();
		
		Optional<Patient> optionalPatient = patientRepo.findByUserId(userId);
		if(optionalPatient.isPresent()) {
			throw new PatientInfoAlreadyExistsException();
		}
		
		optionalPatient = patientRepo.findByMedicareNumber(medicareNumber);
		if(optionalPatient.isPresent()) {
			throw new PatientMedicareNumberInUseException();
		}
		
		optionalPatient = patientRepo.findByConcessionNumber(concessionNumber);
		if(optionalPatient.isPresent()) {
			throw new PatientConcessionNumberInUseException();
		}
		
		Patient patient = new Patient();
		patient.setUserId(userId);
		patient.setConcessionType(concessionType);
		patient.setConcessionNumber(concessionNumber);
		patient.setMedicareNumber(medicareNumber);
		
		patientRepo.save(patient);
		
		return patient;
	}
	
	@PostMapping("/user/patient/add")
	@ResponseBody
	public Patient create(@RequestParam("userId") long userId, @RequestParam("medicareNumber") String medicareNumber) throws UserNotFoundException, PatientInfoAlreadyExistsException, PatientMedicareNumberInUseException {
		Optional<User> optionalUser = userRepo.findById(userId);
		
		if(!optionalUser.isPresent()) {
			throw new UserNotFoundException();
		}
		
		User user = optionalUser.get();
		
		Optional<Patient> optionalPatient = patientRepo.findByUserId(userId);
		if(optionalPatient.isPresent()) {
			throw new PatientInfoAlreadyExistsException();
		}
		
		optionalPatient = patientRepo.findByMedicareNumber(medicareNumber);
		if(optionalPatient.isPresent()) {
			throw new PatientMedicareNumberInUseException();
		}
		
		Patient patient = new Patient();
		patient.setUserId(userId);
		patient.setMedicareNumber(medicareNumber);
		
		patientRepo.save(patient);
		
		return patient;
	}
	
	@GetMapping("/user/patient/{id}")
	@ResponseBody
	public Patient get(@PathVariable long userId) throws PatientNotFoundException {
		Optional<Patient> optionalPatient = patientRepo.findByUserId(userId);
		
		if(!optionalPatient.isPresent()) {
			throw new PatientNotFoundException();
		}
		
		return optionalPatient.get();
	}
	
	@GetMapping("/user/patient/medicare/{number}")
	@ResponseBody
	public Patient getMedicare(@PathVariable String medicareNumber) throws PatientNotFoundException {
		Optional<Patient> optionalPatient = patientRepo.findByMedicareNumber(medicareNumber);
		
		if(!optionalPatient.isPresent()) {
			throw new PatientNotFoundException();
		}
		
		return optionalPatient.get();
	}
	
	@GetMapping("/user/patient/concession/{number}")
	@ResponseBody
	public Patient getConcession(@PathVariable String concessionNumber) throws PatientNotFoundException {
		Optional<Patient> optionalPatient = patientRepo.findByConcessionNumber(concessionNumber);
		
		if(!optionalPatient.isPresent()) {
			throw new PatientNotFoundException();
		}
		
		return optionalPatient.get();
	}
	
}
