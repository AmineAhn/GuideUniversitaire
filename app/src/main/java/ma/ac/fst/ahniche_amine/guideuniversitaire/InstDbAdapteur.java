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

public class InstDbAdapteur
{
    private static final int    INSTBD_VERSION = 1         ;
    private static final String INSTBD_NOM     = "institutions.db";

    private SQLiteDatabase   instBD;
    private InstBDOpenHelper baseHelper;

    public static abstract class table_inst
    {
        public static final String TABLE_INST     = "table_inst";
        public static final String COL_ID         = "id";
        public static final int COL_ID_ID         = 0;
        public static final String COL_NOM_INST   = "nom_inst";
        public static final int COL_NOM_INST_ID   = 1;
        public static final String COL_VILLE      = "ville_inst";
        public static final int COL_VILLE_ID      = 2;
        public static final String COL_DESC       = "desc_inst";
        public static final int COL_DESC_ID       = 3;
        public static final String COL_IMG        = "img_inst";
        public static final int COL_IMG_ID        = 4;
        public static final String COL_UNIV       = "id_univ";
        public static final int COL_UNIV_ID       = 5;
        public static final String COL_UNIV_NOM   = "nom_univ";
        public static final int COL_UNIV_NOM_ID   = 6;


        static final String primary_key     = COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,";
        static final String column_2        = COL_NOM_INST +" TEXT NOT NULL UNIQUE,";
        static final String column_3        = COL_VILLE +" TEXT NOT NULL,";
        static final String column_4        = COL_DESC +" TEXT,";
        static final String column_5        = COL_IMG +" BLOB,";
        static final String column_6        = COL_UNIV+" INTEGER,";
        static final String column_7        = COL_UNIV_NOM +" TEXT";
        static final String columns         = " ("+primary_key + column_2 + column_3 + column_4
                +column_5 + column_6+ column_7
                +");";
        private static final String REQUETE_CREATION_INSTBD = "CREATE TABLE " + TABLE_INST + columns;

    }

    private class InstBDOpenHelper extends SQLiteOpenHelper
    {

        public InstBDOpenHelper(Context context, String nom, SQLiteDatabase.CursorFactory cursorfactory, int version)
        {
            super(context, nom, cursorfactory, version);
            instBD = this.getWritableDatabase();
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(table_inst.REQUETE_CREATION_INSTBD);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE " + table_inst.TABLE_INST+ ";");
            onCreate(db);
        }
    }


    public InstDbAdapteur(Context ctx)
    {
        baseHelper = new InstBDOpenHelper(ctx, INSTBD_NOM, null, INSTBD_VERSION);
        instBD = baseHelper.getWritableDatabase();
    }


    public SQLiteDatabase open()
    {
        instBD = baseHelper.getWritableDatabase();
        return instBD;
    }

    public void close()

    {
        instBD.close();
    }

    public SQLiteDatabase getBaseDonnees()

    {
        return instBD;
    }


    public Institution getInst(String nomInst)
    {
        String query = "SELECT * FROM "
                + table_inst.TABLE_INST+ " WHERE "+ table_inst.COL_NOM_INST
                + "=? ";
        Cursor cursor = instBD.rawQuery(query, new String[]{nomInst});
        return cursorToInst(cursor);
    }


    public Institution getInst(int id)
    {
        String query = "SELECT * FROM "
                + table_inst.TABLE_INST+ " WHERE "+ table_inst.COL_ID
                + "=? " ;
        Cursor cursor = instBD.rawQuery(query, new String[]{Integer.toString(id)});
        return cursorToInst(cursor);
    }

    private Institution cursorToInst(Cursor c) {
        if (c.getCount() == 0)
            return null;

        Institution retInst = new Institution();

        c.moveToFirst();
        retInst.setIdInst   (c.getInt   (table_inst.COL_ID_ID)      );
        retInst.setNomInst  (c.getString(table_inst.COL_NOM_INST_ID));
        retInst.setIdUniv   (c.getInt   (table_inst.COL_UNIV_ID)      );
        retInst.setNomUniv  (c.getString(table_inst.COL_UNIV_NOM_ID));
        retInst.setVilleInst(c.getString(table_inst.COL_VILLE_ID)   );
        retInst.setDescInst (c.getString(table_inst.COL_DESC_ID)    );
        retInst.setImgInst  (c.getBlob  (table_inst.COL_IMG_ID)     );
        // Ferme le curseur pour libérer les ressources.
        c.close();
        return retInst;
    }

