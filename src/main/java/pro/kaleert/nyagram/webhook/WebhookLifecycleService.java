package pro.kaleert.nyagram.webhook;

import pro.kaleert.nyagram.api.methods.updates.DeleteWebhook;
import pro.kaleert.nyagram.api.methods.updates.SetWebhook;
import pro.kaleert.nyagram.client.NyagramClient;
import pro.kaleert.nyagram.core.spi.NyagramBotConfig;
import pro.kaleert.nyagram.core.spi.NyagramBotProvider;
import pro.kaleert.nyagram.context.BotTokenContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.SmartLifecycle;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
public class WebhookLifecycleService implements SmartLifecycle {

    private final NyagramClient client;
    private final NyagramBotConfig config;
    private final ObjectProvider<NyagramBotProvider> botProvider;
    private boolean running = false;

    @Override
    public void start() {
        String url = config.getWebhookUrl();
        String path = config.getWebhookPath();
        
        if (url == null || url.isBlank()) {
            throw new IllegalStateException("Webhook URL is not configured but Bot Mode is WEBHOOK");
        }

        String fullUrl = url.endsWith("/") ? url + path.substring(1) : url + path;
        
        NyagramBotProvider provider = botProvider.getIfAvailable();

        if (provider != null) {
            log.info("🌐 Multi-Bot mode detected. Setting up webhooks for network...");
            
            setupSingleWebhook(config.getBotToken(), fullUrl + "/" + config.getBotToken());
            
            Collection<String> tokens = provider.getBotTokens();
            for (String childToken : tokens) {
                setupSingleWebhook(childToken, fullUrl + "/" + childToken);
            }
            log.info("✅ Multi-Bot webhooks successfully registered! (Total: {})", tokens.size() + 1);
        } else {
            log.info("Setting webhook to: {}", fullUrl);
            setupSingleWebhook(config.getBotToken(), fullUrl);
            log.info("✅ Webhook set successfully!");
        }
        
        running = true;
    }

    private void setupSingleWebhook(String token, String targetUrl) {
        try {
            BotTokenContext.setToken(token);
            
            SetWebhook setWebhook = SetWebhook.builder()
                    .url(targetUrl)
                    .secretToken(config.getWebhookSecretToken())
                    .dropPendingUpdates(false)
                    .allowedUpdates(java.util.List.of("message", "edited_message", "callback_query", "inline_query", "pre_checkout_query", "shipping_query", "chat_member", "my_chat_member"))
                    .build();
            
            Boolean result = client.execute(setWebhook);
            if (!Boolean.TRUE.equals(result)) {
                log.error("Failed to set webhook for token ending in ...{}", token.substring(token.length() - 5));
            }
        } catch (Exception e) {
            log.error("Error setting webhook for token", e);
        } finally {
            BotTokenContext.clear();
        }
    }

    @Override
    public void stop() {
        log.info("Deleting webhook...");
        try {
            client.execute(DeleteWebhook.builder().dropPendingUpdates(false).build());
            log.info("Webhook deleted.");
        } catch (Exception e) {
            log.warn("Failed to delete webhook on shutdown: {}", e.getMessage());
        }
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}