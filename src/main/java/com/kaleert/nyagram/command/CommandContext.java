package com.kaleert.nyagram.command;

import com.kaleert.nyagram.api.methods.send.SendMessage;
import com.kaleert.nyagram.api.methods.updatingmessages.DeleteMessage;
import com.kaleert.nyagram.api.objects.Update;
import com.kaleert.nyagram.api.objects.User;
import com.kaleert.nyagram.api.objects.media.InputMedia;
import com.kaleert.nyagram.api.objects.message.Message;
import com.kaleert.nyagram.api.objects.message.MaybeInaccessibleMessage;
import com.kaleert.nyagram.api.objects.replykeyboard.ReplyKeyboard;
import com.kaleert.nyagram.api.objects.replykeyboard.InlineKeyboardMarkup;
import com.kaleert.nyagram.client.NyagramClient;
import com.kaleert.nyagram.i18n.LocaleService;
import com.kaleert.nyagram.i18n.LocaleResolver;
import com.kaleert.nyagram.i18n.TranslationResolver;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.Locale;

/**
 * Предоставляет доступ к текущему обновлению (Update) и утилитарным методам для взаимодействия.
 * <p>
 * Этот объект создается для каждого входящего обновления и передается в обработчики.
 * Он кэширует разрешенные сущности (User, Chat, Message), чтобы избежать повторного парсинга.
 * </p>
 * 
 * @since 1.0.0
 */
@Slf4j
@Getter
public class CommandContext {
    
    /**
     * Сырой объект Update, полученный от Telegram API.
     */
    private final Update update;
    
    /**
     * HTTP-клиент, используемый для отправки запросов к Telegram API.
     */
    private final NyagramClient client;
    
    private Message cachedMessage;
    private User cachedUser;
    private Long cachedChatId;
    
    private final LocaleService localeService;
    private final LocaleResolver localeResolver;
    
    public CommandContext(Update update, NyagramClient client, LocaleService localeService, LocaleResolver localeResolver) {
        this.update = update;
        this.client = client;
        this.localeService = localeService;
        this.localeResolver = localeResolver;
    }

    public CommandContext(Update update, NyagramClient client) {
        this(update, client, null, null);
    }
    
    /**
     * Возвращает сообщение (Message), связанное с этим обновлением.
     * Обрабатывает стандартные сообщения, посты в каналах, отредактированные сообщения и сообщения из callback-запросов.
     * 
     * @return Optional, содержащий сообщение, или empty, если тип обновления не содержит сообщения.
     */
    public Optional<Message> getMessage() {
        if (cachedMessage != null) {
            return Optional.of(cachedMessage);
        }
        
        if (update.getMessage() != null) {
            cachedMessage = update.getMessage();
        } else if (update.getCallbackQuery() != null) {
            MaybeInaccessibleMessage maybeMessage = update.getCallbackQuery().getMessage();
            if (maybeMessage instanceof Message msg) {
                cachedMessage = msg;
            }
        } else if (update.getEditedMessage() != null) {
            cachedMessage = update.getEditedMessage();
        } else if (update.getChannelPost() != null) {
            cachedMessage = update.getChannelPost();
        } else if (update.getEditedChannelPost() != null) {
            cachedMessage = update.getEditedChannelPost();
        }
        
        return Optional.ofNullable(cachedMessage);
    }
    
    /**
     * Возвращает ID чата, в котором произошло это обновление.
     * <p>
     * Работает для личных чатов, групп, супергрупп и каналов.
     * </p>
     *
     * @return ID чата (Long).
     * @throws IllegalStateException если ID чата невозможно определить из обновления.
     */
    public Long getChatId() {
        if (cachedChatId != null) {
            return cachedChatId;
        }
        
        Long chatId = update.getChatId();
        if (chatId != null) {
            cachedChatId = chatId;
            return chatId;
        }
        
        throw new IllegalStateException("Cannot determine Chat ID from this update");
    }
    
