package projet.uqam.mobileproject.Views.objectifs;

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

import io.realm.Realm;
import projet.uqam.mobileproject.Controllers.ObjectiveController;
import projet.uqam.mobileproject.Models.ObjectifModel;
import projet.uqam.mobileproject.R;

public class ObjectiveFragments extends Fragment {

    public ObjectiveFragments() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_objectifs_fragments, container, false);

        Realm.init(getActivity());
        Realm realm = Realm.getDefaultInstance();

        TextView textView = v.findViewById(R.id.objectif_vide);
        ObjectiveController objectifController = new ObjectiveController(realm);
        ArrayList < ObjectifModel > objectifModels = objectifController.getAllObjectif();
        ArrayList < ObjectifModel > objectifModels1 = new ArrayList < >();
        final ScrollView scrollView = (ScrollView) v.findViewById(R.id.scroll_v2);

        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {@Override
        public void onGlobalLayout() {
            // Ready, move up
            scrollView.fullScroll(View.FOCUS_UP);
        }
        });

        //si la liste des objectifs est vide
        if (objectifModels.size() == 0) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
        for (int i = 0; i < objectifModels.size(); i++) {

            if ((objectifModels.get(i).getStartingBalance() < objectifModels.get(i).getFinalBalance())) {
                objectifModels1.add(objectifModels.get(i));

            } else objectifModels.remove(objectifModels.get(i));

        }

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.RV_objectif_actif);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView.Adapter adapter = new AdapterRCObjective(objectifModels1, getActivity());
        recyclerView.setAdapter(adapter);

        FloatingActionButton menuObjectif = (FloatingActionButton) v.findViewById(R.id.add_objectifs);

        menuObjectif.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View view) {
            Intent intent = new Intent(getContext(), AddObjectiveActivity.class);
            startActivity(intent);

        }
        });

        return v;
    }

}