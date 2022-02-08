package botObjects;

import botObjects.workObjects.TransportRoutes;
import botObjects.workObjects.TransportStop;

/**
 * Класс для выполнения задачи бота
 */
public class WorkChat {
    private BotStates botState = BotStates.NO_WORK;
    private TransportStop startStop;

    public String messaging(String msg) {
        String answer = "";
        switch (botState) {
            case NO_WORK:
                if (msg.equals("/go")) {
                    answer = "Здравствуйте! Где вы сейчас находитесь?\nВведите полное название остановки:";
                    botState = BotStates.WAIT_FIRST_STOP;
                } else {
                    answer = "Введите /go для начала работы бота.";
                }
                return answer;
            case WAIT_FIRST_STOP:
                startStop = new TransportStop(msg);
                startStop.searcher();
                answer = "Куда вам нужно доехать?";
                botState = BotStates.WAIT_SECOND_STOP;
                return answer;
            case WAIT_SECOND_STOP:
                TransportStop finalStop = new TransportStop(msg);
                finalStop.searcher();
                startStop.searchDesiredRoutesTrolleybuses(finalStop);
                startStop.searchDesiredRoutesBuses(finalStop);
                TransportRoutes routes = new TransportRoutes(startStop);
                routes.busesPageParsing();
                routes.trolleybusesPageParsing();
                answer = routes.getNearestTransportByMessage();
                botState = BotStates.NO_WORK;
                return answer;
        }
        return answer;
    }

    public BotStates getBotState() {
        return botState;
    }
}