    /**
     * Возвращает пользователя (User), инициировавшего это обновление.
     * <p>
     * Извлекает пользователя из Message, Callback, InlineQuery, ChatMember и других типов обновлений.
     * </p>
     *
     * @return объект User.
     * @throws IllegalStateException если пользователь не может быть определен (маловероятно).
     */
    public User getTelegramUser() {
        if (cachedUser != null) {
            return cachedUser;
        }
        
        Long fromId = update.getFromId();
        
        if (update.getMessage() != null) cachedUser = update.getMessage().getFrom();
        else if (update.getCallbackQuery() != null) cachedUser = update.getCallbackQuery().getFrom();
        else if (update.getEditedMessage() != null) cachedUser = update.getEditedMessage().getFrom();
        else if (update.getMyChatMember() != null) cachedUser = update.getMyChatMember().getFrom();
        else if (update.getChatMember() != null) cachedUser = update.getChatMember().getFrom();
        else if (update.getMessageReaction() != null) cachedUser = update.getMessageReaction().getUser();
        else if (update.getInlineQuery() != null) cachedUser = update.getInlineQuery().getFrom();
        else if (update.getChannelPost() != null) cachedUser = update.getChannelPost().getFrom();
        
        if (cachedUser == null) {
            if (fromId != null) {
                cachedUser = User.builder().id(fromId).firstName("Unknown").isBot(false).build();
            } else {
                throw new IllegalStateException("Cannot determine User from this update type: " + update.getType());
            }
        }
        
        return cachedUser;
    }
    
    /**
     * Вспомогательный метод для получения ID пользователя.
     *
     * @return ID пользователя.
     */
    public Long getUserId() {
        return getTelegramUser().getId();
    }
    
    /**
     * Возвращает идентификатор топика (Message Thread ID), если сообщение отправлено в ветку форума.
     * <p>
     * Если чат не является форумом или сообщение находится в "General" (и он не скрыт), может вернуть null.
     * </p>
     *
     * @return ID топика или null.
     */
    public Integer getTopicId() {
        return getMessage()
                .filter(m -> Boolean.TRUE.equals(m.getIsTopicMessage()))
                .map(Message::getMessageThreadId)
                .orElse(null);
    }
    
   /**
     * Получает текстовое содержимое сообщения (или подпись, если это медиа).
     *
     * @return текст сообщения или пустая строка.
     */
    public String getText() {
        return getMessage()
                .map(Message::getTextOrCaption)
                .orElse("");
    }
    
    /**
     * Проверяет, является ли чат личным (Private).
     *
     * @return true, если это личный чат с пользователем.
     */
    public boolean isPrivateChat() {
        return getMessage()
                .map(Message::isUserChat)
                .orElse(false);
    }
    
    /**
     * Проверяет, является ли чат группой или супергруппой.
     *
     * @return true, если это групповой чат.
     */
    public boolean isGroupChat() {
        return getMessage()
                .map(Message::isGroupChat)
                .orElse(false);
    }
    
    /**
     * Отправляет текстовый ответ в текущий чат.
     * По умолчанию использует режим парсинга HTML.
     * 
     * @param text Текст сообщения для отправки.
     * @return Future, который завершится отправленным сообщением.
     */
    public CompletableFuture<Message> reply(String text) {
        return reply(text, "HTML", null, null);
    }
    
    /**
     * Отправляет текстовый ответ с настраиваемым режимом парсинга.
     * 
     * @param text Текст сообщения.
     * @param parseMode "HTML", "MarkdownV2" или null.
     * @return Future, который завершится отправленным сообщением.
     */
    public CompletableFuture<Message> reply(String text, String parseMode) {
        return reply(text, parseMode, null, null);
    }
    
