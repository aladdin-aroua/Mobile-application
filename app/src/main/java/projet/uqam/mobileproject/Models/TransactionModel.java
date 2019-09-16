package projet.uqam.mobileproject.Models;

import io.realm.RealmObject;

public class TransactionModel extends RealmObject {

    private int mCodeTransaction;
    private String mCathégorie;
    private String mDate;
    private int mIcon;
    private String mType;
    private String mCompte;
    private Float mBalance;
    private String otherCathégorie;
    private String mTransactionPriceCurrency;

    @Override
    public String toString() {
        return "Transaction{" + "mCodeTransaction=" + mCodeTransaction + ", mCathégorie='" + mCathégorie + '\'' + ", mDate='" + mDate + '\'' + ", mIcon='" + mIcon + '\'' + ", mType='" + mType + '\'' + ", mCompte='" + mCompte + '\'' + ", mBalance=" + mBalance + '}';
    }

    public int getCodeTransaction() {
        return mCodeTransaction;
    }

    public String getOtherCathégorie() {
        return otherCathégorie;
    }

    public void setOtherCathégorie(String otherCathégorie) {
        this.otherCathégorie = otherCathégorie;
    }

    public void setCodeTransaction(int codeTransaction) {
        this.mCodeTransaction = codeTransaction;
    }

    public String getTransactionPriceCurrency() {
        return mTransactionPriceCurrency;
    }

    public void setTransactionPriceCurrency(String transactionPriceCurrency) {
        this.mTransactionPriceCurrency = transactionPriceCurrency;
    }

    public String getCathégorie() {
        return mCathégorie;
    }

    public void setCathégorie(String cathégorie) {
        this.mCathégorie = cathégorie;
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

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public String getCompte() {
        return mCompte;
    }

    public void setCompte(String compte) {
        this.mCompte = compte;
    }

    public Float getBalance() {
        return mBalance;
    }

    public void setBalance(Float balance) {
        this.mBalance = balance;
    }

}