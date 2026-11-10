package pro.kaleert.nyagram.api;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pro.kaleert.nyagram.api.objects.Update;
import pro.kaleert.nyagram.api.objects.widget.*;
import pro.kaleert.nyagram.api.objects.chat.ChatFullInfo;
import pro.kaleert.nyagram.api.objects.ReplyParameters;
import pro.kaleert.nyagram.api.objects.inlinequery.inputmessagecontent.*;
import pro.kaleert.nyagram.api.methods.gifts.*;
import pro.kaleert.nyagram.api.methods.theme.*;
import static org.junit.jupiter.api.Assertions.*;
class BotApi104AdditionalTest {
 private final ObjectMapper mapper = new ObjectMapper();
 @Test void parsesPolymorphicWidgetQueryTypes() throws Exception {
  var data = mapper.readValue("{\"type\":\"data\",\"wallet_card\":{\"address\":\"abc\",\"amount\":\"123\",\"stickers\":[]}}",WidgetQueryType.class);
  assertInstanceOf(WidgetQueryTypeData.class,data);
  assertEquals("abc",((WidgetQueryTypeData)data).walletCard().address());
  assertInstanceOf(WidgetQueryTypeSignData.class,mapper.readValue("{\"type\":\"sign_data\",\"wallet\":{\"address\":\"a\",\"amount\":\"0\"},\"sign_data\":\"{}\"}",WidgetQueryType.class));
  assertInstanceOf(WidgetQueryTypeSentTransaction.class,mapper.readValue("{\"type\":\"sent_transaction\",\"wallet\":{\"address\":\"a\",\"amount\":\"0\"},\"msg_hash\":\"hash\"}",WidgetQueryType.class));
 }
 @Test void parsesWidgetQueryInUpdate() throws Exception {
  var update=mapper.readValue("{\"update_id\":5,\"widget_query\":{\"id\":\"q\",\"type\":{\"type\":\"data\"},\"data\":\"hi\"}}",Update.class);
  assertEquals("widget_query", update.getType());
  assertInstanceOf(WidgetQueryTypeData.class, update.getWidgetQuery().type());
 }
 @Test void chatThemeAndTonWalletCardParse() throws Exception {
  var info=mapper.readValue("{\"id\":1,\"type\":\"private\",\"ton_wallet_card\":{\"address\":\"EQfoo\",\"amount\":\"9\",\"stickers\":[]},\"share_theme\":{\"type\":\"emoji\",\"emojis\":[{\"type\":\"emoji\",\"emoji\":\"🙂\"}]}}",ChatFullInfo.class);
  assertEquals("EQfoo", info.tonWalletCard().address());
  assertEquals("emoji",info.shareTheme().type());
 }
 @Test void quoteRichTextsAreSerialized() throws Exception {
  var args=ReplyParameters.builder().quoteRichTexts(java.util.List.of(mapper.readTree("{\"type\":\"bold\",\"text\":\"hello\"}"))).build();
  assertTrue(mapper.writeValueAsString(args).contains("quote_rich_texts"));
 }
 @Test void inlineWidgetContentSerializes() throws Exception {
  InputMessageContent content=new InputWidgetMessageContent("<b>hi</b>",null,null,null,null,null,null,null,null,true,null);
  content.validate();
  assertTrue(mapper.writeValueAsString(content).contains("allow_queries"));
 }
 @Test void newEndpoints() {
  assertEquals("getUniqueGift",GetUniqueGift.builder().name("SomeGift").build().getMethod());
  assertEquals("deleteBusinessAccountGift",DeleteBusinessAccountGift.builder().businessConnectionId("c").ownedGiftId("g").build().getMethod());
  assertEquals("getChatShareThemeBackdrops",new GetChatShareThemeBackdrops().getMethod());
 }
}
