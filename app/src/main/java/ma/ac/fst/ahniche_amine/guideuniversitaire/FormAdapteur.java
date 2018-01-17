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

public class FormAdapteur extends RecyclerView.Adapter<FormAdapteur.MyViewHolder>
{
    private Context mContext;
    private List<Formation> formList;

    public final static String ID_UNIV           = "ID_UNIV";
    public final static String NOM_UNIV          = "NOM_UNIV";
    public final static String ID_INST           = "ID_INST";
    public final static String NOM_INST          = "NOM_INST";
    public final static String ID_FORM           = "ID_FORM";
    public final static String NOM_FORM          = "NOM_FORM";
    public final static String DESC_FORM         = "DESC_FORM";
    public final static String TYPE_FORM         = "TYPE_FORM";
    public final static String EDITEXT_ENABLED   = "EDITEXT_ENABLED";

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tvNomForm, tvInstForm;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view)
        {
            super(view);
            tvNomForm = view.findViewById(R.id.tvNomForm);
            tvInstForm = view.findViewById(R.id.tvInstForm);
            thumbnail   = view.findViewById(R.id.thumbnailForm);
            overflow    = view.findViewById(R.id.overflowForm);
        }
    }
    public FormAdapteur(Context mContext, List<Formation> List)
    {
        this.mContext = mContext;
        this.formList = List;
    }

    @Override
    public FormAdapteur.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.form_card, parent, false);

        return new FormAdapteur.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FormAdapteur.MyViewHolder holder, int position) {
        final Formation form = formList.get(position);
        holder.tvNomForm.setText(form.getNomForm());
        holder.tvInstForm.setText(form.getNomInst() + " | " + form.getNomUniv());


        if(form.getTypeForm().equals("Master Des Sciences")
                || form.getTypeForm().equals("Master Spécialisé")
            || form.getTypeForm().equals("Master Professionel"))
        {
            Glide.with(mContext).load(R.drawable.master200_200).into(holder.thumbnail);
        }
        if( form.getTypeForm().equals("Licence Fondamentale")
                ||form.getTypeForm().equals("Licence Professionelle"))
        {
            Glide.with(mContext).load(R.drawable.licence200_200).into(holder.thumbnail);
        }

        if( form.getTypeForm().equals("Cycle d'ingénieurs"))
        {
            Glide.with(mContext).load(R.drawable.cycle_ing_200_200).into(holder.thumbnail);
        }
        if( form.getTypeForm().equals("Cycle Préparatoire"))
        {
            Glide.with(mContext).load(R.drawable.cycle_prep_200_200).into(holder.thumbnail);
        }
        if( form.getTypeForm().equals("DEUG"))
        {
            Glide.with(mContext).load(R.drawable.deug200_200).into(holder.thumbnail);
        }
        if( form.getTypeForm().equals("DEUST"))
        {
            Glide.with(mContext).load(R.drawable.deust200_200).into(holder.thumbnail);
        }
        // loading album cover using Glide library


        holder.overflow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow, form);
            }
        });

        holder.thumbnail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                toDisplayForm(form, false);
            }
        });

        holder.tvNomForm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                toDisplayForm(form, false);
            }
        });

        holder.tvInstForm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                toDisplayForm(form, false);
            }
        });
    }
    public void toDisplayForm(Formation form, boolean etEnabled)
    {
        Intent toDisplayInst = new Intent(mContext, Display_Form.class);
        Bundle values        = new Bundle();
        values.putInt(ID_INST, form.getIdInst());
        values.putString(NOM_INST, form.getNomInst());
        values.putInt(ID_UNIV, form.getIdUniv());
        values.putString(NOM_UNIV, form.getNomUniv());
        values.putInt(ID_FORM, form.getIdForm());
        values.putString(NOM_FORM, form.getNomForm());
        values.putString(TYPE_FORM, form.getTypeForm());
        values.putString(DESC_FORM, form.getDescForm());
        values.putBoolean(EDITEXT_ENABLED, etEnabled);
        toDisplayInst.putExtras(values);
        mContext.startActivity(toDisplayInst);
    }




    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, Formation form)
    {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_form, popup.getMenu());
        popup.setOnMenuItemClickListener(new FormAdapteur.MyMenuItemClickListener(form));
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        Formation form;
        public MyMenuItemClickListener(Formation form2) {
            form = form2;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_maj_infoform:
                    Toast.makeText(mContext, "Mettre à jour les informations", Toast.LENGTH_SHORT).show();
                    toDisplayForm(form, true);
                    return true;
                case R.id.action_ajouter_souhait:
                    Toast.makeText(mContext, "Ajouter Souhait", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return formList.size();
    }
}

