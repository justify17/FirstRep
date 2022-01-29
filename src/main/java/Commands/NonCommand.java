package Commands;

public class NonCommand {
    private static int countMessage;
    TransportRoutes trOne;
    TransportRoutes trTwo;

    public String nonCommandExecute(Long chatId, String userName, String text) {
        String msg = "";
        if (countMessage == 0){
            trOne = new TransportRoutes(text);
            trOne.searcher();
            msg="Введите вторую остановку.";
            countMessage++;
            return msg;
        }
        if (countMessage == 1) {
            trTwo = new TransportRoutes(text);
            trTwo.searcher();
            msg = "Автобусы: "+trOne.searchForTheDesiredRoutesBuses(trTwo) + "\nТроллейбусы: "+
                trOne.searchForTheDesiredRoutesTroll(trTwo);
            countMessage = 0;
            return msg;
        }
        return msg;
    }
}