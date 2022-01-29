package Commands;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransportRoutes {

    private  List<String> trolleybuses = new ArrayList<>();
    private List<String> buses = new ArrayList<>();
    private String message;


    public TransportRoutes(String message){
        this.message = message;
    }

    public String pageParsing(){
        Document doc = null;
        try {
            doc = Jsoup.connect("https://kogda.by/stops/gomel/"+message).get();
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

    public void searchTrolleybuses(String str){
        Pattern pattern = Pattern.compile("Троллейбусы[^А-Я]+");
        Matcher matcher = pattern.matcher(str);
        while(matcher.find()){
            pattern = Pattern.compile("[\\d]+[а-яА-Я]?");
            matcher = pattern.matcher(matcher.group());
            while(matcher.find()){
                trolleybuses.add(matcher.group());
            }
        }
    }

    public void searchBuses(String str){
        Pattern pattern = Pattern.compile("Автобусы[^А-Я]+");
        Matcher matcher = pattern.matcher(str);
        while(matcher.find()){
            pattern = Pattern.compile("[\\d]+[а-яА-Я]?");
            matcher = pattern.matcher(matcher.group());
            while(matcher.find()){
                buses.add(matcher.group());
            }
        }
    }

    public void searcher(){
        String result = pageParsing();
        searchTrolleybuses(result);
        searchBuses(result);
    }

    public List<String> getTrolleybuses() {
        return trolleybuses;
    }

    public List<String> getBuses() {
        return buses;
    }

    public String searchForTheDesiredRoutesTroll(TransportRoutes tr){
        List<String> allTrollOne = this.getTrolleybuses();
        List<String> allTrollTwo = tr.getTrolleybuses();
        System.out.println(allTrollOne);
        System.out.println(allTrollTwo);
        List<String> finalTrolleybuses = new ArrayList<>();
        ListIterator<String> itTrollOne = allTrollOne.listIterator();
        while (itTrollOne.hasNext()){
            String strOne = itTrollOne.next();
            for (String string: allTrollTwo){
                if (strOne.equals(string)){
                    finalTrolleybuses.add(strOne);
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (String string: finalTrolleybuses){
            sb.append(string).append(" ");
        }
        System.out.println(sb);
        return sb.toString();
    }

    public String searchForTheDesiredRoutesBuses(TransportRoutes tr){
        List<String> allBusOne = this.getBuses();
        List<String> allBusTwo = tr.getBuses();
        System.out.println(allBusOne);
        System.out.println(allBusTwo);
        List<String> finalBuses = new ArrayList<>();
        ListIterator<String> itBusOne = allBusOne.listIterator();
        while (itBusOne.hasNext()){
            String strOne = itBusOne.next();
            for (String string: allBusTwo){
                if (strOne.equals(string)){
                    finalBuses.add(strOne);
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (String string: finalBuses){
            sb.append(string).append(" ");
        }
        System.out.println(sb);
        return sb.toString();
    }
}