    public Cursor getAllInsts() {
        Cursor cursor = instBD.rawQuery("SELECT * FROM " + table_inst.TABLE_INST, null);

        return cursor;
    }

    public Cursor getAllInstsByUnivID(int univId) {
        String query = "SELECT * FROM "
                + table_inst.TABLE_INST+ " WHERE "+ table_inst.COL_UNIV_ID
                + "=? " ;
        Cursor cursor = instBD.rawQuery(query, new String[]{Integer.toString(univId)});

        return cursor;
    }


    public ArrayList<Institution> cursorToInsts(Cursor c) {
        // Si la requête ne renvoie pas de résultat.
        if (c.getCount() == 0)
            return new ArrayList<Institution>(0);

        ArrayList<Institution> retInsts = new ArrayList<Institution>(c.getCount());
        c.moveToFirst();
        do {
            Institution inst = new Institution();
            inst.setIdInst   (c.getInt   (table_inst.COL_ID_ID)      );
            inst.setNomInst  (c.getString(table_inst.COL_NOM_INST_ID));
            inst.setIdUniv   (c.getInt   (table_inst.COL_UNIV_ID)      );
            inst.setNomUniv  (c.getString(table_inst.COL_UNIV_NOM_ID));
            inst.setVilleInst(c.getString(table_inst.COL_VILLE_ID)   );
            inst.setDescInst (c.getString(table_inst.COL_DESC_ID)    );
            inst.setImgInst  (c.getBlob  (table_inst.COL_IMG_ID)     );
            retInsts.add(inst);
        }
        while (c.moveToNext());

        // Ferme le curseur pour libérer les ressources.
        c.close();
        return retInsts;
    }


    public long insertInst(Institution inst)
    {
        this.open();
        SQLiteDatabase db = getBaseDonnees();
        ContentValues contentValues = new ContentValues();
        contentValues.put(table_inst.COL_NOM_INST, inst.getNomInst()  );
        contentValues.put(table_inst.COL_UNIV    , inst.getIdUniv()  );
        contentValues.put(table_inst.COL_NOM_INST, inst.getNomInst()  );
        contentValues.put(table_inst.COL_UNIV_NOM, inst.getNomUniv()  );
        contentValues.put(table_inst.COL_VILLE   , inst.getVilleInst());
        contentValues.put(table_inst.COL_DESC    , inst.getDescInst() );
        contentValues.put(table_inst.COL_IMG     , inst.getImgInst()  );
        return db.insert (table_inst.TABLE_INST   , null, contentValues);
    }

    public long updateInst(int id, Institution instToUpdate) {
        // Insérer le code de mise à jour de la base.
        SQLiteDatabase db = getBaseDonnees();
        ContentValues contentValues = new ContentValues();
        contentValues.put(table_inst.COL_NOM_INST, instToUpdate.getNomInst()  );
        contentValues.put(table_inst.COL_UNIV    , instToUpdate.getIdUniv()  );
        contentValues.put(table_inst.COL_NOM_INST, instToUpdate.getNomInst()  );
        contentValues.put(table_inst.COL_UNIV_NOM, instToUpdate.getNomUniv()  );
        contentValues.put(table_inst.COL_VILLE   , instToUpdate.getVilleInst());
        contentValues.put(table_inst.COL_DESC    , instToUpdate.getDescInst() );
        contentValues.put(table_inst.COL_IMG     , instToUpdate.getImgInst()  );
        return db.update(table_inst.TABLE_INST, contentValues, table_inst.COL_ID+" = "+id, null);

    }

    public int updateInst(ContentValues valeurs, String where, String[] whereArgs) {
        return instBD.update(table_inst.TABLE_INST, valeurs, where, whereArgs);
    }


    public int removeInst(String nomInst) {
        int succesCode = -1;
        return succesCode;
    }

    public int removeInst(int instId) {
        SQLiteDatabase db = getBaseDonnees();
        return db.delete(table_inst.TABLE_INST, table_inst.COL_ID + " = " +instId, null);
    }

    public int removeAllInst() {
        // Insérer le code de suppression d’une université.
        //String sql_delete = "DELETE FROM " + table_univ.TABLE_UNIV;

        return instBD.delete(table_inst.TABLE_INST,null,null);
    }
}
