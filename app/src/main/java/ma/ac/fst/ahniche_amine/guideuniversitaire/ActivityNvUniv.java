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

public class ActivityNvUniv extends AppCompatActivity {

    EditText etNomUniv;
    AutoCompleteTextView actvVille;
    EditText etDesc;
    ImageView ivImg;
    Button btnAjouterImgUniv;
    Button btnOk;

    String[] villes;
    ArrayList<Univ> allUnivs;
    boolean   imgIsSet;

    UnivDbAdapteur univDbAdapteur;
    SQLiteDatabase univDb;
    final int REQUEST_CODE_GALLERY = 999;

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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nv_univ);
        initComponents();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("VILLE", actvVille.getText().toString());
        outState.putString("NOM_UNIV", etNomUniv.getText().toString());
        outState.putString("DESC", etDesc.getText().toString());
        Toast.makeText(this, " ETAT D INSTANCE SAUVEGARDÉ", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        retrouverUserConnected();
        actvVille.setText(savedInstanceState.getString("VILLE"));
        etNomUniv.setText(savedInstanceState.getString("NOM_UNIV"));
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
                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));
                imgIsSet = true;

            } else {
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
        univDbAdapteur = new UnivDbAdapteur(this);
        univDb = univDbAdapteur.getBaseDonnees();
        setupAutoCompleteTVVilles();
        etNomUniv = findViewById(R.id.etNomInst);
        etDesc= findViewById(R.id.etDesc);
        ivImg= findViewById(R.id.ivImg);
        imgIsSet = false;
        btnAjouterImgUniv= findViewById(R.id.btnAjouterImgInst);
        btnOk= findViewById(R.id.btnOk);
    }

    public void onClickAjouterImg(View view)
    {
        ActivityCompat.requestPermissions(
                ActivityNvUniv.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_GALLERY
        );
        chargerImgDepuisGallery();
    }

    public void onClickOk(View view)
    {
        if(!etNomUniv.getText().toString().isEmpty()
                ||!actvVille.getText().toString().isEmpty()
                ||!etDesc.getText().toString().isEmpty())
        {
            String nomUniv   = etNomUniv.getText().toString();
            String villeUniv = actvVille.getText().toString();
            String descUniv  = etDesc.getText().toString()   ;
            byte[] imgUniv   = null;
            if(imgIsSet)
            {
                imgUniv = imageViewToByte(ivImg);
            }
            else
            {
                imgUniv = DrawableToByteArray(R.drawable.univ);
            }
            Univ univ = new Univ();
            univ.setNomUniv(nomUniv);
            univ.setVilleUniv(villeUniv);
            univ.setDescUniv(descUniv);
            univ.setImgUniv(imgUniv);
            long resultat = univDbAdapteur.insertUniv(univ);
            if(resultat == -1)
            {
                Toast.makeText(this, "Université déja existe!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                if(getIntent() != null) // ActivityResult appelée
                {
                    sendUnivAsResult(univ);
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

    public void chargerImgDepuisGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
    }
    public static byte[] imageViewToByte(ImageView image)
    {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public void sendUnivAsResult(Univ univ)
    {
            Intent returnIntent = getIntent();
            returnIntent.putExtra("NOM_UNIV",univ.getNomUniv());
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


    private int findUnivByNomUniv(String nomUniv)
    {
        allUnivs = new ArrayList<>();
        allUnivs = univDbAdapteur.cursorToUnivs(univDbAdapteur.getAllUnivs());
        for(int i = 0 ; i < allUnivs.size(); i++)
        {
            if(allUnivs.get(i).getNomUniv().equals(nomUniv))
            {
                return allUnivs.get(i).getIdUniv();
            }
        }
        return -1;
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
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
