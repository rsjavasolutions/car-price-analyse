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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    private int numberOfPages(){
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

    private List<Elements> elementsFromEachPage() {
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
        if (allElements.size() != 0) {
            return allElements;
        }
       else return null;
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

    private List<Double> pricesParsedToDouble() {
        List<String> originalStrings = allElementsOnEachPage();

        return originalStrings.stream()
                .map(x -> x.replace(" ", ""))
                .map(x -> x.replace(",", "."))
                .map(x -> x.substring(0, x.indexOf("P")))
                .map(x -> Double.parseDouble(x))
                .collect(Collectors.toList());
    }

    public int getNumberOfElements() {
        return pricesParsedToDouble().size();
    }

    public double getAverage() {
        double average = 0;
        for (Double d : pricesParsedToDouble()) {
            average += d;
        }
        return rounding(average / pricesParsedToDouble().size());
    }

    public double getMedian() {
        double med = 0;
        List<Double> doubleList;
        doubleList = pricesParsedToDouble();
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
        return rounding(Collections.min(pricesParsedToDouble()));
    }

    public double getMaxPrice() {
        return rounding(Collections.max(pricesParsedToDouble()));
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