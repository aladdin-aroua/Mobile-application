package projet.uqam.mobileproject.Views.evenements;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.realm.Realm;
import projet.uqam.mobileproject.Controllers.EvenementController;
import projet.uqam.mobileproject.Models.EvenementModel;
import projet.uqam.mobileproject.R;

public class EventsFragment extends Fragment {

    public EventsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_evenements, container, false);
        // Inflate the layout for this fragment
        Realm.init(getActivity());
        Realm realm = Realm.getDefaultInstance();

        TextView textView = v.findViewById(R.id.event_vide);
        EvenementController evenmentController = new EvenementController(realm);
        ArrayList<EvenementModel> evenmentModels = evenmentController.getAllEvenment();
        final ScrollView scrollView = (ScrollView) v.findViewById(R.id.scroll_v1);
        FloatingActionButton menuEvenements = (FloatingActionButton) v.findViewById(R.id.add_events);

        // si la liste des événements est vide
        eventVisibility(evenmentModels, textView);
        treeEventListe(evenmentModels);
        scrollViewFocusUp(scrollView);
        recycleViewParameter(v, evenmentModels);


        menuEvenements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddEventActivity.class);
                startActivity(intent);

            }
        });

        return v;
    }

    private void eventVisibility(ArrayList<EvenementModel> evenmentModels, TextView textView) {
        if (evenmentModels.size() == 0) {

            textView.setVisibility(View.VISIBLE);

        } else {
            textView.setVisibility(View.GONE);
        }

    }

    private void treeEventListe(ArrayList<EvenementModel> evenmentModels) {
        Collections.sort(evenmentModels, new Comparator<EvenementModel>() {
            public int compare(EvenementModel o1, EvenementModel o2) {
                return o1.getDate1().compareTo(o2.getDate1());
            }
        });
    }

    private void scrollViewFocusUp(final ScrollView scrollView) {
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Ready, move up
                scrollView.fullScroll(View.FOCUS_UP);
            }
        });
    }

    private void recycleViewParameter(View v, ArrayList<EvenementModel> evenmentModels) {
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.RV_evenements_personnel);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView.Adapter adapter = new EventRCAdapter(evenmentModels, getActivity());
        recyclerView.setAdapter(adapter);
    }

}