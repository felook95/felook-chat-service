package hu.martin.felookchatservice.controller;

import hu.martin.felookchatservice.dto.model.ConversationDto;
import hu.martin.felookchatservice.service.ConversationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/conversation")
public class ConversationController {

    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @PostMapping
    Mono<ConversationDto> createConversation(@RequestBody ConversationDto conversationDto) {
        return conversationService.createConversation(conversationDto);
    }
}
