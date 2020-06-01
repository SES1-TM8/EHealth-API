package xyz.jonmclean.EHealth.message;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;

import au.com.origma.pagination.Page;
import xyz.jonmclean.EHealth.models.Session;
import xyz.jonmclean.EHealth.models.User;
import xyz.jonmclean.EHealth.models.exceptions.ForbiddenException;
import xyz.jonmclean.EHealth.models.exceptions.ResourceNotFoundException;
import xyz.jonmclean.EHealth.models.exceptions.SessionExpiredException;
import xyz.jonmclean.EHealth.models.exceptions.SessionNotFoundException;
import xyz.jonmclean.EHealth.models.message.MessageGroup;
import xyz.jonmclean.EHealth.models.message.MessageGroupMember;
import xyz.jonmclean.EHealth.models.message.MessageGroupResponse;
import xyz.jonmclean.EHealth.models.response.UserResponse;
import xyz.jonmclean.EHealth.repositories.MessageGroupMemberRepository;
import xyz.jonmclean.EHealth.repositories.MessageGroupRepository;
import xyz.jonmclean.EHealth.repositories.SessionRepository;
import xyz.jonmclean.EHealth.repositories.UserRepository;

@Controller
public class MessageGroupService {
	
	public static final int PAGE_SIZE = 25;
	
	@Autowired
	MessageGroupRepository groupRepo;
	
	@Autowired
	SessionRepository sessionRepo;
	
	@Autowired
	MessageGroupMemberRepository groupMemberRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	Firestore firestore;
	
	@Autowired
	ObjectMapper map;
	
	
	@PostMapping("/message")
	@ResponseBody
	public MessageGroupResponse createMessageGroup(boolean direct, @RequestParam(required = false) String name, @RequestParam List<Long> memberIds, @RequestParam String sessionToken) throws SessionNotFoundException, ResourceNotFoundException, SessionExpiredException {
		
		Optional<Session> optionalSession = sessionRepo.findByToken(sessionToken);
		
		if(!optionalSession.isPresent()) {
			throw new SessionNotFoundException();
		}
		
		Session session = optionalSession.get();
		
		if(session.getExpiry().before(new Timestamp(System.currentTimeMillis()))) throw new SessionExpiredException();
		
		MessageGroup messageGroup = new MessageGroup();
		messageGroup.setDirect(direct);
		messageGroup.setName(name);
		
		messageGroup = groupRepo.save(messageGroup);
		
		for(Long memberId : memberIds) {
			MessageGroupMember mgm = new MessageGroupMember(memberId, messageGroup);
			groupMemberRepo.save(mgm);
		}
		
		firestore.collection("message-group").document("" + messageGroup.getMessageGroupId()).create(toFirestore(messageGroup, memberIds, session.getUserId()));
		
		return getMessageGroup(messageGroup.getMessageGroupId());
	}
	
	@GetMapping("/message/{messageGroupId}")
	@ResponseBody
	public MessageGroupResponse getMessageGroup(@PathVariable long messageGroupId) throws ResourceNotFoundException {
		Optional<MessageGroup> optionalGroup = groupRepo.findById(messageGroupId);
		
		if(!optionalGroup.isPresent()) {
			throw new ResourceNotFoundException();
		}
		
		return format(optionalGroup.get());
	}
	
	private MessageGroupResponse format(MessageGroup messageGroup) {
		MessageGroupResponse response = new MessageGroupResponse(messageGroup);
		
		List<MessageGroupMember> members = groupMemberRepo.findByMessageGroupMessageGroupId(messageGroup.getMessageGroupId());
		List<UserResponse> memberResponse = new ArrayList<UserResponse>();
		
		for(MessageGroupMember member : members) {
			Optional<User> optionalUser = userRepo.findById(member.getUserId());
			
			if(!optionalUser.isPresent()) continue; // skips this repetition
			
			memberResponse.add(new UserResponse(optionalUser.get()));
		}
		
		response.setMembers(memberResponse);
		
		return response;
	}
	
