package Commands;

public class NonCommand {
    private static int countMessage;
    TransportStop startStop;
    TransportStop finalStop;

    public String nonCommandExecute(Long chatId, String userName, String text) {
        String msg = "";
        if (countMessage == 0){
            startStop = new TransportStop(text);
            startStop.searcher();
            msg="Введите вторую остановку:";
            countMessage++;
            return msg;
        }
        if (countMessage == 1) {
            finalStop = new TransportStop(text);
            finalStop.searcher();
            startStop.searchDesiredRoutesTrolleybuses(finalStop);
            startStop.searchDesiredRoutesBuses(finalStop);
            TransportRoutes routes = new TransportRoutes(startStop);
            routes.busesPageParsing();
            routes.trolleybusesPageParsing();
            msg = " "+routes.getNearestTransport();
            countMessage = 0;
            return msg;
        }
        return msg;
    }
}