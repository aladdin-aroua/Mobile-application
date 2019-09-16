package projet.uqam.mobileproject.Views.evenements;

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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import projet.uqam.mobileproject.Controllers.EvenementController;
import projet.uqam.mobileproject.Models.EvenementModel;
import projet.uqam.mobileproject.R;
import projet.uqam.mobileproject.Views.transactions.Keys;
import projet.uqam.mobileproject.Views.transactions.MainActivity;

public class AddEventActivity extends AppCompatActivity {

    private TextView mdateEvent;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private static final String TAG = "AddEventActivity";
    private Dialog myDialog;
    private TextView mTextViewIcon;
    private RadioGroup mRadioGroup;
    private Button mCancel;
    private Button mApply;
    private Spinner mSpinner;
    private ArrayAdapter<CharSequence> mAdapter;
    private Button mAddEvents;
    private AutoCompleteTextView mName;
    private AutoCompleteTextView mBudget;
    private TextView mDisplayDate;
    private Realm mRealm;
    private ArrayList<EvenementModel> mEventModelArrayList;
    private Button icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_evenement);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mSpinner = (Spinner) findViewById(R.id.dv_evenement);
        mAdapter = ArrayAdapter.createFromResource(this, R.array.objectifPriceCurrency, android.R.layout.simple_spinner_item);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mAdapter);

        mdateEvent = (TextView) findViewById(R.id.calandrier_evenementt);
        mTextViewIcon = (TextView) findViewById(R.id.tv_icon);
        mAddEvents = (Button) findViewById(R.id.bt_add_evenement_personnel);
        mName = (AutoCompleteTextView) findViewById(R.id.add_nom_d_evenement);
        mBudget = (AutoCompleteTextView) findViewById(R.id.add_budget);
        mDisplayDate = (TextView) findViewById(R.id.calandrier_evenementt);
        icon = (Button) findViewById(R.id.bt_icon_nationnal);

        mAddEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setErreur(mName, mBudget, mTextViewIcon, mDisplayDate);

                if (mName.getText().toString().equals("")) {
                    mName.setError("invalide name");
                    mName.requestFocus();
                } else if (mBudget.getText().toString().equals("")) {
                    mBudget.setError("invalide prix");
                    mBudget.requestFocus();
                } else if (!TextUtils.isDigitsOnly(mBudget.getText())) {
                    mBudget.setError("invalide valeur");
                    mBudget.requestFocus();
                } else if (mDisplayDate.getText().toString().equals("Date souhaitée")) {
                    mDisplayDate.setError("invalide date");
                    mDisplayDate.requestFocus();
                    Toast.makeText(AddEventActivity.this, "Choisir un date s'il vous plait !!", Toast.LENGTH_LONG).show();

                } else if (mTextViewIcon.getText().toString().equals("Choisir votre icon")) {
                    mTextViewIcon.setError("invalide valeur");
                    mTextViewIcon.requestFocus();
                    Toast.makeText(AddEventActivity.this, "Choisir une icone s'il vous plait !!", Toast.LENGTH_LONG).show();
                } else {

                    Realm.init(getApplicationContext());
                    mRealm = Realm.getDefaultInstance();

                    setEvent(mName, mSpinner, mBudget, mDisplayDate, mRealm, mEventModelArrayList, icon);

                    finalToast();
                }
                ;
            }
        });

        //Dialog icon event
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustemAlertDiaalog();
            }
        });
        mTextViewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustemAlertDiaalog();
            }
        });

        mdateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddEventActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = day + "/" + "0" + month + "/" + year;
                mdateEvent.setText(date);
            }
        };

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            Intent intent = new Intent(AddEventActivity.this, MainActivity.class);
            intent.putExtra("KEYTAB", 1);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void MyCustemAlertDiaalog() {
        myDialog = new Dialog(AddEventActivity.this);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.dialoge_icon);
        myDialog.setTitle("Choisir une icone");

        mTextViewIcon = (TextView) findViewById(R.id.tv_icon);
        mRadioGroup = (RadioGroup) myDialog.findViewById(R.id.icone_gb);
        mApply = (Button) myDialog.findViewById(R.id.appliquer_icon_bt);
        mCancel = (Button) myDialog.findViewById(R.id.annuler_icon_bt);
        final Button icon = (Button) findViewById(R.id.bt_icon_nationnal);

        mApply.setEnabled(true);
        mCancel.setEnabled(true);

        mApply.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                int checkedRadioButtonId = mRadioGroup.getCheckedRadioButtonId();
                if (checkedRadioButtonId == -1) {

                    Toast.makeText(AddEventActivity.this, "No item selected !!", Toast.LENGTH_LONG).show();

                } else if (checkedRadioButtonId == R.id.anniversaire_rb) {
                    icon.setBackgroundResource(R.drawable.aniversaire);
                    mTextViewIcon.setText("Anniversaire");
                    myDialog.cancel();
                } else if (checkedRadioButtonId == R.id.mariage_rb) {
                    icon.setBackgroundResource(R.drawable.mariage);
                    mTextViewIcon.setText("mariage");
                    myDialog.cancel();
                } else if (checkedRadioButtonId == R.id.reunion_rb) {
                    icon.setBackgroundResource(R.drawable.reunion);
                    mTextViewIcon.setText("Réunion");
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
        mTextViewIcon.setError(null);
        myDialog.show();

    }

    private void setErreur(AutoCompleteTextView mName, AutoCompleteTextView mBudget, TextView mTextViewIcon,
                          TextView mDisplayDate) {
        mName.setError(null);
        mBudget.setError(null);
        mDisplayDate.setError(null);
        mTextViewIcon.setError(null);
    }

    private void finalToast() {
        Toast.makeText(AddEventActivity.this, "Ajouter success !!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(AddEventActivity.this, MainActivity.class);
        intent.putExtra(Keys.KEYTAB, 1);
        startActivity(intent);

    }

    private void setEvent(AutoCompleteTextView mName, Spinner mSpinner, AutoCompleteTextView mBudget,
                         TextView mDisplayDate, Realm mRealm, ArrayList<EvenementModel> mEventModelArrayList, Button icon) {
        EvenementModel e = new EvenementModel();
        e.setName(mName.getText().toString());
        e.setEventPriceCurrency(mSpinner.getSelectedItem().toString());
        String value = mBudget.getText().toString();
        Float finalValue = Float.parseFloat(value);
        e.setBudjet(finalValue);
        e.setDate(mDisplayDate.getText().toString());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            e.setDate1(sdf.parse(mDisplayDate.getText().toString()));
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        if (icon.getBackground().getConstantState() == getResources().getDrawable(R.drawable.aniversaire).getConstantState()) {
            e.setIcon(R.drawable.aniversaire);
        } else if (icon.getBackground().getConstantState() == getResources().getDrawable(R.drawable.mariage).getConstantState()) {
            e.setIcon(R.drawable.mariage);
        } else if (icon.getBackground().getConstantState() == getResources().getDrawable(R.drawable.reunion).getConstantState()) {
            e.setIcon(R.drawable.reunion);
        }

        EvenementController evenmentController = new EvenementController(mRealm);
        mEventModelArrayList = evenmentController.getAllEvenment();
        if (mEventModelArrayList.size() == 0) {
            e.setCodeEvent(1);
        } else {
            e.setCodeEvent(mEventModelArrayList.get(mEventModelArrayList.size() - 1).getCodeEvent() + 1);
        }
        evenmentController.insertEvenment(e);
    }

}