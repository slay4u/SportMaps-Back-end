package spring.app.modules.chatrooms.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class ChatMessage {
    private String message;
    private String sender;
    private Status status;

    public enum Status {
        MESSAGE, LEAVE, JOIN
    }
}
