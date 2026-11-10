package pro.kaleert.nyagram.api.objects.widget;
import com.fasterxml.jackson.annotation.*;
import pro.kaleert.nyagram.api.meta.BotApiObject;
/**
 * Тип запроса к виджету Telegram.
 * @since 1.2.4
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = WidgetQueryTypeData.class, name = "data"),
    @JsonSubTypes.Type(value = WidgetQueryTypeSignData.class, name = "sign_data"),
    @JsonSubTypes.Type(value = WidgetQueryTypeSentTransaction.class, name = "sent_transaction")
})
public sealed interface WidgetQueryType extends BotApiObject permits WidgetQueryTypeData, WidgetQueryTypeSignData, WidgetQueryTypeSentTransaction {}
