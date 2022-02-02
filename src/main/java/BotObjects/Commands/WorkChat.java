package BotObjects.Commands;

import BotObjects.BotStates;
import BotObjects.WorkObjects.TransportRoutes;
import BotObjects.WorkObjects.TransportStop;

/**
 * Класс для выполнения задачи бота
 */
public class WorkChat {
    private static BotStates botState = BotStates.NO_WORK;
    private TransportStop startStop;
    private TransportStop finalStop;

    public String nonCommandExecute(Long chatId, String userName, String text) {
        String msg = "Введите /go для начала работы бота.";
        if (botState == BotStates.WAIT_FIRST_STOP) {
            startStop = new TransportStop(text);
            startStop.searcher();
            msg = "Куда вам нужно доехать?";
            botState = BotStates.WAIT_SECOND_STOP;
            return msg;
        }
        if (botState == BotStates.WAIT_SECOND_STOP) {
            finalStop = new TransportStop(text);
            finalStop.searcher();
            startStop.searchDesiredRoutesTrolleybuses(finalStop);
            startStop.searchDesiredRoutesBuses(finalStop);
            TransportRoutes routes = new TransportRoutes(startStop);
            routes.busesPageParsing();
            routes.trolleybusesPageParsing();
            msg = routes.getNearestTransportByMessage();
            botState = BotStates.NO_WORK;
            return msg;
        }
        return msg;
    }

    public static BotStates getBotState() {
        return botState;
    }

    public static void setBotState(BotStates botState) {
        WorkChat.botState = botState;
    }
}