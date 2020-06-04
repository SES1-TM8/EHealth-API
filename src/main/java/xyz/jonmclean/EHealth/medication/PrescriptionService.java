package xyz.jonmclean.EHealth.medication;

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

import xyz.jonmclean.EHealth.models.Doctor;
import xyz.jonmclean.EHealth.models.Medication;
import xyz.jonmclean.EHealth.models.Patient;
import xyz.jonmclean.EHealth.models.Prescription;
import xyz.jonmclean.EHealth.models.Session;
import xyz.jonmclean.EHealth.models.exceptions.MedicationNotFoundException;
import xyz.jonmclean.EHealth.models.exceptions.NotDoctorException;
import xyz.jonmclean.EHealth.models.exceptions.SessionExpiredException;
import xyz.jonmclean.EHealth.models.exceptions.SessionNotFoundException;
import xyz.jonmclean.EHealth.models.exceptions.patient.PatientNotFoundException;
import xyz.jonmclean.EHealth.repositories.DoctorRepository;
import xyz.jonmclean.EHealth.repositories.MedicationRepository;
import xyz.jonmclean.EHealth.repositories.PatientRepository;
import xyz.jonmclean.EHealth.repositories.PrescriptionRepository;
import xyz.jonmclean.EHealth.repositories.SessionRepository;

@Controller
public class PrescriptionService {
	
	@Autowired
	public PatientRepository patientRepo;
	
	@Autowired
	public DoctorRepository doctorRepo;
	
	@Autowired
	public PrescriptionRepository prescriptionRepo;
	
	@Autowired
	public SessionRepository sessionRepo;
	
	@Autowired
	public MedicationRepository medicationRepo;
	
	@PostMapping("/prescription/add")
	@ResponseBody
	public Prescription create(@RequestParam("patientId") long patientId, @RequestParam("medicationId") long medicationId, @RequestParam("frequency") double frequency, @RequestParam("frequencyUnit") String unit, @RequestParam("notes") String notes, @RequestParam("sessionToken") String token) throws SessionNotFoundException, NotDoctorException, PatientNotFoundException, MedicationNotFoundException, SessionExpiredException {
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
		
		Optional<Patient> optionalPatient = patientRepo.findById(patientId);
		
		if(!optionalPatient.isPresent()) {
			throw new PatientNotFoundException();
		}
		
		Optional<Medication> optionalMedication = medicationRepo.findById(medicationId);
		
		if(!optionalMedication.isPresent()) {
			throw new MedicationNotFoundException();
		}
		
		Prescription prescription = new Prescription();
		prescription.setPatientId(patientId);
		prescription.setMedicationId(medicationId);
		prescription.setPrescriberId(doctor.getDoctorId());
		prescription.setFrequency(frequency);
		prescription.setFrequencyUnit(unit);
		prescription.setNotes(notes);
		
		prescriptionRepo.save(prescription);
		
		return prescription;
	}
	
	@GetMapping("/prescription/all/s/{token}")
	@ResponseBody
	public List<Prescription> getAll(@PathVariable String token) throws SessionNotFoundException, SessionExpiredException, PatientNotFoundException {
		Optional<Session> optionalSession = sessionRepo.findByToken(token);
		
		if(!optionalSession.isPresent()) {
			throw new SessionNotFoundException();
		}
		
		Session session = optionalSession.get();
		
		if(session.expiry.before(new Timestamp(System.currentTimeMillis()))) {
			throw new SessionExpiredException();
		}
		
		Optional<Patient> optionalPatient = patientRepo.findByUserId(session.getUserId());
		
		if(!optionalPatient.isPresent()) {
			System.out.println("Not found patient");
			throw new PatientNotFoundException();
		}
		
		Iterable<Prescription> iterablePrescription = prescriptionRepo.findAllByPatientId(optionalPatient.get().getPatientId());
		List<Prescription> prescriptions = new ArrayList<>();
		
		
		for(Prescription p : iterablePrescription) {
			prescriptions.add(p);
		}
		
		return prescriptions;
	}
	
	@GetMapping("/prescription/all/p/{patientId}")
	@ResponseBody
	public List<Prescription> getPatientPrescriptions(@PathVariable long patientId) throws PatientNotFoundException {
		Optional<Patient> optionalPatient = patientRepo.findById(patientId);
		
		if(!optionalPatient.isPresent()) {
			throw new PatientNotFoundException();
		}
		
		Iterable<Prescription> iterablePrescription = prescriptionRepo.findAllByPatientId(optionalPatient.get().getPatientId());
		List<Prescription> prescriptions = new ArrayList<>();
		
		
		for(Prescription p : iterablePrescription) {
			prescriptions.add(p);
		}
		
		return prescriptions;
	}
	
}
