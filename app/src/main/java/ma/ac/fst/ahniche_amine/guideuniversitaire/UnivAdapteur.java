package ma.ac.fst.ahniche_amine.guideuniversitaire;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Amine on 1/14/2018.
 */

public class UnivAdapteur extends RecyclerView.Adapter<UnivAdapteur.MyViewHolder>
{
    private Context mContext;
    private List<Univ> univList;

    public final static String ID_UNIV           = "ID_UNIV";
    public final static String NOM_UNIV          = "NOM_UNIV";
    public final static String VILLE_UNIV        = "VILLE_UNIV";
    public final static String DESC_UNIV         = "DESC_UNIV";
    public final static String IMG_UNIV          = "IMG_UNIV";
    public final static String EDITEXT_ENABLED   = "EDITEXT_ENABLED";

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tvNomUniv, tvVilleUniv;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view)
        {
            super(view);
            tvNomUniv = (TextView) view.findViewById(R.id.tvNomUniv);
            tvVilleUniv = (TextView) view.findViewById(R.id.tvVilleUniv);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnailUniv);
            overflow = (ImageView) view.findViewById(R.id.overflowUniv);
        }
    }
        public UnivAdapteur(Context mContext, List<Univ> albumList)
        {
            this.mContext = mContext;
            this.univList = albumList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.univ_card, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final Univ univ = univList.get(position);
            holder.tvNomUniv.setText(univ.getNomUniv());
            holder.tvVilleUniv.setText("Ville :" + univ.getVilleUniv());

            // loading album cover using Glide library
            Glide.with(mContext).load(univ.getImgUniv()).into(holder.thumbnail);

            holder.overflow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    showPopupMenu(holder.overflow, univ);
                }
            });

            holder.thumbnail.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    toDisplayUniv(univ, false);
                }
            });

            holder.tvNomUniv.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {

                    toDisplayUniv(univ, false);
                }
            });

            holder.tvVilleUniv.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    toDisplayUniv(univ, false);
                }
            });
        }

        public void toDisplayUniv(Univ univ, boolean etEnabled)
        {
            Intent toDisplayUniv = new Intent(mContext, Display_Univ.class);
            Bundle values        = new Bundle();
            values.putInt(ID_UNIV, univ.getIdUniv());
            values.putString(NOM_UNIV, univ.getNomUniv());
            values.putString(VILLE_UNIV, univ.getVilleUniv());
            values.putString(DESC_UNIV, univ.getDescUniv());
            values.putByteArray(IMG_UNIV, univ.getImgUniv());
            values.putBoolean(EDITEXT_ENABLED, etEnabled);
            toDisplayUniv.putExtras(values);
            mContext.startActivity(toDisplayUniv);
        }

        /**
         * Showing popup menu when tapping on 3 dots
         */
        private void showPopupMenu(View view, Univ univ)
        {
            // inflate menu
            PopupMenu popup = new PopupMenu(mContext, view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_univ, popup.getMenu());
            popup.setOnMenuItemClickListener(new MyMenuItemClickListener(univ));
            popup.show();
        }

        /**
         * Click listener for popup menu items
         */
        class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

            Univ univ;
            public MyMenuItemClickListener(Univ univ2) {
                univ = univ2;
            }

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_maj_infouniv:
                        Toast.makeText(mContext, "Mettre à jour les informations", Toast.LENGTH_SHORT).show();
                        toDisplayUniv(univ, true);
                        return true;
                    case R.id.action_voirinst:
                        Toast.makeText(mContext, "Voir les institutions", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                }
                return false;
            }
        }

        @Override
        public int getItemCount() {
            return univList.size();
        }
    }

