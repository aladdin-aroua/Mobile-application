package projet.uqam.mobileproject.Views.authentification;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Pattern;

import io.realm.Realm;
import projet.uqam.mobileproject.Controllers.UtilisateurController;
import projet.uqam.mobileproject.Models.UtilisateurModel;
import projet.uqam.mobileproject.R;

public class RegisteringActivity extends AppCompatActivity {

    private TextView mConnexion;
    private LinearLayout mLinearLayout;
    private ImageView mImageView;
    private Animation upToDownAnimation;
    private Animation downToUpAnimation;
    private Button creation;
    private EditText mName;
    private EditText mPrenom;
    private EditText e_mail;
    private EditText mPassword;
    private ArrayList<UtilisateurModel> mUserModelArrayList;
    PasswordValidator passwordvalidator = new PasswordValidator();
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enregistrement);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mConnexion = findViewById(R.id.seConncecter);
        mLinearLayout = findViewById(R.id.enregistrement_layout);
        mImageView = findViewById(R.id.img_logo);
        creation = findViewById(R.id.création);
        mName = findViewById(R.id.enregistrementNom);
        mPrenom = findViewById(R.id.enregistrementPrénom);
        e_mail = findViewById(R.id.enregistrementEmail);
        mPassword = findViewById(R.id.enregistrementPassword);

        //animation
        setAnimation(mLinearLayout, mImageView);


        //creation d'un compte
        creation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setError(mName, mPrenom, e_mail, mPassword);

                if (mName.getText().toString().equals("")) {
                    mName.setError("invalide nom");
                    mName.requestFocus();
                } else if (mPrenom.getText().toString().equals("")) {
                    mPrenom.setError("invalide prénom");
                    mPrenom.requestFocus();
                } else if (!validateemail(e_mail.getText().toString())) {
                    e_mail.setError("invalide e_mail");
                    e_mail.requestFocus();
                } else if (!isValidPassword( mPassword.getText().toString())) {
                    mPassword.setError("Minimum de 8 caractères\n" +
                            "Au moins 1 chiffre,\n"+" 1 caractère en majuscule,\n"+" 1 caractère spécial.");
                    mPassword.requestFocus();
                } else {
                    Realm.init(getApplicationContext());
                    realm = Realm.getDefaultInstance();

                    createUser(mName, mPrenom, e_mail, mPassword,
                            mUserModelArrayList);

                }

            }
        });

        mConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisteringActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });

    }

    public void setAnimation(LinearLayout linearLayout, ImageView imageView) {
        upToDownAnimation = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        downToUpAnimation = AnimationUtils.loadAnimation(this, R.anim.downtoup);
        imageView.setAnimation(upToDownAnimation);
        linearLayout.setAnimation(downToUpAnimation);

    }

    public void setError(EditText mName, EditText mPrenom, EditText e_mail, EditText mPassword) {

        mName.setError(null);
        mPrenom.setError(null);
        e_mail.setError(null);
        mPassword.setError(null);

    }

    public void createUser(EditText mName, EditText mPrenom, EditText e_mail, EditText mPassword,
                           ArrayList<UtilisateurModel> mUserModelArrayList) {

        UtilisateurModel u = new UtilisateurModel();
        u.setmName(mName.getText().toString());
        u.setmFirstName(mPrenom.getText().toString());
        u.setmE_mail(e_mail.getText().toString());
        u.setmPassword(mPassword.getText().toString());
        UtilisateurController uc = new UtilisateurController(realm);
        mUserModelArrayList = uc.getAllUser();

        if (mUserModelArrayList.size() == 0) {
            u.setId(1);
        } else {
            u.setId(mUserModelArrayList.get(mUserModelArrayList.size() - 1).getId() + 1);
        }
        uc.insertUtilisateur(u, RegisteringActivity.this);
    }

    public boolean validateemail(String email) {

        return EmailValidator.isValidEmail(email);
    }

    public  boolean isValidPassword(String s) {
       return passwordvalidator.validate(s);
    }

}