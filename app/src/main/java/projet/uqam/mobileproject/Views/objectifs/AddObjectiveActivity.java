package projet.uqam.mobileproject.Views.objectifs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import projet.uqam.mobileproject.Controllers.ObjectiveController;
import projet.uqam.mobileproject.Models.ObjectifModel;
import projet.uqam.mobileproject.R;
import projet.uqam.mobileproject.Views.transactions.Keys;
import projet.uqam.mobileproject.Views.transactions.MainActivity;

public class AddObjectiveActivity extends AppCompatActivity {

    private Button mCancel;
    private Button mApply;
    private RadioGroup radioGroup;
    private Dialog myDialog;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private AutoCompleteTextView nomObjectif;
    private TextView mDisplayDate;
    private static final String TAG = "AddObjectif";
    private Spinner mSpinner;
    private ArrayAdapter<CharSequence> adapter;
    private AutoCompleteTextView addSoldeDepart;
    private AutoCompleteTextView addSoldeObjectif;
    private AutoCompleteTextView note;
    private Button mIconName;
    private Button addObjectif;
    private ArrayList<ObjectifModel> objectifModelArrayList;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_objectifs);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mSpinner = (Spinner) findViewById(R.id.dv_objectif);
        adapter = ArrayAdapter.createFromResource(this, R.array.objectifPriceCurrency, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        nomObjectif = (AutoCompleteTextView) findViewById(R.id.add_nom_objectif);
        mDisplayDate = (TextView) findViewById(R.id.calandrier_objectif);
        addSoldeDepart = (AutoCompleteTextView) findViewById(R.id.solde_econimise_tv);
        addSoldeObjectif = (AutoCompleteTextView) findViewById(R.id.solde_ciblet_tv);
        note = (AutoCompleteTextView) findViewById(R.id.add_note_objectif);
        mIconName = (Button) findViewById(R.id.bt_icon_objectifs);
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_note);
        addObjectif = (Button) findViewById(R.id.bt_add_objectif_personnel);

        //Button ajouter objectifs
        addObjectif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setError(nomObjectif, addSoldeDepart, addSoldeObjectif, note, mDisplayDate);
                if (nomObjectif.getText().toString().equals("")) {
                    nomObjectif.setError("invalide nom");
                    nomObjectif.requestFocus();
                } else if (linearLayout.getVisibility() == View.VISIBLE) {
                    if (note.getText().toString().equals("")) {
                        note.setError("invalide note");
                        note.requestFocus();
                    } else if (addSoldeDepart.getText().toString().equals("")) {
                        addSoldeDepart.setError("invalide solde");
                        addSoldeDepart.requestFocus();
                    } else if (!TextUtils.isDigitsOnly(addSoldeDepart.getText())) {
                        addSoldeDepart.setError("invalide valeur");
                        addSoldeDepart.requestFocus();

                    } else if (addSoldeObjectif.getText().toString().equals("")) {
                        addSoldeObjectif.setError("invalide solde");
                        addSoldeObjectif.requestFocus();
                    } else if (Integer.valueOf(addSoldeObjectif.getText().toString()) < Integer.valueOf(addSoldeDepart.getText().toString())) {
                        Toast.makeText(AddObjectiveActivity.this, "Mantant cibler infarieur au mantant sauvgarder !!", Toast.LENGTH_LONG).show();

                    } else if (!TextUtils.isDigitsOnly(addSoldeObjectif.getText())) {
                        addSoldeObjectif.setError("invalide valeur");
                        addSoldeObjectif.requestFocus();

                    } else if (mDisplayDate.getText().toString().equals("Date souhaitée")) {
                        mDisplayDate.setError("invalide date");
                        mDisplayDate.requestFocus();
                        Toast.makeText(AddObjectiveActivity.this, "Choisir un date s'il vous plait !!", Toast.LENGTH_LONG).show();

                    } else {

                        Realm.init(getApplicationContext());
                        realm = Realm.getDefaultInstance();

                        ObjectifModel e = new ObjectifModel();
                        e.setType("Actif");
                        e.setObjectifPriceCurrency(mSpinner.getSelectedItem().toString());
                        e.setName(note.getText().toString());
                        String value = addSoldeDepart.getText().toString();
                        Float finalValue = Float.parseFloat(value);
                        e.setStartingBalance(finalValue);
                        String value1 = addSoldeObjectif.getText().toString();
                        Float finalValue1 = Float.parseFloat(value1);
                        e.setFinalBalance(finalValue1);
                        e.setStartingDate(mDisplayDate.getText().toString());
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            e.setDate1(sdf.parse(mDisplayDate.getText().toString()));
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                        if (mIconName.getBackground().getConstantState() == getResources().getDrawable(R.drawable.voiture).getConstantState()) {
                            e.setIcon(R.drawable.voiture);
                        } else if (mIconName.getBackground().getConstantState() == getResources().getDrawable(R.drawable.house).getConstantState()) {
                            e.setIcon(R.drawable.house);
                        } else if (mIconName.getBackground().getConstantState() == getResources().getDrawable(R.drawable.voyage).getConstantState()) {
                            e.setIcon(R.drawable.voyage);
                        } else if (mIconName.getBackground().getConstantState() == getResources().getDrawable(R.drawable.formation).getConstantState()) {
                            e.setIcon(R.drawable.formation);
                        } else if (mIconName.getBackground().getConstantState() == getResources().getDrawable(R.drawable.cadeau).getConstantState()) {
                            e.setIcon(R.drawable.cadeau);
                        } else if (mIconName.getBackground().getConstantState() == getResources().getDrawable(R.drawable.autre).getConstantState()) {
                            e.setIcon(R.drawable.autre);
                        }

                        ObjectiveController evenmentController = new ObjectiveController(realm);
                        objectifModelArrayList = evenmentController.getAllObjectif();
                        if (objectifModelArrayList.size() == 0) {
                            e.setCodeObjectif(1);
                        } else {
                            e.setCodeObjectif(objectifModelArrayList.get(objectifModelArrayList.size() - 1).getCodeObjectif() + 1);
                        }
                        evenmentController.insertObjectif(e);

                        Toast.makeText(AddObjectiveActivity.this, "Ajouter success !!", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(AddObjectiveActivity.this, MainActivity.class);
                        intent.putExtra(Keys.KEYTAB, 2);
                        startActivity(intent);
                    }

                } else if (addSoldeDepart.getText().toString().equals("")) {
                    addSoldeDepart.setError("invalide solde");
                    addSoldeDepart.requestFocus();
                } else if (!TextUtils.isDigitsOnly(addSoldeDepart.getText())) {
                    addSoldeDepart.setError("invalide valeur");
                    addSoldeDepart.requestFocus();

                } else if (addSoldeObjectif.getText().toString().equals("")) {
                    addSoldeObjectif.setError("invalide solde");
                    addSoldeObjectif.requestFocus();
                } else if (Integer.valueOf(addSoldeObjectif.getText().toString()) < Integer.valueOf(addSoldeDepart.getText().toString())) {
                    Toast.makeText(AddObjectiveActivity.this, "Mantant cibler infarieur au mantant sauvgarder !!", Toast.LENGTH_LONG).show();

                } else if (!TextUtils.isDigitsOnly(addSoldeObjectif.getText())) {
                    addSoldeObjectif.setError("invalide valeur");
                    addSoldeObjectif.requestFocus();

                } else if (mDisplayDate.getText().toString().equals("Date souhaitée")) {
                    mDisplayDate.setError("invalide date");
                    mDisplayDate.requestFocus();
                    Toast.makeText(AddObjectiveActivity.this, "Choisir un date s'il vous plait !!", Toast.LENGTH_LONG).show();

                } else {

                    Realm.init(getApplicationContext());
                    realm = Realm.getDefaultInstance();
                    setObjectif(nomObjectif, mSpinner, addSoldeDepart, addSoldeObjectif, mDisplayDate, mIconName);
                    finalToast();
                }

            }
        });

        // Dialogue icon objectifs
        mIconName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustemAlertDiaalog();
            }
        });

        nomObjectif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustemAlertDiaalog();
            }
        });

        //Date objectifs
        mDisplayDate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddObjectiveActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = day + "/" + "0" + month + "/" + year;
                mDisplayDate.setText(date);
            }
        };

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            Intent intent = new Intent(AddObjectiveActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void MyCustemAlertDiaalog() {
        myDialog = new Dialog(AddObjectiveActivity.this);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.dialogue_add_objectif);
        myDialog.setTitle("Choisir un objectif");

        nomObjectif = (AutoCompleteTextView) findViewById(R.id.add_nom_objectif);
        radioGroup = (RadioGroup) myDialog.findViewById(R.id.icone_objectifs_gb);
        mApply = (Button) myDialog.findViewById(R.id.appliquer_objectif_icon_bt);
        mCancel = (Button) myDialog.findViewById(R.id.annuler_objectif_icon_bt);
        final Button icon = (Button) findViewById(R.id.bt_icon_objectifs);
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_note);

        mApply.setEnabled(true);
        mCancel.setEnabled(true);

        mApply.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                if (checkedRadioButtonId == -1) {

                    Toast.makeText(AddObjectiveActivity.this, "No item selected !!", Toast.LENGTH_LONG).show();

                } else if (checkedRadioButtonId == R.id.nv_voiture_rb) {
                    icon.setBackgroundResource(R.drawable.voiture);
                    linearLayout.setVisibility(View.GONE);

                    nomObjectif.setText("Nouvelle véhicule");
                    myDialog.cancel();
                } else if (checkedRadioButtonId == R.id.nv_maison_rb) {
                    icon.setBackgroundResource(R.drawable.house);
                    nomObjectif.setText("Nouvelle maison");
                    linearLayout.setVisibility(View.GONE);

                    myDialog.cancel();
                } else if (checkedRadioButtonId == R.id.voyage_rb) {
                    icon.setBackgroundResource(R.drawable.voyage);
                    nomObjectif.setText("Voyage");
                    linearLayout.setVisibility(View.GONE);

                    myDialog.cancel();
                } else if (checkedRadioButtonId == R.id.formation_rb) {
                    icon.setBackgroundResource(R.drawable.formation);
                    nomObjectif.setText("Formation");
                    myDialog.cancel();
                    linearLayout.setVisibility(View.GONE);

                } else if (checkedRadioButtonId == R.id.cadeau_rb) {
                    icon.setBackgroundResource(R.drawable.cadeau);
                    nomObjectif.setText("Cadeau");
                    linearLayout.setVisibility(View.GONE);
                    myDialog.cancel();
                } else if (checkedRadioButtonId == R.id.autre_rb) {
                    icon.setBackgroundResource(R.drawable.autre);
                    nomObjectif.setText("Autre");
                    linearLayout.setVisibility(View.VISIBLE);
                    myDialog.cancel();
                }

            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myDialog.cancel();

            }
        });
        nomObjectif.setError(null);
        myDialog.show();

    }

    private void setError(AutoCompleteTextView nomObjectif,
                          AutoCompleteTextView addSoldeDepart,
                          AutoCompleteTextView addSoldeObjectif,
                          AutoCompleteTextView note,
                          TextView mDisplayDate) {
        nomObjectif.setError(null);
        addSoldeDepart.setError(null);
        addSoldeObjectif.setError(null);
        note.setError(null);
        mDisplayDate.setError(null);
    }

    private void setObjectif(AutoCompleteTextView nomObjectif, Spinner spinner,
                             AutoCompleteTextView addSoldeDepart,
                             AutoCompleteTextView addSoldeObjectif,
                             TextView mDisplayDate,
                             Button mIconName) {
        ObjectifModel e = new ObjectifModel();
        e.setType("Actif");
        e.setName(nomObjectif.getText().toString());
        e.setObjectifPriceCurrency(spinner.getSelectedItem().toString());
        String value = addSoldeDepart.getText().toString();
        Float finalValue = Float.parseFloat(value);
        e.setStartingBalance(finalValue);
        String value1 = addSoldeObjectif.getText().toString();
        Float finalValue1 = Float.parseFloat(value1);
        e.setFinalBalance(finalValue1);
        e.setStartingDate(mDisplayDate.getText().toString());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            e.setDate1(sdf.parse(mDisplayDate.getText().toString()));
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        try {
            e.setDate1(sdf.parse(mDisplayDate.getText().toString()));
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        if (mIconName.getBackground().getConstantState() == getResources().getDrawable(R.drawable.voiture).getConstantState()) {
            e.setIcon(R.drawable.voiture);
        } else if (mIconName.getBackground().getConstantState() == getResources().getDrawable(R.drawable.house).getConstantState()) {
            e.setIcon(R.drawable.house);
        } else if (mIconName.getBackground().getConstantState() == getResources().getDrawable(R.drawable.voyage).getConstantState()) {
            e.setIcon(R.drawable.voyage);
        } else if (mIconName.getBackground().getConstantState() == getResources().getDrawable(R.drawable.formation).getConstantState()) {
            e.setIcon(R.drawable.formation);
        } else if (mIconName.getBackground().getConstantState() == getResources().getDrawable(R.drawable.cadeau).getConstantState()) {
            e.setIcon(R.drawable.cadeau);
        } else if (mIconName.getBackground().getConstantState() == getResources().getDrawable(R.drawable.autre).getConstantState()) {
            e.setIcon(R.drawable.autre);
        }
        ObjectiveController evenmentController = new ObjectiveController(realm);
        objectifModelArrayList = evenmentController.getAllObjectif();
        if (objectifModelArrayList.size() == 0) {
            e.setCodeObjectif(1);
        } else {
            e.setCodeObjectif(objectifModelArrayList.get(objectifModelArrayList.size() - 1).getCodeObjectif() + 1);
        }
        evenmentController.insertObjectif(e);
    }

    private void finalToast() {
        Toast.makeText(AddObjectiveActivity.this, "Ajouter success !!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(AddObjectiveActivity.this, MainActivity.class);
        intent.putExtra(Keys.KEYTAB, 2);
        startActivity(intent);
    }
}