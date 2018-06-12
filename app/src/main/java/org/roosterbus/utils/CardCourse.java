package org.roosterbus.utils;

public class CardCourse {

    private String title;
    private String schedule;
    private String teacher;
    private int color;

    public CardCourse(String title, String schedule, String teacher, int color){
        this.title = title;
        this.schedule = schedule;
        this.teacher = teacher;
        this.color = color;
    }


    public String getTitle() {
        return title;
    }

    public String getSchedule() {
        return schedule;
    }

    public String getTeacher() {
        return teacher;
    }

    public int getColor() {
        return color;
    }
}
