package projet.uqam.mobileproject.Views.transactions;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import projet.uqam.mobileproject.Models.TransactionModel;
import projet.uqam.mobileproject.R;

public class AdapterRCTransaction extends RecyclerView.Adapter<AdapterRCTransaction.ViewHolder> {

    private List<TransactionModel> transactionList;
    private Context context;
    private Realm realm;

    public AdapterRCTransaction(List<TransactionModel> transactionList, Context context) {
        this.transactionList = transactionList;
        this.context = context;
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_transaction, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final TransactionModel transaction = transactionList.get(position);
        if (transaction.getCathégorie().equals("Autre") || transaction.getCathégorie().equals("Autre recette")) {
            holder.textViewNom.setText(transaction.getOtherCathégorie());
        } else {
            holder.textViewNom.setText(transaction.getCathégorie());
        }
        if (transaction.getType().equals("D")) {
            holder.textViewSolde.setText("(" + transaction.getBalance() + " " + transaction.getTransactionPriceCurrency() + ")");
            holder.textViewSolde.setTextColor(Color.RED);
        } else if (transaction.getType().equals("C")) {
            holder.textViewSolde.setText(transaction.getBalance() + " " + transaction.getTransactionPriceCurrency());
            holder.textViewSolde.setTextColor(Color.parseColor("#0fc418"));
        }

        holder.textViewNomCompte.setText("(" + transaction.getCompte() + ")");

        if (transaction.getCompte().equals("Portfeuille")) {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    final EditText edittext = new EditText(context);
                    builder.setView(edittext);

                    builder.setMessage("Entrer le nouveau solde s'il vous plaît").setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            setTransaction(edittext, transaction, position, dialog);
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

        holder.textViewDate.setText(transaction.getDate());
        holder.imageViewIcon.setImageResource(transaction.getIcon());
        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Voulez-vous supprimer cette transaction ?").setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        removeDB(transaction, position, dialog);
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
        return transactionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewNom;
        public TextView textViewNomCompte;
        public TextView textViewDate;
        public TextView textViewSolde;
        public ImageView imageViewDelete;
        public ImageView imageViewIcon;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewNom = (TextView) itemView.findViewById(R.id.nom_transaction);
            textViewNomCompte = (TextView) itemView.findViewById(R.id.nom_compte);
            textViewDate = (TextView) itemView.findViewById(R.id.date_transaction);
            textViewSolde = (TextView) itemView.findViewById(R.id.solde_transaction);
            imageViewIcon = (ImageView) itemView.findViewById(R.id.icon_transaction);
            imageViewDelete = (ImageView) itemView.findViewById(R.id.delete_transaction);
            cardView = (CardView) itemView.findViewById(R.id.card_Transaction);

        }
    }

    private void setTransaction(EditText edittext, TransactionModel transaction, int position, DialogInterface dialog) {

        if (edittext.getText().toString().equals("")) {
            Toast.makeText(context, "invalide solde", Toast.LENGTH_LONG).show();

        } else if (!TextUtils.isDigitsOnly(edittext.getText())) {
            Toast.makeText(context, "invalide valeur", Toast.LENGTH_LONG).show();

        } else {
            String editTextValue = edittext.getText().toString();
            Float finalValue = Float.parseFloat(editTextValue);

            realm.beginTransaction();
            transaction.setBalance(finalValue);
            realm.commitTransaction();
            notifyItemRangeChanged(position, transactionList.size());
            dialog.dismiss(); // do something
            Toast.makeText(context, "le nouveau solde est " + finalValue, Toast.LENGTH_LONG).show();

        }

    }

    private void removeDB(final TransactionModel transaction, final int position, DialogInterface dialog) {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Log.e("test2", transaction.getCodeTransaction() + "");

                RealmResults<TransactionModel> result = realm.where(TransactionModel.class).equalTo("mCodeTransaction", transactionList.get(position).getCodeTransaction()).findAll();
                result.deleteAllFromRealm();
            }
        });
        transactionList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, transactionList.size());
        dialog.dismiss(); // do something
    }

}