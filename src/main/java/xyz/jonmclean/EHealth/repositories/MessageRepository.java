package xyz.jonmclean.EHealth.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import xyz.jonmclean.EHealth.models.Message;

public interface MessageRepository extends CrudRepository<Message, Long>{
	public List<Message> findByMessageGroupIdAndMessageIdLessThanOrderByMessageIdDesc(long messageGroupId, long messageId, Pageable page);
	public List<Message> findByMessageGroupIdOrderByMessageIdDesc(long messageGroupId, long messageId, Pageable page);

}
