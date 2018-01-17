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

public class FormDbAdapteur
{
    private static final int    FORMBD_VERSION = 1         ;
    private static final String FORMBD_NOM     = "formations.db";

    private SQLiteDatabase formBD;
    private FormBDOpenHelper baseHelper;

    public static abstract class table_form
    {
        public static final String TABLE_FORM     = "table_form";
        public static final String COL_ID         = "id";
        public static final int COL_ID_ID         = 0;
        public static final String COL_NOM_FORM   = "nom_form";
        public static final int COL_NOM_FORM_ID   = 1;
        public static final String COL_TYPE       = "type_form";
        public static final int COL_TYPE_ID       = 2;
        public static final String COL_DESC        = "desc_form";
        public static final int COL_DESC_ID        = 3;
        public static final String COL_IMG         = "img_form";
        public static final int COL_IMG_ID         = 4;
        public static final String COL_INST        = "id_inst";
        public static final int COL_INST_ID        = 5;
        public static final String COL_INST_NOM    = "nom_inst";
        public static final int COL_INST_NOM_ID    = 6;
        public static final String COL_UNIV        = "id_univ";
        public static final int COL_UNIV_ID        = 7;
        public static final String COL_UNIV_NOM    = "nom_univ";
        public static final int COL_UNIV_NOM_ID    = 8;


        static final String primary_key     = COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,";
        static final String column_2        = COL_NOM_FORM +" TEXT NOT NULL UNIQUE,";
        static final String column_3        = COL_TYPE +" TEXT NOT NULL,";
        static final String column_4        = COL_DESC +" TEXT,";
        static final String column_5        = COL_IMG +" BLOB,";
        static final String column_6        = COL_INST +" INTEGER,";
        static final String column_7        = COL_INST_NOM +" TEXT,";
        static final String column_8        = COL_UNIV +" INTEGER,";
        static final String column_9        = COL_UNIV_NOM +" TEXT";
        static final String columns         = " ("+primary_key + column_2 + column_3 + column_4
                +column_5 + column_6 + column_7 + column_8 + column_9
                +");";
        private static final String REQUETE_CREATION_FORMBD = "CREATE TABLE " + TABLE_FORM + columns;

    }

    private class FormBDOpenHelper extends SQLiteOpenHelper
    {

        public FormBDOpenHelper(Context context, String nom, SQLiteDatabase.CursorFactory cursorfactory, int version)
        {
            super(context, nom, cursorfactory, version);
            formBD = this.getWritableDatabase();
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(table_form.REQUETE_CREATION_FORMBD);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE " + table_form.TABLE_FORM+ ";");
            onCreate(db);
        }
    }


    public FormDbAdapteur(Context ctx)
    {
        baseHelper = new FormBDOpenHelper(ctx, FORMBD_NOM, null, FORMBD_VERSION);
        formBD = baseHelper.getWritableDatabase();
    }


    public SQLiteDatabase open()
    {
        formBD = baseHelper.getWritableDatabase();
        return formBD;
    }

    public void close()

    {
        formBD.close();
    }

    public SQLiteDatabase getBaseDonnees()

    {
        return formBD;
    }


    public Formation getForm(String nomForm)
    {
        String query = "SELECT * FROM "
                + table_form.TABLE_FORM+ " WHERE "+ table_form.COL_NOM_FORM
                + "=? ";
        Cursor cursor = formBD.rawQuery(query, new String[]{nomForm});
        return cursorToForm(cursor);
    }


    public Formation getForm(int id)
    {
        String query = "SELECT * FROM "
                + table_form.TABLE_FORM+ " WHERE "+ table_form.COL_ID
                + "=? " ;
        Cursor cursor = formBD.rawQuery(query, new String[]{Integer.toString(id)});
        return cursorToForm(cursor);
    }

    private Formation cursorToForm(Cursor c) {
        if (c.getCount() == 0)
            return null;

        Formation retForm = new Formation();

        c.moveToFirst();
        retForm.setIdInst   (c.getInt   (table_form.COL_ID_ID)      );
        retForm.setNomForm  (c.getString(table_form.COL_NOM_FORM_ID));
        retForm.setTypeForm (c.getString(table_form.COL_TYPE_ID)   );
        retForm.setDescForm (c.getString(table_form.COL_DESC_ID)    );
        retForm.setImgForm  (c.getBlob  (table_form.COL_IMG_ID)     );
        retForm.setIdInst   (c.getInt   (table_form.COL_INST_ID)     );
        retForm.setNomInst  (c.getString(table_form.COL_INST_NOM_ID));
        retForm.setIdUniv   (c.getInt   (table_form.COL_UNIV_ID)     );
        retForm.setNomUniv  (c.getString(table_form.COL_UNIV_NOM_ID));
        // Ferme le curseur pour libérer les ressources.
        c.close();
        return retForm;
    }

