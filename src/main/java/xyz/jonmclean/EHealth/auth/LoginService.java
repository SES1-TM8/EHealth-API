package xyz.jonmclean.EHealth.auth;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import xyz.jonmclean.EHealth.helper.HashHelper;
import xyz.jonmclean.EHealth.models.Session;
import xyz.jonmclean.EHealth.models.User;
import xyz.jonmclean.EHealth.models.exceptions.SessionExpiredException;
import xyz.jonmclean.EHealth.models.exceptions.SessionNotFoundException;
import xyz.jonmclean.EHealth.models.exceptions.UserNotFoundException;
import xyz.jonmclean.EHealth.models.exceptions.UserPasswordWrongException;
import xyz.jonmclean.EHealth.models.response.SessionResponse;
import xyz.jonmclean.EHealth.repositories.SessionRepository;
import xyz.jonmclean.EHealth.repositories.UserRepository;

@Controller
public class LoginService {
	
	@Autowired
	public UserRepository userRepo;
	
	@Autowired
	public SessionRepository sessionRepo;
	
	@PostMapping("/user/login")
	@ResponseBody
	public SessionResponse login(@RequestParam("email") String emailAddress, @RequestParam("password") String password) throws UserNotFoundException, UserPasswordWrongException {
		Optional<User> optionalUser = userRepo.findByEmailAddress(emailAddress);
		
		if(!optionalUser.isPresent()) {
			throw new UserNotFoundException();
		}
		
		User user = optionalUser.get();
		
		String hashedPassword = HashHelper.sha256(password + user.getPasswordSalt());
		
		if(!hashedPassword.equals(user.getPasswordHash())) {
			throw new UserPasswordWrongException();
		}
		
		
		SecureRandom rand = new SecureRandom();
		Base64.Encoder enc = Base64.getEncoder();
		
		byte[] tokenBytes = new byte[16];
		rand.nextBytes(tokenBytes);
		String token = enc.encodeToString(tokenBytes);
		
		long expiry = System.currentTimeMillis() + (604800 * 1000);
		
		Session session = new Session(token, new Timestamp(expiry), user.getUserId());
		
		sessionRepo.save(session);
		
		SessionResponse response = new SessionResponse();
		response.setSessionId(session.getSessionId());
		response.setToken(session.getToken());
		response.setExpiry(expiry);
		response.setUserId(user.getUserId());
		
		return response;
	}
	
	@GetMapping("/user/auth/firebase/{session}")
	@ResponseBody
	public String getFirebaseToken(@PathVariable("session") String token) throws SessionNotFoundException, SessionExpiredException, FirebaseAuthException {
		Optional<Session> optionalSession = sessionRepo.findByToken(token);
		
		if(!optionalSession.isPresent()) {
			throw new SessionNotFoundException();
		}
		
		Session session = optionalSession.get();
		
		if(session.getExpiry().before(new Timestamp(System.currentTimeMillis()))) {
			throw new SessionExpiredException();
		}
		
		return FirebaseAuth.getInstance().createCustomToken("" + session.getUserId());
	}
	
}
