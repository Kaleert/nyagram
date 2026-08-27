# Nyagram

![Java 21](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2+-green?style=flat-square&logo=springboot)
![License](https://img.shields.io/badge/License-MIT-blue?style=flat-square)
![Version](https://img.shields.io/badge/version-1.2.2-red?style=flat-square)

[🇺🇲 Читать на английском](Readme.md)

**Nyagram** — это современный, реактивный и типобезопасный фреймворк для создания Telegram-ботов на **Java 21** и **Spring Boot**.

Забудьте о ручном парсинге JSON, бесконечных `switch-case` и аде с машинами состояний. Nyagram берет рутину на себя, позволяя вам сосредоточиться на бизнес-логике.

📚 **[Полная документация и API Reference](https://nyagram.kaleert.pro)**

---

## 🔥 Почему Nyagram?

*   **⚡ Virtual Threads (Project Loom):** Максимальная производительность и конкурентность из коробки.
*   **🧠 Встроенная FSM:** Мощная машина состояний для создания сложных диалогов и воронок.
*   **🎮 Декларативный стиль:** Аннотации `@CommandHandler`, `@Callback`, `@StateAction` делают код чистым.
*   **🛡 Безопасность:** Гибкая система прав (`Permissions`) и уровней доступа (`Levels`).
*   **💎 Типобезопасность:** Никаких `Map<String, Object>`. Строгие типы для всего API (включая Telegram Stars и Business).
*   **🔌 Dual Mode:** Переключение между **Long Polling** и **Webhook** одной строчкой в конфиге.

---

## 📦 Установка

Требуется **Java 21+** и **Spring Boot 3.2+**.

### Gradle
```groovy
dependencies {
    implementation 'io.github.kaleert:nyagram:1.2.2'
}
```

### Maven
```xml
<dependency>
    <groupId>io.github.kaleert</groupId>
    <artifactId>nyagram</artifactId>
    <version>1.2.2</version>
</dependency>
```

---

## ⚡ Быстрый старт

### 1. Конфигурация (`application.yml`)

```yaml
nyagram:
  bot-token: "YOUR_BOT_TOKEN"
  bot-username: "YourBotName"
  mode: POLLING # или WEBHOOK
  
  worker-thread-count: 10 # Использует виртуальные потоки под капотом
```

### 2. Ваш первый бот

```java
@BotCommand(value = "/start", description = "Запустить бота")
public class StartCommand {

    @CommandHandler
    public void handle(CommandContext ctx) {
        ctx.reply("Привет! Я работаю на <b>Nyagram</b> 🚀");
    }
}
```

---

## 🛠 Основные возможности

### 1. Умные аргументы и Флаги (v1.1.0)
Библиотека сама распарсит текст сообщения и разложит всё по полочкам.

```java
// Пример: /ban @spammer 24h -f
@CommandHandler("ban")
public void banUser(
    CommandContext ctx,
    @CommandArgument("target") String username,
    @CommandArgument("duration") Duration duration, // Парсит "24h", "30m"
    @Flag("f") boolean force // true, если есть флаг -f
) {
    if (force) {
        // Баним сразу...
        ctx.reply("Пользователь " + username + " забанен на " + duration);
    }
}
```

### 2. Callbacks с переменными пути
Забудьте о ручном разборе `split(":")`.

```java
// Кнопка: "buy:item:52"
@Callback("buy:item:{id}")
public void onBuy(
    CommandContext ctx,
    @CallbackVar("id") Long itemId
) {
    ctx.reply("Вы выбрали товар #" + itemId);
    // answerCallbackQuery отправляется автоматически!
}
```

### 3. Машина состояний (FSM)
Создавайте сложные диалоги без боли.

```java
@StateAction("WAITING_FOR_NAME")
public void onNameInput(
    CommandContext ctx, 
    UserSession session
) {
    String name = ctx.getText();
    session.putData("name", name);
    
    // Переходим к следующему шагу
    sessionManager.updateState(ctx.getUserId(), "WAITING_FOR_AGE");
    
    ctx.reply("Приятно познакомиться, " + name + "! Сколько вам лет?");
}
```

---

## 🧩 Продвинутые фичи

*   **Middleware Pipeline:** Перехватывайте запросы, логируйте, проверяйте баны до выполнения команд.
*   **Broadcast API:** Умная рассылка сообщений с учетом лимитов Telegram.
*   **Telegram Payments 2.0:** Полная поддержка Stars и фиатных валют.
*   **Telegram Business:** Поддержка бизнес-коннектов и сообщений.

---

## 🤝 Контрибьютинг

Мы рады любым идеям и пулл-реквестам! 
Если вы нашли баг, пожалуйста, создайте [Issue](https://github.com/kaleert/nyagram/issues).

1.  Fork it
2.  Create your feature branch (`git checkout -b feature/amazing-feature`)
3.  Commit your changes (`git commit -m 'Add amazing feature'`)
4.  Push to the branch (`git push origin feature/amazing-feature`)
5.  Open a Pull Request

---

## 📄 Лицензия

Этот проект распространяется под лицензией MIT. Подробнее см. в файле [LICENSE](LICENSE).

---

<div align="center">
    <strong>Made with ❤️ by Kaleert</strong><br>
    2025-2026

</div>
