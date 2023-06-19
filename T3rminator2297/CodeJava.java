//Classe TP2_PPC
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TP2_PPC extends JFrame
{
    //déclarations des différentes variables champs à entrer et boutons
    private JButton boutonCreer;
    private JButton boutonAfficher;
    private JButton boutonRechercher;
    private JTextField champsNom;
    private JTextField champsPrenom;
    private JTextField champsAge;
    private JTextField champsRecherche;

    public TP2_PPC()
    {
        //Constructeur
        super("Application de gestion des employées");

        //Creation des boutons
        boutonCreer = new JButton("Créer");
        boutonAfficher = new JButton("Afficher");
        boutonRechercher = new JButton("Rechercher");
        //Création des champs de texte
        champsNom = new JTextField(20);
        champsPrenom = new JTextField(20);
        champsAge = new JTextField(20);
        champsRecherche = new JTextField(20);

        //Créations des ActionListener sur les boutons
        boutonCreer.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                creerEmploye();
            }
        });
        boutonAfficher.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                afficherEmploye();
            }
        });
        boutonRechercher.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                rechercherEmploye();
            }
        });

        //Ajout des composants sur l'interface graphique
        add(new JLabel("Nom:"));
        add(champsNom);
        add(new JLabel("Prénom:"));
        add(champsPrenom);
        add(new JLabel("Age:"));
        add(champsAge);
        add(new JLabel("Rechercher:"));
        add(champsRecherche);
        add(boutonCreer);
        add(boutonAfficher);
        add(boutonRechercher);

        //Ferme le programme à l'appui du X rouge
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Formater l'interface graphique pour un meilleur affichage
        setLayout(new FlowLayout());
        pack();

        //Afficher l'interface graphique
        setVisible(true);

    }
    //Creation de la methode creerEmployer afin d'implementer une action au bouton Creer
    private void creerEmploye() {
        // Récupérer les valeurs des champs de saisie
        String nom = champsNom.getText();
        String prenom = champsPrenom.getText();
        int age = Integer.parseInt(champsAge.getText());

        // Créer un nouvel objet Employe avec les valeurs saisies
        Employe employe = new Employe(nom, prenom, age);

        try {
            File employesFile = new File("employes.bin");

            List<Employe> employesExistant = new ArrayList<>();

            //Vérifie que le fichier existe
            if (employesFile.exists())
            {
                //Gestion des exceptions
                try (
                        FileInputStream fis = new FileInputStream(employesFile);
                        ObjectInputStream ois = new ObjectInputStream(fis)
                )
                {
                    // Lire la liste des employés existants depuis le fichier
                    employesExistant = (List<Employe>) ois.readObject();
                }
            }

            // Ajouter le nouvel employé à la liste
            employesExistant.add(employe);

            try (
                    FileOutputStream fos = new FileOutputStream(employesFile);
                    ObjectOutputStream oos = new ObjectOutputStream(fos)
            )
            {
                // Écrire la liste mise à jour dans le fichier
                oos.writeObject(employesExistant);
                System.out.println("Employé créé et enregistré dans le fichier binaire.");
            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    //Création de la méthode AfficherEmploye afin d'implémenter une action sur le bouton Afficher
    private void afficherEmploye()
    {
        try {
            // Ouvrir le flux d'entrée vers le fichier "employes.bin"
            FileInputStream fis = new FileInputStream("employes.bin");
            ObjectInputStream ois = new ObjectInputStream(fis);

            // Lire la liste des employés depuis le fichier
            List<Employe> employes = (List<Employe>) ois.readObject();
            System.out.println("Liste des employés :");

            // Parcourir la liste des employés et afficher les informations de chaque employé
            for (Employe employe : employes)
            {
                System.out.println("Nom : " + employe.getNom());
                System.out.println("Prénom : " + employe.getPrenom());
                System.out.println("Âge : " + employe.getAge());
                System.out.println("----------------------");
            }

            // Fermer les flux de données
            ois.close();
            fis.close();
        } catch (IOException ex)
        {
            // Gérer les erreurs d'entrée/sortie
            ex.printStackTrace();
        } catch (ClassNotFoundException ex)
        {
            // Gérer les erreurs de classe non trouvée lors de la désérialisation
            ex.printStackTrace();
        }
    }


    //Créations de la méthode rechercherEmploye afin de trouver un employé existant dans le fichier
    private void rechercherEmploye()
    {
        // Récupérer le nom à rechercher depuis le champ de saisie
        String nomRecherche = champsRecherche.getText();
        boolean trouve = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("employes.bin")))
        {
            String ligne;
            // Lire chaque ligne du fichier
            while ((ligne = reader.readLine()) != null)
            {
                // Vérifier si la ligne contient le nom recherché
                if (ligne.contains(nomRecherche))
                {
                    System.out.println("Le nom '" + nomRecherche + "' a été trouvé dans le fichier.");
                    trouve = true;
                    break; // Sortir de la boucle une fois que le nom est trouvé
                }
            }
            // Si le nom n'a pas été trouvé
            if (!trouve)
            {
                System.out.println("Le nom '" + nomRecherche + "' n'a pas été trouvé dans le fichier.");
            }
        } catch (IOException ex)
        {
            // Gérer les erreurs d'entrée/sortie
            ex.printStackTrace();
        }
    }
}

//Classe Employe
import java.io.Serializable;

public class Employe implements Serializable
{
    //Déclaration des attributs
    private String nom;
    private String prenom;
    private int age;

    //Création constructeur
    public Employe(String nom, String prenom, int age)
    {
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
    }

    //Creation des getters/setters des attributs
    public String getNom()
    {
        return nom;
    }

    public void setNom(String nom)
    {
        this.nom = nom;
    }

    public String getPrenom()
    {
        return prenom;
    }

    public void setPrenom(String prenom)
    {
        this.prenom = prenom;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }
}

//classe Main
import javax.swing.*;
public class Main {
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                new TP2_PPC();
            }
        });
    }
}
