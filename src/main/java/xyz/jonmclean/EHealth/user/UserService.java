package xyz.jonmclean.EHealth.user;

import java.sql.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.jonmclean.EHealth.helper.HashHelper;
import xyz.jonmclean.EHealth.models.User;
import xyz.jonmclean.EHealth.models.UserResponse;
import xyz.jonmclean.EHealth.models.exceptions.UserAlreadyExistsException;
import xyz.jonmclean.EHealth.models.exceptions.UserNotFoundException;

public class UserService {
	
	@Autowired
	public UserRepository userRepo;
	
	@GetMapping("user/{id}")
	@ResponseBody
	public UserResponse get(@PathVariable long id) throws UserNotFoundException {
		Optional<User> optional = userRepo.findById(id);
		
		if(!optional.isPresent()) {
			throw new UserNotFoundException();
		}
		
		User user = optional.get();
		UserResponse response = new UserResponse();
		response.setUserId(user.getUserId());
		response.setEmailAddress(user.getEmailAddress());
		response.setFirstName(user.getFirstName());
		response.setLastName(user.getLastName());
		response.setDob(user.getDob());
		response.setPhoneNumber(user.getPhoneNumber());
		
		return response;
	}
	
	@GetMapping("user/{email}")
	@ResponseBody
	public UserResponse get(@PathVariable String email) throws UserNotFoundException {
		Optional<User> optional = userRepo.findByEmailAddress(email);
		
		if(!optional.isPresent()) {
			throw new UserNotFoundException();
		}
		
		User user = optional.get();
		UserResponse response = new UserResponse();
		response.setUserId(user.getUserId());
		response.setEmailAddress(user.getEmailAddress());
		response.setFirstName(user.getFirstName());
		response.setLastName(user.getLastName());
		response.setDob(user.getDob());
		response.setPhoneNumber(user.getPhoneNumber());
		
		return response;
	}
	
	@PostMapping("/user/register")
	@ResponseBody
	public UserResponse create(@RequestParam("email") String emailAddress, @RequestParam("password") String password, @RequestParam("phone") String phoneNumber, @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName, @RequestParam("dob") Long dobTimestamp) throws UserAlreadyExistsException, UserNotFoundException {
		String salt = UUID.randomUUID().toString();
		String hashedPassword = HashHelper.sha256(password + salt);
		
		Optional<User> existingUser = userRepo.findByEmailAddress(emailAddress);
		
		if(existingUser.isPresent()) {
			throw new UserAlreadyExistsException();
		}
		
		User user = new User(emailAddress, hashedPassword, salt, firstName, lastName, new Date(dobTimestamp), phoneNumber);
		
		userRepo.save(user);
		
		return get(user.getUserId());
	}
	
}
