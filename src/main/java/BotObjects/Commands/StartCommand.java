package BotObjects.Commands;

import BotObjects.BotStates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * Стартовая команда для начала работы бота
 */
public class StartCommand extends ServiceCommand {
    final static Logger logger = LoggerFactory.getLogger(StartCommand.class);

    public StartCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        // формируем имя пользователя - поскольку userName может быть не заполнено, для этого случая используем имя и фамилию пользователя
        String userName = (user.getUserName() != null) ? user.getUserName() :
                String.format("%s %s", user.getLastName(), user.getFirstName());
        // обращаемся к методу суперкласса для отправки пользователю ответа
        logger.info("userName: " + user.getUserName() + "(chatId: " + chat.getId() + ") ввёл " + "/go");
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                "Где вы сейчас находитесь?");
        WorkChat.setBotState(BotStates.WAIT_FIRST_STOP);
    }
}