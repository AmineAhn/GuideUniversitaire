package ma.ac.fst.ahniche_amine.guideuniversitaire;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ActivityAccueil extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar              toolbar           ;
    FloatingActionButton fab               ;
    DrawerLayout         drawer            ;
    NavigationView       navigationView    ;
    Button               btnCompleterProfil;
    Button               btnConsulterUniv  ;
    Button               btnConsulterForm  ;
    Button               btnConsulterInst  ;

    ImageView            ivProfilPic       ;
    UnivDbAdapteur       univDbAdapteur    ;
    SQLiteDatabase       univDb            ;
    ArrayList<Univ>      univs             ;

    InstDbAdapteur        instDbAdapteur   ;
    SQLiteDatabase        instDb           ;
    ArrayList<Institution>insts            ;

    FormDbAdapteur       formDbAdapteur    ;
    SQLiteDatabase       formDb            ;
    ArrayList<Formation> forms             ;

    User                 userConnected     ;

    boolean univIsConsultation;
    boolean univIsAjout;
    boolean instIsConsultation;
    boolean instIsAjout;
    boolean formIsConsultation;
    boolean formIsAjout;
    boolean profilToBeCompleted;

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
        super.onCreate(savedInstanceState)       ;
        setContentView(R.layout.activity_accueil);
        retrouverUserConnected()                 ;
        remplirUnivs()                           ;
        remplirInsts();
        remplirForms();
        initComponents()                         ;
    }

    private void initComponents()
    {
        setupToolbar()             ;
        //setupFloatingActionButton();
        //setupDrawerLayout()        ;
        //setupNavigationView()      ;
        //setupBtnCompleterProfil()  ;
        setupBtnsConsultation()    ;

    }

    private void setupBtnsConsultation()
    {
        btnConsulterUniv   = findViewById(R.id.btnConsulterUniv)  ;
        btnConsulterForm   = findViewById(R.id.btnConsulterForm)  ;
        btnConsulterInst   = findViewById(R.id.btnConsulterInst)  ;
        if(univs.isEmpty())
        {
            univIsAjout = true;
            univIsConsultation = false;
            instIsAjout = true;
            instIsConsultation = false;
            formIsAjout = true;
            formIsConsultation = false;
            btnConsulterUniv.setText(getString(R.string.btnConsulterUniv2));
            btnConsulterInst.setText(getString(R.string.btnCatalogueInst2));
            btnConsulterForm.setText(getString(R.string.btnConsulerForm2));
            btnConsulterForm.setEnabled(false);
            btnConsulterInst.setEnabled(false);
            TextView tvCat = findViewById(R.id.tvCat);
            tvCat.setVisibility(View.VISIBLE);
        }
        else
        {
            univIsAjout = false;
            univIsConsultation = true;
            btnConsulterUniv.setText(getString(R.string.btnConsulterUniv));
            if(insts.isEmpty())
            {
                TextView tvCat = findViewById(R.id.tvCat);
                tvCat.setVisibility(View.VISIBLE);
                tvCat.setText("Liste des institutions est encore vide! Ajouter quelques élèments.");

                instIsAjout = true;
                instIsConsultation = false;
                formIsAjout = true;
                formIsConsultation = false;
                btnConsulterInst.setText(getString(R.string.btnCatalogueInst2));
                btnConsulterForm.setText(getString(R.string.btnConsulerForm2));
                btnConsulterForm.setEnabled(false);
                btnConsulterInst.setEnabled(true);

            }
            else
            {
                instIsAjout = false;
                instIsConsultation = true;
                btnConsulterInst.setText(getString(R.string.btnCatalogueInst));
                btnConsulterForm.setEnabled(true);
                btnConsulterInst.setEnabled(true);

                if(forms.isEmpty())
                {
                    TextView tvCat = findViewById(R.id.tvCat);
                    tvCat.setVisibility(View.VISIBLE);
                    tvCat.setText("Liste des formations est encore vide! Ajouter quelques élèments.");
                    formIsAjout = true;
                    formIsConsultation = false;
                    btnConsulterForm.setText(getString(R.string.btnConsulerForm2));
                }
                else
                {
                    TextView tvCat = findViewById(R.id.tvCat);
                    tvCat.setVisibility(View.INVISIBLE);
                    formIsAjout = false;
                    formIsConsultation = true;
                    btnConsulterForm.setText(getString(R.string.btnConsulerForm));
                }
            }
        }
    }

    private void setupBtnCompleterProfil()
    {
        btnCompleterProfil = findViewById(R.id.btnCompleterProfil);
        if(!userConnected.isCompleted())
        {
            TextView tvProfilInc = findViewById(R.id.tvProfilInc);
            tvProfilInc.setVisibility(View.VISIBLE);
            profilToBeCompleted = true;
            btnCompleterProfil.setVisibility(View.VISIBLE);
        }
        else
        {
            profilToBeCompleted = false;
            btnCompleterProfil.setVisibility(View.INVISIBLE);
            TextView tvProfilInc = findViewById(R.id.tvProfilInc);
            tvProfilInc.setVisibility(View.VISIBLE);

        }
    }

    private void setupNavigationView()
    {
        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this)       ;
        ivProfilPic = findViewById(R.id.ivProfilPic);
        if(userConnected.getImgUser() == null)
        {
            userConnected.setImgUser(DrawableToByteArray(R.drawable.temp_profil));
            Glide.with(this).load(R.drawable.temp_profil).into(ivProfilPic);
        }
        else
        {
            Glide.with(this).load(userConnected.getImgUser()).into(ivProfilPic);

        }
    }

    private void setupDrawerLayout()
    {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



    }

    private void setupFloatingActionButton()
    {
        /*fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    private void setupToolbar()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Confirmation Déconnexion")
                    .setMessage("Voulez-vous se déconnecter?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            toNextActivity(null, ActivityAuthentification.class);


                        }})
                    .setNegativeButton(android.R.string.no, null).show();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_accueil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up btnConsulterInst, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            new AlertDialog.Builder(this)
                    .setTitle("Confirmation Déconnexion")
                    .setMessage("Voulez-vous se déconnecter?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            toNextActivity(null, ActivityAuthentification.class);


                        }})
                    .setNegativeButton(android.R.string.no, null).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void remplirUnivs()
    {
        univs              = new ArrayList<>()                    ;
        univDbAdapteur     = new UnivDbAdapteur(this)        ;
        univDb             = univDbAdapteur.getBaseDonnees()      ;
        //formDbAdapteur.removeAllUniv();
        univs = univDbAdapteur.cursorToUnivs(univDbAdapteur.getAllUnivs());
    }

    private void remplirInsts()
    {
        insts              = new ArrayList<>()                    ;
        instDbAdapteur     = new InstDbAdapteur(this)        ;
        instDb             = instDbAdapteur.getBaseDonnees()      ;
        //formDbAdapteur.removeAllUniv();
        insts = instDbAdapteur.cursorToInsts(instDbAdapteur.getAllInsts());
    }

    private void remplirForms()
    {
        forms              = new ArrayList<>()                    ;
        formDbAdapteur     = new FormDbAdapteur(this)        ;
        formDb             = formDbAdapteur.getBaseDonnees()      ;
        //formDbAdapteur.removeAllUniv();
        forms = formDbAdapteur.cursorToForms(formDbAdapteur.getAllForms());
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

    public void onClickCompleterProfil(View view)
    {

    }
    public void onClickConsulterUniv(View view)
    {
        if(univIsConsultation)
        {
            toNextActivity(userConnected, ActivityUniv.class);

        }
        else if(univIsAjout)
        {
            toNextActivity(userConnected, ActivityNvUniv.class);
        }

    }

    public void onClickConsulterInst(View view)
    {
        if(instIsConsultation)
        {
            toNextActivity(userConnected, ActivityInst.class);
        }
        else if(instIsAjout)
        {
            toNextActivity(userConnected, ActivityNvInst.class);
        }

    }

    public void onClickConsulterForm(View view)
    {
        if(formIsConsultation)
        {
            toNextActivity(userConnected, ActivityForm.class);
        }
        else if(formIsAjout)
        {
            toNextActivity(userConnected, ActivityNvForm.class);
        }

    }

    private void toNextActivity(User user, Class aClass )
    {
        if(user == null)
        {
            Intent toNext = new Intent(this, aClass);
            startActivity(toNext);
        }
        else
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

    }
    public byte[] DrawableToByteArray(int drawbleId)
    {
        Resources res = getResources();
        Drawable drawable = res.getDrawable(drawbleId);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

}
