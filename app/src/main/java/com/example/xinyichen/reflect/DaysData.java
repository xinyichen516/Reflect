package com.example.xinyichen.reflect;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by xinyichen on 10/7/17.
 */

public class DaysData implements Serializable{
    long timestamp;
    String date;
    String firebaseImageURL;
    String key;
    String transcription;
    double anger;
    double contempt;
    double disgust;
    double fear;
    double happiness;
    double neutral;
    double sadness;
    double surprise;


    DaysData() {}

    DaysData(String firebaseImageURL, String key, String transcription, double anger, double contempt, double disgust, double fear, double happiness, double neutral, double sadness, double surprise) {
        this.firebaseImageURL = firebaseImageURL;
        this.key = key;
        this.transcription = transcription;
        this.anger = anger;
        this.contempt = contempt;;
        this.disgust = disgust;
        this.fear = fear;
        this.happiness = happiness;
        this.neutral = neutral;
        this.sadness = sadness;
        this.surprise = surprise;
        this.timestamp = -1 * new Date().getTime();
        Calendar currDate = new GregorianCalendar();
        this.date = currDate.YEAR + "/" + currDate.MONTH + "/" + currDate.DAY_OF_MONTH;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getFirebaseImageURL() {
        return firebaseImageURL;
    }

    public void setFirebaseImageURL(String firebaseImageURL) {
        this.firebaseImageURL = firebaseImageURL;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTranscription() {
        return transcription;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }

    public double getAnger() {
        return anger;
    }

    public void setAnger(double anger) {
        this.anger = anger;
    }

    public double getContempt() {
        return contempt;
    }

    public void setContempt(double contempt) {
        this.contempt = contempt;
    }

    public double getDisgust() {
        return disgust;
    }

    public void setDisgust(double disgust) {
        this.disgust = disgust;
    }

    public double getFear() {
        return fear;
    }

    public void setFear(double fear) {
        this.fear = fear;
    }

    public double getHappiness() {
        return happiness;
    }

    public void setHappiness(double happiness) {
        this.happiness = happiness;
    }

    public double getNeutral() {
        return neutral;
    }

    public void setNeutral(double neutral) {
        this.neutral = neutral;
    }

    public double getSadness() {
        return sadness;
    }

    public void setSadness(double sadness) {
        this.sadness = sadness;
    }

    public double getSurprise() {
        return surprise;
    }

    public void setSurprise(double surprise) {
        this.surprise = surprise;
    }
}
