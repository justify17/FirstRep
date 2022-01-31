package BotObjects;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransportRoutes {
    private final TransportStop transportStop;
    private final Map<String, Integer> nearestTransport = new ConcurrentHashMap<>();

    public TransportRoutes(TransportStop transportStop) {
        this.transportStop = transportStop;
    }

    public void busesPageParsing() {
        Document doc = null;
        try {
            doc = Jsoup.connect(transportStop.URLFormatting()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements elements = doc.getElementsByAttributeValue("data-transport", "autobus");
        for (Element element : elements) {
            Pattern pattern = Pattern.compile("([0-9]+[а-я]?)\\s[()А-Яа-яЁё.-]+\\s?[()А-Яа-яЁё.-]*\\s?[()А-Яа-яЁё.]*[-]" +
                    "{1}([()А-Яа-яЁё.-]+\\s?[()А-Яа-яЁё.-]*\\s?[()А-Яа-яЁё.]*)\\s([\\d]|[1][\\d])\\sмин");
            Matcher matcher = pattern.matcher(element.text());
            while (matcher.find()) {
                for (String bus : transportStop.getFinalBuses()) {
                    if (!matcher.group(2).equals(transportStop.getStopName()) && matcher.group(1).equals(bus)) {
                        nearestTransport.put("Автобус № " + matcher.group(1), Integer.valueOf(matcher.group(3)));
                    }
                }
            }
        }
    }

    public void trolleybusesPageParsing() {
        Document doc = null;
        try {
            doc = Jsoup.connect(transportStop.URLFormatting()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements elements = doc.getElementsByAttributeValue("data-transport", "trolleybus");
        for (Element element : elements) {
            Pattern pattern = Pattern.compile("([0-9]+[а-я]?)\\s[()А-Яа-яЁё.-]+\\s?[()А-Яа-яЁё.-]*\\s?[()А-Яа-яЁё.]*[-]" +
                    "{1}([()А-Яа-яЁё.-]+\\s?[()А-Яа-яЁё.-]*\\s?[()А-Яа-яЁё.]*)\\s([\\d]|[1][\\d])\\sмин");
            Matcher matcher = pattern.matcher(element.text());
            while (matcher.find()) {
                for (String troll : transportStop.getFinalTrolleybuses()) {
                    if (!matcher.group(2).equals(transportStop.getStopName()) && matcher.group(1).equals(troll)) {
                        nearestTransport.put("Троллейбус № " + matcher.group(1), Integer.valueOf(matcher.group(3)));
                    }
                }
            }
        }
    }

    public String getNearestTransportByMessage() {
        StringBuilder sb = new StringBuilder();
        nearestTransport.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach((stringIntegerEntry) ->
                    sb.append(stringIntegerEntry.getKey()).append(" через ~ ")
                            .append(stringIntegerEntry.getValue()).append(" мин.\n")
                );
        if (sb.toString().trim().length() == 0) {
            return "Ошибка, проверьте правильность написания остановок.";
        }
        return "Ближайший транспорт:\n" + sb;
    }
}