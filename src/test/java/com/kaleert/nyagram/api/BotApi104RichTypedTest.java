package pro.kaleert.nyagram.api;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pro.kaleert.nyagram.api.objects.richmessage.*;
import pro.kaleert.nyagram.api.methods.send.SendRichMessage;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BotApi104RichTypedTest {
 private final ObjectMapper mapper = new ObjectMapper();
 @Test void htmlMessageKeepsWireNames() throws Exception {
   var msg=InputRichMessage.html("<p>Hi</p>");
   msg.validate();
   var req=SendRichMessage.builder().chatId("42").build().withTypedRichMessage(msg);
   req.validate();
   var tree=mapper.readTree(mapper.writeValueAsString(req));
   assertEquals("<p>Hi</p>",tree.path("rich_message").path("html").asText());
   assertFalse(tree.path("rich_message").has("blocks"));
   assertEquals("<p>Hi</p>",req.typedRichMessage().html());
 }
 @Test void blocksSerializeAndDeserializePolymorphically() throws Exception {
   var text=mapper.readTree("\"Hello\"");
   var blocks=InputRichMessage.blocks(List.of(new InputRichBlockParagraph(text),
         new InputRichBlockDivider(),new InputRichBlockHeading(text,2)));
   var tree=mapper.readTree(mapper.writeValueAsString(blocks));
   assertEquals("paragraph",tree.path("blocks").get(0).path("type").asText());
   assertEquals("divider",tree.path("blocks").get(1).path("type").asText());
   var back=mapper.treeToValue(tree,InputRichMessage.class);
   assertInstanceOf(InputRichBlockHeading.class,back.blocks().get(2));
   assertEquals(2,((InputRichBlockHeading)back.blocks().get(2)).size());
 }
 @Test void nestedListAndQuotePreserveTypes() throws Exception {
   var node=mapper.readTree("\"Nested\"");
   var item=new InputRichBlockListItem(List.of(new InputRichBlockParagraph(node)),true,true,null,null);
   var msg=InputRichMessage.blocks(List.of(new InputRichBlockList(List.of(item)),
                   new InputRichBlockBlockQuotation(List.of(new InputRichBlockParagraph(node)),null)));
   var restored=mapper.readValue(mapper.writeValueAsString(msg),InputRichMessage.class);
   assertInstanceOf(InputRichBlockList.class,restored.blocks().get(0));
   assertInstanceOf(InputRichBlockBlockQuotation.class,restored.blocks().get(1));
 }
 @Test void onlyOneContentModeIsAllowed() {
   assertThrows(IllegalArgumentException.class,()->new InputRichMessage(null,"html","markdown",null,null,null).validate());
   assertThrows(IllegalArgumentException.class,()->new InputRichMessage(null,null,null,null,null,null).validate());
   assertDoesNotThrow(()->InputRichMessage.blocks(List.of()).validate());
 }
 @Test void mediaIdRejectsInvalidCharacters() throws Exception {
   var node=mapper.readTree("{\"type\":\"photo\",\"media\":\"id\"}");
   assertDoesNotThrow(()->new InputRichMessageMedia("photo_1",node).validate());
   assertThrows(IllegalArgumentException.class,()->new InputRichMessageMedia("a!",node).validate());
 }
}
