import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class AppLauncher {
    final static Logger logger = LoggerFactory.getLogger(AppLauncher.class);

    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new RideBot());
            logger.info("Бот начал работу");
        } catch (TelegramApiException e) {
            logger.error(e.toString());
        }
    }
}