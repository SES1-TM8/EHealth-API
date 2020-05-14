package xyz.jonmclean.EHealth.repositories;

import org.springframework.data.repository.CrudRepository;

import xyz.jonmclean.EHealth.models.message.MessageGroup;

public interface MessageGroupRepository extends CrudRepository<MessageGroup, Long>{

}
