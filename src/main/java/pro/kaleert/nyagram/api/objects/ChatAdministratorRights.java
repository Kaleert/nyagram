package pro.kaleert.nyagram.api.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import lombok.Builder;

/**
 * Представляет права администратора в чате.
 *
 *
 * @param isAnonymous Разрешает администратору оставаться анонимным.
 * @param canManageChat Разрешает управлять чатом, включая просмотр журнала событий и статистики.
 * @param canDeleteMessages Разрешает удалять сообщения других пользователей.
 * @param canManageVideoChats Разрешает управлять видеочатами.
 * @param canRestrictMembers Разрешает ограничивать права и блокировать участников.
 * @param canPromoteMembers Разрешает назначать новых администраторов.
 * @param canChangeInfo Разрешает изменять название, фото и описание чата.
 * @param canInviteUsers Разрешает приглашать пользователей.
 * @param canPostStories Разрешает публиковать истории от имени чата.
 * @param canEditStories Разрешает редактировать истории чата.
 * @param canDeleteStories Разрешает удалять истории чата.
 * @param canPostMessages Разрешает публиковать сообщения от имени канала.
 * @param canEditMessages Разрешает редактировать сообщения в канале.
 * @param canPinMessages Разрешает закреплять сообщения.
 * @param canManageTopics Разрешает создавать, изменять и удалять темы форума.
 * @param canManageDirectMessages Разрешает управлять личными сообщениями канала.
 * @param canManageTags Разрешает управлять тегами участников.
 * @param canSendWelcomeMessages Разрешает отправлять приветственные сообщения.
 * @since 1.1.4
 */
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record ChatAdministratorRights(
    @JsonProperty("is_anonymous") Boolean isAnonymous,
    @JsonProperty("can_manage_chat") Boolean canManageChat,
    @JsonProperty("can_delete_messages") Boolean canDeleteMessages,
    @JsonProperty("can_manage_video_chats") Boolean canManageVideoChats,
    @JsonProperty("can_restrict_members") Boolean canRestrictMembers,
    @JsonProperty("can_promote_members") Boolean canPromoteMembers,
    @JsonProperty("can_change_info") Boolean canChangeInfo,
    @JsonProperty("can_invite_users") Boolean canInviteUsers,
    @JsonProperty("can_post_stories") Boolean canPostStories,
    @JsonProperty("can_edit_stories") Boolean canEditStories,
    @JsonProperty("can_delete_stories") Boolean canDeleteStories,
    @JsonProperty("can_post_messages") Boolean canPostMessages,
    @JsonProperty("can_edit_messages") Boolean canEditMessages,
    @JsonProperty("can_pin_messages") Boolean canPinMessages,
    @JsonProperty("can_manage_topics") Boolean canManageTopics,
    @JsonProperty("can_manage_direct_messages") Boolean canManageDirectMessages,
    @JsonProperty("can_manage_tags") Boolean canManageTags,
    
    /*
     * @since 1.2.2
     */
    @JsonProperty("can_send_welcome_messages") Boolean canSendWelcomeMessages
) implements BotApiObject {}