package ma.ac.fst.ahniche_amine.guideuniversitaire;

import android.Manifest;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
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

public class ActivityNvInst extends AppCompatActivity {

    EditText etNomInst;
    AutoCompleteTextView actvVille;
    AutoCompleteTextView actvUniv;
    EditText etDesc;
    ImageView ivImg;
    Button btnAjouterImgInst;
    Button btnAjUniv;
    Button btnOk;

    String[] villes  ;
    String[] univs   ;
    ArrayList<Univ> allUnivs;

    Institution inst;
    boolean  imgIsSet;

    InstDbAdapteur instDbAdapteur;
    SQLiteDatabase instDb;
    final int REQUEST_CODE_GALLERY = 1;
    final int REQUEST_CODE_UNIV    = 2;

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

    String    imgDecodableString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nv_inst);
        initComponents();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("NOM_INST", etNomInst.getText().toString());
        outState.putString("VILLE", actvVille.getText().toString());
        outState.putString("NOM_UNIV", actvUniv.getText().toString());
        outState.putString("DESC", etDesc.getText().toString());
        Toast.makeText(this, " ETAT D INSTANCE SAUVEGARDÉ", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        retrouverUserConnected();
        actvVille.setText(savedInstanceState.getString("VILLE"));
        actvUniv.setText(savedInstanceState.getString("NOM_UNIV"));
        etNomInst.setText(savedInstanceState.getString("NOM_INST"));
        etDesc.setText(savedInstanceState.getString("DESC"));


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK
                    && null != data) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);

                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView =findViewById(R.id.ivImg);
                // Set the Image in ImageView after decoding the String
                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));
                imgIsSet = true;

            }
            else if (requestCode == REQUEST_CODE_UNIV)
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
            }
            else {
                Toast.makeText(this, "Vous n'avez pas choisi une image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }


    private void initComponents()
    {
        retrouverUserConnected();
        instDbAdapteur = new InstDbAdapteur(this);
        instDb = instDbAdapteur.getBaseDonnees();
        setupAutoCompleteTVVilles();
        setupAutoCompleteTVUnivs();
        etDesc= findViewById(R.id.etDesc);
        etNomInst = findViewById(R.id.etNomInst);
        ivImg= findViewById(R.id.ivImg);
        imgIsSet = false;
        btnAjouterImgInst= findViewById(R.id.btnAjouterImgInst);
        btnAjUniv        = findViewById(R.id.btnAjUniv);
        btnOk= findViewById(R.id.btnOk);
        inst = new Institution();
    }

    public void onClickAjouterImg(View view)
    {
        ActivityCompat.requestPermissions(
                ActivityNvInst.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_GALLERY
        );
        chargerImgDepuisGallery();
    }

    public void onClickOk(View view)
    {
        if(!etNomInst.getText().toString().isEmpty()
                ||!actvVille.getText().toString().isEmpty()
                ||!actvUniv.getText().toString().isEmpty()
                ||!etDesc.getText().toString().isEmpty())
        {
            String nomInst   = etNomInst.getText().toString();
            String nomUniv   = actvUniv.getText().toString();
            String villeInst = actvVille.getText().toString();
            String descInst  = etDesc.getText().toString()   ;
            byte[] imgInst   = null;
            if(imgIsSet)
            {
                imgInst = imageViewToByte(ivImg);
            }
            else
            {
                imgInst = DrawableToByteArray(R.drawable.univ);
            }
            Institution inst = new Institution();
            inst.setNomInst(nomInst);
            inst.setIdUniv(findUnivByNomUniv(nomUniv));
            inst.setNomUniv(nomUniv);
            inst.setVilleInst(villeInst);
            inst.setDescInst(descInst);
            inst.setImgInst(imgInst);
            long resultat = instDbAdapteur.insertInst(inst);
            if(resultat == -1)
            {
                Toast.makeText(this, "Insetion échoué!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this,"Insertion réussieª ... resultat action à modifier",Toast.LENGTH_LONG);
                if(getIntent() != null) // ActivityResult appelée
                {
                    sendInstAsResult(inst);
                }
                toNextActivity(userConnected, ActivityAccueil.class);
            }
        }
        else
        {
            Toast.makeText(this, "Veuillez remplir tous les champs!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupAutoCompleteTVVilles()
    {
        villes = new String[]{
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
        actvVille= findViewById(R.id.actvVille);
        actvVille.setThreshold(1);//will start working from first character
        actvVille.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView


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
                (this,android.R.layout.select_dialog_item,univs);
        //Getting the instance of AutoCompleteTextView
        actvUniv= findViewById(R.id.actvUniv);
        actvUniv.setThreshold(1);//will start working from first character
        actvUniv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView


    }

    private int findUnivByNomUniv(String nomUniv)
    {
        UnivDbAdapteur adapteur = new UnivDbAdapteur(this);
        //allUnivs = new ArrayList<>();
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


    public void chargerImgDepuisGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
    }
    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public void onClickAjouterUniv(View view) {

        toNextActivityForResult(userConnected, ActivityNvUniv.class, REQUEST_CODE_UNIV);
    }

    public void sendInstAsResult(Institution inst)
    {
        Intent returnIntent = getIntent();
        returnIntent.putExtra("NOM_INST",inst.getNomInst());
        returnIntent.putExtra(USER_ID      , userConnected.getIdUser () );
        returnIntent.putExtra(USER_NOM_USER, userConnected.getNomUser() );
        returnIntent.putExtra(USER_MDP_USER, userConnected.getMdpUser() );
        returnIntent.putExtra(USER_PRENOM  , userConnected.getPrenom () );
        returnIntent.putExtra(USER_NOM     , userConnected.getNom    () );
        returnIntent.putExtra(USER_EMAIL   , userConnected.getEmail  () );
        returnIntent.putExtra(USER_IMG     , userConnected.getImgUser() );
        returnIntent.putExtra(USER_FORM    , userConnected.getIdForm () );
        returnIntent.putExtra(USER_INST    , userConnected.getIdInst () );
        returnIntent.putExtra(USER_UNIV    , userConnected.getIdUniv () );
        returnIntent.putExtra(USER_COMP    , userConnected.isCompleted() );
        setResult(RESULT_OK,returnIntent);
        finish();
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

    public byte[] DrawableToByteArray(int drawbleId)
    {
        Resources res = getResources();
        Drawable drawable = res.getDrawable(drawbleId);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }
}
