package ma.ac.fst.ahniche_amine.guideuniversitaire;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Amine on 1/13/2018.
 */

public class UnivDbAdapteur
{
    private static final int    UNIVBD_VERSION = 1         ;
    private static final String UNIVBD_NOM     = "univs.db";

    private SQLiteDatabase   univBD;
    private UnivBDOpenHelper baseHelper;

    public static abstract class table_univ
    {
        public static final String TABLE_UNIV     = "table_univ";
        public static final String COL_ID         = "id";
        public static final int COL_ID_ID         = 0;
        public static final String COL_NOM_UNIV   = "nom_univ";
        public static final int COL_NOM_UNIV_ID   = 1;
        public static final String COL_VILLE      = "ville_univ";
        public static final int COL_VILLE_ID      = 2;
        public static final String COL_DESC       = "desc_univ";
        public static final int COL_DESC_ID       = 3;
        public static final String COL_IMG        = "img_univ";
        public static final int COL_IMG_ID        = 4;


        static final String primary_key     = COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,";
        static final String column_2        = COL_NOM_UNIV +" TEXT NOT NULL UNIQUE,";
        static final String column_3        = COL_VILLE +" TEXT NOT NULL,";
        static final String column_4        = COL_DESC +" TEXT,";
        static final String column_5        = COL_IMG +" BLOB";
        static final String columns         = " ("+primary_key + column_2 + column_3 + column_4
                +column_5
                +");";
        private static final String REQUETE_CREATION_UNIVBD = "CREATE TABLE " + TABLE_UNIV + columns;

    }

    private class UnivBDOpenHelper extends SQLiteOpenHelper
    {

        public UnivBDOpenHelper(Context context, String nom, SQLiteDatabase.CursorFactory cursorfactory, int version)
        {
            super(context, nom, cursorfactory, version);
            univBD = this.getWritableDatabase();
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(table_univ.REQUETE_CREATION_UNIVBD);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE " + table_univ.TABLE_UNIV+ ";");
            onCreate(db);
        }
    }


    public UnivDbAdapteur(Context ctx)
    {
        baseHelper = new UnivBDOpenHelper(ctx, UNIVBD_NOM, null, UNIVBD_VERSION);
        univBD = baseHelper.getWritableDatabase();
    }


    public SQLiteDatabase open()
    {
        univBD = baseHelper.getWritableDatabase();
        return univBD;
    }

    public void close()

    {
        univBD.close();
    }

    public SQLiteDatabase getBaseDonnees()

    {
        return univBD;
    }


    public Univ getUniv(String nomUniv)
    {
        String query = "SELECT * FROM "
                + table_univ.TABLE_UNIV+ " WHERE "+ table_univ.COL_NOM_UNIV
                + "=? ";
        Cursor cursor = univBD.rawQuery(query, new String[]{nomUniv});
        return cursorToUniv(cursor);
    }


    public Univ getUniv(int id)
    {
        String query = "SELECT * FROM "
                + table_univ.TABLE_UNIV + " WHERE "+ table_univ.COL_ID
                + "=? " ;
        Cursor cursor = univBD.rawQuery(query, new String[]{Integer.toString(id)});
        return cursorToUniv(cursor);
    }

    private Univ cursorToUniv(Cursor c) {
        // Si la requête ne renvoie pas de résultat.
        if (c.getCount() == 0)
            return null;

        Univ retUniv = new  Univ();

        // Extraction des valeurs depuis le curseur.
        c.moveToFirst();
        retUniv.setIdUniv   (c.getInt   (table_univ.COL_ID_ID)      );
        retUniv.setNomUniv  (c.getString(table_univ.COL_NOM_UNIV_ID));
        retUniv.setVilleUniv(c.getString(table_univ.COL_VILLE_ID)   );
        retUniv.setDescUniv (c.getString(table_univ.COL_DESC_ID)    );
        retUniv.setImgUniv  (c.getBlob  (table_univ.COL_IMG_ID)     );
        // Ferme le curseur pour libérer les ressources.
        c.close();
        return retUniv;
    }

    public Cursor getAllUnivs() {
        if(!univBD.isOpen())
        {
            univBD = this.open();
        }
        Cursor cursor = univBD.rawQuery("SELECT * FROM " + table_univ.TABLE_UNIV, null);

        return cursor;
    }


    public ArrayList<Univ> cursorToUnivs(Cursor c) {
        // Si la requête ne renvoie pas de résultat.
        if (c.getCount() == 0)
            return new ArrayList<Univ>(0);

        ArrayList<Univ> retUnivs = new ArrayList<Univ>(c.getCount());
        c.moveToFirst();
        do {
            Univ univ = new Univ();
            univ.setIdUniv   (c.getInt   (table_univ.COL_ID_ID)      );
            univ.setNomUniv  (c.getString(table_univ.COL_NOM_UNIV_ID));
            univ.setVilleUniv(c.getString(table_univ.COL_VILLE_ID)   );
            univ.setDescUniv (c.getString(table_univ.COL_DESC_ID)    );
            univ.setImgUniv  (c.getBlob  (table_univ.COL_IMG_ID)     );
            retUnivs.add(univ);
        }
        while (c.moveToNext());

        // Ferme le curseur pour libérer les ressources.
        c.close();
        return retUnivs;
    }


    public long insertUniv(Univ univ)
    {
        this.open();
        SQLiteDatabase db = getBaseDonnees();
        ContentValues contentValues = new ContentValues();
        contentValues.put(table_univ.COL_NOM_UNIV, univ.getNomUniv()  );
        contentValues.put(table_univ.COL_VILLE   , univ.getVilleUniv());
        contentValues.put(table_univ.COL_DESC    , univ.getDescUniv() );
        contentValues.put(table_univ.COL_IMG     , univ.getImgUniv()  );
        return db.insert(table_univ.TABLE_UNIV   , null, contentValues);
    }

    public long updateUniv(int id, Univ univToUpdate) {
        // Insérer le code de mise à jour de la base.
        SQLiteDatabase db = getBaseDonnees();
        ContentValues contentValues = new ContentValues();
        contentValues.put(table_univ.COL_NOM_UNIV, univToUpdate.getNomUniv()  );
        contentValues.put(table_univ.COL_VILLE   , univToUpdate.getVilleUniv());
        contentValues.put(table_univ.COL_DESC    , univToUpdate.getDescUniv() );
        contentValues.put(table_univ.COL_IMG     , univToUpdate.getImgUniv()  );
        return db.update(table_univ.TABLE_UNIV , contentValues, table_univ.COL_ID+" = "+id, null);

    }

    public int updateUniv(ContentValues valeurs, String where, String[] whereArgs) {
        return univBD.update(table_univ.TABLE_UNIV, valeurs, where, whereArgs);
    }


    public int removeUniv(String nomUniv) {
        // Insérer le code de suppression d’une université.
        int succesCode = -1;
        return succesCode;
    }

    public int removeAllUniv() {
        // Insérer le code de suppression d’une université.
        //String sql_delete = "DELETE FROM " + table_univ.TABLE_UNIV;

        return univBD.delete(table_univ.TABLE_UNIV,null,null);
    }

    public int removeUniv(int univId) {
        // Insérer le code de suppression d’une université.
        SQLiteDatabase db = getBaseDonnees();
        return db.delete(table_univ.TABLE_UNIV , table_univ.COL_ID + " = " +univId, null);
    }
}
