package pro.kaleert.nyagram.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pro.kaleert.nyagram.api.methods.send.SendRichMessage;
import pro.kaleert.nyagram.api.methods.gifts.GetUniqueGift;
import pro.kaleert.nyagram.api.objects.gifts.UniqueGift;
import pro.kaleert.nyagram.api.objects.ton.TonNft;
import pro.kaleert.nyagram.api.objects.theme.ChatShareThemeBackdrop;
import pro.kaleert.nyagram.api.objects.message.EphemeralMessageParameters;
import pro.kaleert.nyagram.api.methods.widget.EditSharedWidgetData;
import static org.junit.jupiter.api.Assertions.*;

class BotApi104CompletionTest {
 private final ObjectMapper json=new ObjectMapper();
 @Test void uniqueGiftIsTyped() throws Exception {
  UniqueGift gift=json.readValue("""
    {"gift_id":"g", "base_name":"Gift", "name":"Gift-1", "number":1,
     "model":{"name":"Cat", "rarity_per_mille":50},
     "symbol":{"name":"Star", "rarity_per_mille":100},
     "backdrop":{"name":"Blue", "colors":{"center_color":123}}}
    """, UniqueGift.class);
  assertEquals("Gift-1",gift.name());
  assertEquals(123,gift.backdrop().colors().centerColor());
  assertEquals("getUniqueGift",GetUniqueGift.builder().name("Gift-1").build().getMethod());
 }
 @Test void tonNftCanContainTypedGift() throws Exception {
  TonNft nft=json.readValue("""
    {"address":"EQ", "index":"1", "name":"Cat", "description":"nft",
     "gift":{"name":"Gift-2", "number":2}}
    """,TonNft.class);
  assertEquals("Gift-2",nft.gift().name());
 }
 @Test void themeColorsAreTyped() throws Exception {
  ChatShareThemeBackdrop backdrop=json.readValue("""
    {"id":1,"name":"Blue", "colors":{"edge_color":12,"text_color":20}}
    """,ChatShareThemeBackdrop.class);
  assertEquals(12,backdrop.colors().edgeColor());
 }
 @Test void richMessageAllowsCallbackOnlyEphemeral() throws Exception {
  var params=EphemeralMessageParameters.builder().callbackQueryId("callback").build();
  var method=SendRichMessage.builder().ephemeralMessageParameters(params)
      .richMessage(json.readTree("{\"blocks\":[]}")).build();
  assertDoesNotThrow(method::validate);
  var wire=json.readTree(json.writeValueAsString(method));
  assertFalse(wire.has("chat_id"));
  assertEquals("callback",wire.path("ephemeral_message_parameters").path("callback_query_id").asText());
  assertThrows(Exception.class,()->SendRichMessage.builder()
      .richMessage(json.readTree("{\"blocks\":[]}")).build().validate());
 }
 @Test void sharedWidgetCanClearPublicData() {
  assertDoesNotThrow(()->EditSharedWidgetData.builder().widgetId("w").publicData("").build().validate());
 }
}
