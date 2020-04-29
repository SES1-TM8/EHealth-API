package xyz.jonmclean.EHealth.appointment;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.jonmclean.EHealth.models.Appointment;
import xyz.jonmclean.EHealth.models.AppointmentInformation;
import xyz.jonmclean.EHealth.models.Session;
import xyz.jonmclean.EHealth.models.exceptions.AppointmentInformationNotFoundException;
import xyz.jonmclean.EHealth.models.exceptions.AppointmentNotFoundException;
import xyz.jonmclean.EHealth.models.exceptions.SessionExpiredException;
import xyz.jonmclean.EHealth.models.exceptions.SessionNotFoundException;
import xyz.jonmclean.EHealth.repositories.AppointmentInformationRepository;
import xyz.jonmclean.EHealth.repositories.AppointmentRepository;
import xyz.jonmclean.EHealth.repositories.SessionRepository;

@Controller
public class AppointmentInformationService {
	
	@Autowired
	public AppointmentRepository appointmentRepo;
	
	@Autowired
	public AppointmentInformationRepository infoRepo;
	
	@Autowired
	public SessionRepository sessionRepo;
	
	@PostMapping("/appointment/info/add")
	@ResponseBody
	public AppointmentInformation add(@RequestParam("sessionToken") String token, @RequestParam("appointmentId") long appointmentId, @RequestParam("description") String description, @RequestParam("image") List<Long> images) throws SessionNotFoundException, AppointmentNotFoundException, SessionExpiredException {
		Optional<Session> optionalSession = sessionRepo.findByToken(token);
		
		if(!optionalSession.isPresent()) {
			throw new SessionNotFoundException();
		}
		
		Session session = optionalSession.get();
		
		if(session.getExpiry().after(new Timestamp(System.currentTimeMillis()))) {
			throw new SessionExpiredException();
		}
		
		Optional<Appointment> optionalAppointment = appointmentRepo.findById(appointmentId);
		
		if(!optionalAppointment.isPresent()) {
			throw new AppointmentNotFoundException();
		}
		
		Appointment appointment = optionalAppointment.get();
		
		AppointmentInformation information = new AppointmentInformation();
		information.setAppointmentId(appointment.getAppointmentId());
		information.setDescription(description);
		information.setImageIds(images);
		
		infoRepo.save(information);
		
		return information;
	}
	
	@GetMapping("/appointment/info/get/{sessionToken}/{id}")
	@ResponseBody
	public AppointmentInformation get(@PathVariable("sessionToken") String sessionToken, @PathVariable("id") long appointmentId) throws SessionNotFoundException, AppointmentInformationNotFoundException, SessionExpiredException {
		
		Optional<Session> optionalSession = sessionRepo.findByToken(sessionToken);
		
		if(!optionalSession.isPresent()) {
			throw new SessionNotFoundException();
		}
		
		Session session = optionalSession.get();
		
		if(session.getExpiry().after(new Timestamp(System.currentTimeMillis()))) {
			throw new SessionExpiredException();
		}
		
		Optional<AppointmentInformation> optionalInformation = infoRepo.findByAppointmentId(appointmentId);
		
		if(!optionalInformation.isPresent()) {
			throw new AppointmentInformationNotFoundException();
		}
		
		return optionalInformation.get();
	}
}
