package ma.ac.fst.ahniche_amine.guideuniversitaire;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by Amine on 1/11/2018.
 */

public class UserDbAdapteur
{


    private static final int    USERBD_VERSION = 1         ;
    private static final String USERBD_NOM     = "users.db";

    // L’instance de la base qui sera manipulée au travers de cette classe.
    private SQLiteDatabase userBD;
    private UserBDOpenHelper baseHelper;
    private Context mContext;
    // Classe interne contenant la définition des champs de la table table_universites.
    public static abstract class table_users
    {
        public static final String TABLE_USERS     = "table_users";
        public static final String COL_ID          = "id";
        public static final int COL_ID_ID          = 0;
        public static final String COL_NOM_USER    = "nom_user";
        public static final int COL_NOM_USER_ID    = 1;
        public static final String COL_MDP         = "mdp_user";
        public static final int COL_MDP_ID         = 2;
        public static final String COL_PRENOM      = "prenom";
        public static final int COL_PRENOM_ID      = 3;
        public static final String COL_NOM         = "nom";
        public static final int COL_NOM_ID         = 4;
        public static final String COL_EMAIL       = "email";
        public static final int COL_EMAIL_ID       = 5;
        public static final String COL_IMG         = "img_user";
        public static final int COL_IMG_ID         = 6;
        public static final String COL_INST        = "inst_id";
        public static final int COL_INST_ID        = 7;
        public static final String COL_UNIV        = "univ_id";
        public static final int COL_UNIV_ID        = 8;
        public static final String COL_FORM        = "form_id";
        public static final int COL_FORM_ID        = 9;
        public static final String COL_COMP        = "completed";
        public static final int COL_COMP_ID        = 10;

        // La requête de création de la structure de la base de données.

        static final String primary_key     = COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,";
        static final String column_2        = COL_NOM_USER +" TEXT NOT NULL UNIQUE,";
        static final String column_3        = COL_MDP +" TEXT NOT NULL,";
        static final String column_4        = COL_PRENOM +" TEXT NOT NULL,";
        static final String column_5        = COL_NOM +" TEXT NOT NULL,";
        static final String column_6        = COL_EMAIL +" TEXT UNIQUE,";
        static final String column_7        = COL_IMG +" BLOB,";
        static final String column_8        = COL_INST +" INTEGER,";
        static final String column_9        = COL_UNIV +" INTEGER,";
        static final String column_10        = COL_FORM +" INTEGER,";
        static final String column_11        = COL_COMP +" INTEGER ";
        static final String columns         = " ("+primary_key + column_2 + column_3
                + column_4 + column_5
                + column_6 + column_7 + column_8+ column_9 + column_10 + column_11 +" );";
        private static final String REQUETE_CREATION_USERBD = "CREATE TABLE " + TABLE_USERS + columns;

    }

    private class UserBDOpenHelper extends SQLiteOpenHelper
    {

        public UserBDOpenHelper(Context context, String nom, SQLiteDatabase.CursorFactory cursorfactory, int version)
        {
            super(context, nom, cursorfactory, version); // Création d'une base de données s'il n'est pas déjà créée.
            userBD = this.getWritableDatabase();
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(table_users.REQUETE_CREATION_USERBD);  // Création de la table table_universites.
            Log.d("TAG", " COLUMNS ARE: " + db.toString());
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE " + table_users.TABLE_USERS+ ";");
            onCreate(db);
        }
    }

    // Constructeur de la classe adaptateur UDBAdaptateur.

    // Ouvre la base de données en écriture.

    public UserDbAdapteur(Context ctx)
    {
        mContext = ctx;
        baseHelper = new UserBDOpenHelper(ctx, USERBD_NOM, null, USERBD_VERSION);
        userBD = baseHelper.getWritableDatabase();
    }


    public SQLiteDatabase open()
    {
        userBD = baseHelper.getWritableDatabase();
        return userBD;
    }

    public void close()

    {
        userBD.close();
    }

    public SQLiteDatabase getBaseDonnees()

    {
        return userBD;
    }


    public User getUser(String nomUser)
    {
        String query = "SELECT * FROM "
                + table_users.TABLE_USERS + " WHERE "+table_users.COL_NOM_USER
                + "=? ";
        Cursor cursor = userBD.rawQuery(query, new String[]{nomUser});
        return cursorToUser(cursor);
    }
    public User getUser(String nomUser, String mdp)
    {
        String query = "SELECT * FROM "
                + table_users.TABLE_USERS + " WHERE "+table_users.COL_NOM_USER
                + "=? " + "AND "+table_users.COL_MDP + "=?";
        Cursor cursor = userBD.rawQuery(query, new String[]{nomUser,mdp});
        return cursorToUser(cursor);
    }

    public User getUser(int id)
    {
        String query = "SELECT * FROM "
                + table_users.TABLE_USERS + " WHERE "+table_users.COL_ID
                + "=? " ;
        Cursor cursor = userBD.rawQuery(query, new String[]{Integer.toString(id)});
        return cursorToUser(cursor);
    }

