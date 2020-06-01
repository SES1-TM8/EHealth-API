package xyz.jonmclean.EHealth.urgent;

import java.io.IOException;
import java.sql.Timestamp;
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

import xyz.jonmclean.EHealth.image.ImageService;
import xyz.jonmclean.EHealth.image.models.Image;
import xyz.jonmclean.EHealth.image.models.S3Upload;
import xyz.jonmclean.EHealth.models.Doctor;
import xyz.jonmclean.EHealth.models.InternalImageCallback;
import xyz.jonmclean.EHealth.models.Patient;
import xyz.jonmclean.EHealth.models.Session;
import xyz.jonmclean.EHealth.models.UrgentCase;
import xyz.jonmclean.EHealth.models.exceptions.NotDoctorException;
import xyz.jonmclean.EHealth.models.exceptions.NotPatientException;
import xyz.jonmclean.EHealth.models.exceptions.ResourceNotFoundException;
import xyz.jonmclean.EHealth.models.exceptions.SessionExpiredException;
import xyz.jonmclean.EHealth.models.exceptions.SessionNotFoundException;
import xyz.jonmclean.EHealth.models.response.UrgentCaseCallbackResponse;
import xyz.jonmclean.EHealth.repositories.DoctorRepository;
import xyz.jonmclean.EHealth.repositories.PatientRepository;
import xyz.jonmclean.EHealth.repositories.SessionRepository;
import xyz.jonmclean.EHealth.repositories.UrgentCaseRepository;

@Controller
public class UrgentCaseService {
	
	@Autowired
	SessionRepository sessionRepo;
	
	@Autowired
	DoctorRepository doctorRepo;
	
	@Autowired
	UrgentCaseRepository urgentRepo;
	
	@Autowired
	PatientRepository patientRepo;
	
	@Autowired
	ImageService imageService;
	
	@GetMapping("/urgent/{id}/{token}")
	@ResponseBody
	public UrgentCase get(@PathVariable long id, @PathVariable String token) throws SessionNotFoundException, SessionExpiredException, ResourceNotFoundException {
		Optional<Session> optionalSession = sessionRepo.findByToken(token);
		
		if(!optionalSession.isPresent()) {
			throw new SessionNotFoundException();
		}
		
		Session session = optionalSession.get();
		
		if(session.expiry.before(new Timestamp(System.currentTimeMillis()))) {
			throw new SessionExpiredException();
		}
		
		Optional<UrgentCase> optional = urgentRepo.findById(id);
		
		if(!optional.isPresent()) throw new ResourceNotFoundException();
		
		return optional.get();
	}
	
	@GetMapping("/urgent/unresolved/{token}")
	@ResponseBody
	public List<UrgentCase> getUnresolvedUrgentCases(@PathVariable String token) throws NotDoctorException, SessionNotFoundException, SessionExpiredException {
		Optional<Session> optionalSession = sessionRepo.findByToken(token);
		
		if(!optionalSession.isPresent()) {
			throw new SessionNotFoundException();
		}
		
		Session session = optionalSession.get();
		
		if(session.expiry.before(new Timestamp(System.currentTimeMillis()))) {
			throw new SessionExpiredException();
		}
		
		Optional<Doctor> optionalDoctor = doctorRepo.findByUserId(session.getUserId());
		
		if(!optionalDoctor.isPresent()) throw new NotDoctorException();
		
		List<UrgentCase> cases = urgentRepo.findByResolved(false);
		
		return cases;
	}
	
	@PostMapping("/urgent/resolve")
	@ResponseBody
	public UrgentCase resolve(@RequestParam String token, @RequestParam long caseId) throws SessionNotFoundException, SessionExpiredException, NotDoctorException, ResourceNotFoundException {
		Optional<Session> optionalSession = sessionRepo.findByToken(token);
		
		if(!optionalSession.isPresent()) {
			throw new SessionNotFoundException();
		}
		
		Session session = optionalSession.get();
		
		if(session.expiry.before(new Timestamp(System.currentTimeMillis()))) {
			throw new SessionExpiredException();
		}
		
		Optional<Doctor> optionalDoctor = doctorRepo.findByUserId(session.getUserId());
		
		if(!optionalDoctor.isPresent()) throw new NotDoctorException();
		
		Optional<UrgentCase> optionalCase = urgentRepo.findById(caseId);
		
		if(!optionalCase.isPresent()) throw new ResourceNotFoundException();
		
		UrgentCase c = optionalCase.get();
		c.setResolved(true);
		c.setCloseTime(new Timestamp(System.currentTimeMillis()));
		c.setResolvedById(optionalDoctor.get().getDoctorId());
		
		urgentRepo.save(c);
		
		return c;
	}
	
	@PostMapping("/urgent/submit")
	@ResponseBody
	public UrgentCaseCallbackResponse post(@RequestParam String token, @RequestParam String description, @RequestParam("image") List<String> mimeTypes) throws SessionNotFoundException, SessionExpiredException, NotPatientException {
		Optional<Session> optionalSession = sessionRepo.findByToken(token);
		
		if(!optionalSession.isPresent()) {
			throw new SessionNotFoundException();
		}
		
		Session session = optionalSession.get();
		
		if(session.expiry.before(new Timestamp(System.currentTimeMillis()))) {
			throw new SessionExpiredException();
		}
		
		Optional<Patient> optionalPatient = patientRepo.findByUserId(session.getUserId());
		
		if(!optionalPatient.isPresent()) throw new NotPatientException();
		
		Patient patient = optionalPatient.get();
		
		UrgentCase c = new UrgentCase();
		c.setOpenTime(new Timestamp(System.currentTimeMillis()));
		c.setDescription(description);
		c.setPatientId(patient.getPatientId());
		c.setResolved(false);
		
		urgentRepo.save(c);
		
		UrgentCaseCallbackResponse response = new UrgentCaseCallbackResponse();
		response.setPatientId(patient.getPatientId());
		response.setDescription(description);
		response.setResolved(false);
		response.setOpenTime(c.getOpenTime());
		response.setUrgentId(c.getId());
		
		List<S3Upload> uploads = new ArrayList<>();
		
		for(String mimeType : mimeTypes) {
			S3Upload upload = imageService.getUpload("urgent_case", mimeType, patient.getPatientId(), "urgent/submit/callback", c.getId());
			uploads.add(upload);
		}
		
		response.setUploads(uploads);
		
		return response;
	}
	
	@GetMapping("/urgent/submit/callback")
	@ResponseBody
	public Image callback(@RequestParam String id) throws ResourceNotFoundException, IOException {
		InternalImageCallback internal = imageService.process(id);
		Optional<UrgentCase> optionalCase = urgentRepo.findById(internal.getAppointmentInfoId());
		
		if(!optionalCase.isPresent()) throw new ResourceNotFoundException();
		
		UrgentCase c = optionalCase.get();
		c.getImageIds().add(internal.getImageId());
		
		urgentRepo.save(c);
		
		return imageService.getImage(internal.getImageId());
	}
	
}
