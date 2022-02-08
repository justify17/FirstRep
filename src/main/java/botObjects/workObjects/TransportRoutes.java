package botObjects.workObjects;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс для поиска ближайшего по времени транспорта с выбранной остановки
 */
public class TransportRoutes {
    final static Logger logger = LoggerFactory.getLogger(TransportRoutes.class);
    private final List<Transport> nearestTransport = new ArrayList<>();
    private final TransportStop transportStop;

    public TransportRoutes(TransportStop transportStop) {
        this.transportStop = transportStop;
    }

    public List<Transport> getNearestTransport() {
        return nearestTransport;
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
                for (Trolleybus troll : transportStop.getNecessaryTrolleybuses()) {
                    if (!matcher.group(2).equals(transportStop.getStopName()) && matcher.group(1).equals(troll.getNumber())) {
                        nearestTransport.add(new Trolleybus(matcher.group(1), Integer.parseInt(matcher.group(3))));
                    }
                }
            }
        }
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
                for (Bus bus : transportStop.getNecessaryBuses()) {
                    if (!matcher.group(2).equals(transportStop.getStopName()) && matcher.group(1).equals(bus.getNumber())) {
                        nearestTransport.add(new Bus(matcher.group(1), Integer.parseInt(matcher.group(3))));
                    }
                }
            }
        }
    }

    public String getNearestTransportByMessage() {
        StringBuilder sb = new StringBuilder();
        nearestTransport.stream()
                .sorted(Comparator.comparing(Transport::getArrivalTime))
                .forEach((transport -> sb.append(transport.getRouteInformation())));
        if (sb.toString().trim().length() == 0) {
            logger.info("Пользователь ошибся при написании остановок и не получил результат.");
            return "Ошибка, проверьте правильность написания остановок.";
        }
        logger.info("Пользователь получил результат.");
        return "Ближайший транспорт:\n" + sb;
    }
}