	@GetMapping("/user/message/{sessionToken}")
	@ResponseBody
	public Page<MessageGroupResponse, Long> getUserMessageGroups(@PathVariable String sessionToken, @RequestParam(name = "after", required = false) Long after) throws SessionNotFoundException, SessionExpiredException {
		List<MessageGroupMember> groupMember;
		
		Optional<Session> optionalSession = sessionRepo.findByToken(sessionToken);
		
		if(!optionalSession.isPresent()) throw new SessionNotFoundException();
		
		Session session = optionalSession.get();
		
		if(session.getExpiry().before(new Timestamp(System.currentTimeMillis()))) throw new SessionExpiredException();
		
		long userId = session.getUserId();
		
		groupMember = (after == null) ? groupMemberRepo.findByUserIdOrderByMessageGroupMemberIdDesc(userId, PageRequest.of(0, PAGE_SIZE)) : groupMemberRepo.findByUserIdAndMessageGroupMemberIdLessThanOrderByMessageGroupMemberIdDesc(userId, after, PageRequest.of(0, PAGE_SIZE));
		
		List<MessageGroupResponse> page = new ArrayList<>();
		groupMember.iterator().forEachRemaining(p -> page.add(format(p.getMessageGroup())));
		
		
		Long first = null;
		Long next = null;
		if(page.size() > 0) {
			first = groupMember.get(0).getMessageGroupMemberId();
			next = groupMember.get(groupMember.size() - 1).getMessageGroupMemberId();
		}
		
		return new Page<MessageGroupResponse, Long>(first, next, page.size(), PAGE_SIZE, page);
	}
	
	@PostMapping("/message/{messageGroupId}/member/{memberId}")
	@ResponseBody
	public MessageGroupResponse addMember(@PathVariable long messageGroupId, @PathVariable long memberId, @RequestParam String sessionToken) 
			throws SessionNotFoundException, SessionExpiredException, ResourceNotFoundException, ForbiddenException, InterruptedException, ExecutionException {
		Optional<Session> optionalSession = sessionRepo.findByToken(sessionToken);
		
		if(!optionalSession.isPresent()) throw new SessionNotFoundException();
		
		Session session = optionalSession.get();
		
		if(session.getExpiry().before(new Timestamp(System.currentTimeMillis()))) throw new SessionExpiredException();
		
		Optional<MessageGroup> optionalGroup = groupRepo.findById(messageGroupId);
		
		if(!optionalGroup.isPresent()) throw new ResourceNotFoundException();
		
		MessageGroup group = optionalGroup.get();
		
		if(group.isDirect()) throw new ForbiddenException();
		
		Optional<MessageGroupMember> optionalMember = groupMemberRepo.findByUserIdAndMessageGroupMessageGroupId(memberId, messageGroupId);
		
		if(optionalMember.isPresent()) {
			return format(group);
		}
		
		MessageGroupMember member = new MessageGroupMember(memberId, group);
		member = groupMemberRepo.save(member);
		
		DocumentSnapshot snap = firestore.collection("message-group").document(messageGroupId + "").get().get();
		((Map<String, Object>) snap.getData().get("roles")).put(memberId+"", "member");
		
		return getMessageGroup(messageGroupId);
	}
	
	@DeleteMapping("/message/{messageGroupId}/member/{memberId}")
	@ResponseBody
	public MessageGroupResponse removeMember(@PathVariable long messageGroupId, @PathVariable long memberId, @RequestParam String sessionToken) 
			throws ResourceNotFoundException, SessionExpiredException, SessionNotFoundException, InterruptedException, ExecutionException {
		Optional<Session> optionalSession = sessionRepo.findByToken(sessionToken);
		
		if(!optionalSession.isPresent()) throw new SessionNotFoundException();
		
		Session session = optionalSession.get();
		
		if(session.getExpiry().before(new Timestamp(System.currentTimeMillis()))) throw new SessionExpiredException();
		
		
		Optional<MessageGroupMember> optionalGroupMember = groupMemberRepo.findByUserIdAndMessageGroupMessageGroupId(memberId, messageGroupId);
		
		if(!optionalGroupMember.isPresent()) throw new ResourceNotFoundException();
		
		groupMemberRepo.deleteById(optionalGroupMember.get().getMessageGroupMemberId());
		
		DocumentSnapshot snap = firestore.collection("message-group").document("" + messageGroupId).get().get();
		((Map<String, Object>) snap.getData().get("roles")).remove(memberId + "");
		
		return getMessageGroup(messageGroupId);
	}
	
	
	
	public Map<String, Object> toFirestore(MessageGroup mg, List<Long> members, Long ownerId) {
		Map<String, Object> doc = map.convertValue(mg, Map.class);
		members.remove(ownerId);
		
		Map<String, Object> roles = new HashMap<>();
		members.forEach(m -> roles.put(m+"", "member"));
		if(ownerId != null) {
			roles.put(ownerId + "", "owner");
		}
		
		doc.put("roles", roles);
		return doc;
	}
}
