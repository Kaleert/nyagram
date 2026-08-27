package pro.kaleert.nyagram.api.objects.managedbots;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import lombok.Builder;

/**
 * Настройки доступа для управляемого (Managed) бота.
 * @since 1.2.2
 */
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record BotAccessSettings(
    @JsonProperty("can_read_messages") Boolean canReadMessages,
    @JsonProperty("can_send_messages") Boolean canSendMessages,
    @JsonProperty("can_restrict_members") Boolean canRestrictMembers,
    @JsonProperty("can_promote_members") Boolean canPromoteMembers,
    @JsonProperty("can_manage_topics") Boolean canManageTopics,
    @JsonProperty("can_pin_messages") Boolean canPinMessages
) implements BotApiObject {}
