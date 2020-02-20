package com.rsjava.CarAverage.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CarService {

    private String link;

    public CarService() {
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    //liczba stron
    private int numberOfPages() {
        int pages = 0;

        try {
            Document documentPage = Jsoup.connect(link).get();
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

    private List<Elements> elementsFromEachPage()  {
        Elements elements = null;
        List<Elements> allElements = new ArrayList<>();

        for (int i = 1; i <= numberOfPages(); i++) {
            Document document = null;
            try {
                document = Jsoup.connect(link + "&page=" + i).get();
                elements = document.getElementsByClass("offer-price__number ds-price-number");
                allElements.add(elements);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return allElements;
    }

    private List<String> allElementsOnEachPage() {
        List<String> elementsList = new LinkedList<>();
        for (Elements elements1 : elementsFromEachPage()) {
            for (int i = 0; i < elements1.size(); i++) {
                elementsList.add(elements1.get(i).text());
            }
        }
        return elementsList;
    }

    private List<Double> prices() {
        List<String> originalStrings = allElementsOnEachPage();

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
        return rounding(average / prices().size());
    }

    public double getMedian() {
        double med = 0;
        List<Double> doubleList;
        doubleList = prices();
        Collections.sort(doubleList);

        if (isOdd(doubleList.size())) {
            med = doubleList.get(doubleList.size() / 2);
        } else {
            med = (doubleList.get(doubleList.size() / 2)
                    + doubleList.get((doubleList.size() / 2) - 1)) / 2.0;
        }
        return rounding(med);
    }

    public double getMinPrice() {
        return rounding(Collections.min(prices()));
    }

    public double getMaxPrice() {
        return rounding(Collections.max(prices()));
    }

    private boolean isOdd(int a) {
        return (a & 1) == 1;

    }

    private double rounding(double number) {
        number *= 100;
        number = Math.round(number);
        number /= 100;
        return number;
    }
}