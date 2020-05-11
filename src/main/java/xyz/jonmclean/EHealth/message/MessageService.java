package xyz.jonmclean.EHealth.message;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.firestore.Firestore;

import xyz.jonmclean.EHealth.models.Message;
import xyz.jonmclean.EHealth.repositories.MessageRepository;

@Controller
public class MessageService {

	@Autowired
	public MessageRepository messageRepo;
	
	@Autowired
	public Firestore firestore;
	
	@Autowired
	ObjectMapper objMap;
	
	@PostMapping("/message/{messageGroupId}/send")
	@ResponseBody
	public Message sendMessage(@PathVariable long messageGroupId, @RequestParam long userId, @RequestParam String content) throws InterruptedException, ExecutionException {
		Message message = new Message(userId, messageGroupId, content);
		message = messageRepo.save(message);
		
		firestore.collection("message-group").document(""+messageGroupId).collection("message").document(""+message.getMessageId()).create(objMap.convertValue(message, Map.class)).get();
		
		return message;
	}

	
}
