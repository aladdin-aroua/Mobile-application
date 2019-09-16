package projet.uqam.mobileproject.Views.authentification;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;
import projet.uqam.mobileproject.Controllers.UtilisateurController;
import projet.uqam.mobileproject.Models.UtilisateurModel;
import projet.uqam.mobileproject.R;
import projet.uqam.mobileproject.Views.transactions.Keys;
import projet.uqam.mobileproject.Views.transactions.MainActivity;

// LogInActivity c'est la classe relative a l'authentification

public class LogInActivity extends AppCompatActivity {
    private LinearLayout mLinearLayout1;
    private LinearLayout mLinearLayout2;
    private Animation mUpToDownAnimation;
    private Animation mDownToUpAnimation;
    private TextView mRegistering;
    private Button mConnect;
    private ProgressBar mProgressBar;
    private AutoCompleteTextView mE_mail;
    private EditText mPassword;
    private ArrayList<UtilisateurModel> mUtilisateurModelArrayList;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        mLinearLayout1 = findViewById(R.id.email_login_form);
        mLinearLayout2 = findViewById(R.id.layout_animation);
        mProgressBar = findViewById(R.id.login_progress);
        mRegistering = findViewById(R.id.enregistrement);
        mConnect = findViewById(R.id.email_sign_in_button);
        mE_mail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mE_mail.setError(null);
                mPassword.setError(null);
                if (mE_mail.getText().toString().equals("")) {
                    mE_mail.setError("invalide e_mail");
                    mE_mail.requestFocus();
                } else if (mPassword.getText().toString().equals("")) {
                    mPassword.setError("invalide mot de passe");
                    mPassword.requestFocus();
                } else {
                    Realm.init(getApplicationContext());
                    realm = Realm.getDefaultInstance();
                    UtilisateurController uc = new UtilisateurController(realm);
                    mUtilisateurModelArrayList = uc.getAllUser();
                    int k = 0;
                    for (int i = 0; i < mUtilisateurModelArrayList.size(); i++) {
                        if (mE_mail.getText().toString().equals(mUtilisateurModelArrayList.get(i).getmE_mail())
                                && mUtilisateurModelArrayList.get(i).getmPassword().equals(mPassword.getText().toString())) {
                            k = i + 1;
                        }
                    }
                    if (k == 0) {
                        Toast.makeText(LogInActivity.this, "E-mail ou Mot de passe invalide !!", Toast.LENGTH_LONG).show();
                    } else {


                        mLinearLayout1.setVisibility(View.INVISIBLE);
                        mLinearLayout2.setVisibility(View.INVISIBLE);
                        mLinearLayout1.postDelayed(new Runnable() {
                                                       public void run() {
                                                           mLinearLayout1.setVisibility(View.VISIBLE);
                                                           mLinearLayout2.setVisibility(View.VISIBLE);
                                                       }
                                                   },
                                5000);

                        mProgressBar.setVisibility(View.VISIBLE);
                        mProgressBar.postDelayed(new Runnable() {
                                                     public void run() {
                                                         mProgressBar.setVisibility(View.GONE);
                                                     }
                                                 },
                                5000);

                        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                        intent.putExtra(Keys.UTNAME, mUtilisateurModelArrayList.get(k - 1).getmName());
                        intent.putExtra(Keys.UTPRENOM, mUtilisateurModelArrayList.get(k - 1).getmFirstName());
                        intent.putExtra(Keys.UTID, mUtilisateurModelArrayList.get(k - 1).getId());
                        startActivity(intent);

                    }
                }
            }

        });

        setSupportActionBar(toolbar);
        //hiding action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //Animation
        mUpToDownAnimation = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        mDownToUpAnimation = AnimationUtils.loadAnimation(this, R.anim.downtoup);
        mLinearLayout1.setAnimation(mUpToDownAnimation);
        mLinearLayout2.setAnimation(mDownToUpAnimation);

        mRegistering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInActivity.this, RegisteringActivity.class);
                startActivity(intent);
            }
        });

    }

}