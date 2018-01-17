package ma.ac.fst.ahniche_amine.guideuniversitaire;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityAuthentification extends AppCompatActivity {

    ImageView ivLogo      ;
    TextView  tvLoginTitle;
    EditText  etNomUser   ;
    EditText  etMdp       ;
    Button    btnConnexion;
    TextView  tvOu        ;
    Button    btnNvCompte ;

    UserDbAdapteur userDb;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification);
        initComponents();
        userDb.open();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("NOM_USER", etNomUser.getText().toString());
        outState.putString("MDP_USER", etMdp.getText().toString());
        Toast.makeText(this, " ETAT D INSTANCE SAUVEGARDÉ", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        //initialiserWidgets();
        etNomUser.setText(savedInstanceState.getString("NOM_USER"));
        etMdp.setText(savedInstanceState.getString("MDP_USER"));

    }

    private void initComponents()
    {
        ivLogo       = findViewById(R.id.ivLogo)      ;
        tvLoginTitle = findViewById(R.id.tvLoginTitle);
        etNomUser    = findViewById(R.id.etNomUser)   ;
        etMdp        = findViewById(R.id.etMdP)       ;
        btnConnexion = findViewById(R.id.btnConnexion);
        tvOu         = findViewById(R.id.tvOu)        ;
        btnNvCompte  = findViewById(R.id.btnNvCompte) ;
        userDb       = new UserDbAdapteur(this)  ;
    }

    public void onClickConnexion(View view)
    {
        if(etNomUser.getText().toString().isEmpty() || etMdp.getText().toString().isEmpty())
        {
            Toast.makeText(this, "VEUILLEZ REMMPLIR TOUS LES CHAMPS!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            String nomUser = etNomUser.getText().toString();
            String mdp     = etMdp.getText().toString();
            User user = userDb.getUser(nomUser, mdp);
            if(user == null)
            {
                Toast.makeText(this, "NOM USER OU MDP ERRONÉE. VEUILLEZ RÉESAYER!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "NOM USER ET MDP ACCEPTABLES", Toast.LENGTH_SHORT).show();
                ArrayList<User> uuu = userDb.cursorToUsers(userDb.getAllUsers());
                Log.v("TAG", " LIST OF USERS IS: " + uuu );
                toNextActivity(user);
            }
        }
    }

    public void onClickNvCompte(View view)
    {
        Intent intent = new Intent(this, ActivityNvCompte.class);
        startActivity(intent);
    }


    public void onClickRESET(View view)
    {
        Button reset = findViewById(R.id.button2);
        userDb.removeAllUser();
        UnivDbAdapteur univDbAdapteur = new UnivDbAdapteur(this);
        univDbAdapteur.removeAllUniv();
        InstDbAdapteur instDbAdapteur = new InstDbAdapteur(this);
        instDbAdapteur.removeAllInst();
        FormDbAdapteur formDbAdapteur = new FormDbAdapteur(this);
        formDbAdapteur.removeAllForm();

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

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation Sortie")
                .setMessage("Voulez-vous quitter l'application?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {


                        QuitApplication();


                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void QuitApplication(){

        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);

    }
}
