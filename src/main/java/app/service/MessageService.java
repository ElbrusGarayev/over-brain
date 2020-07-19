package app.service;

import app.entity.Message;
import app.entity.User;
import app.repository.MessageRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class MessageService {

    private final MessageRepo messageRepo;

    public List<Message> getAll(User who, User whom){
        return messageRepo.getChat(who, whom, whom, who);
    }

    public void send(Message message){
        messageRepo.save(message);
    }
}
