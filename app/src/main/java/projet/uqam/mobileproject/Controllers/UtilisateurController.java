package projet.uqam.mobileproject.Controllers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import projet.uqam.mobileproject.Models.UtilisateurModel;
import projet.uqam.mobileproject.Views.authentification.LogInActivity;

public class UtilisateurController {

    Realm realm;

    public UtilisateurController(Realm realm) {
        this.realm = realm;
    }

    public void  insertUtilisateur(final UtilisateurModel utilisateurModel, final Context c) {


        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {

                UtilisateurModel utilisateurModel1 = bgRealm.createObject(UtilisateurModel.class);
                utilisateurModel1.setId(utilisateurModel.getId());
                utilisateurModel1.setmName(utilisateurModel.getmName());
                utilisateurModel1.setmFirstName(utilisateurModel.getmFirstName());
                utilisateurModel1.setmE_mail(utilisateurModel.getmE_mail());
                utilisateurModel1.setmPassword(utilisateurModel.getmPassword());



            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(c, "Ajouter success !!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(c, LogInActivity.class);
               c.startActivity(intent);
                Log.v("Database",">>>>>>>>>>>Stored OK<<<<<<<<<<<<");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e("Database",error.getMessage());
            }
        });
    }

    public ArrayList<UtilisateurModel> getAllUser() {

        ArrayList<UtilisateurModel> listUser=new ArrayList  <>();
        RealmResults<UtilisateurModel> realmResults=realm.where(UtilisateurModel.class).findAll();
        for (UtilisateurModel e:realmResults)
        {
            listUser.add(e);
        }
        return listUser;
    }



}
