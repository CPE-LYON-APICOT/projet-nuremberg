package fr.horizion.savoir.models;

import java.util.Date;
import java.util.regex.Pattern;

public class Etudiant {


    private String nom;
    private String prenom;
    private String adresse;
    private Date dateNaissance;
    private String email;

    public Etudiant(String nom, String prenom, String adresse, Date dateNaissance, String email) throws Exception {

        if (isValid(email)){
            this.email = email;
            this.nom = nom;
            this.prenom = prenom;
            this.adresse = adresse;
            this.dateNaissance = dateNaissance;
        }else{
            throw  new Exception("Email not valid");
        }

    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {

        this.dateNaissance = dateNaissance;
    }

    public String getEmail() {
        return email;
    }

    public static boolean isValid(String email) {

        // Regular expression to match valid email formats
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        // Compile the regex
        Pattern p = Pattern.compile(emailRegex);

        // Check if email matches the pattern
        return email != null && p.matcher(email).matches();
    }

    public void setEmail(String email) {
        if (isValid(email)) {
            this.email = email;
        } else {
            System.out.println("Erreur de validation");
        }
    }


    @Override
    public String toString() {
        return "nom:" + nom + " prenom:" + prenom + " adresse:" + adresse + " dateNaissance:" + dateNaissance + " email:" + email;
    }


}
