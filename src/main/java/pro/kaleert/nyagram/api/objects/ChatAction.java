package pro.kaleert.nyagram.api.objects;

/**
 * Действия, отображаемые пользователю в строке состояния чата, пока бот выполняет операцию. Значения передаются методу sendChatAction.
 *
* @since 1.1.4
*/
public enum ChatAction {
    TYPING("typing"),
    UPLOAD_PHOTO("upload_photo"),
    RECORD_VIDEO("record_video"),
    UPLOAD_VIDEO("upload_video"),
    RECORD_VOICE("record_voice"),
    UPLOAD_VOICE("upload_voice"),
    UPLOAD_DOCUMENT("upload_document"),
    CHOOSE_STICKER("choose_sticker"),
    FIND_LOCATION("find_location"),
    RECORD_VIDEO_NOTE("record_video_note"),
    UPLOAD_VIDEO_NOTE("upload_video_note");

    private final String value;
    ChatAction(String value) { this.value = value; }
    
    /**
     * Возвращает строковое значение действия в формате, ожидаемом Telegram Bot API.
     *
     * @return значение действия, например {@code typing}.
     */
    public String getValue() { return value; }
}