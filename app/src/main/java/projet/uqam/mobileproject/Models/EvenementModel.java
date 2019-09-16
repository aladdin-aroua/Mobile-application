package projet.uqam.mobileproject.Models;

import java.util.Date;

import io.realm.RealmObject;

public class EvenementModel extends RealmObject {

    private int mCodeEvent;
    private Date mDate1;
    private Float mBudjet;
    private String mDate;
    private int mIcon;
    private String mName;
    private String mEventPriceCurrency;

    public Date getDate1() {
        return mDate1;
    }

    public void setDate1(Date date1) {
        this.mDate1 = date1;
    }

    public int getCodeEvent() {
        return mCodeEvent;
    }

    public void setCodeEvent(int codeEvent) {
        this.mCodeEvent = codeEvent;
    }

    public Float getBudjet() {
        return mBudjet;
    }

    public String getEventPriceCurrency() {
        return mEventPriceCurrency;
    }

    public void setEventPriceCurrency(String eventPriceCurrency) {
        this.mEventPriceCurrency = eventPriceCurrency;
    }

    public void setBudjet(Float budjet) {
        this.mBudjet = budjet;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public int getIcon() {
        return mIcon;
    }

    public void setIcon(int icon) {
        this.mIcon = icon;
    }



    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }
}