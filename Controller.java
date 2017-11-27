/* Controller.java
==============
PURPOSE: main controller  
AUTHOR:Jainlin Luo
CREATED: 2017-09-23
UPDATED: 
*/
package luojianl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.beans.binding.Bindings;

public class Controller {
    @FXML
    private RadioButton r1,r2,r3,r4,r5,r6,r7,r8,r9,r10,r11,r12,r13,r14,r15,r16,r17,r18,r19,r20,
            r21,r22,r23,r24,r25,r26,r27,r28;
    @FXML
    private Label count;
    @FXML
    private ComboBox yearBox;
    @FXML
    private TextField url,user,pass;
    @FXML
    private TableView<Movie> tbv;
    @FXML
    private TableColumn<Movie,String> colTitle,colRun,colYear;
    @FXML
    private Button connect;
    
    private static HashMap<String,Integer> idMatch;
    //obsList is the List displayed in our tableView,we add movie items in it
    private ObservableList<Movie> obsList= FXCollections.observableArrayList();
    //conList is a Stirng Oberservabale List only giving oberservable item to 
    //bind with some disableProperties.
    private ObservableList<String> conList=FXCollections.observableArrayList();
   //yearList is the observable list for the year comboBox 
    private ObservableList<Integer> yearList=FXCollections.observableArrayList();
    //toogle group for all the radio butons
    private ToggleGroup tg=new ToggleGroup();
    
    public void initialize(){
        initIdMatch();
        conList.add("notConnect");
        //add all the radio buttons into toggle group;
        r1.setToggleGroup(tg);r2.setToggleGroup(tg);r3.setToggleGroup(tg);
        r4.setToggleGroup(tg);r5.setToggleGroup(tg);r6.setToggleGroup(tg);
        r7.setToggleGroup(tg);r8.setToggleGroup(tg);r9.setToggleGroup(tg);
        r10.setToggleGroup(tg);r11.setToggleGroup(tg);r12.setToggleGroup(tg);
        r13.setToggleGroup(tg);r14.setToggleGroup(tg);r15.setToggleGroup(tg);
        r16.setToggleGroup(tg);r17.setToggleGroup(tg);r18.setToggleGroup(tg);
        r19.setToggleGroup(tg);r20.setToggleGroup(tg);r21.setToggleGroup(tg);
        r22.setToggleGroup(tg);r23.setToggleGroup(tg);r24.setToggleGroup(tg);
        r25.setToggleGroup(tg);r26.setToggleGroup(tg);r27.setToggleGroup(tg);
        r28.setToggleGroup(tg);
        //bind the tableview columns with differnt properties
        colRun.setCellValueFactory(new PropertyValueFactory<>("runningTime"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        
       //set the year combo list items
        tbv.setItems(obsList);
        for(int i=1874;i<=2017;i++){
        yearList.add((Integer)i);
        }
        yearBox.setItems(yearList);
        //the default selected year is 2017;
        yearBox.getSelectionModel().select((Integer)2017);
        //the default selected button is "any "button
        r1.setSelected(true);
        //disable all the combo box and radio buttons before any connection established
       for(Toggle t:tg.getToggles()){
           if (t instanceof RadioButton) {
                        ((RadioButton) t).disableProperty().bind(Bindings.stringValueAt(conList,0).isEqualTo("notConnect"));
            }
       }
        yearBox.disableProperty().bind(Bindings.stringValueAt(conList,0).isEqualTo("notConnect"));
    }
     JdbcHelper jdbc=new JdbcHelper();
     
 
 //initIDMatch is a method to start up a hashmap mapping genreId to genre name
    private static void initIdMatch(){
        idMatch=new HashMap<String,Integer>();
        idMatch.put("Action",1);idMatch.put("Adventure",2);idMatch.put("Adult",3);idMatch.put("Animation",4);
        idMatch.put("Comedy",5);idMatch.put("Crime",6);idMatch.put("Documentary",7);idMatch.put("Drama",8);
        idMatch.put("Fantasy",9);idMatch.put("Family",10);idMatch.put("Film-Noir",11);idMatch.put("Horror",12);
        idMatch.put("Musical",13);idMatch.put("Mystery",14);idMatch.put("Romance",15);idMatch.put("Sci-Fi",16);
        idMatch.put("Short",17);idMatch.put("Thriller",18);idMatch.put("War",19);idMatch.put("Western",20);
        idMatch.put("Biography",21);idMatch.put("History",22);idMatch.put("Sport",23);idMatch.put("Reality-TV",24);
        idMatch.put("News",25);idMatch.put("Talk-Show",26);idMatch.put("Game-Show",27);
    }
    
    //Connectbutton is a method bind to "connnect", it will establish a connection with 
    //DB, disconnect if alreday connected, and throws alert window if things go wrong
    @FXML
    public void connectButton()throws Exception{
        if(connect.getText().equals("Connect")){
        String link=url.getText();
        String use=user.getText();
        String password=pass.getText();
        if(jdbc.connect(link,use,password)){
            setGenre();
            tbv.refresh();
            connect.setText("Disconnect");
            conList.clear();
            conList.add("Connect");
        }
        else{
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("User or Pass Incorrect  "+jdbc.getMsg());
            alert.showAndWait();
            }
        }
        else {
            jdbc.disconnect();
            connect.setText("Connect");
            obsList.clear();
            conList.clear();
            conList.add("notConnect");
            count.setText("");
            }
    }
    //to display different rows in DB in my tableview base on the genre/year chosen
    public void setGenre()throws Exception{
        //clear the obsList at the start
        obsList.clear();
        //get the text on the selected radio button
        RadioButton selected=(RadioButton)tg.getSelectedToggle();
        String genreRadio=selected.getText();
        //get the selected year store it in selectedyear
        Integer selectedYear;
        selectedYear=(Integer)yearBox.getSelectionModel().getSelectedItem();
        String title;
        //set up fields for the instance of movie
        ArrayList<String> genres=new ArrayList<>();
        int year;
        int runTime;
        //when "any" is selected
        if(genreRadio.equals("Any")){
            String sql="SELECT * FROM Movie WHERE year=?";
            PreparedStatement pstmt=jdbc.connection.prepareStatement(sql);
            pstmt.setInt(1,selectedYear);
            ResultSet resultSet=pstmt.executeQuery();
            while (resultSet.next()){
                title=resultSet.getString("title");
                year=resultSet.getInt("year");
                runTime=resultSet.getInt("runningTime");
                Movie movie=new Movie(title,year,runTime,genres);
                obsList.add(movie);
            }
            //set the count label's text to the size of new obsList
            count.setText(String.valueOf(obsList.size()));
        }
        //when the other radio buttons are selected 
        else{
            String sql="SELECT * FROM Movie INNER JOIN MovieGenre ON Movie.id=MovieGenre.movieId WHERE (MovieGenre.genreId=? AND year=?)";
            Integer genreId=idMatch.get(genreRadio);
            PreparedStatement pstmt=jdbc.connection.prepareStatement(sql);
            pstmt.setInt(1,genreId);
            pstmt.setInt(2,selectedYear);
            ResultSet resultSet=pstmt.executeQuery();        
            while (resultSet.next()){
                title=resultSet.getString("title");
                year=resultSet.getInt("year");
                runTime=resultSet.getInt("runningTime");
                Movie movie=new Movie(title,year,runTime,genres);
                obsList.add(movie);
                } 
            count.setText(String.valueOf(obsList.size()));
            }
        } 
    }
