package projet.uqam.mobileproject.Views.evenements;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import projet.uqam.mobileproject.Controllers.EvenementController;
import projet.uqam.mobileproject.Models.EvenementModel;
import projet.uqam.mobileproject.R;

public class EventRCAdapter extends RecyclerView.Adapter<EventRCAdapter.ViewHolder> {
    private List<EvenementModel> evenmentModelList;
    private Context context;
    Realm realm;

    public EventRCAdapter(List<EvenementModel> evenmentModelList, Context context) {
        this.evenmentModelList = evenmentModelList;
        this.context = context;
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public EventRCAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_evenements, parent, false);

        return new EventRCAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EventRCAdapter.ViewHolder holder, final int position) {

        final EvenementModel evenmentModel = evenmentModelList.get(position);
        final EvenementController evenmentController = new EvenementController(realm);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final EditText edittext = new EditText(context);
                builder.setView(edittext);

                builder.setMessage("Entrer le nouveau budjet s'il vous plaît").setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (edittext.getText().toString().equals("")) {
                            Toast.makeText(context, "invalide solde", Toast.LENGTH_LONG).show();

                        } else if (!TextUtils.isDigitsOnly(edittext.getText())) {
                            Toast.makeText(context, "invalide valeur", Toast.LENGTH_LONG).show();

                        } else {
                            String editTextValue = edittext.getText().toString();
                            Float finalValue = Float.parseFloat(editTextValue);
                            updateBudget(evenmentModel, finalValue, position, dialog);
                        }
                    }

                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                builder.show();

            }
        });

        holder.textViewNom.setText(evenmentModel.getName());
        holder.textViewSolde.setText(evenmentModel.getBudjet().intValue() + " " + evenmentModel.getEventPriceCurrency());
        holder.textViewSolde.setTextColor(Color.parseColor("#e2b628"));
        holder.textViewDate.setText(evenmentModel.getDate());
        holder.imageViewIcon.setImageResource(evenmentModel.getIcon());
        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                Log.e("test", evenmentModel.getCodeEvent() + "");
                builder.setMessage("Voulez-vous supprimer cet événement ?").setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        deleteFromDB(evenmentModelList, evenmentModel, position, dialog);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                builder.show();

            }
        });

    }

    private void deleteFromDB(final List<EvenementModel> evenmentModelList, final EvenementModel evenmentModel, final int position, DialogInterface dialog) {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Log.e("test2", evenmentModel.getCodeEvent() + "");

                RealmResults<EvenementModel> result = realm.where(EvenementModel.class).equalTo("mCodeEvent", evenmentModelList.get(position).getCodeEvent()).findAll();
                result.deleteAllFromRealm();
            }
        });

        evenmentModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, evenmentModelList.size());
        dialog.dismiss();

    }

    private void updateBudget(EvenementModel evenmentModel, Float finalValue, int position, DialogInterface dialog) {
        realm.beginTransaction();
        evenmentModel.setBudjet(finalValue);
        realm.commitTransaction();
        notifyItemRangeChanged(position, evenmentModelList.size());
        dialog.dismiss();
        Toast.makeText(context, "le nouveau budjet est " + finalValue + " " +
                evenmentModel.getEventPriceCurrency(), Toast.LENGTH_LONG).show();
    }

    @Override
    public int getItemCount() {
        return evenmentModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewNom;
        public TextView textViewDate;
        public TextView textViewSolde;
        public ImageView imageViewIcon;
        public ImageView imageViewDelete;
        public CardView cardView;

        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linear_layout_evenement);
            textViewNom = (TextView) itemView.findViewById(R.id.nom_evenement);
            textViewDate = (TextView) itemView.findViewById(R.id.date_evenement);
            textViewSolde = (TextView) itemView.findViewById(R.id.solde_evenement);
            imageViewIcon = (ImageView) itemView.findViewById(R.id.icon_evenement);
            imageViewDelete = (ImageView) itemView.findViewById(R.id.delete_evenement);
            cardView = (CardView) itemView.findViewById(R.id.update_evenement);

        }
    }

    void deleteItem(int index) {

    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.action_settings).setPositiveButton(R.string.action_settings, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                // FIRE ZE MISSILES!
            }
        }).setNegativeButton(R.string.action_settings, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                // User cancelled the dialog
            }
        });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}