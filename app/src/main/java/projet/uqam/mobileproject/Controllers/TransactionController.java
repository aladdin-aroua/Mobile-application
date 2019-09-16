package projet.uqam.mobileproject.Controllers;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import projet.uqam.mobileproject.Models.TransactionModel;

public class TransactionController {

   Realm realm;
    public TransactionController(Realm realm, Context context) {
        this.context= context;
        this.realm = realm;
    }
    public   Context context;


    public void insertAllTransaction(final List<TransactionModel> transcations) {
        this.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                for (int i = 0; i < transcations.size(); i++) {
                    // insert data
                    TransactionModel transaction = transcations.get(i);
                    TransactionModel transaction1 =  bgRealm.createObject(TransactionModel.class);
                    transaction1.setCodeTransaction(transaction.getCodeTransaction());
                    transaction1.setCathégorie(transaction.getCathégorie());
                    transaction1.setOtherCathégorie(transaction.getOtherCathégorie());
                    transaction1.setCompte(transaction.getCompte());
                    transaction1.setDate(transaction.getDate());
                    transaction1.setIcon(transaction.getIcon());
                    transaction1.setBalance(transaction.getBalance());
                    transaction1.setType(transaction.getType());
                    transaction1.setTransactionPriceCurrency(transaction.getTransactionPriceCurrency());
                }
            }
        });

    }




    public void  insertTransaction(final TransactionModel transaction) {

        this.realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {

                TransactionModel transaction1 = bgRealm.createObject(TransactionModel.class);
                transaction1.setCodeTransaction(transaction.getCodeTransaction());
                transaction1.setCathégorie(transaction.getCathégorie());
                transaction1.setOtherCathégorie(transaction.getOtherCathégorie());
                transaction1.setCompte(transaction.getCompte());
                transaction1.setDate(transaction.getDate());
                transaction1.setIcon(transaction.getIcon());
                transaction1.setBalance(transaction.getBalance());
                transaction1.setType(transaction.getType());
                transaction1.setTransactionPriceCurrency(transaction.getTransactionPriceCurrency());
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
    public void  deletetTransaction(TransactionModel transaction) {

    }
    public ArrayList<TransactionModel> getAllTransaction() {

        ArrayList<TransactionModel> listTransaction=new ArrayList<>();
        RealmResults<TransactionModel> realmResults=realm.where(TransactionModel.class).findAll();
        for (TransactionModel t:realmResults)
        {
            listTransaction.add(t);
        }

        return listTransaction;
    }



}
