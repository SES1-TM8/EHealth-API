package xyz.jonmclean.EHealth.helper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;

import xyz.jonmclean.EHealth.models.Session;
import xyz.jonmclean.EHealth.repositories.SessionRepository;

public class NotificationController {
	
	@Autowired
	static SessionRepository sessionRepo;
	
	public static List<String> getFCMTokens(long userId) {
		List<Session> sessions = sessionRepo.findAllByUserId(userId);
		
		List<String> tokens = new ArrayList<>();
		
		for(Session session : sessions) {
			if(session.getExpiry().after(new Timestamp(System.currentTimeMillis()))) {
				tokens.add(session.getFcmToken());
			}
		}
		
		return tokens;
	}
	
	public static void sendCloudMessageToUser(String title, String message, String topic, List<String> fcmTokens, Map<String, String> data) throws FirebaseMessagingException {
		MulticastMessage.Builder m = MulticastMessage.builder();
		
		for(String token : fcmTokens) {
			m.addToken(token);
		}
		
		Notification.Builder builder = Notification.builder();
		builder.setTitle(title);
		builder.setBody(message);
		m.setNotification(builder.build()).putData("message", message).putData("topic", topic);
		
		FirebaseMessaging.getInstance().sendMulticast(m.build());
	}
	
}
