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
 * Created by Amine on 1/15/2018.
 */

public class InstAdapteur extends RecyclerView.Adapter<InstAdapteur.MyViewHolder>
{
    private Context mContext;
    private List<Institution> instList;

    public final static String ID_UNIV           = "ID_UNIV";
    public final static String NOM_UNIV          = "NOM_UNIV";
    public final static String ID_INST           = "ID_INST";
    public final static String NOM_INST          = "NOM_INST";
    public final static String VILLE_INST        = "VILLE_INST";
    public final static String DESC_INST         = "DESC_INST";
    public final static String IMG_INST          = "IMG_INST";
    public final static String EDITEXT_ENABLED   = "EDITEXT_ENABLED";


    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tvNomInst, tvVilleInst;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view)
        {
            super(view);
            tvNomInst   = view.findViewById(R.id.tvNomInst);
            tvVilleInst = view.findViewById(R.id.tvVilleInst);
            thumbnail   = view.findViewById(R.id.thumbnailInst);
            overflow    = view.findViewById(R.id.overflowInst);
        }
    }
    public InstAdapteur(Context mContext, List<Institution> List)
    {
        this.mContext = mContext;
        this.instList = List;
    }

    @Override
    public InstAdapteur.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inst_card, parent, false);

        return new InstAdapteur.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final InstAdapteur.MyViewHolder holder, int position) {
        final Institution inst = instList.get(position);
        holder.tvNomInst.setText(inst.getNomInst());
        holder.tvVilleInst.setText("Ville :" + inst.getVilleInst());

        // loading album cover using Glide library
        Glide.with(mContext).load(inst.getImgInst()).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow, inst);
            }
        });

        holder.thumbnail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                toDisplayInst(inst, false);
            }
        });

        holder.tvNomInst.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                toDisplayInst(inst, false);
            }
        });

        holder.tvVilleInst.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                toDisplayInst(inst, false);
            }
        });
    }
    public void toDisplayInst(Institution inst, boolean etEnabled)
    {
        Intent toDisplayInst = new Intent(mContext, Display_Inst.class);
        Bundle values        = new Bundle();
        values.putInt(ID_INST, inst.getIdInst());
        values.putString(NOM_INST, inst.getNomInst());
        values.putInt(ID_UNIV, inst.getIdUniv());
        values.putString(NOM_UNIV, inst.getNomUniv());
        values.putString(VILLE_INST, inst.getVilleInst());
        values.putString(DESC_INST, inst.getDescInst());
        values.putByteArray(IMG_INST, inst.getImgInst());
        values.putBoolean(EDITEXT_ENABLED, etEnabled);
        toDisplayInst.putExtras(values);
        mContext.startActivity(toDisplayInst);
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, Institution inst)
    {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_inst, popup.getMenu());
        popup.setOnMenuItemClickListener(new InstAdapteur.MyMenuItemClickListener(inst));
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        Institution inst;
        public MyMenuItemClickListener(Institution inst2) {
            inst = inst2;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_maj_infoinst:
                    Toast.makeText(mContext, "Mettre à jour les informations", Toast.LENGTH_SHORT).show();
                    toDisplayInst(inst, true);
                    return true;
                case R.id.action_voirform:
                    Toast.makeText(mContext, "Voir les formations", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return instList.size();
    }
}
