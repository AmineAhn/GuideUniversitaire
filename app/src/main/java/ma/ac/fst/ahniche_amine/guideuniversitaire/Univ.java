package ma.ac.fst.ahniche_amine.guideuniversitaire;

/**
 * Created by Amine on 1/13/2018.
 */

public class Univ
{
    int    idUniv   ;
    String nomUniv  ;
    String villeUniv;
    String descUniv ;
    byte[] imgUniv  ;

    public Univ() {
    }

    public Univ(String nomUniv, String villeUniv) {
        this.nomUniv = nomUniv;
        this.villeUniv = villeUniv;
    }

    public Univ(String nomUniv, String villeUniv, byte[] imgUniv) {
        this.nomUniv = nomUniv;
        this.villeUniv = villeUniv;
        this.imgUniv = imgUniv;
    }

    public Univ(int idUniv, String nomUniv, String villeUniv, byte[] imgUniv) {
        this.idUniv = idUniv;
        this.nomUniv = nomUniv;
        this.villeUniv = villeUniv;
        this.imgUniv = imgUniv;
    }

    public Univ(int idUniv, String nomUniv, String villeUniv, String descUniv, byte[] imgUniv) {
        this.idUniv    = idUniv   ;
        this.nomUniv   = nomUniv  ;
        this.villeUniv = villeUniv;
        this.imgUniv   = imgUniv  ;
        this.descUniv  = descUniv ;
    }

    public String getDescUniv() {
        return descUniv;
    }

    public void setDescUniv(String descUniv) {
        this.descUniv = descUniv;
    }

    public int getIdUniv() {
        return idUniv;
    }

    public void setIdUniv(int idUniv) {
        this.idUniv = idUniv;
    }

    public String getNomUniv() {
        return nomUniv;
    }

    public void setNomUniv(String nomUniv) {
        this.nomUniv = nomUniv;
    }

    public String getVilleUniv() {
        return villeUniv;
    }

    public void setVilleUniv(String villeUniv) {
        this.villeUniv = villeUniv;
    }

    public byte[] getImgUniv() {
        return imgUniv;
    }

    public void setImgUniv(byte[] imgUniv) {
        this.imgUniv = imgUniv;
    }
}
