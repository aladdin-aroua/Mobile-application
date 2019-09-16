package projet.uqam.mobileproject.Models;

import io.realm.RealmObject;

public class UtilisateurModel extends RealmObject {

    private int id;
    private String mName;
    private String mFirstName;
    private String mE_mail;
    private String mPassword;


    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmFirstName() {
        return mFirstName;
    }

    public void setmFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getmE_mail() {
        return mE_mail;
    }

    public void setmE_mail(String mE_mail) {
        this.mE_mail = mE_mail;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
