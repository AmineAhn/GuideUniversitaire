package ma.ac.fst.ahniche_amine.guideuniversitaire;

/**
 * Created by Amine on 1/11/2018.
 */

public class User
{
    int     idUser;
    String  nomUser;
    String  mdpUser;
    String  prenom;
    String  nom;
    String  email;
    byte[] imgUser;
    int     idInst;
    int     idUniv;
    int     idForm;
    boolean isCompleted;

    public User()
    {
        setCompleted(false);
    }

    public User(int idUser, String nomUser, String mdpUser, String prenom, String nom, String email, byte[] imgUser, int idInst, int idUniv, int idForm, boolean isCompleted) {
        this.idUser = idUser;
        this.nomUser = nomUser;
        this.mdpUser = mdpUser;
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.imgUser = imgUser;
        this.idInst = idInst;
        this.idUniv = idUniv;
        this.idForm = idForm;
        this.isCompleted = isCompleted;
    }

    public User(String nomUser, String mdpUser, String prenom, String nom, String email, byte[] imgUser, int idInst, int idUniv, int idForm) {
    this.nomUser = nomUser;
    this.mdpUser = mdpUser;
    this.prenom = prenom;
    this.nom = nom;
    this.email = email;
    this.imgUser = imgUser;
    this.idInst = idInst;
    this.idUniv = idUniv;
    this.idForm = idForm;
    setCompleted(true);
    }

    public User(int idUser)
    {
        this.idUser = idUser;
        setCompleted(false);
    }

    public User(int idUser, String nomUser, String mdpUser, String prenom, String nom)
    {
        this.idUser = idUser;
        this.nomUser = nomUser;
        this.mdpUser = mdpUser;
        this.prenom = prenom;
        this.nom = nom;
        setCompleted(false);
    }

    public User(int idUser, String nomUser, String mdpUser)
    {
        this.idUser = idUser;
        this.nomUser = nomUser;
        this.mdpUser = mdpUser;
        setCompleted(false);
    }

    public int getIdUser()
    {
        return idUser;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getImgUser() {
        return imgUser;
    }

    public void setImgUser(byte[] imgUser) {
        this.imgUser = imgUser;
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

    public int getIdForm() {
        return idForm;
    }

    public void setIdForm(int idForm) {
        this.idForm = idForm;
    }

    public void setIdUser(int idUser)
    {
        this.idUser = idUser;
    }

    public String getNomUser()
    {
        return nomUser;
    }

    public void setNomUser(String nomUser)
    {
        this.nomUser = nomUser;
    }

    public String getMdpUser()
    {
        return mdpUser;
    }

    public void setMdpUser(String mdpUser)
    {
        this.mdpUser = mdpUser;
    }

    public String getPrenom()
    {
        return prenom;
    }

    public void setPrenom(String prenom)
    {
        this.prenom = prenom;
    }

    public String getNom()
    {
        return nom;
    }

    public void setNom(String nom)
    {
        this.nom = nom;
    }
}
