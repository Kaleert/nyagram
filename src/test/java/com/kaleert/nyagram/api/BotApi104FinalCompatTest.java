package pro.kaleert.nyagram.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pro.kaleert.nyagram.api.objects.chat.ChatFullInfo;
import pro.kaleert.nyagram.api.objects.theme.*;
import pro.kaleert.nyagram.api.objects.message.EphemeralMessageParameters;
import pro.kaleert.nyagram.api.methods.send.SendMessage;
import static org.junit.jupiter.api.Assertions.*;

class BotApi104FinalCompatTest {
    private final ObjectMapper json = new ObjectMapper();

    @Test void themeVariantsDeserializeByDiscriminator() throws Exception {
        ChatShareTheme emoji = json.readValue("{\"type\":\"emoji\",\"emojis\":[{\"type\":\"emoji\",\"emoji\":\"🚀\"},{\"type\":\"custom_emoji\",\"custom_emoji_id\":\"123\"}]}", ChatShareTheme.class);
        assertInstanceOf(ChatShareThemeEmoji.class, emoji);
        assertInstanceOf(EmojiTypeEmoji.class, ((ChatShareThemeEmoji) emoji).emojis().get(0));
        assertInstanceOf(EmojiTypeCustomEmoji.class, ((ChatShareThemeEmoji) emoji).emojis().get(1));
        assertEquals("emoji", emoji.type());
        ChatShareTheme gift = json.readValue("{\"type\":\"unique_gift\",\"gift\":{\"name\":\"SomeGift-1\"}}", ChatShareTheme.class);
        assertInstanceOf(ChatShareThemeUniqueGift.class, gift);
        assertEquals("unique_gift", json.readTree(json.writeValueAsString(gift)).get("type").asText());
    }

    @Test void chatWithThemeDeserializes() throws Exception {
        ChatFullInfo info = json.readValue("{\"id\":1,\"type\":\"private\",\"share_theme\":{\"type\":\"emoji\",\"emojis\":[]}}", ChatFullInfo.class);
        assertInstanceOf(ChatShareThemeEmoji.class, info.shareTheme());
    }

    @Test void callbackOnlyEphemeralReplyNeedsNoChatId() throws Exception {
        var p = EphemeralMessageParameters.builder().callbackQueryId("q").build();
        var req = SendMessage.builder().text("hello").ephemeralMessageParameters(p).build();
        assertDoesNotThrow(req::validate);
        var body = json.readTree(json.writeValueAsString(req));
        assertFalse(body.hasNonNull("chat_id"));
        assertEquals("q", body.get("ephemeral_message_parameters").get("callback_query_id").asText());
        assertThrows(Exception.class, () -> SendMessage.builder().text("hello").build().validate());
    }
}
