package hu.martin.felookchatservice.repository;

import hu.martin.felookchatservice.model.Conversation;
import hu.martin.felookchatservice.model.Message;
import hu.martin.felookchatservice.model.User;
import hu.martin.felookchatservice.model.helper.SwitchConversationUser;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static org.springframework.data.relational.core.query.Criteria.where;

@Repository
@Log4j2
public class CustomizedConversationRepositoryImpl implements CustomizedConversationRepository {

    private final DatabaseClient databaseClient;

    public CustomizedConversationRepositoryImpl(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public Mono<Conversation> saveConversation(Conversation conversation) {
        return databaseClient
                .insert()
                .into(Conversation.class)
                .using(conversation)
                .map((row, rowMetadata) -> {
                    Integer id = row.get("id", Integer.class);
                    Assert.notNull(id, "Id must not be null!");
                    return (Long.valueOf(id));
                })
                .first()
                .flatMap(conversationId -> {
                    conversation.setId(conversationId);

                    return Flux.fromIterable(conversation.getUsers())
                            .flatMap(user -> addUserToConversation(conversationId, user.getId()))
                            .collectList()
                            .map(switchConversationUsers -> conversation);
                });
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
                .flatMap(message ->
                        getConversation(conversationId)
                                .map(message::setConversation));
    }

    @Override
    public Flux<User> findUsersForConversation(Long conversationId) {
        return databaseClient
                .execute("""
                        select * from user join sw_conversation_user 
                        on user.id=sw_conversation_user.user_id
                         where conversation_id=:conversationId
                        """)
                .bind("conversationId", conversationId)
                .as(User.class)
                .fetch()
                .all();
    }

    @Override
    public Mono<SwitchConversationUser> addUserToConversation(Long conversationId, Long userId) {
        SwitchConversationUser switchConversationUser =
                new SwitchConversationUser()
                        .setConversationId(conversationId)
                        .setUserId(userId);

        return databaseClient
                .select()
                .from(SwitchConversationUser.class)
                .matching(
                        where("conversation_id").is(conversationId)
                                .and("user_id").is(userId))
                .as(SwitchConversationUser.class)
                .first()
                .map(switchConversationUser1 -> switchConversationUser1).switchIfEmpty(Mono.defer(() -> databaseClient
                        .insert()
                        .into(SwitchConversationUser.class)
                        .using(switchConversationUser)
                        .map((row, rowMetadata) -> {
                            Integer id = row.get("id", Integer.class);
                            Assert.notNull(id, "Id must not be null!");
                            return switchConversationUser.setId(Long.valueOf(id));
                        })
                        .first()));

    }
}
