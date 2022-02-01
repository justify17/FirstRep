package BotObjects.WorkObjects;

public class Trolleybus extends Transport {

    public Trolleybus(String number) {
        this.number = number;
    }

    public Trolleybus(String number, int arrivalTime) {
        this.number = number;
        this.arrivalTime = arrivalTime;
    }

    @Override
    public String getRouteInformation() {
        return "Троллейбус № " + number + " через ~ " + arrivalTime + " мин.\n";
    }

    @Override
    public String toString() {
        return "Троллейбус № " + number;
    }
}