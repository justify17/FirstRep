import botObjects.BotStates;
import botObjects.Keyboard;
import botObjects.Config;
import botObjects.WorkChat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.ConcurrentHashMap;

public class RideBot extends TelegramLongPollingBot {
    private static ConcurrentHashMap<Long, WorkChat> storage = new ConcurrentHashMap<>();
    final static Logger logger = LoggerFactory.getLogger(RideBot.class);

    @Override
    public String getBotToken() {
        return Config.BOT_TOKEN;
    }

    @Override
    public String getBotUsername() {
        return Config.BOT_NAME;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message msg = update.getMessage();
        storage.putIfAbsent(msg.getChatId(), new WorkChat());
        logger.info("userName: " + getUserName(msg) + "(chatId: " + msg.getChatId() + ") ввёл " + msg.getText());
        setAnswer(msg, storage.get(msg.getChatId()).messaging(msg.getText()));
    }

    private String getUserName(Message msg) {
        User user = msg.getFrom();
        String userName = user.getUserName();
        return (userName != null) ? userName : String.format("%s %s", user.getLastName(), user.getFirstName());
    }

    private void setAnswer(Message message, String text) {
        SendMessage answer = new SendMessage();
        answer.enableMarkdown(true);
        answer.setChatId(message.getChatId().toString());
        answer.setText(text);
        if (storage.get(message.getChatId()).getBotState() == BotStates.NO_WORK) {
            Keyboard keyboard = new Keyboard(answer);
            keyboard.setButtonGo();
        }
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            logger.error(e + " Не удалось отправить ответ userName: " + getUserName(message) +
                    "(chatId: " + message.getChatId() + ").");
        }
    }
}