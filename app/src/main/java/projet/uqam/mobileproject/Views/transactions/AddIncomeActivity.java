package projet.uqam.mobileproject.Views.transactions;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import projet.uqam.mobileproject.Controllers.TransactionController;
import projet.uqam.mobileproject.Models.TransactionModel;
import projet.uqam.mobileproject.R;

public class AddIncomeActivity extends AppCompatActivity {
    private Spinner mSpinner;
    private ArrayAdapter<CharSequence> mAdapter;
    private Button mAddRecette;
    private AutoCompleteTextView mInitialValue;
    private RadioGroup radioGroup;
    private ArrayList<TransactionModel> mTransactionArrayList;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_recette);

        radioGroup = (RadioGroup) findViewById(R.id.recette_portfeuille_gb);
        mInitialValue = (AutoCompleteTextView) findViewById(R.id.add_valeur_initiale_recette_portfeuille);
        mAddRecette = (Button) findViewById(R.id.button_add_recettte_portfeuille);
        mSpinner = (Spinner) findViewById(R.id.dv_recette);
        mAdapter = ArrayAdapter.createFromResource(this, R.array.objectifPriceCurrency, android.R.layout.simple_spinner_item);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mAdapter);

        mAddRecette.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (!TextUtils.isDigitsOnly(mInitialValue.getText())) {
                    mInitialValue.setError("invalide valeur");
                    ;
                } else if (mInitialValue.getText().toString().equals("")) {
                    mInitialValue.setError("invalide valeur");
                    mInitialValue.requestFocus();
                } else {
                    int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                    if (checkedRadioButtonId == -1) {

                        Toast.makeText(AddIncomeActivity.this, "No item selected !!", Toast.LENGTH_LONG).show();

                    } else {

                        Realm.init(getApplicationContext());
                        realm = Realm.getDefaultInstance();
                        final TransactionModel t = new TransactionModel();
                        t.setType("C");
                        String value = mInitialValue.getText().toString();
                        Float finalValue = Float.parseFloat(value);
                        t.setBalance(finalValue);
                        t.setCompte("Portfeuille");
                        Calendar calendar = Calendar.getInstance();
                        final String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
                        if (checkedRadioButtonId == R.id.salaire_rb) {
                            setSalary(t, currentDate, mSpinner, realm, mTransactionArrayList);
                            finalToast();

                        } else if (checkedRadioButtonId == R.id.autre_recette_rb) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddIncomeActivity.this);
                            final EditText edittext = new EditText(AddIncomeActivity.this);
                            builder.setView(edittext);

                            builder.setMessage("Entrer la cathégorie de votre dépense s'il vous plaît").setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (edittext.getText().toString().equals("")) {
                                        Toast.makeText(AddIncomeActivity.this, "invalide cathégorie", Toast.LENGTH_LONG).show();

                                    } else {
                                        String editTextValue = edittext.getText().toString();
                                        otherRecete(t, currentDate, mSpinner, realm, mTransactionArrayList, editTextValue);
                                        finalToast();



                                    }
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });

                            builder.show();

                        } else {
                            setDepot(t, currentDate, mSpinner, realm, mTransactionArrayList);
                            finalToast();
                        }
                    }

                }

            }

        });
    }

    private void finalToast() {
        Toast.makeText(AddIncomeActivity.this, "Ajouter success !!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(AddIncomeActivity.this, MainActivity.class);
        intent.putExtra(Keys.KEYTAB, 0);
        startActivity(intent);
    }

    private void setSalary(TransactionModel t, String currentDate, Spinner spinner,
                           Realm realm, ArrayList<TransactionModel> transactionArrayList) {
        t.setCathégorie("Salaire");
        t.setIcon(R.drawable.salaire);
        t.setDate(currentDate);
        t.setTransactionPriceCurrency(spinner.getSelectedItem().toString());
        TransactionController tc = new TransactionController(realm, AddIncomeActivity.this);

        transactionArrayList = tc.getAllTransaction();
        if (transactionArrayList.size() == 0) {
            t.setCodeTransaction(1);
        } else {
            t.setCodeTransaction(transactionArrayList.get(transactionArrayList.size() - 1).getCodeTransaction() + 1);
        }
        tc.insertTransaction(t);
    }

    private void otherRecete(TransactionModel t, String currentDate, Spinner spinner,
                             Realm realm, ArrayList<TransactionModel> transactionArrayList, String editTextValue) {
        t.setCathégorie("Autre recette");
        t.setIcon(R.drawable.autre);
        t.setDate(currentDate);
        t.setOtherCathégorie(editTextValue);
        t.setTransactionPriceCurrency(spinner.getSelectedItem().toString());
        TransactionController tc = new TransactionController(realm, AddIncomeActivity.this);

        transactionArrayList = tc.getAllTransaction();

        if (transactionArrayList.size() == 0) {
            t.setCodeTransaction(1);
        } else {
            t.setCodeTransaction(transactionArrayList.get(transactionArrayList.size() - 1).getCodeTransaction() + 1);
        }
        tc.insertTransaction(t);
    }

    private void setDepot(TransactionModel t, String currentDate, Spinner spinner,
                          Realm realm, ArrayList<TransactionModel> transactionArrayList) {
        t.setCathégorie("Dépot");
        t.setIcon(R.drawable.depot);
        t.setDate(currentDate);
        t.setTransactionPriceCurrency(spinner.getSelectedItem().toString());
        TransactionController tc = new TransactionController(realm, AddIncomeActivity.this);

        transactionArrayList = tc.getAllTransaction();

        if (transactionArrayList.size() == 0) {
            t.setCodeTransaction(1);
        } else {
            t.setCodeTransaction(transactionArrayList.get(transactionArrayList.size() - 1).getCodeTransaction() + 1);
        }
        tc.insertTransaction(t);
    }

}