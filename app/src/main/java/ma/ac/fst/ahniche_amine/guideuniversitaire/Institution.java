package ma.ac.fst.ahniche_amine.guideuniversitaire;

/**
 * Created by Amine on 1/13/2018.
 */

public class Institution
{
    int    idInst    ;
    String nomInst   ;
    String villeInst ;
    byte[] imgInst   ;
    String descInst  ;
    int    idUniv    ;
    String nomUniv   ;
    public Institution()
    {
    }

    public String getNomUniv() {
        return nomUniv;
    }

    public void setNomUniv(String nomUniv) {
        this.nomUniv = nomUniv;
    }

    public Institution(int idInst, String nomInst, String villeInst, byte[] imgInst, String descInst, int idUniv, String nomUniv) {
        this.idInst = idInst;
        this.nomInst = nomInst;
        this.villeInst = villeInst;
        this.imgInst = imgInst;
        this.descInst = descInst;
        this.idUniv = idUniv;
        this.nomUniv = nomUniv;
    }

    public Institution(int idInst, String nomInst, String villeInst, byte[] imgInst, String descInst, int idUniv) {
        this.idInst = idInst;
        this.nomInst = nomInst;
        this.villeInst = villeInst;
        this.imgInst = imgInst;
        this.descInst = descInst;
        this.idUniv = idUniv;
    }

    public Institution(int idInst, String nomInst, String villeInst, String descInst, int idUniv) {
        this.idInst = idInst;
        this.nomInst = nomInst;
        this.villeInst = villeInst;
        this.descInst = descInst;
        this.idUniv = idUniv;
    }

    public int getIdInst() {
        return idInst;
    }

    public void setIdInst(int idInst) {
        this.idInst = idInst;
    }

    public String getNomInst() {
        return nomInst;
    }

    public void setNomInst(String nomInst) {
        this.nomInst = nomInst;
    }

    public String getVilleInst() {
        return villeInst;
    }

    public void setVilleInst(String villeInst) {
        this.villeInst = villeInst;
    }

    public byte[] getImgInst() {
        return imgInst;
    }

    public void setImgInst(byte[] imgInst) {
        this.imgInst = imgInst;
    }

    public String getDescInst() {
        return descInst;
    }

    public void setDescInst(String descInst) {
        this.descInst = descInst;
    }

    public int getIdUniv() {
        return idUniv;
    }

    public void setIdUniv(int idUniv) {
        this.idUniv = idUniv;
    }
}
