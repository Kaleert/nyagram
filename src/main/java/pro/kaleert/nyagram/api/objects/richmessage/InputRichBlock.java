package pro.kaleert.nyagram.api.objects.richmessage;
import com.fasterxml.jackson.annotation.*;
/** Telegram Bot API input rich blocks; 'type' is the JSON discriminator. */
/**
 * Базовый тип блока форматированного сообщения Telegram. JSON-поле type определяет конкретный вариант.
 * @since 1.2.4
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="type")
@JsonSubTypes({
    @JsonSubTypes.Type(value=InputRichBlockParagraph.class, name="paragraph"),
    @JsonSubTypes.Type(value=InputRichBlockHeading.class, name="heading"),
    @JsonSubTypes.Type(value=InputRichBlockPreformatted.class, name="pre"),
    @JsonSubTypes.Type(value=InputRichBlockFooter.class, name="footer"),
    @JsonSubTypes.Type(value=InputRichBlockDivider.class, name="divider"),
    @JsonSubTypes.Type(value=InputRichBlockMathematicalExpression.class, name="mathematical_expression"),
    @JsonSubTypes.Type(value=InputRichBlockAnchor.class, name="anchor"),
    @JsonSubTypes.Type(value=InputRichBlockList.class, name="list"),
    @JsonSubTypes.Type(value=InputRichBlockBlockQuotation.class, name="blockquote"),
    @JsonSubTypes.Type(value=InputRichBlockExpandableBlockQuotation.class, name="expandable_blockquote"),
    @JsonSubTypes.Type(value=InputRichBlockPullQuotation.class, name="pullquote"),
    @JsonSubTypes.Type(value=InputRichBlockCollage.class, name="collage"),
    @JsonSubTypes.Type(value=InputRichBlockSlideshow.class, name="slideshow"),
    @JsonSubTypes.Type(value=InputRichBlockTable.class, name="table"),
    @JsonSubTypes.Type(value=InputRichBlockDetails.class, name="details"),
    @JsonSubTypes.Type(value=InputRichBlockMap.class, name="map"),
    @JsonSubTypes.Type(value=InputRichBlockButtons.class, name="buttons"),
    @JsonSubTypes.Type(value=InputRichBlockAnimation.class, name="animation"),
    @JsonSubTypes.Type(value=InputRichBlockAudio.class, name="audio"),
    @JsonSubTypes.Type(value=InputRichBlockDocument.class, name="document"),
    @JsonSubTypes.Type(value=InputRichBlockPhoto.class, name="photo"),
    @JsonSubTypes.Type(value=InputRichBlockVideo.class, name="video"),
    @JsonSubTypes.Type(value=InputRichBlockVoiceNote.class, name="voice_note"),
    @JsonSubTypes.Type(value=InputRichBlockThinking.class, name="thinking")
})
public interface InputRichBlock {}
