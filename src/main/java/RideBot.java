import BotObjects.Commands.Keyboard;
import BotObjects.Config;
import BotObjects.Commands.WorkChat;
import BotObjects.Commands.StartCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class RideBot extends TelegramLongPollingCommandBot {
    private final WorkChat chat;
    final static Logger logger = LoggerFactory.getLogger(RideBot.class);

    public RideBot() {
        super();
        this.chat = new WorkChat();
        register(new StartCommand("go", "Старт"));
    }

    @Override
    public String getBotToken() {
        return Config.BOT_TOKEN;
    }

    @Override
    public String getBotUsername() {
        return Config.BOT_NAME;
    }

    /**
     * Ответ на запрос, не являющийся командой
     */
    @Override
    public void processNonCommandUpdate(Update update) {
        Message msg = update.getMessage();
        Long chatId = msg.getChatId();
        String userName = getUserName(msg);
        logger.info("userName: " + userName + "(chatId: " + chatId + ") ввёл " + msg.getText());
        String answer = chat.nonCommandExecute(chatId, userName, msg.getText());
        setAnswer(chatId, userName, answer);
    }

    /**
     * Формирование имени пользователя
     *
     * @param msg сообщение
     */
    private String getUserName(Message msg) {
        User user = msg.getFrom();
        String userName = user.getUserName();
        return (userName != null) ? userName : String.format("%s %s", user.getLastName(), user.getFirstName());
    }

    /**
     * Отправка ответа
     *
     * @param chatId   id чата
     * @param userName имя пользователя
     * @param text     текст ответа
     */
    private void setAnswer(Long chatId, String userName, String text) {
        SendMessage answer = new SendMessage();
        answer.setText(text);
        answer.setChatId(chatId.toString());
        Keyboard keyboard = new Keyboard(answer);
        keyboard.setButtonGo();
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            logger.error(e + " Не удалось отправить ответ userName: " + userName + "(chatId: " + chatId + ").");
        }
    }
}