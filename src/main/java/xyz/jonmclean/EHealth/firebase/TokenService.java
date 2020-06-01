package xyz.jonmclean.EHealth.firebase;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.jonmclean.EHealth.models.Session;
import xyz.jonmclean.EHealth.models.exceptions.SessionExpiredException;
import xyz.jonmclean.EHealth.models.exceptions.SessionNotFoundException;
import xyz.jonmclean.EHealth.models.response.GenericResponse;
import xyz.jonmclean.EHealth.repositories.SessionRepository;

@Controller
public class TokenService {
	
	@Autowired
	SessionRepository sessionRepo;
	
	@PostMapping("/firebase/token")
	@ResponseBody
	public GenericResponse registerToken(@RequestParam String token, @RequestParam String fcmToken) throws SessionNotFoundException, SessionExpiredException {
		Optional<Session> optionalSession = sessionRepo.findByToken(token);
		if(!optionalSession.isPresent()) {
			throw new SessionNotFoundException();
		}
		
		Session session = optionalSession.get();
		
		if(session.expiry.before(new Timestamp(System.currentTimeMillis()))) {
			throw new SessionExpiredException();
		}
		
		session.setFcmToken(fcmToken);
		
		sessionRepo.save(session);
		
		return new GenericResponse(true, "Registered FCM token");
	}
	
}
