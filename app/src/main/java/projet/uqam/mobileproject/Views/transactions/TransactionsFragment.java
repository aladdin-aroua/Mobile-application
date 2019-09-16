package projet.uqam.mobileproject.Views.transactions;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

import io.realm.Realm;
import projet.uqam.mobileproject.Controllers.TransactionController;
import projet.uqam.mobileproject.Models.TransactionModel;
import projet.uqam.mobileproject.R;

public class TransactionsFragment extends Fragment {

    public TransactionsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_transactions, container, false);

        PieChart pieChart = (PieChart) v.findViewById(R.id.chart);

        Realm.init(getContext());
        Realm realm = Realm.getDefaultInstance();

        Button equilibre = (Button) v.findViewById(R.id.portfeuille_equilibre_bt);

        TextView isVide = (TextView) v.findViewById(R.id.transaction_vide);
        final ScrollView scrollView = (ScrollView) v.findViewById(R.id.scroll_protfeuille);
        FloatingActionButton addRecette = (FloatingActionButton) v.findViewById(R.id.add_portfeuille_recette);
        FloatingActionButton addDepense = (FloatingActionButton) v.findViewById(R.id.add_portfeuille_depense);
        TransactionController tc = new TransactionController(realm, getContext());
        ArrayList<TransactionModel> transactionArrayList = tc.getAllTransaction();

        pieChartVisibility(pieChart, isVide, transactionArrayList);

        addDepense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddSpentActivity.class);
                startActivity(intent);
            }
        });

        addRecette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddIncomeActivity.class);
                startActivity(intent);
            }
        });

        //recycleView parametrage
        setRVParameter(v, transactionArrayList);
        //Equelibre boutton
        setBalanceButon(transactionArrayList, equilibre);

        //pie chart setting
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(60f);

        setPieChart(pieChart, transactionArrayList);

        return v;
    }

    private void setBalanceButon(ArrayList<TransactionModel> transactionArrayList, Button equilibre) {
        Float soldeEq = 0.0f;
        for (int i = 0; i < transactionArrayList.size(); i++) {
            if (transactionArrayList.get(i).getType().equals("D")) {
                soldeEq -= transactionArrayList.get(i).getBalance();
            } else {
                soldeEq += transactionArrayList.get(i).getBalance();
            }

        }

        if (soldeEq > 0) {
            equilibre.setText("Equilibre \n" + soldeEq.toString() + " TND");
            equilibre.setBackgroundResource(R.drawable.rounded_green_button);
        } else if (soldeEq < 0) {
            equilibre.setText("Equilibre \n" + "(" + soldeEq.toString() + " TND)");
            equilibre.setBackgroundResource(R.drawable.rounded_red_bouton);
        } else {
            equilibre.setText("Equilibre \n" + soldeEq.toString() + " TND");
            equilibre.setBackgroundResource(R.drawable.rounded_creme_button);
        }
    }

    private void setRVParameter(View v, ArrayList<TransactionModel> transactionArrayList) {
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.RV_transaction);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerView.Adapter adapter = new AdapterRCTransaction(transactionArrayList, getContext());
        recyclerView.setAdapter(adapter);

    }

    private void setPieChart(PieChart pieChart, ArrayList<TransactionModel> transactionArrayList) {

        float soldeMaison = 0.0f;
        float soldeSanté = 0.0f;
        float soldeTransport = 0.0f;
        float soldeCommunication = 0.0f;
        float soldevétement = 0.0f;
        float soldeRéstaurants = 0.0f;
        float soldeAutre = 0.0f;
        for (int i = 0; i < transactionArrayList.size(); i++) {
            if (transactionArrayList.get(i).getCathégorie().equals("Maison")) {
                soldeMaison += transactionArrayList.get(i).getBalance();

            } else if (transactionArrayList.get(i).getCathégorie().equals("Santé")) {
                soldeSanté += transactionArrayList.get(i).getBalance();

            } else if (transactionArrayList.get(i).getCathégorie().equals("Réstaurant")) {
                soldeRéstaurants += transactionArrayList.get(i).getBalance();

            } else if (transactionArrayList.get(i).getCathégorie().equals("Communication")) {
                soldeCommunication += transactionArrayList.get(i).getBalance();

            } else if (transactionArrayList.get(i).getCathégorie().equals("Transports")) {
                soldeTransport += transactionArrayList.get(i).getBalance();

            } else if (transactionArrayList.get(i).getCathégorie().equals("Vétements")) {
                soldevétement += transactionArrayList.get(i).getBalance();

            } else if (transactionArrayList.get(i).getCathégorie().equals("Autre")) {
                soldeAutre += transactionArrayList.get(i).getBalance();

            }
        }

        final ArrayList<PieEntry> yvalue = new ArrayList<>();
        yvalue.add(new PieEntry(soldeMaison, "Maison"));
        yvalue.add(new PieEntry(soldeSanté, "Santé"));
        yvalue.add(new PieEntry(soldeTransport, "Transports"));
        yvalue.add(new PieEntry(soldeCommunication, "Communication"));
        yvalue.add(new PieEntry(soldevétement, "Vétements"));
        yvalue.add(new PieEntry(soldeRéstaurants, "Réstaurant"));
        yvalue.add(new PieEntry(soldeAutre, "Autre"));
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {

                Intent intent = new Intent(getActivity(), DetailDepance.class);
                intent.putExtra(Keys.CATNAME, ((PieEntry) e).getLabel());
                startActivity(intent);
            }

            @Override
            public void onNothingSelected() {

            }

        });

        pieChart.animateY(1000);
        pieChart.setDrawSliceText(false);
        pieChart.getLegend().setWordWrapEnabled(true);
        PieDataSet pieDataSet = new PieDataSet(yvalue, "");
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(6f);
        pieDataSet.setColors(new int[]{
                        R.color.red,
                        R.color.vert,
                        R.color.bleu_ciel,
                        R.color.bleu,
                        R.color.creme,
                        R.color.gris,
                        R.color.autre
                },
                getContext());
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.YELLOW);
        pieData.setValueFormatter(new PercentFormatter());
        pieChart.setData(pieData);
    }

    private void pieChartVisibility(PieChart pieChart, TextView isVide, ArrayList<TransactionModel> transactionArrayList) {
        if (transactionArrayList.size() == 0) {
            pieChart.setVisibility(View.INVISIBLE);
            isVide.setVisibility(View.VISIBLE);
        } else {
            pieChart.setVisibility(View.VISIBLE);
            isVide.setVisibility(View.GONE);
        }
    }
}