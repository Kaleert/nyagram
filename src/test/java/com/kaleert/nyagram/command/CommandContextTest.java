package pro.kaleert.nyagram.command;

import pro.kaleert.nyagram.api.methods.send.SendMessage;
import pro.kaleert.nyagram.api.objects.Update;
import pro.kaleert.nyagram.api.objects.chat.Chat;
import pro.kaleert.nyagram.api.objects.message.Message;
import pro.kaleert.nyagram.client.NyagramClient;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CommandContextTest {

    @Test
    void replyKeepsTopicThreadForTopicMessage() {
        NyagramClient client = mock(NyagramClient.class);
        when(client.executeAsync(any(SendMessage.class))).thenReturn(CompletableFuture.completedFuture(null));

        Message message = Message.builder()
                .messageId(10L)
                .messageThreadId(77)
                .isTopicMessage(true)
                .chat(Chat.builder().id(-100123L).type("supergroup").isForum(true).build())
                .text("/profile")
                .build();

        Update update = new Update().setMessage(message);
        CommandContext context = new CommandContext(update, client);

        context.reply("ok");

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        org.mockito.Mockito.verify(client).executeAsync(captor.capture());
        assertEquals(77, captor.getValue().getMessageThreadId());
    }
}
