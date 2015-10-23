/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fx.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import jpa.controller.PersonJpaController;
import model.Person;
import javafx.scene.control.ListCell;
import javafx.util.Callback;


/**
 *
 * @author Phil O'Connell <pxo4@psu.edu>
 */
public class FXMLDocumentController implements Initializable {

  @FXML
  private Label label;
  @FXML
  private Button button;
  @FXML
  private ListView<Person> lvPeople;
  @FXML
  private TextField tfFirstName;
  @FXML
  private TextField tfLastName;
  @FXML
  private Button btnAddPerson;
  @FXML
  private Button btnDelete;

  @FXML
  private void handleButtonAction(ActionEvent event) {
    System.out.println("You clicked me!");
    label.setText("Hello World!");
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
      lvPeople.setCellFactory(new Callback<ListView<Person>, ListCell<Person>>(){
  @Override
  public ListCell<Person> call(ListView<Person> param) {

    ListCell<Person> cell = new ListCell<Person>() {
      @Override
      public void updateItem(Person person, boolean empty) {
        super.updateItem(person, empty);
        if (person != null) {
          this.setText(person.getFirstName() + " " + person.getLastName());
        }
      }
    };
    return cell;
  }
});

    SyncPeopleListView();
  }

  public void SyncPeopleListView() {

    // Items inside the list
    ObservableList<Person> items = lvPeople.getItems();

    // Clear out the list
    items.clear();

    // Get list of all people in the database
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("PopulateListPU");
    PersonJpaController jpaPerson = new PersonJpaController(emf);
    List<Person> people = jpaPerson.findPersonEntities();

    // Add each person to the list
    for (Person p : people) {
lvPeople.getItems().add(p);
    }
  }

  @FXML
  private void handleBtnAddPersonClicked(MouseEvent event) {
    Person newPerson = new Person();
    newPerson.setFirstName(tfFirstName.getText());
    newPerson.setLastName(tfLastName.getText());

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("PopulateListPU");
    PersonJpaController jpaPerson = new PersonJpaController(emf);

    try {
      jpaPerson.create(newPerson);
    } catch (Exception ex) {
      Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
    }

    SyncPeopleListView();
  }

  @FXML
  private void handleBtnDeletePersonClicked(MouseEvent event) {
    Person selectedPerson = lvPeople.getSelectionModel().getSelectedItem();
    System.out.println("Delete " + selectedPerson.toString());


    System.out.println("Id: " + selectedPerson.getId());

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("PopulateListPU");
    PersonJpaController jpaPerson = new PersonJpaController(emf);

    try {
      jpaPerson.destroy(selectedPerson.getId());
    } catch (Exception ex) {
      Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
    }

    SyncPeopleListView();
  }

}
