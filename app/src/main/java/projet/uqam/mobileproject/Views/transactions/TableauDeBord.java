package projet.uqam.mobileproject.Views.transactions;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import projet.uqam.mobileproject.Views.evenements.EventsFragment;
import projet.uqam.mobileproject.Views.objectifs.ObjectiveFragments;

class TableauDeBord extends FragmentStatePagerAdapter {

    String[] Titles = new String[] {
            "Portefeuille",
            "Evenement",
            "objectifs"
    };

    public TableauDeBord(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TransactionsFragment transactionsFragment = new TransactionsFragment();
                return transactionsFragment;

            case 1:
                EventsFragment evenementPersonnel = new EventsFragment();
                return evenementPersonnel;
            case 2:
                ObjectiveFragments objectiveFragments = new ObjectiveFragments();
                return objectiveFragments;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }
}