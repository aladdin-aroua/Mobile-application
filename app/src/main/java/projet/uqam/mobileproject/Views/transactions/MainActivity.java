package projet.uqam.mobileproject.Views.transactions;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import projet.uqam.mobileproject.R;
import projet.uqam.mobileproject.Views.authentification.LogInActivity;

//MainActivity C'EST LA CLASSE PRINCIPALLE DE NOTRE PROJET QU'ELLE CONTIENT LES TROIS FRAGMENT QUI SONT transactionzFragment,ObjectifsFragment
//et EvenementsFragments
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager mViewPager;
    private TableauDeBord mSectionsPagerAdapter;
    private TextView nom;
    private TextView prenom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar23);
        setSupportActionBar(toolbar);




        mSectionsPagerAdapter = new TableauDeBord(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container1);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs1);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        nom=headerView.findViewById(R.id.nom_nav);
        prenom=headerView.findViewById(R.id.prenom_nav);
        if (getIntent().hasExtra(Keys.KEYTAB)){
            int index = getIntent().getExtras().getInt(Keys.KEYTAB);
            TabLayout tabhost = (TabLayout) findViewById(R.id.tabs1);
            tabhost.getTabAt(index).select();
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("Really Exit?").setMessage("Are you sure you want to exit?").setNegativeButton(android.R.string.no, null).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface arg0, int arg1) {
                MainActivity.super.onBackPressed();
                Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        }).create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_portfeuille) {
            TabLayout tabhost = (TabLayout) findViewById(R.id.tabs1);
            tabhost.getTabAt(0).select();
        }
        else if (id == R.id.nav_evenement) {

            TabLayout tabhost = (TabLayout) findViewById(R.id.tabs1);
            tabhost.getTabAt(1).select();

        } else if (id == R.id.nav_objectif) {

            TabLayout tabhost = (TabLayout) findViewById(R.id.tabs1);
            tabhost.getTabAt(2).select();

        } else if (id == R.id.nav_version) {

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            LinearLayout layout = new LinearLayout(MainActivity.this);
            layout.setOrientation(LinearLayout.VERTICAL);

            builder.setMessage("This version is 1.0.0")

                    .setNegativeButton("Done", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });

            builder.show();

        } else if (id == R.id.nav_deconexion) {

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            LinearLayout layout = new LinearLayout(MainActivity.this);
            layout.setOrientation(LinearLayout.VERTICAL);

            builder.setMessage("Are you sure that you want log out ?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                    startActivity(intent);

                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    dialog.dismiss();
                }
            });

            builder.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}