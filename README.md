SFT-tutorial
============

# Functional and acceptance testing using SimpleFunctionalTest

A quality is an obvious attribute or a property.

Quality assurance, is a way of ensuring the presence of qualities.

Many kind of test ensure the software quality:
* Unit Test ensure the correctness of an algorithm
* Integration Test ensure the component is integrated
* Benchmark ensure the performance.
* ...

All of them (except maybe UI testing)


Les tests deviennent vites indispensables lors de l'laboration d'un logiciel de qualit&eacute;.
  
Ceux-ci sont de plusieurs types en fonction du p&eacute;rimêtre test et sont souvent automatisables: tests unitaires, tests de charges, tests d'int&eacute;grations...
Dans cette palette d'outils, deux types test permettent de centrer le dveloppement sur l'expression des besoins:

* les tests fonctionnels (permet de dcrire d'un point de vue client ou utilisateur le logiciel) 
* les tests de validations (dtermine les objectifs d'un dveloppement et les moyens pour les valider).

 SimpleFunctionnalTest propose un moyen simple pour un dveloppeur de rdiger rapidement des tests de validation ou des tests fonctionnels.


# M&eacute;thodes 
Afin de facilter le d&eacute;roullement de ce tutoriels les sources utilis&eacute;es sont disponibles sur [github](https://github.com/slezier/SFT-tutoriel) et [t&eacute;l&eacute;chargeable](https://github.com/slezier/SFT-tutoriel/archive/master.zip).

* Ce projet utilisera maven 3.1, une JDK 1.6+ et l'IDE de votre choix.
* Un minimum de connaissance sur JUnit 1.4 et les tests utnitaires sont n&eacute;cessaires.
* Le projet &agrave; couvrir est un distributeur automatique de billets (ATM en anglais) 

# Premier Test
##  R&eacute;daction d'un test humainement 'lisible'

 Premier fonctionnalit&eacute; &agrave; tester: un retrait bancaire classique.
 
 Ce _cas d'utilisation_ peut faire l'objet de diff&eacute;rents _Sc&eacute;narios_ de tests: Cas passant (retrait autoris&eacute;), cas alternatif (retrait refus&eacute;), cas en &eacute;chec (&eacute;chec de connexion &agrave; la banque).

 Une _classe_ Java sera cr&eacute;e par _cas d'utilisation_. 
 Chaque _tests unitaires_ de cette classe d&eacute;criront un _sc&eacute;nario_.
 
 Le test doit être &eacute;crit de la façon la plus 'humaine' et la moins 'informatique' possible.

 Dans notre cas la classe de test s'appelera : _RetraitBanquaire_.
 
 Le premier sc&eacute;nario ( _retraitAutoris&eacute;_ ) n'est compos&eacute; que d'appels de m&eacute;thodes non publique (_fixtures_) d&eacute;crivant le sc&eacute;nario.
 
 Ces _fixtures_ permettent d'interagir avec le logiciel.
 
 Des champs non publiques seront utilis&eacute;s pour enregistrer le contexte associ&eacute; au test.

package bancomat;


public class RetraitBancaire{

  @Test
  public void retraitAutoris(){
    soitUnUtilisateurAyantUnCompteCreditDe1000Euros();
    quandIlDemandeUnRetraitDe200Euros();
    alorsLeGuichetDistribue200Euros();
    leCompteEstAlorsCrditDe800Euros();
  }

  private int compte ;
  private void soitUnUtilisateurAyantUnCompteCreditDe1000Euros(){
    compte = 1000;
  }
  private void quandIlDemandeUnRetraitDe200Euros(){
	guichet.sEnregistre(utilisateur, pin);
  }
  private void alorsLeGuichetDistribue200Euros(){
  }
  private void leCompteEstAlorsCrditDe800Euros(){
  }


}
  
  

    /*
    Scenario 1: Account has sufficient funds
Given the account balance is \$100
 And the card is valid
 And the machine contains enough money
When the Account Holder requests \$20
Then the ATM should dispense \$20
 And the account balance should be \$80
 And the card should be returned

    Scenario 2: Account has insufficient funds
Given the account balance is \$10
 And the card is valid
 And the machine contains enough money
When the Account Holder requests \$20
Then the ATM should not dispense any money
 And the ATM should say there are insufficient funds
 And the account balance should be \$20
 And the card should be returned

Scenario 3: Card has been disabled
Given the card is disabled
When the Account Holder requests \$20
Then the ATM should retain the card
And the ATM should say the card has been retained
     */

  Ajouter dans votre descripteur de pr