package pro.kaleert.nyagram.core;

import pro.kaleert.nyagram.api.objects.Update;
import pro.kaleert.nyagram.callback.CallbackDispatcher;
import pro.kaleert.nyagram.client.NyagramClient;
import pro.kaleert.nyagram.core.concurrency.BotConcurrencyStrategy;
import pro.kaleert.nyagram.dispatcher.CommandDispatcher;
import pro.kaleert.nyagram.dispatcher.EventDispatcher;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UpdateProcessorTest {

    @Test
    void routesPlainTextMessagesThroughCommandDispatcher() {
        CommandDispatcher commandDispatcher = mock(CommandDispatcher.class);
        EventDispatcher eventDispatcher = mock(EventDispatcher.class);
        CallbackDispatcher callbackDispatcher = mock(CallbackDispatcher.class);
        BotConcurrencyStrategy concurrencyStrategy = mock(BotConcurrencyStrategy.class);
        NyagramClient nyagramClient = mock(NyagramClient.class);

        UpdateProcessor updateProcessor = new UpdateProcessor(
                commandDispatcher,
                eventDispatcher,
                callbackDispatcher,
                concurrencyStrategy,
                nyagramClient,
                Optional.empty(),
                List.of(),
                Optional.empty(),
                Optional.empty()
        );

        doAnswer(invocation -> {
            Runnable task = invocation.getArgument(1);
            task.run();
            return null;
        }).when(concurrencyStrategy).execute(org.mockito.ArgumentMatchers.any(Update.class), org.mockito.ArgumentMatchers.any(Runnable.class));

        Update update = mock(Update.class);
        when(update.hasMessage()).thenReturn(true);
        when(update.isCommand()).thenReturn(false);
        when(commandDispatcher.dispatch(update)).thenReturn(CompletableFuture.completedFuture(CommandResult.noResponse()));

        updateProcessor.processAsync(update);

        verify(commandDispatcher).dispatch(update);
        verify(eventDispatcher, never()).dispatch(update);
    }
}