    public Cursor getAllForms() {
        Cursor cursor = formBD.rawQuery("SELECT * FROM " + table_form.TABLE_FORM, null);

        return cursor;
    }

    public Cursor getAllFormsByUnivID(int univId) {
        String query = "SELECT * FROM "
                + table_form.TABLE_FORM+ " WHERE "+ table_form.COL_UNIV_ID
                + "=? " ;
        Cursor cursor = formBD.rawQuery(query, new String[]{Integer.toString(univId)});

        return cursor;
    }

    public Cursor getAllFormsByInstID(int instId) {
        String query = "SELECT * FROM "
                + table_form.TABLE_FORM+ " WHERE "+ table_form.COL_INST_ID
                + "=? " ;
        Cursor cursor = formBD.rawQuery(query, new String[]{Integer.toString(instId)});

        return cursor;
    }

    public ArrayList<Formation> cursorToForms(Cursor c) {
        // Si la requête ne renvoie pas de résultat.
        if (c.getCount() == 0)
            return new ArrayList<Formation>(0);

        ArrayList<Formation> retForms = new ArrayList<Formation>(c.getCount());
        c.moveToFirst();
        do {
            Formation form = new Formation();
            form.setIdForm   (c.getInt   (table_form.COL_ID_ID)      );
            form.setNomForm  (c.getString(table_form.COL_NOM_FORM_ID));
            form.setTypeForm (c.getString(table_form.COL_TYPE_ID)   );
            form.setDescForm (c.getString(table_form.COL_DESC_ID)    );
            form.setImgForm  (c.getBlob  (table_form.COL_IMG_ID)     );
            form.setIdInst   (c.getInt   (table_form.COL_INST_ID)     );
            form.setNomInst  (c.getString(table_form.COL_INST_NOM_ID));
            form.setIdUniv   (c.getInt   (table_form.COL_UNIV_ID)     );
            form.setNomUniv  (c.getString(table_form.COL_UNIV_NOM_ID));
            retForms.add(form);
        }
        while (c.moveToNext());

        // Ferme le curseur pour libérer les ressources.
        c.close();
        return retForms;
    }


    public long insertForm(Formation form)
    {
        this.open();
        SQLiteDatabase db = getBaseDonnees();
        ContentValues contentValues = new ContentValues();
        contentValues.put(table_form.COL_NOM_FORM, form.getNomForm()  );
        contentValues.put(table_form.COL_TYPE   , form.getTypeForm());
        contentValues.put(table_form.COL_DESC    , form.getDescForm() );
        contentValues.put(table_form.COL_IMG     , form.getImgForm()  );
        contentValues.put(table_form.COL_INST     , form.getIdInst()  );
        contentValues.put(table_form.COL_INST_NOM , form.getNomInst()  );
        contentValues.put(table_form.COL_UNIV    , form.getIdUniv()  );
        contentValues.put(table_form.COL_UNIV_NOM , form.getNomUniv()  );
        return db.insert (table_form.TABLE_FORM   , null, contentValues);
    }

    public int updateForm(int id, Formation formToUpdate) {
        // Insérer le code de mise à jour de la base.
        int succesCode = -1;
        SQLiteDatabase db = getBaseDonnees();
        ContentValues contentValues = new ContentValues();
        contentValues.put(table_form.COL_NOM_FORM , formToUpdate.getNomForm()  );
        contentValues.put(table_form.COL_TYPE     , formToUpdate.getTypeForm());
        contentValues.put(table_form.COL_DESC     , formToUpdate.getDescForm() );
        contentValues.put(table_form.COL_IMG      , formToUpdate.getImgForm()  );
        contentValues.put(table_form.COL_INST     , formToUpdate.getIdInst()  );
        contentValues.put(table_form.COL_INST_NOM , formToUpdate.getNomInst()  );
        contentValues.put(table_form.COL_UNIV     , formToUpdate.getIdUniv()  );
        contentValues.put(table_form.COL_UNIV_NOM , formToUpdate.getNomUniv()  );


        return db.update(table_form.TABLE_FORM, contentValues, table_form.COL_ID+" = "+id, null);
    }

    public int updateForm(ContentValues valeurs, String where, String[] whereArgs) {
        return formBD.update(table_form.TABLE_FORM, valeurs, where, whereArgs);
    }


    public int removeForm(String nomForm) {
        int succesCode = -1;
        return succesCode;
    }

    public int removeForm(int formId) {
        SQLiteDatabase db = getBaseDonnees();
        return db.delete(table_form.TABLE_FORM , table_form.COL_ID + " = " +formId, null);
    }

    public int removeAllForm() {
        // Insérer le code de suppression d’une université.
        //String sql_delete = "DELETE FROM " + table_univ.TABLE_UNIV;

        return formBD.delete(table_form.TABLE_FORM,null,null);
    }
}
