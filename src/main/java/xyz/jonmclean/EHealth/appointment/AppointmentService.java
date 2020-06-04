package xyz.jonmclean.EHealth.appointment;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.jonmclean.EHealth.models.Appointment;
import xyz.jonmclean.EHealth.models.AppointmentInformation;
import xyz.jonmclean.EHealth.models.Doctor;
import xyz.jonmclean.EHealth.models.Patient;
import xyz.jonmclean.EHealth.models.Session;
import xyz.jonmclean.EHealth.models.exceptions.AppointmentNotFoundException;
import xyz.jonmclean.EHealth.models.exceptions.DoctorInfoNotFoundException;
import xyz.jonmclean.EHealth.models.exceptions.NotDoctorException;
import xyz.jonmclean.EHealth.models.exceptions.SessionExpiredException;
import xyz.jonmclean.EHealth.models.exceptions.SessionNotFoundException;
import xyz.jonmclean.EHealth.models.exceptions.patient.PatientNotFoundException;
import xyz.jonmclean.EHealth.models.response.GenericResponse;
import xyz.jonmclean.EHealth.repositories.AppointmentInformationRepository;
import xyz.jonmclean.EHealth.repositories.AppointmentRepository;
import xyz.jonmclean.EHealth.repositories.DoctorRepository;
import xyz.jonmclean.EHealth.repositories.PatientRepository;
import xyz.jonmclean.EHealth.repositories.SessionRepository;

@Controller
public class AppointmentService {
	
	@Autowired
	AppointmentRepository appointmentRepo;
	
	@Autowired
	AppointmentInformationRepository infoRepo;
	
	@Autowired
	SessionRepository sessionRepo;
	
	@Autowired
	PatientRepository patientRepo;
	
	@Autowired
	DoctorRepository doctorRepo;
	
	@PostMapping("/appointment/doctor/create")
	@ResponseBody
	public Appointment createDoctor(@RequestParam("session") String sessionToken, @RequestParam("patient") long patientId, @RequestParam("startTimestamp") long startTimestamp) 
			throws SessionNotFoundException, NotDoctorException, PatientNotFoundException, SessionExpiredException {
		Optional<Session> optionalSession = sessionRepo.findByToken(sessionToken);
		
		if(!optionalSession.isPresent()) {
			throw new SessionNotFoundException();
		}
		
		Session session = optionalSession.get();
		
		if(session.getExpiry().before(new Timestamp(System.currentTimeMillis()))) {
			throw new SessionExpiredException();
		}
		
		Optional<Doctor> optionalDoctor = doctorRepo.findByUserId(session.getUserId());
		
		if(!optionalDoctor.isPresent()) {
			throw new NotDoctorException();
		}
		
		Doctor doctor = optionalDoctor.get();
		
		Optional<Patient> optionalPatient = patientRepo.findById(patientId);
		
		if(!optionalPatient.isPresent()) {
			throw new PatientNotFoundException();
		}
		
		Patient patient = optionalPatient.get();
		
		Appointment appointment = new Appointment();
		appointment.setDoctorId(doctor.getDoctorId());
		appointment.setPatientId(patient.getPatientId());
		appointment.setStart(startTimestamp);
		
		appointmentRepo.save(appointment);
		
		return appointment;
	}
	
	@PostMapping("/appointment/patient/create")
	@ResponseBody
	public Appointment createPatient(@RequestParam("session") String sessionToken, @RequestParam("doctor") long doctorId, @RequestParam("startTimestamp") long startTimestamp) 
			throws SessionNotFoundException, NotDoctorException, PatientNotFoundException, SessionExpiredException {
		Optional<Session> optionalSession = sessionRepo.findByToken(sessionToken);
		
		if(!optionalSession.isPresent()) {
			throw new SessionNotFoundException();
		}
		
		Session session = optionalSession.get();
		
		if(session.getExpiry().before(new Timestamp(System.currentTimeMillis()))) {
			throw new SessionExpiredException();
		}
		
		Optional<Patient> optionalPatient = patientRepo.findByUserId(session.getUserId());
		
		if(!optionalPatient.isPresent()) {
			throw new PatientNotFoundException();
		}
		
		Patient patient = optionalPatient.get();
		
		Optional<Doctor> optionalDoctor = doctorRepo.findById(doctorId);
		
		if(!optionalDoctor.isPresent()) {
			throw new PatientNotFoundException();
		}
		
		Doctor doctor = optionalDoctor.get();
		
		Appointment appointment = new Appointment();
		appointment.setDoctorId(doctor.getDoctorId());
		appointment.setPatientId(patient.getPatientId());
		appointment.setStart(startTimestamp);
		
		appointmentRepo.save(appointment);
		
		return appointment;
	}
	
