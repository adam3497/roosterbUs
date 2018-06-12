package org.roosterbus.utils;

public class CardRoutes {

    private String title;
    private String rate;
    private int color;

    public CardRoutes(String title, String rate, int color){
        this.title = title;
        this.rate = rate;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public String getRate() {
        return rate;
    }

    public int getColor() {
        return 0;
    }
}
