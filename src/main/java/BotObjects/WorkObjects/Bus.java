package BotObjects.WorkObjects;

public class Bus extends Transport {

    public Bus(String number) {
        this.number = number;
    }

    public Bus(String number, int arrivalTime) {
        this.number = number;
        this.arrivalTime = arrivalTime;
    }

    @Override
    public String getRouteInformation() {
        return "Автобус № " + number + " через ~ " + arrivalTime + " мин.\n";
    }

    @Override
    public String toString() {
        return "Автобус № " + number;
    }
}