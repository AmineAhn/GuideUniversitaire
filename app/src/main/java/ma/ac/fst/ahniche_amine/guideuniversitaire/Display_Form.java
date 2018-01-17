package ma.ac.fst.ahniche_amine.guideuniversitaire;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Display_Form extends AppCompatActivity {

    public final static String ID_UNIV           = "ID_UNIV";
    public final static String NOM_UNIV          = "NOM_UNIV";
    public final static String ID_INST           = "ID_INST";
    public final static String NOM_INST          = "NOM_INST";
    public final static String ID_FORM           = "ID_FORM";
    public final static String NOM_FORM          = "NOM_FORM";
    public final static String DESC_FORM         = "DESC_FORM";
    public final static String TYPE_FORM         = "TYPE_FORM";
    public final static String EDITEXT_ENABLED   = "EDITEXT_ENABLED";


    AutoCompleteTextView etNomForm ;
    AutoCompleteTextView etNomInst ;
    AutoCompleteTextView  etNomUniv;
    AutoCompleteTextView etType    ;
    EditText etDesc    ;

    Button btnModForm  ;
    Button btnSuppForm ;

    int    idUniv   ;
    String nomUniv  ;
    int    idInst   ;
    String nomInst  ;
    int    idForm   ;
    String nomForm  ;
    String typeForm;
    String descForm;

    boolean etEnabled;

    boolean firstClickMod;
    boolean firstClickSup;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display__form);
        initComponents();
        SetForm();
    }


    private void initComponents()
    {
        retrouverUserConnected();
        etNomForm  = findViewById(R.id.etf0nomform);
        etNomInst = findViewById(R.id.etf1nomintst);
        etNomUniv = findViewById(R.id.etf2nomuniv);
        etType    = findViewById(R.id.etf3type);
        etDesc    = findViewById(R.id.etf4desc);
        setupAutoCompleteTVTypeForm();
        setupAutoCompleteTVUnivs();
        setupAutoCompleteTVInsts();
        btnModForm  = findViewById(R.id.btnModForm);
        btnSuppForm = findViewById(R.id.btnSuppForm);

        firstClickMod = false;
        firstClickSup = false;

    }

    public void SetForm()
    {
        Intent intent = getIntent();
        Bundle values = intent.getExtras();
        idUniv    = values.getInt(ID_UNIV);
        nomUniv   = values.getString(NOM_UNIV);
        idInst    = values.getInt(ID_INST);
        nomInst   = values.getString(NOM_INST);
        idForm    = values.getInt(ID_FORM);
        nomForm   = values.getString(NOM_FORM);
        typeForm  =values.getString(TYPE_FORM);
        descForm  =values.getString(DESC_FORM);
        etEnabled =values.getBoolean(EDITEXT_ENABLED);
        firstClickMod = etEnabled;
        etNomForm.setText(nomForm);
        etNomInst.setText(nomInst);
        etNomUniv.setText(nomUniv);
        etType.setText(typeForm);
        etDesc.setText(descForm);

        etNomForm.setEnabled(etEnabled);
        etNomInst.setEnabled(etEnabled);
        etNomUniv.setEnabled(etEnabled);
        etType.setEnabled(etEnabled);
        etDesc.setEnabled(etEnabled);


    }


    private void setupAutoCompleteTVInsts()
    {
        InstDbAdapteur adapteur = new InstDbAdapteur(this);
        ArrayList<Institution> allInsts = adapteur.cursorToInsts(adapteur.getAllInsts());
        String[] insts = new String[allInsts.size()];
        for(int i = 0 ; i < allInsts.size(); i++)
        {
            insts[i] = allInsts.get(i).getNomInst();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item,insts);
        //Getting the instance of AutoCompleteTextView
        etNomInst=findViewById(R.id.etf1nomintst);
        etNomInst.setThreshold(1);//will start working from first character
        etNomInst.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        //actvInst.setTextColor(Color.RED);

    }

    private void setupAutoCompleteTVTypeForm()
    {
        String[] types = new String[]{
                "Master Des Sciences", "Master Spécialisé", "Master Professionel",
                "Licence Fondamentale","Licence Professionelle","DEUG",
                "DEUST","Cycle d'ingénieurs","Cycle Préparatoire"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item, types);
        //Getting the instance of AutoCompleteTextView
        etType= findViewById(R.id.etf3type);
        etType.setThreshold(1);//will start working from first character
        etType.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        //actvTypeForm.setTextColor(Color.RED);

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
        etNomUniv = findViewById(R.id.etf2nomuniv);
        etNomUniv.setThreshold(1);//will start working from first character
        etNomUniv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        //etNomUniv.setTextColor(Color.RED);

    }

    public void onClickModifierForm(View view)
    {
        btnSuppForm.setEnabled(false);

        if(!firstClickMod)
        {
            etNomForm.setEnabled(false);
            etNomInst.setEnabled(false);
            etNomUniv.setEnabled(false);
            etType.setEnabled(false);
            etDesc.setEnabled(false);
            firstClickMod = true;

        }
        else
        {
            String nomform = etNomForm.getText().toString();
            String nominst = etNomInst.getText().toString();
            String nomuniv = etNomUniv.getText().toString();
            String descform = etDesc.getText().toString();
            String typeform = etType.getText().toString();
            if(nomform.isEmpty() || nominst.isEmpty() || nomuniv.isEmpty()
                    ||descform.isEmpty() || typeform.isEmpty())
            {
                Toast.makeText(this, "Vous devez remplir tous les champs", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Formation f = new Formation();
                f.setIdUniv(idUniv);
                f.setNomUniv(nomuniv);
                f.setNomForm(nomform);
                f.setIdInst(idUniv);
                f.setNomInst(nomInst);
                f.setDescForm(descform);
                f.setTypeForm(typeform);

                FormDbAdapteur formDbAdapteur = new FormDbAdapteur(this);
                long resultat = formDbAdapteur.updateForm(idForm, f);
                if(resultat != -1 )
                {
                    Toast.makeText(this, "Modification réussie", Toast.LENGTH_SHORT).show();
                    toNextActivity(userConnected, ActivityForm.class);
                }
                else
                {
                    Toast.makeText(this, "Modification echouée", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    public void onClickSupprimerForm(View view)
    {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation Suppression")
                .setMessage("Êtes-vous sûre?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(Display_Form.this, ":(", Toast.LENGTH_SHORT).show();
                        FormDbAdapteur formDbAdapteur = new FormDbAdapteur(Display_Form.this);

                        formDbAdapteur.removeForm(idForm);
                        Toast.makeText(Display_Form.this, "Formation Supprimée!", Toast.LENGTH_SHORT).show();
                        toNextActivity(userConnected, ActivityForm.class);


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
