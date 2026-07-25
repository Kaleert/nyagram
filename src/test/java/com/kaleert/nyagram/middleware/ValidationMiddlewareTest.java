package pro.kaleert.nyagram.middleware;

import pro.kaleert.nyagram.api.objects.Update;
import pro.kaleert.nyagram.api.objects.message.Message;
import pro.kaleert.nyagram.client.NyagramClient;
import pro.kaleert.nyagram.command.CommandContext;
import pro.kaleert.nyagram.meta.CommandMeta;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ValidationMiddlewareTest {

    @Test
    void skipsValidationForFallbackCommand() {
        ValidationMiddleware middleware = new ValidationMiddleware();

        Message message = mock(Message.class);
        when(message.getTextOrCaption()).thenReturn("/admin");

        Update update = mock(Update.class);
        when(update.getMessage()).thenReturn(message);

        CommandContext context = new CommandContext(update, mock(NyagramClient.class));
        CommandMeta meta = CommandMeta.builder()
                .fullCommandPath("fallback")
                .methodParameters(List.of())
                .usageSyntax("fallback")
                .build();

        Optional<?> result = middleware.process(context, meta);

        assertTrue(result.isEmpty());
    }
}
