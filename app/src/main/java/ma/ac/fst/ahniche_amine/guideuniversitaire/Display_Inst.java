package ma.ac.fst.ahniche_amine.guideuniversitaire;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Display_Inst extends AppCompatActivity {

    ImageView ivImgInst;
    EditText eti1nominst;
    AutoCompleteTextView eti2nomuniv;
    AutoCompleteTextView eti3ville;
    EditText eti4desc;

    Button btnSuppInst;
    Button btnModInst;

    int    idUniv   ;
    String nomUniv  ;
    int    idInst   ;
    String nomInst  ;
    String villeInst;
    String descInst;
    byte[] imgInst  ;

    boolean etEnabled;

    boolean firstClickMod;
    boolean firstClickSup;

    public final static String ID_UNIV           = "ID_UNIV";
    public final static String NOM_UNIV          = "NOM_UNIV";
    public final static String ID_INST           = "ID_INST";
    public final static String NOM_INST          = "NOM_INST";
    public final static String VILLE_INST        = "VILLE_INST";
    public final static String DESC_INST         = "DESC_INST";
    public final static String IMG_INST          = "IMG_INST";
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
        setContentView(R.layout.display__inst);
        initComponents();
        SetInst();
    }

    private void initComponents()
    {

        retrouverUserConnected();
        ivImgInst   = findViewById(R.id.iv1inst);
        eti1nominst = findViewById(R.id.eti1nomintst);
        eti2nomuniv= findViewById(R.id.eti2nomuniv);
        eti3ville = findViewById(R.id.eti3ville);
        eti4desc = findViewById(R.id.eti4desc);

        setupAutoCompleteTVUnivs();
        setupAutoCompleteTVVilles();

        btnSuppInst = findViewById(R.id.btnSuppInst);
        btnModInst  = findViewById(R.id.btnModInst);

        firstClickMod = false;
        firstClickSup = false;
    }

    public void SetInst()
    {
        Intent intent = getIntent();
        Bundle values = intent.getExtras();
        idUniv    = values.getInt(ID_UNIV);
        nomUniv   = values.getString(NOM_UNIV);
        idInst    = values.getInt(ID_INST);
        nomInst   = values.getString(NOM_INST);
        villeInst =values.getString(VILLE_INST);
        descInst  =values.getString(DESC_INST);
        imgInst   = values.getByteArray(IMG_INST);
        etEnabled =values.getBoolean(EDITEXT_ENABLED);
        firstClickMod = etEnabled;
        eti1nominst.setText(nomInst);
        eti2nomuniv.setText(nomUniv);
        eti3ville.setText(villeInst);
        eti4desc.setText(descInst);
        Glide.with(this).load(imgInst).into(ivImgInst);

        eti1nominst.setEnabled(etEnabled);
        eti2nomuniv.setEnabled(etEnabled);
        eti3ville.setEnabled(etEnabled);
        eti4desc.setEnabled(etEnabled);

    }



    private void setupAutoCompleteTVUnivs()
    {
        UnivDbAdapteur adapteur = new UnivDbAdapteur(this);
        ArrayList<Univ> allUnivs = adapteur.cursorToUnivs(adapteur.getAllUnivs());
        String[] univs = new String[allUnivs.size()];
        for(int i = 0 ; i < allUnivs.size(); i++)
        {
            univs[i] = allUnivs.get(i).getNomUniv();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item, univs);
        //Getting the instance of AutoCompleteTextView
        eti2nomuniv = findViewById(R.id.eti2nomuniv);
        eti2nomuniv.setThreshold(1);//will start working from first character
        eti2nomuniv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        //etNomUniv.setTextColor(Color.RED);

    }

    private void setupAutoCompleteTVVilles()
    {
        String[] villes = new String[]{
                "Tétouan", "Tanger", "Rabat",
                "Casablanca","Agadir","Marrakech",
                "Guelmim","Fes","Meknes","Ifrane",
                "Mohammedia","Larache","Kenitra",
                "Setta","Laayoune","Oujda","Martil",
                "El Jadida","Béni-Mellal"

        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item,villes);
        //Getting the instance of AutoCompleteTextView
        eti3ville = findViewById(R.id.eti3ville);
        eti3ville.setThreshold(1);//will start working from first character
        eti3ville.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView


    }

    public void onClickModifierInst(View view)
    {
        btnSuppInst.setEnabled(false);

        if(!firstClickMod)
        {
            eti1nominst.setEnabled(true);
            eti2nomuniv.setEnabled(true);
            eti3ville.setEnabled(true);
            eti4desc.setEnabled(true);
            firstClickMod = true;

        }
        else
        {
            String nominst   = eti1nominst.getText().toString();
            String nomuniv   = eti2nomuniv.getText().toString();
            String villeinst = eti3ville.getText().toString();
            String descinst  = eti4desc.getText().toString();
            if(nominst.isEmpty() || villeinst.isEmpty() || descinst.isEmpty() || nomuniv.isEmpty())
            {
                Toast.makeText(this, "Vous devez remplir tous les champs", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Institution in = new Institution();
                in.setIdUniv(idUniv);
                in.setNomInst(nominst);
                in.setIdInst(idInst);
                in.setNomUniv(nomuniv);
                in.setDescInst(descinst);
                in.setVilleInst(villeinst);
                in.setImgInst(imgInst);

                InstDbAdapteur instDbAdapteur = new InstDbAdapteur(this);
                long resultat = instDbAdapteur.updateInst(idInst, in);
                if(resultat != -1 )
                {
                    Toast.makeText(this, "Modification réussie", Toast.LENGTH_SHORT).show();
                    toNextActivity(userConnected, ActivityInst.class);
                }
                else
                {
                    Toast.makeText(this, "Modification echouée", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    public void onClickSupprimerInst(View view)
    {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation Suppression")
                .setMessage("Supprimer cette institution causera la suppression de ses formations. Êtes-vous sûre?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(Display_Inst.this, ":(", Toast.LENGTH_SHORT).show();
                        FormDbAdapteur formDbAdapteur = new FormDbAdapteur(Display_Inst.this);
                        InstDbAdapteur instDbAdapteur = new InstDbAdapteur(Display_Inst.this);

                        ArrayList<Formation> formations = formDbAdapteur.cursorToForms(formDbAdapteur.getAllFormsByInstID(idInst));

                        for (int i = 0; i < formations.size();i++)
                        {
                            formDbAdapteur.removeForm(formations.get(i).getIdForm());
                            Toast.makeText(Display_Inst.this, "Formations Supprimées!", Toast.LENGTH_SHORT).show();
                        }


                        instDbAdapteur.removeInst(idInst);
                        Toast.makeText(Display_Inst.this, "Institut Supprimé!", Toast.LENGTH_SHORT).show();
                        toNextActivity(userConnected, ActivityInst.class);

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
