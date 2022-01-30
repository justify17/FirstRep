package Commands;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TransportRoutes {
    TransportStop transportStop;
    private Map<String,Integer> nearestTransport = new HashMap<>();

    public TransportRoutes(TransportStop transportStop) {
        this.transportStop = transportStop;
    }

    public void busesPageParsing(){
        Document doc = null;
        try {
            doc = Jsoup.connect(transportStop.URLFormatting()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements elements = doc.getElementsByAttributeValue("data-transport","autobus");
        for(Element element:elements){
            Pattern pattern = Pattern.compile("([0-9]+[а-я]?)\\s[()А-Яа-яЁё.-]+\\s?[()А-Яа-яЁё.-]*\\s?[()А-Яа-яЁё.]*[-]" +
                    "{1}([()А-Яа-яЁё.-]+\\s?[()А-Яа-яЁё.-]*\\s?[()А-Яа-яЁё.]*)\\s([\\d]|[1][\\d])\\sмин");
            Matcher matcher = pattern.matcher(element.text());
            while(matcher.find()){
                for (String bus: transportStop.getFinalBuses()){
                    if (/*!matcher.group(2).equals(transportStop.getStopName()) &&*/ matcher.group(1).equals(bus)){
                        nearestTransport.put("Автобус № "+matcher.group(1),Integer.valueOf(matcher.group(3)));
                    }
                }
            }
        }
    }

    public void trolleybusesPageParsing(){
        Document doc = null;
        try {
            doc = Jsoup.connect(transportStop.URLFormatting()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements elements = doc.getElementsByAttributeValue("data-transport","trolleybus");
        for(Element element:elements){
            Pattern pattern = Pattern.compile("([0-9]+[а-я]?)\\s[()А-Яа-яЁё.-]+\\s?[()А-Яа-яЁё.-]*\\s?[()А-Яа-яЁё.]*[-]" +
                    "{1}([()А-Яа-яЁё.-]+\\s?[()А-Яа-яЁё.-]*\\s?[()А-Яа-яЁё.]*)\\s([\\d]|[1][\\d])\\sмин");
            Matcher matcher = pattern.matcher(element.text());
            while(matcher.find()){
                for(String troll: transportStop.getFinalTrolleybuses()){
                    if (/*!matcher.group(2).equals(transportStop.getStopName()) && */matcher.group(1).equals(troll)){
                        nearestTransport.put("Троллейбус № "+matcher.group(1),Integer.valueOf(matcher.group(3)));
                    }
                }
            }
        }
    }

    public Map<String, Integer> getNearestTransport() {
        nearestTransport = nearestTransport.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        return nearestTransport;
    }
}