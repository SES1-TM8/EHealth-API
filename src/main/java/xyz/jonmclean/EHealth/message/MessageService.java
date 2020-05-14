package xyz.jonmclean.EHealth.message;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.firestore.Firestore;

import xyz.jonmclean.EHealth.models.Session;
import xyz.jonmclean.EHealth.models.exceptions.SessionExpiredException;
import xyz.jonmclean.EHealth.models.exceptions.SessionNotFoundException;
import xyz.jonmclean.EHealth.models.message.Message;
import xyz.jonmclean.EHealth.repositories.MessageRepository;
import xyz.jonmclean.EHealth.repositories.SessionRepository;

@Controller
public class MessageService {

	@Autowired
	public MessageRepository messageRepo;
	
	@Autowired
	public SessionRepository sessionRepo;
	
	@Autowired
	public Firestore firestore;
	
	@Autowired
	ObjectMapper objMap;
	
	@PostMapping("/message/{messageGroupId}/send")
	@ResponseBody
	public Message sendMessage(@PathVariable long messageGroupId, @RequestParam String sessionToken, @RequestParam String content) 
			throws InterruptedException, ExecutionException, SessionNotFoundException, SessionExpiredException {
		Optional<Session> optionalSession = sessionRepo.findByToken(sessionToken);
		
		if(!optionalSession.isPresent()) throw new SessionNotFoundException();
		
		Session session = optionalSession.get();
		
		if(session.getExpiry().before(new Timestamp(System.currentTimeMillis()))) throw new SessionExpiredException();
		
		Message message = new Message(session.getUserId(), messageGroupId, content);
		message = messageRepo.save(message);
		
		firestore.collection("message-group").document(""+messageGroupId).collection("message").document(""+message.getMessageId()).create(objMap.convertValue(message, Map.class)).get();
		
		return message;
	}

	
}
