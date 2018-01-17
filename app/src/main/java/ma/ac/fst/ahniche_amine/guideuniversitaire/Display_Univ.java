package ma.ac.fst.ahniche_amine.guideuniversitaire;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Display_Univ extends AppCompatActivity {

    ImageView ivImgUniv;
    EditText et1nomuniv;
    EditText et2villeuniv;
    EditText et3descuniv;

    Button btnSuppUniv;
    Button btnModUniv;

    int    idUniv   ;
    String nomUniv  ;
    String villeUniv;
    String descUniv ;
    byte[] imgUniv  ;

    boolean etEnabled;

    boolean firstClickMod;
    boolean firstClickSup;


    public final static String ID_UNIV           = "ID_UNIV";
    public final static String NOM_UNIV          = "NOM_UNIV";
    public final static String VILLE_UNIV        = "VILLE_UNIV";
    public final static String DESC_UNIV         = "DESC_UNIV";
    public final static String IMG_UNIV          = "IMG_UNIV";
    public final static String EDITEXT_ENABLED   = "EDITEXT_ENABLED";

    User                 userConnected     ;

    public static final String USER_ID       = "USER_ID";
    public static final String USER_NOM_USER = "USER_NOM_USER";
    public static final String USER_MDP_USER = "USER_MDP_USER";
    public static final String USER_PRENOM   = "USER_PRENOM";
    public static final String USER_NOM      = "USER_NOM";
    public static final String USER_EMAIL    = "USER_EMAIL";
    public static final String USER_IMG      = "USER_IMG";
    public static final String USER_INST     = "USER_INST";
    public static final String USER_UNIV     = "USER_UNIV";
    public static final String USER_FORM     = "USER_FORM";
    public static final String USER_COMP     = "USER_COMP";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display__univ);
        initComponents();
        SetUniv();
    }

    private void initComponents()
    {
        retrouverUserConnected();
        ivImgUniv   = findViewById(R.id.iv1inst);
        et1nomuniv  = findViewById(R.id.et1nomuniv);
        et2villeuniv= findViewById(R.id.et2villeuniv);
        et3descuniv = findViewById(R.id.et4desc);

        btnSuppUniv = findViewById(R.id.btnSuppInst);
        btnModUniv  = findViewById(R.id.btnModInst);

        firstClickMod = false;
        firstClickSup = false;
    }

    public void SetUniv()
    {
        Intent intent = getIntent();
        Bundle values = intent.getExtras();
        idUniv    = values.getInt(ID_UNIV);
        nomUniv   = values.getString(NOM_UNIV);
        villeUniv =values.getString(VILLE_UNIV);
        descUniv  =values.getString(DESC_UNIV);
        imgUniv   = values.getByteArray(IMG_UNIV);
        etEnabled =values.getBoolean(EDITEXT_ENABLED);
        firstClickMod = etEnabled;
        et1nomuniv.setText(nomUniv);
        et2villeuniv.setText(villeUniv);
        et3descuniv.setText(descUniv);
        Glide.with(this).load(imgUniv).into(ivImgUniv);

        et1nomuniv.setEnabled(etEnabled);
        et2villeuniv.setEnabled(etEnabled);
        et3descuniv.setEnabled(etEnabled);

    }

    public void onClickModifierUniv(View view)
    {
        btnSuppUniv.setEnabled(false);

        if(!firstClickMod)
        {
            et1nomuniv.setEnabled(true);
            et2villeuniv.setEnabled(true);
            et3descuniv.setEnabled(true);
            firstClickMod = true;

        }
        else
        {
            String nom = et1nomuniv.getText().toString();
            String ville = et2villeuniv.getText().toString();
            String desc = et3descuniv.getText().toString();
            if(nom.isEmpty() || ville.isEmpty() || desc.isEmpty())
            {
                Toast.makeText(this, "Vous devez remplir tous les champs", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Univ u = new Univ();
                u.setIdUniv(idUniv);
                u.setNomUniv(nom);
                u.setDescUniv(desc);
                u.setVilleUniv(ville);
                u.setImgUniv(imgUniv);

                UnivDbAdapteur univDbAdapteur = new UnivDbAdapteur(this);
                long resultat = univDbAdapteur.updateUniv(idUniv, u);
                if(resultat != -1 )
                {
                    Toast.makeText(this, "Modification réussie", Toast.LENGTH_SHORT).show();
                    toNextActivity(userConnected, ActivityUniv.class);
                }
                else
                {
                    Toast.makeText(this, "Modification echouée", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    public void onClickSupprimerUniv(View view)
    {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation Suppression")
                .setMessage("Supprimer cette université causera la suppression de ses instituts et formations. Êtes-vous sûre?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(Display_Univ.this, ":(", Toast.LENGTH_SHORT).show();
                        FormDbAdapteur formDbAdapteur = new FormDbAdapteur(Display_Univ.this);
                        InstDbAdapteur instDbAdapteur = new InstDbAdapteur(Display_Univ.this);
                        UnivDbAdapteur univDbAdapteur = new UnivDbAdapteur(Display_Univ.this);

                        ArrayList<Formation> formations = formDbAdapteur.cursorToForms(formDbAdapteur.getAllFormsByUnivID(idUniv));
                        ArrayList<Institution> institutions= instDbAdapteur.cursorToInsts(instDbAdapteur.getAllInstsByUnivID(idUniv));

                        for (int i = 0; i < formations.size();i++)
                        {
                            formDbAdapteur.removeForm(formations.get(i).getIdForm());
                            Toast.makeText(Display_Univ.this, "Formations Supprimées!", Toast.LENGTH_SHORT).show();
                        }

                        for (int i = 0; i < institutions.size();i++)
                        {
                            instDbAdapteur.removeInst(institutions.get(i).getIdInst());
                            Toast.makeText(Display_Univ.this, "Instituts Supprimées!", Toast.LENGTH_SHORT).show();
                        }

                        univDbAdapteur.removeUniv(idUniv);
                        Toast.makeText(Display_Univ.this, "Université Supprimée!", Toast.LENGTH_SHORT).show();
                        toNextActivity(userConnected, ActivityUniv.class);

                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void toNextActivity(User user, Class aClass )
    {
        Intent toNext = new Intent(this, aClass);
        toNext.putExtra(USER_ID      , user.getIdUser () );
        toNext.putExtra(USER_NOM_USER, user.getNomUser() );
        toNext.putExtra(USER_MDP_USER, user.getMdpUser() );
        toNext.putExtra(USER_PRENOM  , user.getPrenom () );
        toNext.putExtra(USER_NOM     , user.getNom    () );
        toNext.putExtra(USER_EMAIL   , user.getEmail  () );
        toNext.putExtra(USER_IMG     , user.getImgUser() );
        toNext.putExtra(USER_FORM    , user.getIdForm () );
        toNext.putExtra(USER_INST    , user.getIdInst () );
        toNext.putExtra(USER_UNIV    , user.getIdUniv () );
        toNext.putExtra(USER_COMP    , user.isCompleted() );
        startActivity(toNext);
    }

    private void toNextActivityForResult(User user, Class aClass, int REQUSEST_CODE )
    {
        Intent toNext = new Intent(this, aClass);
        toNext.putExtra(USER_ID      , user.getIdUser () );
        toNext.putExtra(USER_NOM_USER, user.getNomUser() );
        toNext.putExtra(USER_MDP_USER, user.getMdpUser() );
        toNext.putExtra(USER_PRENOM  , user.getPrenom () );
        toNext.putExtra(USER_NOM     , user.getNom    () );
        toNext.putExtra(USER_EMAIL   , user.getEmail  () );
        toNext.putExtra(USER_IMG     , user.getImgUser() );
        toNext.putExtra(USER_FORM    , user.getIdForm () );
        toNext.putExtra(USER_INST    , user.getIdInst () );
        toNext.putExtra(USER_UNIV    , user.getIdUniv () );
        toNext.putExtra(USER_COMP    , user.isCompleted() );
        startActivityForResult(toNext, REQUSEST_CODE);
    }

    private void retrouverUserConnected()
    {
        Bundle  bundle      = getIntent().getExtras();
        int     idUser      = bundle.getInt(USER_ID);
        String  nomUser     = bundle.getString(USER_NOM_USER);
        String  mdpUser     = bundle.getString(USER_MDP_USER);
        String  prenom      = bundle.getString(USER_PRENOM);
        String  nom         = bundle.getString(USER_NOM);
        String  email       = bundle.getString(USER_EMAIL);
        byte[]  imgUser     = bundle.getByteArray(USER_IMG);
        int     idForm      = bundle.getInt(USER_FORM);
        int     idInst      = bundle.getInt(USER_INST);
        int     idUniv      = bundle.getInt(USER_UNIV);
        boolean isCompleted = bundle.getBoolean(USER_COMP);
        userConnected = new User(idUser, nomUser, mdpUser, prenom, nom, email, imgUser, idInst, idUniv, idForm, isCompleted);
    }
}
