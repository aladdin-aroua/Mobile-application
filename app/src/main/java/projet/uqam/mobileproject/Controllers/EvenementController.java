package projet.uqam.mobileproject.Controllers;

import android.util.Log;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import projet.uqam.mobileproject.Models.EvenementModel;

public class EvenementController {

   private Realm mRealm;

    public EvenementController(Realm realm) {
        this.mRealm = realm;
    }

    public void  insertEvenment(final EvenementModel evenmentModel) {


        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {

                EvenementModel evenmentModel1 = bgRealm.createObject(EvenementModel.class);
                evenmentModel1.setName(evenmentModel.getName());
                evenmentModel1.setCodeEvent(evenmentModel.getCodeEvent());
                evenmentModel1.setDate(evenmentModel.getDate());
                evenmentModel1.setIcon(evenmentModel.getIcon());
                evenmentModel1.setDate1(evenmentModel.getDate1());
                evenmentModel1.setBudjet(evenmentModel.getBudjet());
                evenmentModel1.setEventPriceCurrency(evenmentModel.getEventPriceCurrency());

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
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
    public void  deletetEvenement( int code) {
        final RealmResults<EvenementModel> rows = mRealm.where(EvenementModel.class).equalTo("codeEvenement",code).findAll();

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                rows.deleteAllFromRealm();
            }
        });

    }
    public ArrayList<EvenementModel> getAllEvenment() {

        ArrayList<EvenementModel> listEvenment=new ArrayList<>();
        RealmResults<EvenementModel> realmResults= mRealm.where(EvenementModel.class).findAll();
        for (EvenementModel e:realmResults)
        {
            listEvenment.add(e);
        }
        return listEvenment;
    }



}
