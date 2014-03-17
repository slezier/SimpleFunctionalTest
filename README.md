SFT-tutoriel
============


I. Présentation de SimpleFunctionnalTest

 Les tests deviennent vites indispensables lors de l'élaboration d'un logiciel de qualité. 
 Ceux-ci sont de plusieurs types en fonction du périmètre testé et sont souvent automatisabes: tests unitaires, tests de charges, tests d'intégarations...
 Dans cette palette d'outil, deux typologie de test permettent de centrer le développement sur l'expression des besoins:
 les tests fonctionnels (permet de décrire d'un point de vue client ou utilisateur le logiciel) et les tests de validations (détermine les objectifs d'un développement et les moyens pour les valider).

 SimpleFunctionnalTest propose un moyen simple pour un développeur de rédiger rapidement des tests de validation ou des tests fonctionnels.


II. Méthodes 
 Afin de facilter le déroullement de ce tutoriels les sources utilisées sont disponibles sur github https://github.com/slezier/SFT-tutoriel et téléchargeable https://github.com/slezier/SFT-tutoriel/archive/master.zip .
 Ce projet utilisera maven 3.1, une JDK 1.6+ et l'IDE de votre choix.
 Un minimum de connaissance sur JUnit 1.4 et les tests utnitaires sont nécessaires.
 Le projet à couvrir est un distributeur automatique de billets (ATM en anglais) 

III. Premier Test
III.1  Rédaction d'un test humainement 'lisible'

 Premier fonctionnalité à tester: un retrait bancaire classique.
 Ce cas d'utilisation peut faire l'objet de différents 'Scénarios' de tests: Cas passsant (retrait autorisé), cas alternatifs (retrait refusé) cas en echec (échec de connexion à la banque).

 Une classe Java sera créée par cas d'utilisation. 
 Chaque tests unitaires de cette classe écriront un scénario.
 
 Le test doit être écrit de la façon la plus 'humaine' et la moins 'informatique' possible.

 Dans notre cas la classe de test s'appelera : RetraitBanquaire
 Le premier scénario ( retraitAutorisé ) n'est composé que d'appels de méthodes (fixtures) décrivant le scénario.
 Ces fixtures permettent d'interagir avec le logiciel; ce sont des méthodes non publiques.
 Des champs non publiques seront utilisés pourenrgistrer le contexte associé au teste.

package bancomat;


public class RetraitBancaire{

  @Test
  public void retraitAutorisé(){
    soitUnUtilisateurAyantUnCompteCreditéDe1000Euros();
    quandIlDemandeUnRetraitDe200Euros();
    alorsLeGuichetDistribue200Euros();
    leCompteEstAlorsCréditéDe800Euros();
  }

  private int compte ;
  private void soitUnUtilisateurAyantUnCompteCreditéDe1000Euros(){
    compte = 1000;
  }
  private void quandIlDemandeUnRetraitDe200Euros(){
	guichet.sEnregistre(utilisateur, pin);
  }
  private void alorsLeGuichetDistribue200Euros(){
  }
  private void leCompteEstAlorsCréditéDe800Euros(){
  }


}
  
  


  Ajouter dans votre descripteur de pr