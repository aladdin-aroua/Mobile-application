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

//cette classe est relative au ajout d'une dépense
public class AddSpentActivity extends AppCompatActivity {

    private Spinner mSpinner;
    private ArrayAdapter<CharSequence> mAdapter;
    private AutoCompleteTextView mInitialValue;
    private ArrayList<TransactionModel> mTransactionArrayList;
    private RadioGroup mRadioGroup;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_transaction);

        mSpinner = (Spinner) findViewById(R.id.dv_depense);
        mAdapter = ArrayAdapter.createFromResource(this, R.array.objectifPriceCurrency, android.R.layout.simple_spinner_item);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mAdapter);
        Button ajouterDepense = (Button) findViewById(R.id.button_add_depense_portfeuille);
        mInitialValue = (AutoCompleteTextView) findViewById(R.id.add_valeur_initiale_depense_portfeuille);
        mRadioGroup = (RadioGroup) findViewById(R.id.RG_add_depense);

        ajouterDepense.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (!TextUtils.isDigitsOnly(mInitialValue.getText())) {
                    mInitialValue.setError("invalide valeur");
                    mInitialValue.requestFocus();
                } else if (mInitialValue.getText().toString().equals("")) {
                    mInitialValue.setError("invalide valeur");
                    mInitialValue.requestFocus();
                } else {
                    int checkedRadioButtonId = mRadioGroup.getCheckedRadioButtonId();
                    if (checkedRadioButtonId == -1) {
                        Toast.makeText(AddSpentActivity.this, "No item selected !!", Toast.LENGTH_LONG).show();
                    } else {

                        Realm.init(getApplicationContext());
                        realm = Realm.getDefaultInstance();
                        final TransactionModel t = new TransactionModel();
                        t.setType("D");
                        t.setCompte("Portfeuille");
                        String value = mInitialValue.getText().toString();
                        Float finalValue = Float.parseFloat(value);
                        t.setBalance(finalValue);
                        Calendar calendar = Calendar.getInstance();
                        final String currentDate = DateFormat.getDateInstance().format(calendar.getTime());

                        if (checkedRadioButtonId == R.id.maison_rb) {
                            sethouse(t, currentDate, mSpinner, realm, mTransactionArrayList);
                            toastFinale();
                        } else if (checkedRadioButtonId == R.id.santé_rb) {
                            setHealth(t, currentDate, mSpinner, realm, mTransactionArrayList);
                            toastFinale();
                        } else if (checkedRadioButtonId == R.id.restaurant_rb) {
                            setRestaurant(t, currentDate, mSpinner, realm, mTransactionArrayList);
                            toastFinale();
                        } else if (checkedRadioButtonId == R.id.communication_rb) {
                            setCommunication(t, currentDate, mSpinner, realm, mTransactionArrayList);
                            toastFinale();
                        } else if (checkedRadioButtonId == R.id.transports_rb) {
                            setTransport(t, currentDate, mSpinner, realm, mTransactionArrayList);
                            toastFinale();
                        } else if (checkedRadioButtonId == R.id.autre_rb) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddSpentActivity.this);
                            final EditText edittext = new EditText(AddSpentActivity.this);
                            builder.setView(edittext);
                            builder.setMessage("Entrer la cathégorie de votre dépense s'il vous plaît").setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (edittext.getText().toString().equals("")) {
                                        Toast.makeText(AddSpentActivity.this, "invalide solde", Toast.LENGTH_LONG).show();
                                    } else {
                                        String editTextValue = edittext.getText().toString();
                                        setOther(t, currentDate, mSpinner, realm, mTransactionArrayList, editTextValue);
                                        toastFinale();
                                    }
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                            builder.show();
                        } else {
                            setClothers(t, currentDate, mSpinner, realm, mTransactionArrayList);
                            toastFinale();
                        }

                    }

                }

            }

        });
    }

    private void toastFinale() {
        Toast.makeText(AddSpentActivity.this, "Ajouter success !!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(AddSpentActivity.this, MainActivity.class);
        intent.putExtra(Keys.KEYTAB, 0);
        startActivity(intent);
    }

    private void sethouse(TransactionModel t, String currentDate, Spinner spinner,
                          Realm realm, ArrayList<TransactionModel> transactionArrayList) {
        t.setCathégorie("Maison");
        t.setIcon(R.drawable.house);
        t.setDate(currentDate);
        t.setTransactionPriceCurrency(spinner.getSelectedItem().toString());
        TransactionController tc = new TransactionController(realm, AddSpentActivity.this);

        transactionArrayList = tc.getAllTransaction();

        if (transactionArrayList.size() == 0) {
            t.setCodeTransaction(1);
        } else {
            t.setCodeTransaction(transactionArrayList.get(transactionArrayList.size() - 1).getCodeTransaction() + 1);
        }
        tc.insertTransaction(t);

    }

    private void setHealth(TransactionModel t, String currentDate, Spinner spinner,
                           Realm realm, ArrayList<TransactionModel> transactionArrayList) {
        t.setCathégorie("Santé");
        t.setIcon(R.drawable.doctor);
        t.setTransactionPriceCurrency(spinner.getSelectedItem().toString());
        t.setDate(currentDate);
        TransactionController tc = new TransactionController(realm, AddSpentActivity.this);

        transactionArrayList = tc.getAllTransaction();

        if (transactionArrayList.size() == 0) {
            t.setCodeTransaction(1);
        } else {
            t.setCodeTransaction(transactionArrayList.get(transactionArrayList.size() - 1).getCodeTransaction() + 1);
        }
        tc.insertTransaction(t);
    }

    private void setRestaurant(TransactionModel t, String currentDate, Spinner spinner,
                               Realm realm, ArrayList<TransactionModel> transactionArrayList) {
        t.setCathégorie("Réstaurant");
        t.setIcon(R.drawable.restaurant);
        t.setTransactionPriceCurrency(spinner.getSelectedItem().toString());
        t.setDate(currentDate);
        TransactionController tc = new TransactionController(realm, AddSpentActivity.this);

        transactionArrayList = tc.getAllTransaction();

        if (transactionArrayList.size() == 0) {
            t.setCodeTransaction(1);
        } else {
            t.setCodeTransaction(transactionArrayList.get(transactionArrayList.size() - 1).getCodeTransaction() + 1);
        }
        tc.insertTransaction(t);
    }

    private void setCommunication(TransactionModel t, String currentDate, Spinner spinner,
                                  Realm realm, ArrayList<TransactionModel> transactionArrayList) {
        t.setCathégorie("Communication");
        t.setTransactionPriceCurrency(spinner.getSelectedItem().toString());
        t.setIcon(R.drawable.communication);
        t.setDate(currentDate);
        TransactionController tc = new TransactionController(realm, AddSpentActivity.this);

        transactionArrayList = tc.getAllTransaction();

        if (transactionArrayList.size() == 0) {
            t.setCodeTransaction(1);
        } else {
            t.setCodeTransaction(transactionArrayList.get(transactionArrayList.size() - 1).getCodeTransaction() + 1);
        }
        tc.insertTransaction(t);
    }

    private void setTransport(TransactionModel t, String currentDate, Spinner spinner,
                              Realm realm, ArrayList<TransactionModel> transactionArrayList) {
        t.setTransactionPriceCurrency(spinner.getSelectedItem().toString());
        t.setCathégorie("Transports");
        t.setIcon(R.drawable.voiture);
        t.setDate(currentDate);
        TransactionController tc = new TransactionController(realm, AddSpentActivity.this);

        transactionArrayList = tc.getAllTransaction();

        if (transactionArrayList.size() == 0) {
            t.setCodeTransaction(1);
        } else {
            t.setCodeTransaction(transactionArrayList.get(transactionArrayList.size() - 1).getCodeTransaction() + 1);
        }

        tc.insertTransaction(t);
    }

    private void setOther(TransactionModel t, String currentDate, Spinner spinner,
                          Realm realm, ArrayList<TransactionModel> transactionArrayList, String editTextValue) {
        t.setCathégorie("Autre");
        t.setIcon(R.drawable.settings);
        t.setTransactionPriceCurrency(spinner.getSelectedItem().toString());
        t.setDate(currentDate);
        t.setOtherCathégorie(editTextValue);
        TransactionController tc = new TransactionController(realm, AddSpentActivity.this);

        transactionArrayList = tc.getAllTransaction();

        if (transactionArrayList.size() == 0) {
            t.setCodeTransaction(185);
        } else {
            t.setCodeTransaction(transactionArrayList.get(transactionArrayList.size() - 1).getCodeTransaction() + 1);
        }
        tc.insertTransaction(t);

    }

    private void setClothers(TransactionModel t, String currentDate, Spinner spinner,
                             Realm realm, ArrayList<TransactionModel> transactionArrayList) {
        t.setCathégorie("Vétements");
        t.setTransactionPriceCurrency(spinner.getSelectedItem().toString());
        t.setIcon(R.drawable.vetement);
        t.setDate(currentDate);
        TransactionController tc = new TransactionController(realm, AddSpentActivity.this);

        transactionArrayList = tc.getAllTransaction();

        if (transactionArrayList.size() == 0) {
            t.setCodeTransaction(1);
        } else {
            t.setCodeTransaction(transactionArrayList.get(transactionArrayList.size() - 1).getCodeTransaction() + 1);
        }
        tc.insertTransaction(t);
    }
}