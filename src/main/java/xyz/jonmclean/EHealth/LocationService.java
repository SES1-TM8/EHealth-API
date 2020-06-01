package xyz.jonmclean.EHealth;

import java.awt.geom.Path2D;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.jonmclean.EHealth.models.BoundCheckResponse;
import xyz.jonmclean.EHealth.models.BoundaryResponse;
import xyz.jonmclean.EHealth.models.Coordinate;
import xyz.jonmclean.EHealth.models.Doctor;
import xyz.jonmclean.EHealth.models.MapCoordinate;
import xyz.jonmclean.EHealth.models.OfficeBoundary;
import xyz.jonmclean.EHealth.models.Session;
import xyz.jonmclean.EHealth.models.exceptions.NotDoctorException;
import xyz.jonmclean.EHealth.models.exceptions.ResourceNotFoundException;
import xyz.jonmclean.EHealth.models.exceptions.SessionExpiredException;
import xyz.jonmclean.EHealth.models.exceptions.SessionNotFoundException;
import xyz.jonmclean.EHealth.repositories.BoundaryRepository;
import xyz.jonmclean.EHealth.repositories.CoordinateRepository;
import xyz.jonmclean.EHealth.repositories.DoctorRepository;
import xyz.jonmclean.EHealth.repositories.SessionRepository;

@Controller
public class LocationService {
	
	@Autowired
	BoundaryRepository boundaryRepo;
	
	@Autowired
	CoordinateRepository coordRepo;
	
	@Autowired
	SessionRepository sessionRepo;
	
	@Autowired
	DoctorRepository doctorRepo;
	
	@GetMapping("/boundary/{boundaryId}")
	@ResponseBody
	public BoundaryResponse get(@PathVariable long boundaryId) throws ResourceNotFoundException{
		Optional<OfficeBoundary> optionalBoundary = boundaryRepo.findById(boundaryId);
		
		if(!optionalBoundary.isPresent()) {
			throw new ResourceNotFoundException();
		}
		
		OfficeBoundary boundary = optionalBoundary.get();
		BoundaryResponse response = new BoundaryResponse();
		
		List<List<Coordinate>> groups = new ArrayList<>();
		List<Coordinate> currentGroup = new ArrayList<>();
		int currentGroupId = -1;
		
		for(MapCoordinate mc : boundary.getBounds()) {
			if(currentGroupId != mc.getCoordGroup()) {
				groups.add(currentGroup = new ArrayList<>());
				currentGroupId = mc.getCoordGroup();
			}
			
			currentGroup.add(new Coordinate(mc.getLatitude(), mc.getLongitude(), mc.getCoordOrder()));
		}
		
		response.setCoordinates(groups);
		response.setName(boundary.getName());
		
		Optional<Doctor> optionalDoctor = doctorRepo.findById(boundary.getOwnerId());
		
		if(optionalDoctor.isPresent()) {
			response.setOwner(optionalDoctor.get());
		}
		
		response.setId(boundary.getBoundaryId());
		
		return response;
	}
	
	@PostMapping("/boundary/{token}")
	@ResponseBody
	public BoundaryResponse post(@RequestBody BoundaryResponse boundary, @PathVariable String token) throws ResourceNotFoundException, SessionNotFoundException, SessionExpiredException, NotDoctorException {
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
		
		OfficeBoundary b;
		if(boundary.getId() != null) {
			Optional<OfficeBoundary> optionalOb = boundaryRepo.findById(boundary.getId());
			if(!optionalOb.isPresent()) {
				throw new ResourceNotFoundException();
			}
			
			b = optionalOb.get();
		}else {
			b = boundaryRepo.save(new OfficeBoundary());
		}
		
		coordRepo.deleteByBoundary(b);
		
		for(int i = 0; i < boundary.getCoordinates().size(); i++) {
			for(Coordinate c : boundary.getCoordinates().get(i)) {
				b.getBounds().add(coordRepo.save(new MapCoordinate(b, c.getLatitude(), c.getLongitude(), i, c.getOrder())));
			}
		}
		
		b.setOwnerId(doctor.getDoctorId());
		b.setName(boundary.getName());
		
		boundaryRepo.save(b);
		
		return get(b.getBoundaryId());
	}
	
	@DeleteMapping("/boundary/{boundaryId}")
	@ResponseBody
	public void delete(@PathVariable long boundaryId) {
		boundaryRepo.deleteById(boundaryId);
	}
	
	@PostMapping("/boundary/check/")
	@ResponseBody
	public BoundCheckResponse check(@RequestParam String token, @RequestParam double latitude, @RequestParam double longitude) throws ResourceNotFoundException, SessionNotFoundException, SessionExpiredException, NotDoctorException {
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
		
		Iterable<OfficeBoundary> boundaries = boundaryRepo.findAllByOwnerId(doctor.getDoctorId());
		
		BoundCheckResponse response = new BoundCheckResponse();
		response.setLatitude(latitude);
		response.setLongitude(longitude);
		
		for(OfficeBoundary boundary : boundaries) {
			MapCoordinate[] coordinates = new MapCoordinate[boundary.getBounds().size()];
			coordinates = boundary.getBounds().toArray(coordinates);
			
			if(isInsideBoundary(latitude, longitude, coordinates)) {
				response.setBoundaryId(boundary.getBoundaryId());
				response.setWithinBounds(true);
				return response;
			}
		}
		
		response.setWithinBounds(false);
		response.setBoundaryId(null);
		
		return response;
	}
	
	private boolean isInsideBoundary(double lat, double lon, MapCoordinate[] boundary) {
		Path2D polygon = new Path2D.Double();
		polygon.moveTo(boundary[0].getLatitude(), boundary[0].getLongitude());
		for(int i = 0; i < boundary.length; i++) {
			polygon.lineTo(boundary[i].getLatitude(), boundary[i].getLongitude());
		}
		
		polygon.closePath();
		
		return polygon.contains(lat, lon);
	}
}
