package projet.uqam.mobileproject.Views.transactions;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

import io.realm.Realm;
import projet.uqam.mobileproject.Controllers.TransactionController;
import projet.uqam.mobileproject.Models.TransactionModel;
import projet.uqam.mobileproject.R;

// cette classe affiche les detail de dépense quand l'utilisateur clique sur le pieChart
public class DetailDepance extends AppCompatActivity {

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_depense);
        if (getIntent().hasExtra(Keys.CATNAME)) {
            String cat = getIntent().getExtras().getString(Keys.CATNAME);
            Log.e("test", cat);
            Realm.init(getApplicationContext());
            realm = Realm.getDefaultInstance();
            TransactionController tc = new TransactionController(realm, DetailDepance.this);
            ArrayList<TransactionModel> transactionArrayList = tc.getAllTransaction();
            ArrayList<TransactionModel> transactionArrayList1 = new ArrayList<>();
            ;

            for (int i = 0; i < transactionArrayList.size(); i++) {
                if (transactionArrayList.get(i).getCathégorie().equals(cat)) {

                    transactionArrayList1.add(transactionArrayList.get(i));

                }

            }

            Collections.reverse(transactionArrayList1);

            setRVParameter(transactionArrayList1);

        }

    }

    private void setRVParameter(ArrayList<TransactionModel> transactionArrayList1) {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.RV_transaction_detail);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(DetailDepance.this));
        RecyclerView.Adapter adapter = new AdapterRCTransaction(transactionArrayList1, DetailDepance.this);
        recyclerView.setAdapter(adapter);
    }

}