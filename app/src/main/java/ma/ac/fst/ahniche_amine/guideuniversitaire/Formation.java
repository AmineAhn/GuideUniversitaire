package ma.ac.fst.ahniche_amine.guideuniversitaire;

/**
 * Created by Amine on 1/13/2018.
 */

public class Formation
{
    int    idForm  ;
    String nomForm ;
    String typeForm;
    String descForm;
    byte[] imgForm ;
    int    idInst  ;
    String nomInst;
    int    idUniv  ;
    String nomUniv;


    public Formation() {
    }

    public Formation(int idForm, String nomForm, String typeForm, String descForm, byte[] imgForm, int idInst, String nomInst, int idUniv, String nomUniv) {
        this.idForm = idForm;
        this.nomForm = nomForm;
        this.typeForm = typeForm;
        this.descForm = descForm;
        this.imgForm = imgForm;
        this.idInst = idInst;
        this.nomInst = nomInst;
        this.idUniv = idUniv;
        this.nomUniv = nomUniv;
    }

    public String getNomInst() {
        return nomInst;
    }

    public void setNomInst(String nomInst) {
        this.nomInst = nomInst;
    }

    public String getNomUniv() {
        return nomUniv;
    }

    public void setNomUniv(String nomUniv) {
        this.nomUniv = nomUniv;
    }

    public Formation(int idForm, String nomForm, String typeForm, String descForm, byte[] imgForm, int idInst, int idUniv) {
        this.idForm = idForm;
        this.nomForm = nomForm;
        this.typeForm = typeForm;
        this.descForm = descForm;
        this.imgForm = imgForm;
        this.idInst = idInst;
        this.idUniv = idUniv;
    }

    public Formation(int idForm, String nomForm, String typeForm, String descForm, int idInst, int idUniv) {
        this.idForm = idForm;
        this.nomForm = nomForm;
        this.typeForm = typeForm;
        this.descForm = descForm;
        this.imgForm = imgForm;
        this.idInst = idInst;
        this.idUniv = idUniv;
    }

    public int getIdForm() {
        return idForm;
    }

    public void setIdForm(int idForm) {
        this.idForm = idForm;
    }

    public String getNomForm() {
        return nomForm;
    }

    public void setNomForm(String nomForm) {
        this.nomForm = nomForm;
    }

    public String getTypeForm() {
        return typeForm;
    }

    public void setTypeForm(String typeForm) {
        this.typeForm = typeForm;
    }

    public String getDescForm() {
        return descForm;
    }

    public void setDescForm(String descForm) {
        this.descForm = descForm;
    }

    public byte[] getImgForm() {
        return imgForm;
    }

    public void setImgForm(byte[] imgForm) {
        this.imgForm = imgForm;
    }

    public int getIdInst() {
        return idInst;
    }

    public void setIdInst(int idInst) {
        this.idInst = idInst;
    }

    public int getIdUniv() {
        return idUniv;
    }

    public void setIdUniv(int idUniv) {
        this.idUniv = idUniv;
    }
}