    /**
     * Полный метод отправки сообщения с клавиатурой и ответом на конкретное сообщение.
     *
     * @param text Текст сообщения.
     * @param parseMode Режим парсинга ("HTML", "MarkdownV2").
     * @param replyToMessageId ID сообщения, на которое нужно ответить (или null).
     * @param replyMarkup Клавиатура (Inline или Reply) или null.
     * @return Future, который завершится отправленным сообщением.
     */
    public CompletableFuture<Message> reply(String text, String parseMode, 
                                            Integer replyToMessageId, 
                                            ReplyKeyboard replyMarkup) {
        var msgBuilder = SendMessage.builder()
                .chatId(getChatId().toString())
                .text(text)
                .parseMode(parseMode)
                .replyToMessageId(replyToMessageId)
                .replyMarkup(replyMarkup);

        getMessage()
                .filter(m -> Boolean.TRUE.equals(m.getIsTopicMessage()))
                .map(Message::getMessageThreadId)
                .ifPresent(msgBuilder::messageThreadId);
        
        return client.executeAsync(msgBuilder.build());
    }
    
    /**
     * Удаляет конкретное сообщение в текущем чате.
     * 
     * @param messageId ID сообщения для удаления. Если null, пытается удалить сообщение команды.
     * @return true, если удаление прошло успешно.
     */
    public boolean deleteMessage(Integer messageId) {
        try {
            Long msgIdLong = messageId != null ? messageId.longValue() : 
                    getMessage().map(Message::getMessageId).orElse(null);
            
            if (msgIdLong == null) return false;

            DeleteMessage deleteMessage = new DeleteMessage(getChatId().toString(), msgIdLong.intValue());
            return Boolean.TRUE.equals(client.execute(deleteMessage));
            
        } catch (Exception e) {
            log.warn("Failed to delete message: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Быстро ставит реакцию (эмодзи) на текущее сообщение пользователя.
     * <p>
     * Идеально подходит для подтверждения получения команды (например, поставить "👍").
     * </p>
     * 
     * @param emoji Эмодзи для реакции.
     *
     * @since 1.1.4
     */
    public void react(String emoji) {
        getMessage().ifPresent(msg -> {
            try {
                client.execute(com.kaleert.nyagram.api.methods.reactions.SetMessageReaction.builder()
                        .chatId(msg.getChat().getId().toString())
                        .messageId(msg.getMessageId().intValue())
                        .reaction(java.util.List.of(
                                com.kaleert.nyagram.api.objects.reactions.ReactionTypeEmoji.builder()
                                        .emoji(emoji)
                                        .build()
                        ))
                        .build());
            } catch (Exception e) {
                log.warn("Failed to set reaction: {}", e.getMessage());
            }
        });
    }

    /**
     * Отправляет всплывающее уведомление (Alert) пользователю.
     * <p>
     * Работает только если текущий контекст вызван нажатием на Inline-кнопку (CallbackQuery).
     * Если это обычное сообщение, метод ничего не сделает.
     * </p>
     *
     * @param text Текст уведомления.
     * @param showAlert Если true, покажет модальное окно с кнопкой "ОК". Если false — toast сверху.
     *
     * @since 1.1.4
     */
    public void answerCallback(String text, boolean showAlert) {
        if (update.getCallbackQuery() != null) {
            try {
                client.execute(com.kaleert.nyagram.api.methods.AnswerCallbackQuery.builder()
                        .callbackQueryId(update.getCallbackQuery().getId())
                        .text(text)
                        .showAlert(showAlert)
                        .build());
            } catch (Exception e) {
                log.warn("Failed to answer callback query: {}", e.getMessage());
            }
        }
    }
    
    /**
     * Автоматически определяет локаль пользователя и переводит текст.
     * 
     * @param key Ключ перевода.
     *
     * @since 1.1.4
     */
    public TranslationResolver translate(String key) {
        if (localeService == null) return new TranslationResolver(key);
        Locale locale = localeResolver != null ? localeResolver.resolve(getUserId()) : Locale.forLanguageTag("ru");
        return localeService.translate(locale, key);
    }

    /**
     * Быстрое редактирование текста (или подписи, если это медиа) текущего сообщения.
     * Автоматически определяет тип сообщения (Text или Caption) и вызывает нужный метод API.
     *
     * @param text Текст сообщения.
     * @param replyMarkup Клавиатура, отправленная вместе с сообщением.
     *
     * @since 1.1.4
     */
    public CompletableFuture<Serializable> editMessage(String text, ReplyKeyboard replyMarkup) {
        if (update.getCallbackQuery() == null || update.getCallbackQuery().getMessage() == null) {
            return CompletableFuture.failedFuture(new IllegalStateException("Not a callback query"));
        }
        Integer msgId = update.getCallbackQuery().getMessage().messageId().intValue();
        Long chatId;
        try { chatId = getChatId(); } catch (Exception e) { return CompletableFuture.failedFuture(e); }
        
        com.kaleert.nyagram.api.objects.message.MaybeInaccessibleMessage maybeMsg = update.getCallbackQuery().getMessage();
        boolean hasMedia = false;
        if (maybeMsg instanceof com.kaleert.nyagram.api.objects.message.Message msg) {
            hasMedia = msg.hasMedia();
        }

        if (hasMedia) {
            return client.executeAsync(com.kaleert.nyagram.api.methods.updatingmessages.EditMessageCaption.builder()
                    .chatId(chatId.toString())
                    .messageId(msgId)
                    .caption(text)
                    .parseMode("HTML")
                    .replyMarkup((InlineKeyboardMarkup) replyMarkup)
                    .build());
        } else {
            return client.executeAsync(com.kaleert.nyagram.api.methods.updatingmessages.EditMessageText.builder()
                    .chatId(chatId.toString())
                    .messageId(msgId)
                    .text(text)
                    .parseMode("HTML")
                    .replyMarkup((InlineKeyboardMarkup) replyMarkup)
                    .build());
        }
    }

    public CompletableFuture<Serializable> editMessage(String text) {
        return editMessage(text, null);
    }
    
    /**
     * Изменяет саму картинку или видео в текущем сообщении с кнопками.
     *
     * @param media Новый медиафайл (например, InputMediaPhoto).
     * @param replyMarkup Клавиатура (опционально).
     *
     * @since 1.1.5
     */
    public CompletableFuture<Serializable> editMedia(InputMedia media, ReplyKeyboard replyMarkup) {
        if (update.getCallbackQuery() == null || update.getCallbackQuery().getMessage() == null) {
            return CompletableFuture.failedFuture(new IllegalStateException("Not a callback query"));
        }
        Integer msgId = update.getCallbackQuery().getMessage().messageId().intValue();
        Long chatId;
        try { chatId = getChatId(); } catch (Exception e) { return CompletableFuture.failedFuture(e); }

        return client.executeAsync(com.kaleert.nyagram.api.methods.updatingmessages.EditMessageMedia.builder()
                .chatId(chatId.toString())
                .messageId(msgId)
                .media(media)
                .replyMarkup((InlineKeyboardMarkup) replyMarkup)
                .build());
    }

    public CompletableFuture<Serializable> editMedia(InputMedia media) {
        return editMedia(media, null);
    }

    /**
     * Тихое удаление сообщения. Перехватывает ошибки (если сообщение уже удалено).
     *
     * @param messageId Айди сообщения, которое будет удалено.
     *
     * @since 1.1.4
     */
    public CompletableFuture<Void> deleteMessageQuietly(Integer messageId) {
        Long chatId;
        try { chatId = getChatId(); } catch (Exception e) { return CompletableFuture.completedFuture(null); }
        
        Integer targetId = messageId != null ? messageId : 
            (update.getCallbackQuery() != null && update.getCallbackQuery().getMessage() != null) 
                ? update.getCallbackQuery().getMessage().messageId().intValue() 
                : getMessage().map(m -> m.getMessageId().intValue()).orElse(null);
                
        if (targetId == null) return CompletableFuture.completedFuture(null);

        return client.executeAsync(com.kaleert.nyagram.api.methods.updatingmessages.DeleteMessage.of(chatId.toString(), targetId))
                .exceptionally(ex -> null)
                .thenApply(ignored -> null);
    }
}
