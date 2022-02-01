package BotObjects.WorkObjects;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransportStop {
    private final List<Trolleybus> allTrolleybusesAtStop = new ArrayList<>();
    private final List<Bus> allBusesAtStop = new ArrayList<>();
    private final String stopName;
    private final List<Trolleybus> necessaryTrolleybuses = new ArrayList<>();
    private final List<Bus> necessaryBuses = new ArrayList<>();

    public TransportStop(String stopName){
        this.stopName = stopNameFormatting(stopName);
    }

    public List<Trolleybus> getAllTrolleybusesAtStop() {
        return allTrolleybusesAtStop;
    }

    public List<Bus> getAllBusesAtStop() {
        return allBusesAtStop;
    }

    public List<Trolleybus> getNecessaryTrolleybuses() {
        return necessaryTrolleybuses;
    }

    public List<Bus> getNecessaryBuses() {
        return necessaryBuses;
    }

    private String stopNameFormatting(String stopName){
        if (stopName.equalsIgnoreCase("ЗИП")){
            return  "Завод Измерительных Приборов";
        } else if (stopName.equalsIgnoreCase("Домой")){
            return "Кинотеатр Октябрь";
        }
        return stopName;
    }

    public String URLFormatting(){
        return "https://kogda.by/stops/gomel/"+stopName.replaceAll(" +","%20");
    }

    public String pageParsing(){
        Document doc = null;
        try {
            doc = Jsoup.connect(URLFormatting()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements newsHeadlines = doc.select("div.transport-block");
        StringBuilder sb = new StringBuilder();
        for (Element headline : newsHeadlines) {
            sb.append(headline.text()+" ");
        }
        return sb.toString();
    }

    public void searchAllTrolleybusesAtStop(String str){
        System.out.println(stopName);
        Pattern pattern = Pattern.compile("Троллейбусы[^А-Я]+");
        Matcher matcher = pattern.matcher(str);
        while(matcher.find()){
            pattern = Pattern.compile("[\\d]+[а-яА-Я]?");
            matcher = pattern.matcher(matcher.group());
            while(matcher.find()){
                allTrolleybusesAtStop.add(new Trolleybus(matcher.group()));
            }
        }
        System.out.println(allTrolleybusesAtStop);
    }

    public void searchAllBusesAtStop(String str){
        System.out.println(stopName);
        Pattern pattern = Pattern.compile("Автобусы[^А-Я]+");
        Matcher matcher = pattern.matcher(str);
        while(matcher.find()){
            pattern = Pattern.compile("[\\d]+[а-яА-Я]?");
            matcher = pattern.matcher(matcher.group());
            while(matcher.find()){
                allBusesAtStop.add(new Bus(matcher.group()));
            }
        }
        System.out.println(allBusesAtStop);
    }

    public void searcher(){
        String stop = pageParsing();
        searchAllTrolleybusesAtStop(stop);
        searchAllBusesAtStop(stop);
    }

    public String getStopName() {
        return stopName;
    }

    public void searchDesiredRoutesTrolleybuses(TransportStop tr){
        for (Trolleybus trolleybusOne : this.allTrolleybusesAtStop) {
            for (Trolleybus trolleybusTwo : tr.allTrolleybusesAtStop) {
                if (trolleybusOne.getNumber().equals(trolleybusTwo.getNumber())) {
                    necessaryTrolleybuses.add(trolleybusOne);
                }
            }
        }
        System.out.println("Троллейбусы, которые едут по маршруту "+ this.getStopName()+" -> "+
                tr.getStopName()+":\n"+necessaryTrolleybuses);
    }

    public void searchDesiredRoutesBuses(TransportStop tr){
        for (Bus busOne : this.allBusesAtStop) {
            for (Bus busTwo : tr.allBusesAtStop) {
                if (busOne.getNumber().equals(busTwo.getNumber())) {
                    necessaryBuses.add(busOne);
                }
            }
        }
        System.out.println("Автобусы, которые едут по маршруту "+ this.getStopName()+" -> "+
                tr.getStopName()+":\n"+necessaryBuses);
    }
}