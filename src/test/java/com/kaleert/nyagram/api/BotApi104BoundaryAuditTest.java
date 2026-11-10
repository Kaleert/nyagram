package pro.kaleert.nyagram.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pro.kaleert.nyagram.api.methods.widget.*;
import pro.kaleert.nyagram.api.objects.widget.*;
import pro.kaleert.nyagram.api.objects.inlinequery.inputmessagecontent.*;
import pro.kaleert.nyagram.api.objects.ton.RichTextTonAddress;
import static org.junit.jupiter.api.Assertions.*;

class BotApi104BoundaryAuditTest {
 private final ObjectMapper mapper = new ObjectMapper();

 @Test void htmlRequiresNonemptyUtf8AndMaxZlibSize() {
  assertThrows(Exception.class, () -> WidgetLimits.html("", "test"));
  assertDoesNotThrow(() -> WidgetLimits.html("<p>Hello world</p>", "test"));
  // Character count isn't byte count: 1000 emoji occupy 4000 UTF-8 bytes.
  assertDoesNotThrow(() -> WidgetLimits.html("😀".repeat(1000), "test"));
  assertThrows(Exception.class, () -> WidgetLimits.html("😀".repeat(8200), "test"));
 }
 @Test void dataLimitsAreUtf8Bytes() {
  assertDoesNotThrow(() -> WidgetLimits.utf8("🦊".repeat(256),0,1024,"public_data","test"));
  assertThrows(Exception.class, () -> WidgetLimits.utf8("🦊".repeat(257),0,1024,"public_data","test"));
  assertThrows(Exception.class, () -> SendWidget.builder().chatId("1").html("x").chatData("🦊".repeat(257)).build().validate());
  assertThrows(Exception.class, () -> SendSharedWidget.builder().chatId("1").widgetId("w").chatData("🦊".repeat(257)).build().validate());
 }
 @Test void answerAndEditEnforceBounds() {
  assertDoesNotThrow(() -> AnswerWidgetQuery.builder().widgetQueryId("q").data("x".repeat(1024)).build().validate());
  assertThrows(Exception.class, () -> AnswerWidgetQuery.builder().widgetQueryId("q").data("x".repeat(1025)).build().validate());
  assertThrows(Exception.class, () -> EditMessageWidgetData.builder().chatId("1").messageId(1).publicData("🦊".repeat(257)).build().validate());
  assertDoesNotThrow(() -> EditSharedWidgetData.builder().widgetId("w").publicData("").cacheTime(5).build().validate());
  assertThrows(Exception.class, () -> EditSharedWidgetData.builder().widgetId("w").publicData("").cacheTime(4).build().validate());
  assertThrows(Exception.class, () -> EditSharedWidgetData.builder().widgetId("w").publicData("").cacheTime(3601).build().validate());
 }
 @Test void inlineWidgetValidation() {
  var w = new InputWidgetMessageContent("<b>hi</b>", null,null,null,"x",null,null,null,null,true,null);
  assertDoesNotThrow(w::validate);
  assertThrows(Exception.class, () -> new InputSharedWidgetMessageContent("id","x".repeat(1025),null,null,null,null).validate());
 }
 @Test void polymorphicTypeRoundTripAndTonRichTextDto() throws Exception {
  var data=mapper.readValue("{\"type\":\"data\",\"wallet_card\":{\"address\":\"EQ\",\"amount\":\"0\",\"stickers\":[]}}",WidgetQueryType.class);
  assertInstanceOf(WidgetQueryTypeData.class,data);
  assertEquals("data",mapper.readTree(mapper.writeValueAsString(data)).get("type").asText());
  var ton=mapper.readValue("{\"type\":\"ton_address\",\"text\":{\"type\":\"plain\",\"text\":\"Hello\"},\"address\":\"EQabc\"}",RichTextTonAddress.class);
  assertEquals("EQabc",ton.address());
 }
}