    private User cursorToUser(Cursor c) {
        // Si la requête ne renvoie pas de résultat.
        if (c.getCount() == 0)
            return null;

        User retUser = new  User();

        // Extraction des valeurs depuis le curseur.
        c.moveToFirst();
        retUser.setIdUser(c.getInt(table_users.COL_ID_ID))          ;
        retUser.setNomUser(c.getString(table_users.COL_NOM_USER_ID));
        retUser.setMdpUser(c.getString(table_users.COL_MDP_ID))     ;
        retUser.setPrenom(c.getString(table_users.COL_PRENOM_ID))   ;
        retUser.setNom(c.getString(table_users.COL_NOM_ID));
        retUser.setEmail(c.getString(table_users.COL_EMAIL_ID));
        retUser.setImgUser(c.getBlob(table_users.COL_IMG_ID));
        retUser.setIdUniv(c.getInt(table_users.COL_UNIV_ID));
        retUser.setIdForm(c.getInt(table_users.COL_FORM_ID));
        retUser.setIdInst(c.getInt(table_users.COL_INST_ID));
        // Ferme le curseur pour libérer les ressources.
        c.close();
        return retUser;
    }

    public Cursor getAllUsers() {
        Cursor cursor = userBD.rawQuery("SELECT * FROM " + table_users.TABLE_USERS, null);
        return cursor;
    }


    public ArrayList<User> cursorToUsers(Cursor c) {
        // Si la requête ne renvoie pas de résultat.
        if (c.getCount() == 0)
            return new ArrayList<User>(0);

        ArrayList<User> retUsers = new ArrayList<User>(c.getCount());
        c.moveToFirst();
        do {
            User user = new User();
            user.setIdUser(c.getInt(table_users.COL_ID_ID))          ;
            user.setNomUser(c.getString(table_users.COL_NOM_USER_ID));
            user.setMdpUser(c.getString(table_users.COL_MDP_ID))     ;
            user.setPrenom(c.getString(table_users.COL_PRENOM_ID))   ;
            user.setNom(c.getString(table_users.COL_NOM_ID));
            retUsers.add(user);
        }
        while (c.moveToNext());

        // Ferme le curseur pour libérer les ressources.
        c.close();
        return retUsers;
    }


    public long insertUserAlpha(User user)
    {
        this.open();
        SQLiteDatabase db = getBaseDonnees();
        ContentValues contentValues = new ContentValues();
        contentValues.put(table_users.COL_NOM_USER, user.getNomUser());
        contentValues.put(table_users.COL_MDP, user.getMdpUser());
        contentValues.put(table_users.COL_PRENOM, user.getPrenom());
        contentValues.put(table_users.COL_NOM, user.getNom());
        contentValues.put(table_users.COL_COMP, 0);
        contentValues.put(table_users.COL_EMAIL, "null");

        contentValues.put(table_users.COL_IMG, drawableIdToByteArray(R.drawable.temp_profil));
        contentValues.put(table_users.COL_UNIV, -1);
        contentValues.put(table_users.COL_INST, -1);
        contentValues.put(table_users.COL_FORM, -1);
        return db.insert(table_users.TABLE_USERS, null, contentValues);
    }


    public int updateUser(int id, User userToUpdate) {

        int succesCode = -1;
        SQLiteDatabase db = getBaseDonnees();
        ContentValues contentValues = new ContentValues();

        if(userToUpdate.isCompleted())
        {
            contentValues.put(table_users.COL_NOM_USER, userToUpdate.getNomUser());
            contentValues.put(table_users.COL_MDP, userToUpdate.getMdpUser());
            contentValues.put(table_users.COL_PRENOM, userToUpdate.getPrenom());
            contentValues.put(table_users.COL_NOM, userToUpdate.getNom());
            contentValues.put(table_users.COL_EMAIL, userToUpdate.getEmail());
            contentValues.put(table_users.COL_IMG, userToUpdate.getImgUser());
            contentValues.put(table_users.COL_INST, userToUpdate.getIdInst());
            contentValues.put(table_users.COL_UNIV, userToUpdate.getIdUniv());
            contentValues.put(table_users.COL_FORM, userToUpdate.getIdForm());
        }
        return succesCode;
    }

    public int updateUser(ContentValues valeurs, String where, String[] whereArgs) {
        return userBD.update(table_users.TABLE_USERS, valeurs, where, whereArgs);
    }


    public int removeUser(String nomUser) {

        int succesCode = -1;
        return succesCode;
    }


    public int removeUser(int userId) {

        int succesCode = -1;
        return succesCode;
    }

    public int removeAllUser() {
        // Insérer le code de suppression d’une université.
        //String sql_delete = "DELETE FROM " + table_univ.TABLE_UNIV;

        return userBD.delete(table_users.TABLE_USERS,null,null);
    }

    private byte[] drawableIdToByteArray(int resId)
    {
        Drawable d = ResourcesCompat.getDrawable(mContext.getResources(), resId, null);
        Bitmap bmp = ((BitmapDrawable) d).getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();

    }
}
