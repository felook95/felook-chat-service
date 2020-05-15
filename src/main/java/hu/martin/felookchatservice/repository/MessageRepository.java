package hu.martin.felookchatservice.repository;

import hu.martin.felookchatservice.model.Message;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends ReactiveCrudRepository<Message, Long> {
}
