package com.kaleert.nyagram.middleware;

import com.kaleert.nyagram.api.objects.Update;
import com.kaleert.nyagram.api.objects.message.Message;
import com.kaleert.nyagram.client.NyagramClient;
import com.kaleert.nyagram.command.CommandContext;
import com.kaleert.nyagram.meta.CommandMeta;
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