	@GetMapping("/appointment/find/patient/{sessionToken}/{timestamp}")
	@ResponseBody
	public List<Appointment> getPatientAppointmentsForDay(@PathVariable("sessionToken") String sessionToken, @PathVariable("timestamp") long timestamp) 
			throws SessionNotFoundException, PatientNotFoundException, SessionExpiredException {
		Optional<Session> optionalSession = sessionRepo.findByToken(sessionToken);
		
		if(!optionalSession.isPresent()) {
			throw new SessionNotFoundException();
		}
		
		Session session = optionalSession.get();
		
		if(session.getExpiry().before(new Timestamp(System.currentTimeMillis()))) {
			throw new SessionExpiredException();
		}
		
		Optional<Patient> optionalPatient = patientRepo.findByUserId(session.getUserId());
		
		if(!optionalPatient.isPresent()) {
			throw new PatientNotFoundException();
		}
		
		Patient patient = optionalPatient.get();
		
		Iterable<Appointment> iterableAppointments = appointmentRepo.findAllByPatientId(patient.getPatientId());
		List<Appointment> unfilteredAppointments = new ArrayList<Appointment>();
		iterableAppointments.forEach(unfilteredAppointments::add);
		
		List<Appointment> filtered = new ArrayList<Appointment>();
		
		for(Appointment appointment : unfilteredAppointments) {
			if((new Date(appointment.getStart())).toString().equalsIgnoreCase(new Date(timestamp).toString())) {
				filtered.add(appointment);
			}
		}
		
		return filtered;
	}
	
	@GetMapping("/appointment/find/doctor/{sessionToken}/{timestamp}")
	@ResponseBody
	public List<Appointment> getDoctorAppointmentsForDay(@PathVariable("sessionToken") String sessionToken, @PathVariable("timestamp") long timestamp) 
			throws SessionNotFoundException, PatientNotFoundException, SessionExpiredException, DoctorInfoNotFoundException {
		Optional<Session> optionalSession = sessionRepo.findByToken(sessionToken);
		
		if(!optionalSession.isPresent()) {
			throw new SessionNotFoundException();
		}
		
		Session session = optionalSession.get();
		
		if(session.getExpiry().before(new Timestamp(System.currentTimeMillis()))) {
			throw new SessionExpiredException();
		}
		
		Optional<Doctor> optionalDoctor = doctorRepo.findByUserId(session.getUserId());
		
		if(!optionalDoctor.isPresent()) {
			throw new DoctorInfoNotFoundException();
		}
		
		Doctor doctor = optionalDoctor.get();
		
		Iterable<Appointment> iterableAppointments = appointmentRepo.findAllByDoctorId(doctor.getDoctorId());
		List<Appointment> unfilteredAppointments = new ArrayList<Appointment>();
		iterableAppointments.forEach(unfilteredAppointments::add);
		
		List<Appointment> filtered = new ArrayList<Appointment>();
		
		for(Appointment appointment : unfilteredAppointments) {
			if((new Date(appointment.getStart())).toString().equalsIgnoreCase(new Date(timestamp).toString())) {
				filtered.add(appointment);
			}
		}
		
		return filtered;
	}
	
	@GetMapping("/appointment/find/patient/{token}")
	@ResponseBody
	public List<Appointment> getAppointmentsForPatient(@PathVariable("token") String sessionToken) throws SessionNotFoundException, SessionExpiredException, PatientNotFoundException {
		Optional<Session> optionalSession = sessionRepo.findByToken(sessionToken);
		
		if(!optionalSession.isPresent()) {
			throw new SessionNotFoundException();
		}
		
		Session session = optionalSession.get();
		
		if(session.getExpiry().before(new Timestamp(System.currentTimeMillis()))) {
			throw new SessionExpiredException();
		}
		
		Optional<Patient> optionalPatient = patientRepo.findByUserId(session.getUserId());
		
		if(!optionalPatient.isPresent()) {
			throw new PatientNotFoundException();
		}
		
		Patient patient = optionalPatient.get();
		
		Iterable<Appointment> iterableAppointments = appointmentRepo.findAllByPatientId(patient.getPatientId());
		List<Appointment> unfilteredAppointments = new ArrayList<Appointment>();
		iterableAppointments.forEach(unfilteredAppointments::add);
		
		return unfilteredAppointments;
	}
	
	@GetMapping("/appointment/find/universal/{sessionToken}/{id}")
	@ResponseBody
	public Appointment getAppointment(@PathVariable("sessionToken") String sessionToken, @PathVariable("appointmentId") long appointmentId) 
			throws SessionNotFoundException, SessionExpiredException, AppointmentNotFoundException {
		Optional<Session> optionalSession = sessionRepo.findByToken(sessionToken);
		
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
		
		return optionalAppointment.get();
	}
	
	@DeleteMapping("/appointment/cancel/{sessionToken}/{id}")
	@ResponseBody
	public GenericResponse deleteAppointment(@PathVariable("sessionToken") String token, @PathVariable("id") long appointmentId) 
			throws SessionNotFoundException, SessionExpiredException, AppointmentNotFoundException {
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
		
		Optional<AppointmentInformation> optionalInfo = infoRepo.findByAppointmentId(appointment.getAppointmentId());
		
		if(optionalInfo.isPresent()) {
			AppointmentInformation information = optionalInfo.get();
			
			infoRepo.delete(information);
		}
		
		appointmentRepo.delete(appointment);
		
		return new GenericResponse(true, "");
	}
	
	@GetMapping("/appointment/all/doctor/{doctorId}")
	@ResponseBody
	public List<Appointment> findAppointmentsForDoctor(@PathVariable long doctorId) {
		Iterable<Appointment> iterable = appointmentRepo.findAllByDoctorId(doctorId);
		List<Appointment> appointment = new ArrayList<>();
		
		for(Appointment app : iterable) {
			appointment.add(app);
		}
		
		return appointment;
	}
	
}
