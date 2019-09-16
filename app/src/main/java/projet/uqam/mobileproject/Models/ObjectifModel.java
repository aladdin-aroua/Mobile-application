package projet.uqam.mobileproject.Models;

import java.util.Date;

import io.realm.RealmObject;

public class ObjectifModel extends RealmObject {

    private int mCodeObjectif;
    private Float mStartingBalance;
    private Float mFinalBalance;
    private String mStartingDate;
    private String mFinalDate;
    private String mObjectifPriceCurrency;
    private Date mDate1;
    private int mIcon;
    private String mType;
    private String mName;

    public Date getDate1() {
        return mDate1;
    }

    public void setDate1(Date date1) {
        this.mDate1 = date1;
    }

    public int getCodeObjectif() {
        return mCodeObjectif;
    }

    public void setCodeObjectif(int codeObjectif) {
        this.mCodeObjectif = codeObjectif;
    }

    public String getObjectifPriceCurrency() {
        return mObjectifPriceCurrency;
    }

    public void setObjectifPriceCurrency(String objectifPriceCurrency) {
        this.mObjectifPriceCurrency = objectifPriceCurrency;
    }

    public Float getStartingBalance() {
        return mStartingBalance;
    }

    public void setStartingBalance(Float startingBalance) {this.mStartingBalance = startingBalance; }

    public Float getFinalBalance() {
        return mFinalBalance;
    }

    public void setFinalBalance(Float finalBalance) {
        this.mFinalBalance = finalBalance;
    }

    public String getStartingDate() {
        return mStartingDate;
    }

    public void setStartingDate(String startingDate) {
        this.mStartingDate = startingDate;
    }

    public String getFinalDate() {
        return mFinalDate;
    }

    public void setFinalDate(String finalDate) {
        this.mFinalDate = finalDate;
    }

    public int getIcon() {
        return mIcon;
    }

    public void setIcon(int icon) {
        this.mIcon = icon;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

}