package BotObjects.WorkObjects;

public abstract class Transport {
    String number;
    int arrivalTime;

    public String getNumber() {
        return number;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public String getRouteInformation(){
        return "Транспорт";
    }
}
