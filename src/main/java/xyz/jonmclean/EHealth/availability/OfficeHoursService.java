package xyz.jonmclean.EHealth.availability;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.jonmclean.EHealth.models.Doctor;
import xyz.jonmclean.EHealth.models.DoctorHours;
import xyz.jonmclean.EHealth.models.InOfficeStatus;
import xyz.jonmclean.EHealth.models.Session;
import xyz.jonmclean.EHealth.models.exceptions.NotDoctorException;
import xyz.jonmclean.EHealth.models.exceptions.ResourceNotFoundException;
import xyz.jonmclean.EHealth.models.exceptions.SessionExpiredException;
import xyz.jonmclean.EHealth.models.exceptions.SessionNotFoundException;
import xyz.jonmclean.EHealth.repositories.DoctorHoursRepository;
import xyz.jonmclean.EHealth.repositories.DoctorRepository;
import xyz.jonmclean.EHealth.repositories.InOfficeRepository;
import xyz.jonmclean.EHealth.repositories.SessionRepository;

@Controller
public class OfficeHoursService {
	
	@Autowired
	DoctorHoursRepository hoursRepo;
	
	@Autowired
	SessionRepository sessionRepo;
	
	@Autowired
	DoctorRepository doctorRepo;
	
	@Autowired
	InOfficeRepository statusRepo;
	
	@GetMapping("/availability/hours/{doctorId}")
	@ResponseBody
	public DoctorHours get(@PathVariable long doctorId) throws ResourceNotFoundException {
		Optional<DoctorHours> optional = hoursRepo.findByDoctorId(doctorId);
		
		if(!optional.isPresent()) throw new ResourceNotFoundException();
		
		return optional.get();
	}
	
	@GetMapping("/availability/hours/id/{hourId}")
	@ResponseBody
	public DoctorHours getFromId(@PathVariable long hourId) throws ResourceNotFoundException {
		Optional<DoctorHours> optional = hoursRepo.findById(hourId);
		
		if(!optional.isPresent()) throw new ResourceNotFoundException();
		
		return optional.get();
	}
	
	@PostMapping("/availability/hours")
	@ResponseBody
	public DoctorHours post(@RequestParam String token, @RequestParam long start, @RequestParam long end) throws SessionNotFoundException, SessionExpiredException, NotDoctorException {
		Optional<Session> optionalSession = sessionRepo.findByToken(token);
		
		if(!optionalSession.isPresent()) {
			throw new SessionNotFoundException();
		}
		
		Session session = optionalSession.get();
		
		if(session.expiry.before(new Timestamp(System.currentTimeMillis()))) {
			throw new SessionExpiredException();
		}
		
		Optional<Doctor> optionalDoctor = doctorRepo.findByUserId(session.getUserId());
		
		if(!optionalDoctor.isPresent()) {
			throw new NotDoctorException();
		}
		
		Doctor doctor = optionalDoctor.get();
		
		DoctorHours hours = new DoctorHours();
		hours.setDoctorId(doctor.getDoctorId());
		hours.setStartTime(new Timestamp(start));
		hours.setEndTime(new Timestamp(end));
		
		hoursRepo.save(hours);
		
		return hours;
	}
	
	@GetMapping("/availability/inoffice/{doctorId}")
	@ResponseBody
	public InOfficeStatus getStatus(@PathVariable long doctorId) throws ResourceNotFoundException {
		Optional<InOfficeStatus> optional = statusRepo.findByDoctorId(doctorId);
		
		if(optional.isPresent()) throw new ResourceNotFoundException();
		
		return optional.get();
	}
	
	@PostMapping("/availability/inoffice/set")
	@ResponseBody
	public InOfficeStatus set(@RequestParam String token, @RequestParam boolean inOffice) throws SessionNotFoundException, SessionExpiredException, NotDoctorException {
		Optional<Session> optionalSession = sessionRepo.findByToken(token);
		
		if(!optionalSession.isPresent()) {
			throw new SessionNotFoundException();
		}
		
		Session session = optionalSession.get();
		
		if(session.expiry.before(new Timestamp(System.currentTimeMillis()))) {
			throw new SessionExpiredException();
		}
		
		Optional<Doctor> optionalDoctor = doctorRepo.findByUserId(session.getUserId());
		
		if(!optionalDoctor.isPresent()) {
			throw new NotDoctorException();
		}
		
		Doctor doctor = optionalDoctor.get();
		
		Optional<InOfficeStatus> optional = statusRepo.findByDoctorId(doctor.getDoctorId());
		
		if(optional.isPresent()) {
			InOfficeStatus status = optional.get();
			status.setInOffice(inOffice);
			return statusRepo.save(status);
		}
		
		InOfficeStatus status = new InOfficeStatus();
		status.setDoctorId(doctor.getDoctorId());
		status.setInOffice(inOffice);
		
		statusRepo.save(status);
		
		return status;
	}
}
