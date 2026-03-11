package com.kaleert.nyagram.api.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaleert.nyagram.api.meta.BotApiObject;
import lombok.Builder;

/**
 * Представляет права администратора в чате.
 *
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
    @JsonProperty("can_manage_tags") Boolean canManageTags
) implements BotApiObject {}