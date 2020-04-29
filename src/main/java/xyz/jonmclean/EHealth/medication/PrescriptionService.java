package xyz.jonmclean.EHealth.medication;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
	public Prescription create(@RequestParam("patientId") long patientId, @RequestParam("medicationId") long medicationId, @RequestParam("frequency") double frequency, @RequestParam("frequencyUnit") String unit, @RequestParam("notes") String notes, @RequestParam("sessionToken") String token) throws SessionNotFoundException, NotDoctorException, PatientNotFoundException, MedicationNotFoundException {
		Optional<Session> optionalSession = sessionRepo.findByToken(token);
		
		if(!optionalSession.isPresent()) {
			throw new SessionNotFoundException();
		}
		
		Session session = optionalSession.get();
		
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
	
}
