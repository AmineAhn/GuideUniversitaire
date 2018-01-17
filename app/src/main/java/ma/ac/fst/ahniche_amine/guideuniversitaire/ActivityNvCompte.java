package ma.ac.fst.ahniche_amine.guideuniversitaire;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityNvCompte extends AppCompatActivity {

    EditText etNomUser   ;
    EditText etPrenom    ;
    EditText etNom       ;
    EditText etMdp       ;
    EditText etConfirmMdp;
    Button   btnCreer    ;

    UserDbAdapteur userDbAdapteur;
    SQLiteDatabase userDb        ;


    public static final String USER_ID       = "USER_ID"      ;
    public static final String USER_NOM_USER = "USER_NOM_USER";
    public static final String USER_MDP_USER = "USER_MDP_USER";
    public static final String USER_PRENOM   = "USER_PRENOM"  ;
    public static final String USER_NOM      = "USER_NOM"     ;
    public static final String USER_EMAIL    = "USER_EMAIL"   ;
    public static final String USER_IMG      = "USER_IMG"     ;
    public static final String USER_INST     = "USER_INST"    ;
    public static final String USER_UNIV     = "USER_UNIV"    ;
    public static final String USER_FORM     = "USER_FORM"    ;
    public static final String USER_COMP     = "USER_COMP"    ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState)           ;
        setContentView(R.layout.activity_nv_compte)  ;
        initComponents()                             ;

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("NOM_USER", etNomUser.getText().toString());
        outState.putString("MDP_USER", etMdp.getText().toString());
        outState.putString("NOM", etNom.getText().toString());
        outState.putString("PRENOM", etPrenom.getText().toString());
        outState.putString("CONFIRM", etConfirmMdp.getText().toString());
        Toast.makeText(this, " ETAT D INSTANCE SAUVEGARDÉ", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        //initialiserWidgets();
        etNomUser.setText(savedInstanceState.getString("NOM_USER"));
        etMdp.setText(savedInstanceState.getString("MDP_USER"));
        etNom.setText(savedInstanceState.getString("NOM"));
        etPrenom.setText(savedInstanceState.getString("PRENOM"));
        etConfirmMdp.setText(savedInstanceState.getString("CONFIRM"));

    }

    private void initComponents()
    {
        etNomUser   = findViewById(R.id.etNomUser)   ;
        etPrenom    = findViewById(R.id.etPrenom)    ;
        etNom       = findViewById(R.id.etNom)       ;
        etMdp       = findViewById(R.id.etMdp)       ;
        etConfirmMdp= findViewById(R.id.etConfirmMdp);
        btnCreer    = findViewById(R.id.btnCreer)    ;
        userDbAdapteur= new UserDbAdapteur(this);
        userDb = userDbAdapteur.getBaseDonnees();

    }

    public void onClickCreer(View view)
    {

        if (etNomUser.getText().toString().isEmpty()
                || etPrenom.getText().toString().isEmpty()
                || etNom.getText().toString().isEmpty()
                || etMdp.getText().toString().isEmpty()
                || etConfirmMdp.getText().toString().isEmpty())
        {
            Toast.makeText(this, "VOUS DEVEZ REMPLIR TOUS LES CHAMPS!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(!etMdp.getText().toString().equals(etConfirmMdp.getText().toString()))
            {
                Toast.makeText(this, "VÉRIFIER VOTRE MOT DE PASSE!", Toast.LENGTH_SHORT).show();
                etConfirmMdp.setText("");
            }
            else
            {
                //Ajouter Utilisateur en bd et allez à l'activité accueil
                User u = new User();
                u.setNomUser(etNomUser.getText().toString());
                u.setMdpUser(etMdp.getText().toString())    ;
                u.setPrenom(etPrenom.getText().toString())  ;
                u.setNom(etNom.getText().toString())        ;
                u.setCompleted(false);
                long result = userDbAdapteur.insertUserAlpha(u);
                if (result == -1 )
                {
                    Toast.makeText(this, "ERREUR AJOUT USER!", Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(this, "AJOUT RÉUSSI : " + result , Toast.LENGTH_SHORT).show();
                    userDb.close();
                    toNextActivity(u);
                }
            }
        }
    }

    private void toNextActivity(User user)
    {
        Intent toAccueil = new Intent(this, ActivityAccueil.class);
        toAccueil.putExtra(USER_ID      , user.getIdUser () );
        toAccueil.putExtra(USER_NOM_USER, user.getNomUser() );
        toAccueil.putExtra(USER_MDP_USER, user.getMdpUser() );
        toAccueil.putExtra(USER_PRENOM  , user.getPrenom () );
        toAccueil.putExtra(USER_NOM     , user.getNom    () );
        toAccueil.putExtra(USER_EMAIL   , user.getEmail  () );
        toAccueil.putExtra(USER_IMG     , user.getImgUser() );
        toAccueil.putExtra(USER_FORM    , user.getIdForm () );
        toAccueil.putExtra(USER_INST    , user.getIdInst () );
        toAccueil.putExtra(USER_UNIV    , user.getIdUniv () );
        toAccueil.putExtra(USER_COMP    , user.isCompleted() );
        startActivity(toAccueil);
    }
}
