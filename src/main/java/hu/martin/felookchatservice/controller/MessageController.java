package hu.martin.felookchatservice.controller;

import hu.martin.felookchatservice.dto.model.MessageDto;
import hu.martin.felookchatservice.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/message")
public class MessageController {

    private MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public Flux<MessageDto> getMessages() {
        return messageService.getMessages();
    }

    @GetMapping("/{id}")
    public Mono<MessageDto> getMessage(@PathVariable Long id) {
        return messageService.getMessageById(id);
    }

    @PostMapping
    public Mono<MessageDto> addMessage(@RequestBody MessageDto messageDto) {
        return messageService.createMessage(messageDto);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteMessage(@PathVariable Long id) {
        return messageService.deleteMessage(id);
    }
}
