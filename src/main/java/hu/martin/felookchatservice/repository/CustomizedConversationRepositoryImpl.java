package hu.martin.felookchatservice.repository;

import hu.martin.felookchatservice.model.Conversation;
import hu.martin.felookchatservice.model.Message;
import hu.martin.felookchatservice.model.User;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Repository
public class CustomizedConversationRepositoryImpl implements CustomizedConversationRepository {

    private final DatabaseClient databaseClient;

    public CustomizedConversationRepositoryImpl(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public Mono<Conversation> getConversation(Long id) {
        return databaseClient
                .execute("select * from conversation where id=:id")
                .bind("id", id)
                .as(Conversation.class)
                .fetch()
                .first()
                .flatMap(conversation -> databaseClient
                        //.execute("select * from user join sw_conversation_user on user.id=sw_conversation_user.user_id where sw_conversation_user.conversation_id=:conversationId")
                        .execute("""
                                select * from user join sw_conversation_user
                                on user.id=sw_conversation_user.user_id 
                                where sw_conversation_user.conversation_id=:conversationId
                                """)
                        .bind("conversationId", conversation.getId())
                        .as(User.class)
                        .fetch()
                        .all()
                        .collect(Collectors.toSet())
                        .map(conversation::setUsers));
    }

    @Override
    public Flux<Message> findMessagesByConversationId(Long conversationId) {
        return databaseClient
                .execute("select * from message where conversation_id=:conversationId")
                .bind("conversationId", conversationId)
                .as(Message.class)
                .fetch()
                .all()
                .flatMap(message -> databaseClient
                        .execute("select * from user where id=:userId")
                        .bind("userId", message.getUserId())
                        .as(User.class)
                        .fetch()
                        .first()
                        .map(message::setUser))
                .flatMap(message -> databaseClient
                        .execute("select * from conversation where id=:conversationId")
                        .bind("conversationId", message.getConversationId())
                        .as(Conversation.class)
                        .fetch()
                        .first()
                        .map(message::setConversation));
    }
}
