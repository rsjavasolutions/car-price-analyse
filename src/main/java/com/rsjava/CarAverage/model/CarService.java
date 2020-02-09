package com.rsjava.CarAverage.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CarService {

    private String link;

    public CarService() {
    }

    public CarService(String link) {
        this.link = link;

    }

    //liczba stron
    private int numberOfPages() {
        int pages = 0;

        try {
            Document documentPage = Jsoup.connect(link + 1).get();
            Elements elements = documentPage.getElementsByClass("page");
            if (elements.size() != 0) {
                pages = Integer.valueOf(elements.get(elements.size() - 1).text());
            } else {
                pages = 1;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return pages;
    }

    //pobieram wszystie elementy z karzdej strony
    private List<Elements> allPagesElements() {
        Elements elements = null;
        List<Elements> allElements = new ArrayList<>();

        for (int i = 1; i <= numberOfPages(); i++) {
            Document document = null;
            try {
                document = Jsoup.connect(link + i).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            elements = document.getElementsByClass("offer-price__number ds-price-number");
            allElements.add(elements);
        }
        return allElements;
    }

    //dodaję elementy ze wszystkich stron do jednej listy
    private List<String> allElements() {
        List<String> elementsList = new LinkedList<>();
        for (Elements elements1 : allPagesElements()) {
            for (int i = 0; i < elements1.size(); i++) {
                elementsList.add(elements1.get(i).text());
            }
        }
        return elementsList;
    }

    //konwertuje na listę cen
    private List<Double> prices() {
        List<String> originalStrings = allElements();

        return originalStrings.stream()
                .map(s -> s.replace(" ", ""))
                .map(t -> t.substring(0, t.indexOf("P")))
                .map(x -> Double.parseDouble(x))
                .collect(Collectors.toList());
    }

    public int getNumberOfElements() {
        return prices().size();
    }

    public double getAverage() {
        double average = 0;
        for (Double d : prices()) {
            average += d;
        }
        return average / prices().size();
    }

    public double getMinPrice(){
        return   Collections.min(prices());
    }

    public double getMaxPrice(){
        return Collections.max(prices());
    }
}