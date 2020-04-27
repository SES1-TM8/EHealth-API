package xyz.jonmclean.EHealth.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.jonmclean.EHealth.models.Doctor;
import xyz.jonmclean.EHealth.models.User;
import xyz.jonmclean.EHealth.models.exceptions.DoctorAlreadyExistsException;
import xyz.jonmclean.EHealth.models.exceptions.DoctorInfoNotFoundException;
import xyz.jonmclean.EHealth.models.exceptions.UserNotFoundException;
import xyz.jonmclean.EHealth.repositories.DoctorRepository;
import xyz.jonmclean.EHealth.repositories.UserRepository;

public class DoctorInformationService {
	
	@Autowired
	public UserRepository userRepo;
	
	@Autowired
	public DoctorRepository doctorRepo;
	
	@PostMapping("user/doctor/add")
	@ResponseBody
	public Doctor addDoctorInformationForUser(@RequestParam("registration") String registrationNumber, @RequestParam("userId") long userId) throws UserNotFoundException, DoctorInfoNotFoundException, DoctorAlreadyExistsException {
		Optional<User> optionalUser = userRepo.findById(userId);
		
		if(!optionalUser.isPresent()) {
			throw new UserNotFoundException();
		}
		
		User user = optionalUser.get();
		
		Optional<Doctor> optionalDoctor = doctorRepo.findByUserId(userId);
		
		if(optionalDoctor.isPresent()) {
			throw new DoctorAlreadyExistsException();
		}
		
		
		Doctor doctor = new Doctor(registrationNumber, user.getUserId());
		
		doctorRepo.save(doctor);
		
		return get(user.getUserId());
	}
	
	@GetMapping("user/doctor/{id}")
	@ResponseBody
	public Doctor get(@PathVariable long userId) throws DoctorInfoNotFoundException {
		Optional<Doctor> optionalDoctor = doctorRepo.findByUserId(userId);
		
		if(!optionalDoctor.isPresent()) {
			throw new DoctorInfoNotFoundException();
		}
		
		return optionalDoctor.get();
	}
	
}
