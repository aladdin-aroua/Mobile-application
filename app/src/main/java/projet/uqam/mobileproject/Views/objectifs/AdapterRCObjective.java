package projet.uqam.mobileproject.Views.objectifs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import projet.uqam.mobileproject.Models.ObjectifModel;
import projet.uqam.mobileproject.R;

public class AdapterRCObjective extends RecyclerView.Adapter<AdapterRCObjective.ViewHolder> {

    private List<ObjectifModel> objectifModelList;
    private Context context;
    Realm realm;
    Dialog myDialog;

    public AdapterRCObjective(List<ObjectifModel> objectifModelList, Context context) {
        this.objectifModelList = objectifModelList;
        this.context = context;
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public AdapterRCObjective.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_objectifs, parent, false);

        return new AdapterRCObjective.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AdapterRCObjective.ViewHolder holder, final int position) {

        final ObjectifModel objectifModel = objectifModelList.get(position);

        //Modifier le montant de l'objectif

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final EditText edittext = new EditText(context);
                builder.setView(edittext);

                builder.setMessage("Entrer le montant a ajouter s'il vous plaît").setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (edittext.getText().toString().equals("")) {
                            Toast.makeText(context, "invalide solde", Toast.LENGTH_LONG).show();

                        } else if (!TextUtils.isDigitsOnly(edittext.getText())) {
                            Toast.makeText(context, "invalide valeur", Toast.LENGTH_LONG).show();

                        } else {
                            String editTextValue = edittext.getText().toString();
                            Float finalValue = Float.parseFloat(editTextValue);
                            Float value = objectifModel.getStartingBalance() + finalValue;
                            setObjectivBudget(edittext, objectifModel, position, dialog, finalValue, value);
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

        holder.textViewNom.setText(objectifModel.getName());
        holder.textViewsoldeObjectif.setText("Objectif:\n" + objectifModel.getFinalBalance().intValue() + " " + objectifModel.getObjectifPriceCurrency());
        holder.getTextViewSoldeeconomise.setText("Economisé:\n" + objectifModel.getStartingBalance().intValue() + " " + objectifModel.getObjectifPriceCurrency());
        holder.imageViewIcon.setImageResource(objectifModel.getIcon());

        holder.progressBar.setProgress((int) ((objectifModel.getStartingBalance() / objectifModel.getFinalBalance()) * 100));

        //Supprimer un objectif
        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Voulez-vous supprimer cet objectif ?").setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteFromDBObjectic(objectifModelList, objectifModel, position, dialog);
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

    @Override
    public int getItemCount() {
        return objectifModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewNom;
        public TextView getTextViewSoldeeconomise;
        public TextView textViewsoldeObjectif;
        public TextView textViewSolde;
        public ImageView imageViewDelete;
        public ImageView imageViewUpdate;
        public ImageView imageViewIcon;
        public ProgressBar progressBar;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewNom = (TextView) itemView.findViewById(R.id.nom_objectif);
            getTextViewSoldeeconomise = (TextView) itemView.findViewById(R.id.solde_econimise);
            textViewsoldeObjectif = (TextView) itemView.findViewById(R.id.solde_objectif);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar_objectif);
            imageViewIcon = (ImageView) itemView.findViewById(R.id.icon_objectifs);
            imageViewDelete = (ImageView) itemView.findViewById(R.id.delete_objrctif);
            cardView = (CardView) itemView.findViewById(R.id.update_objectif);

            //   imageViewUpdate= (ImageView) itemView.findViewById(R.id.update_objrctif);

        }
    }

    private void setObjectivBudget(EditText edittext, ObjectifModel objectifModel,
                                   int position, DialogInterface dialog, Float finalValue, Float value) {

        if (objectifModel.getStartingBalance() + finalValue < objectifModel.getFinalBalance()) {
            objectifRemainingBudget(objectifModel, value, position, dialog);
        } else {
            finalObjectif(objectifModel, position, dialog);
        }
    }

    private void deleteFromDBObjectic(final List<ObjectifModel> objectifModelList, final ObjectifModel objectifModel, final int position, DialogInterface dialog) {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Log.e("test2", objectifModel.getCodeObjectif() + "");

                RealmResults<ObjectifModel> result = realm.where(ObjectifModel.class).equalTo("mCodeObjectif", objectifModelList.get(position).getCodeObjectif()).findAll();
                result.deleteAllFromRealm();
            }
        });
        objectifModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, objectifModelList.size());
        dialog.dismiss();

    }

    private void objectifRemainingBudget(ObjectifModel objectifModel, Float value, int position, DialogInterface dialog) {
        realm.beginTransaction();
        objectifModel.setStartingBalance(value);
        realm.commitTransaction();
        notifyItemRangeChanged(position, objectifModelList.size());
        dialog.dismiss();
        Toast.makeText(context, "il vous reste " + (int) (objectifModel.getFinalBalance() - value) +
                " " + objectifModel.getObjectifPriceCurrency() +
                " pour atteindre le mantant cibler", Toast.LENGTH_LONG).show();
    }

    public void finalObjectif(ObjectifModel objectifModel, int position, DialogInterface dialog) {

        realm.beginTransaction();
        objectifModel.setStartingBalance(objectifModel.getFinalBalance());
        realm.commitTransaction();
        notifyItemRangeChanged(position, objectifModelList.size());
        dialog.dismiss(); // do something
        Toast.makeText(context, "Bravo vous avez atteint le mantant cibler", Toast.LENGTH_LONG).show();
    }

}