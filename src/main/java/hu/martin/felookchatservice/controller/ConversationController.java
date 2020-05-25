package hu.martin.felookchatservice.controller;

import hu.martin.felookchatservice.dto.model.ConversationDto;
import hu.martin.felookchatservice.dto.model.MessageDto;
import hu.martin.felookchatservice.service.ConversationService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
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

    @GetMapping
    Flux<ConversationDto> getAllConversation() {
        return conversationService.getAllConversation();
    }

    @GetMapping("/{id}")
    Mono<ConversationDto> getConversation(@PathVariable Long id) {
        return conversationService.getConversation(id);
    }

    @PostMapping("/message")
    public Mono<MessageDto> addMessage(@RequestBody MessageDto messageDto) {
        return conversationService.addMessage(messageDto);
    }

    @GetMapping("/{id}/message")
    public Flux<MessageDto> getMessagesForConversation(@PathVariable Long id) {
        return conversationService.getAllMessageForConversation(id);
    }

}
