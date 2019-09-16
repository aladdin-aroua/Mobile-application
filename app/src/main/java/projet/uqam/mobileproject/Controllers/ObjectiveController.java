package projet.uqam.mobileproject.Controllers;

import android.util.Log;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import projet.uqam.mobileproject.Models.ObjectifModel;

public class ObjectiveController {

    private Realm mRealm;

    public ObjectiveController(Realm realm) {
        this.mRealm = realm;
    }

    public void  insertObjectif(final ObjectifModel objectifModel) {


        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                ObjectifModel objectifModel1 = bgRealm.createObject(ObjectifModel.class);
                objectifModel1.setStartingBalance(objectifModel.getStartingBalance());
                objectifModel1.setFinalBalance(objectifModel.getFinalBalance());
                objectifModel1.setStartingDate(objectifModel.getStartingDate());
                objectifModel1.setFinalDate(objectifModel.getFinalDate());
                objectifModel1.setDate1(objectifModel.getDate1());
                objectifModel1.setCodeObjectif(objectifModel.getCodeObjectif());
                objectifModel1.setIcon(objectifModel.getIcon());
                objectifModel1.setType(objectifModel.getType());
                objectifModel1.setName(objectifModel.getName());
                objectifModel1.setObjectifPriceCurrency(objectifModel.getObjectifPriceCurrency());
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
    public void  deletetObjectif(ObjectifModel objectifModel) {

    }
    public ArrayList<ObjectifModel> getAllObjectif() {

        ArrayList<ObjectifModel> listObjectif=new ArrayList<>();
        RealmResults<ObjectifModel> realmResults= mRealm.where(ObjectifModel.class).findAll();
        for (ObjectifModel o:realmResults)
        {
            listObjectif.add(o);
        }

        return listObjectif;
    }
}
