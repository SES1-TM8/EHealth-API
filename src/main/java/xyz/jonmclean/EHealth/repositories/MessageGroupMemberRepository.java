package xyz.jonmclean.EHealth.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import xyz.jonmclean.EHealth.models.message.MessageGroupMember;

public interface MessageGroupMemberRepository extends CrudRepository<MessageGroupMember, Long>{
	
	public List<MessageGroupMember> findByMessageGroupMessageGroupId(long messageGroupId);
	
	public Optional<MessageGroupMember> findByUserIdAndMessageGroupMessageGroupId(long userId, long messageGroupId);
	
	public List<MessageGroupMember> findByUserIdAndMessageGroupMemberIdLessThanOrderByMessageGroupMemberIdDesc(long userId, long messageGroupMemberId, Pageable page);
	public List<MessageGroupMember> findByUserIdOrderByMessageGroupMemberIdDesc(long userId, Pageable page);

	
}
