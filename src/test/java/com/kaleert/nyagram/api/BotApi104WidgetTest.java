package pro.kaleert.nyagram.api;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pro.kaleert.nyagram.api.methods.widget.*;
import pro.kaleert.nyagram.api.objects.Update;
import pro.kaleert.nyagram.api.objects.widget.Widget;
import static org.junit.jupiter.api.Assertions.*;
class BotApi104WidgetTest {
 private final ObjectMapper json = new ObjectMapper();
 @Test void serializesWidgetRequest() throws Exception {
  SendWidget w = SendWidget.builder().chatId("42").html("<b>Hello</b>").allowQueries(true).isShared(true).build();
  w.validate(); var node=json.readTree(json.writeValueAsString(w));
  assertEquals("sendWidget",w.getMethod());
  assertEquals("42",node.get("chat_id").asText());
  assertTrue(node.get("allow_queries").asBoolean());
  assertTrue(node.get("is_shared").asBoolean());
 }
 @Test void rejectsInvalidWidgetHtml() {
  assertThrows(Exception.class,()->SendWidget.builder().chatId("42").build().validate());
  assertThrows(Exception.class,()->SendWidget.builder().chatId("42").html("x".repeat(32769)).build().validate());
 }
 @Test void parsesWidgetUpdate() throws Exception {
  Update update=json.readValue("{\"update_id\":1,\"widget_query\":{\"id\":\"q1\",\"chat_instance\":\"chat\",\"type\":{\"type\":\"data\"},\"data\":\"hello\"}}",Update.class);
  assertEquals("widget_query",update.getType());
  assertEquals("hello", update.getWidgetQuery().data());
 }
 @Test void parsesWidget() throws Exception {
  Widget w=json.readValue("{\"id\":\"abc\",\"html\":\"<p>Test</p>\"}",Widget.class);
  assertEquals("abc",w.id());
 }
 @Test void endpoints() {
  assertEquals("sendSharedWidget",SendSharedWidget.builder().chatId("42").widgetId("abc").build().getMethod());
  assertEquals("answerWidgetQuery",AnswerWidgetQuery.builder().widgetQueryId("q").build().getMethod());
  assertEquals("editMessageWidgetData",EditMessageWidgetData.builder().chatId("42").messageId(1).build().getMethod());
  assertEquals("editSharedWidgetData",EditSharedWidgetData.builder().widgetId("abc").publicData("").build().getMethod());
 }
}
