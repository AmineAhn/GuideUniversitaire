package ma.ac.fst.ahniche_amine.guideuniversitaire;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ActivityNvForm extends AppCompatActivity {

    AutoCompleteTextView actvUniv;
    AutoCompleteTextView actvInst;
    AutoCompleteTextView actvTypeForm;
    EditText etDescForm;
    EditText etNomForm;
    Button btnAjUniv;
    Button btnAjInst;
    Button btnOk;

    String[] types;
    String[] univs;
    String[] insts   ;
    ArrayList<Institution> allInsts;
    ArrayList<Univ> allUnivs;


    FormDbAdapteur formDbAdapteur;
    SQLiteDatabase formDb;

    Formation formation;

    final int REQUEST_CODE_UNIV    = 2;
    final int REQUEST_CODE_INST    = 3;

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
        setContentView(R.layout.activity_nv_form);
        initComponents();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("NOM_INST", actvInst.getText().toString());
        outState.putString("NOM_FORM", etNomForm.getText().toString());
        outState.putString("NOM_UNIV", actvUniv.getText().toString());
        outState.putString("TYPE", actvTypeForm.getText().toString());
        outState.putString("DESC", etDescForm.getText().toString());
        Toast.makeText(this, " ETAT D INSTANCE SAUVEGARDÉ", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        retrouverUserConnected();
        actvTypeForm.setText(savedInstanceState.getString("TYPE"));
        actvUniv.setText(savedInstanceState.getString("NOM_UNIV"));
        actvInst.setText(savedInstanceState.getString("NOM_INST"));
        etDescForm.setText(savedInstanceState.getString("DESC"));
        etNomForm.setText(savedInstanceState.getString("NOM_FORM"));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == REQUEST_CODE_UNIV)
            {
                if(resultCode == RESULT_OK && null != data)
                {
                    // Get the new univ data
                    String nomUniv = data.getExtras().getString("NOM_UNIV");
                    actvUniv.setText(nomUniv);

                }
                else
                {
                    Toast.makeText(this, "RETURN RESULT IS CANCELLED :( ", Toast.LENGTH_SHORT).show();
                }
            }else if (requestCode == REQUEST_CODE_INST)
            {
                if(resultCode == RESULT_OK && null != data)
                {
                    // Get the new univ data
                    String nomInst = data.getExtras().getString("NOM_INST");
                    actvInst.setText(nomInst);
                }
                else
                {
                    Toast.makeText(this, "RETURN RESULT IS CANCELLED :( ", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }


    private void initComponents()
    {
        formDbAdapteur = new FormDbAdapteur(this);
        formDb = formDbAdapteur.getBaseDonnees();
        retrouverUserConnected();
        setupAutoCompleteTVTypeForm();
        setupAutoCompleteTVUnivs();
        setupAutoCompleteTVInsts();
        etDescForm= findViewById(R.id.etDescForm);
        etNomForm = findViewById(R.id.etNomInst);
        btnAjUniv        = findViewById(R.id.btnAjUniv);
        btnAjInst        = findViewById(R.id.btnAjInst);
        btnOk= findViewById(R.id.btnOk);
        formation = new Formation();
    }

    private void setupAutoCompleteTVInsts()
    {
        InstDbAdapteur adapteur = new InstDbAdapteur(this);
        allInsts = adapteur.cursorToInsts(adapteur.getAllInsts());
        insts = new String[allInsts.size()];
        for(int i = 0 ; i < allInsts.size(); i++)
        {
            insts[i] = allInsts.get(i).getNomInst();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item,insts);
        //Getting the instance of AutoCompleteTextView
        actvInst= findViewById(R.id.actvInst);
        actvInst.setThreshold(1);//will start working from first character
        actvInst.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        actvInst.setTextColor(Color.RED);

    }

    private void setupAutoCompleteTVTypeForm()
    {
        types = new String[]{
                "Master Des Sciences", "Master Spécialisé", "Master Professionel",
                "Licence Fondamentale","Licence Professionelle","DEUG",
                "DEUST","Cycle d'ingénieurs","Cycle Préparatoire"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item, types);
        //Getting the instance of AutoCompleteTextView
        actvTypeForm= findViewById(R.id.actvTypeForm);
        actvTypeForm.setThreshold(1);//will start working from first character
        actvTypeForm.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView


    }
    private void setupAutoCompleteTVUnivs()
    {
        UnivDbAdapteur adapteur = new UnivDbAdapteur(this);
        allUnivs = adapteur.cursorToUnivs(adapteur.getAllUnivs());
        univs = new String[allUnivs.size()];
        for(int i = 0 ; i < allUnivs.size(); i++)
        {
            univs[i] = allUnivs.get(i).getNomUniv();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item, univs);
        //Getting the instance of AutoCompleteTextView
        actvUniv= findViewById(R.id.actvUniv);
        actvUniv.setThreshold(1);//will start working from first character
        actvUniv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView


    }

    public void onClickOk(View view)
    {
        if(!actvInst.getText().toString().isEmpty()
                ||!actvTypeForm.getText().toString().isEmpty()
                ||!actvUniv.getText().toString().isEmpty()
                ||!etNomForm.getText().toString().isEmpty()
                ||!actvTypeForm.getText().toString().isEmpty())
        {
            String nomInst   = actvInst.getText().toString();
            String nomUniv   = actvUniv.getText().toString();
            String nomForm   = etNomForm.getText().toString();
            String descForm  = etDescForm.getText().toString();
            String typeForm  = actvTypeForm.getText().toString();

            formation.setNomForm(nomForm);
            formation.setIdUniv(findUnivByNomUniv(nomUniv));
            formation.setNomUniv(nomUniv);
            formation.setNomInst(nomInst);
            formation.setIdInst(findInstByNomInst(nomInst));
            formation.setDescForm(descForm);
            formation.setTypeForm(typeForm);
            long resultat = formDbAdapteur.insertForm(formation);
            if(resultat == -1)
            {
                Toast.makeText(this, "Insetion échoué!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this,"Insertion réussieª ... resultat action à modifier",Toast.LENGTH_LONG);
                toNextActivity(userConnected, ActivityForm.class);
            }
        }
        else
        {
            Toast.makeText(this, "Veuillez remplir tous les champs!", Toast.LENGTH_SHORT).show();
        }
    }

    private int findUnivByNomUniv(String nomUniv)
    {
        UnivDbAdapteur adapteur = new UnivDbAdapteur(this);
        allUnivs = adapteur.cursorToUnivs(adapteur.getAllUnivs());
        for(int i = 0 ; i < allUnivs.size(); i++)
        {
            if(allUnivs.get(i).getNomUniv().equals(nomUniv))
            {
                return allUnivs.get(i).getIdUniv();
            }
        }
        return -1;
    }

    private int findInstByNomInst(String nomInst)
    {
        InstDbAdapteur adapteur = new InstDbAdapteur(this);
        allInsts = adapteur.cursorToInsts(adapteur.getAllInsts());
        for(int i = 0 ; i < allUnivs.size(); i++)
        {
            if(allInsts.get(i).getNomInst().equals(nomInst))
            {
                return allInsts.get(i).getIdInst();
            }
        }
        return -1;
    }


    public void onClickAjouterUniv(View view) {
       toNextActivityForResult(userConnected, ActivityNvUniv.class, REQUEST_CODE_UNIV);
    }

    public void onClickAjouterInst(View view) {

        toNextActivityForResult(userConnected, ActivityNvInst.class, REQUEST_CODE_INST);
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
