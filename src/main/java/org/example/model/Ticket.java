package org.example.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Ticket {
    private int id;
    private LocalDateTime timeIn = LocalDateTime.now();
    private LocalDateTime timeOut;
    private Duration peakDuration;
    private Duration offPeakDuration;
    private Duration weekendDuration;
    private double peakCost;
    private double offPeakCost;
    private double weekendCost;
    private double price;

    public Ticket() {
    }

    public Ticket(int id, LocalDateTime timeIn, LocalDateTime timeOut, Duration peakDuration, Duration offPeakDuration, Duration weekendDuration, double peakCost, double offPeakCost, double weekendCost, double price) {
        this.id = id;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.peakDuration = peakDuration;
        this.offPeakDuration = offPeakDuration;
        this.weekendDuration = weekendDuration;
        this.peakCost = peakCost;
        this.offPeakCost = offPeakCost;
        this.weekendCost = weekendCost;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(LocalDateTime timeIn) {
        this.timeIn = timeIn;
    }

    public LocalDateTime getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(LocalDateTime timeOut) {
        this.timeOut = timeOut;
    }

    public Duration getPeakDuration() {
        return peakDuration;
    }

    public void setPeakDuration(Duration peakDuration) {
        this.peakDuration = peakDuration;
    }

    public Duration getOffPeakDuration() {
        return offPeakDuration;
    }

    public void setOffPeakDuration(Duration offPeakDuration) {
        this.offPeakDuration = offPeakDuration;
    }

    public Duration getWeekendDuration() {
        return weekendDuration;
    }

    public void setWeekendDuration(Duration weekendDuration) {
        this.weekendDuration = weekendDuration;
    }

    public double getPeakCost() {
        return peakCost;
    }

    public void setPeakCost(double peakCost) {
        this.peakCost = peakCost;
    }

    public double getOffPeakCost() {
        return offPeakCost;
    }

    public void setOffPeakCost(double offPeakCost) {
        this.offPeakCost = offPeakCost;
    }

    public double getWeekendCost() {
        return weekendCost;
    }

    public void setWeekendCost(double weekendCost) {
        this.weekendCost = weekendCost;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}