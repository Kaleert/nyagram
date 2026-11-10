package pro.kaleert.nyagram.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pro.kaleert.nyagram.api.objects.richtext.RichTextValue;
import pro.kaleert.nyagram.api.objects.ReplyParameters;
import pro.kaleert.nyagram.api.objects.message.TextQuote;
import pro.kaleert.nyagram.api.objects.message.EphemeralMessageParameters;
import pro.kaleert.nyagram.api.methods.send.SendLocation;
import pro.kaleert.nyagram.api.methods.send.SendMessage;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BotApi104Stage5Test {
    private final ObjectMapper mapper = new ObjectMapper();

    @Test void richTextStringListAndTonAddressPreserveJson() throws Exception {
        String source = "[\"hello\", {\"type\": \"ton_address\", \"text\": [\"TON \", {\"type\": \"bold\", \"text\": \"address\"}], \"address\": \"EQexample\"}]";
        JsonNode input = mapper.readTree(source);
        RichTextValue result = RichTextValue.from(input);
        assertInstanceOf(RichTextValue.Sequence.class,result);
        RichTextValue.TonAddress ton = (RichTextValue.TonAddress) ((RichTextValue.Sequence) result).elements().get(1);
        assertEquals("EQexample",ton.address());
        assertInstanceOf(RichTextValue.Sequence.class,ton.text());
        assertEquals(input,result.json());
    }

    @Test void quoteRichTextsStayWireCompatible() throws Exception {
        var request=ReplyParameters.builder().quoteRichTexts(List.of(mapper.readTree("\"hello\""),
                mapper.readTree("{\"type\":\"ton_address\",\"text\":\"TON\",\"address\":\"EQtest\"}"))).build();
        var json=mapper.readTree(mapper.writeValueAsString(request));
        assertTrue(json.get("quote_rich_texts").isArray());
        assertInstanceOf(RichTextValue.TonAddress.class,request.quoteRichTextsDecoded().get(1));
        assertFalse(json.has("quoteRichTextsDecoded"));
    }

    @Test void textQuoteCanReadRichTextStrings() throws Exception {
        String json="{\"text\":\"hello\",\"rich_texts\":[\"hello\",[\"world\"]]}";
        TextQuote quote=mapper.readValue(json,TextQuote.class);
        assertInstanceOf(RichTextValue.Sequence.class,quote.richTextsDecoded().get(1));
    }

    @Test void callbackOnlyEphemeralSendLocationAllowsNoChatId() {
        var ep=EphemeralMessageParameters.builder().callbackQueryId("callback123").build();
        assertDoesNotThrow(() -> SendLocation.builder().ephemeralMessageParameters(ep)
                .latitude(51.5).longitude(-0.1).build().validate());
        assertThrows(Exception.class, () -> SendLocation.builder().latitude(51.5).longitude(-0.1).build().validate());
        assertDoesNotThrow(() -> SendMessage.builder().ephemeralMessageParameters(ep).text("hello").build().validate());
        assertThrows(Exception.class, () -> SendLocation.builder().ephemeralMessageParameters(ep).build().validate());
    }
}
