package pro.kaleert.nyagram.api.objects.richmessage;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
/**
 * Связанное медиа форматированного сообщения; вложенные данные сохраняются в JSON.
 * @param id Идентификатор объекта.
 * @param media Вложенные медиаобъекты.
 * @since 1.2.4
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record InputRichMessageMedia(
  @JsonProperty("id") String id,
  @JsonProperty("media") JsonNode media
) {
  /**
   * Проверяет корректность вложенного медиа богатого сообщения.
   */
  public void validate() {
    if(id==null || !id.matches("[A-Za-z0-9_-]{1,64}"))
       throw new IllegalArgumentException("Invalid rich media id");
    if(media==null || !media.isObject())
       throw new IllegalArgumentException("Rich media requires an object");
  }
}
