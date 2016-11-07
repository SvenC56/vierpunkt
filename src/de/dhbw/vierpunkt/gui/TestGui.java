package de.dhbw.vierpunkt.gui;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.util.Duration;
import javafx.util.StringConverter;
import java.awt.Toolkit;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.dhbw.vierpunkt.interfaces.ParamListener;
import de.dhbw.vierpunkt.interfaces.ZugListener;
import de.dhbw.vierpunkt.objects.NameListener;
/**
 *
 * @author janaschaub
 */
public class TestGui implements ZugListener {

	/****** VARIABLENDEKLARATION *****/
	

	
	
	/**
	 * Gibt den aktuellen Fuellstand aller Spalten an
	 */	
	static int[] plaetzeFreiInReihe = new int[7];
	private static List<NameListener> NameListeners = new ArrayList<NameListener>();
	private static List<ParamListener> listeners = new ArrayList<ParamListener>();
	
	public static Stage primaryStage = new Stage();
	
	// Einstellungen
	private String schnittstelle = "pusher";
	private int zugzeit = 2000;
	private char xodero = 'x';
	private String fileString = new String();
	
	// Pusher Credentials
	private String appId;
	private String appKey;
	private String appSecret;
	
	// Grid
	private int anzahlzeilen;
	private int anzahlspalten;
	private final int l = 70; 		// Seitenlaenge der Grids - spaeter manuelle Einstellung
	public int spieler = 1; 		// Spieler 1
	private double breite = Toolkit.getDefaultToolkit().getScreenSize().width; // Breite des Fensters in Pixeln
	private int thema = 1;
	
	// Spielernamen
	private String names1 = "Spieler1";
	private String names2 = "Spieler2";
	
	// Angaben aus anderen Klassen
	private Text spielstand = new Text("0 : 0");
	private Text satzstatus = new Text("Spiel noch nicht begonnen");
	
	
	//Erzeugen der Spielsteine
    public javafx.scene.image.Image image1 = new javafx.scene.image.Image(getClass().getResource("kuerbis.png").toExternalForm());
	public javafx.scene.image.Image image2 = new javafx.scene.image.Image(getClass().getResource("fledermaus.png").toExternalForm());
    public javafx.scene.image.Image image3 = new javafx.scene.image.Image(getClass().getResource("spielstein_grau.png").toExternalForm());
	public javafx.scene.image.Image kuerbis = new javafx.scene.image.Image(getClass().getResource("kuerbis.png").toExternalForm()); 
    public javafx.scene.image.Image fledermaus = new javafx.scene.image.Image(getClass().getResource("fledermaus.png").toExternalForm()); 
    public javafx.scene.image.Image pizza = new javafx.scene.image.Image(getClass().getResource("pizza.png").toExternalForm());
    public javafx.scene.image.Image burger = new javafx.scene.image.Image(getClass().getResource("burger.png").toExternalForm());
    public javafx.scene.image.Image basketball = new javafx.scene.image.Image(getClass().getResource("basketball.png").toExternalForm());
    public javafx.scene.image.Image baseball = new javafx.scene.image.Image(getClass().getResource("baseball.png").toExternalForm());
    public javafx.scene.image.Image orange = new javafx.scene.image.Image(getClass().getResource("spielstein_orange.png").toExternalForm());
    public javafx.scene.image.Image gruen = new javafx.scene.image.Image(getClass().getResource("spielstein_gruen.png").toExternalForm());
    
    // Variable fuer die Farbe des Spielfelds
    public Color color = Color.rgb(0, 0, 0);
    public GridPane spielfeld = new GridPane();
    
    // Getter und Setter Methoden
    public void setColor(Color color) {	this.color = color;}
	public void setImage1(javafx.scene.image.Image image1) {this.image1 = image1;}
	public void setImage2(javafx.scene.image.Image image2) {this.image2 = image2;}
	public void setImage3(javafx.scene.image.Image image3) {this.image3 = image3;}
	public int getZugzeit() {return zugzeit;}
	public void setZugzeit(int zugzeit) {this.zugzeit = zugzeit;}
	public String getSchnittstelle() {return schnittstelle;}
	public void setSchnittstelle(String schnittstelle) {this.schnittstelle = schnittstelle;}
	public String getNames1() {return names1;}
	public void setNames1(String names1){this.names1 = names1;}
	public String getNames2() {return names2;}
	public void setNames2(String names2){this.names2 = names2;}
	public char getXodero() {return xodero;}
	public void setXodero(char xodero) {this.xodero = xodero;}
	public javafx.scene.image.Image getImage1() {return image1;}
	public javafx.scene.image.Image getImage2() {return image2;}
	public void setSatzstatus(String satzstatus) {this.satzstatus.setText(satzstatus);}
	public void setSpielstand(String spielstand) {this.spielstand.setText(spielstand);}
	public Text getSpielstand() {return spielstand;}
	public Text getSatzstatus() {return satzstatus;}
	public String getFileSting(){return fileString;}

	

	/*********************************************************************************************************************
	 ******************************************* START METHODE *********************************************************
	 ********************************************************************************************************************/
	
	public void start(Stage primaryStage) {
		
		for (int i = 0; i < plaetzeFreiInReihe.length; i++){
			plaetzeFreiInReihe[i]=5;
		}
		
		// Grundlegende Eigenschaften der Stage
		primaryStage.setFullScreen(true); 		// automatisches Oeffnen im Fullscreen
		primaryStage.setTitle("VierPunkt");
		primaryStage.setResizable(true);

		// Layout Boxen
		VBox root = new VBox(); 				// aeusserste Box
		root.setId("root");
		HBox content = new HBox();
		content.setPrefWidth(breite); 			// content ueber gesamte Bildschirmbreite
		content.setAlignment(Pos.TOP_CENTER); 	// alle Inhalte werden mittig ausgerichtet

		/********************************************** MENUBAR ******************************************************/
		// MenuBar Hauptkategorien
		final Menu vierpunkt = new Menu("VierPunkt");
		final Menu themen = new Menu("Themen");

		// Menubar, Hauptkategorien setzen
		MenuBar menuBar = new MenuBar();
		menuBar.setId("menu");
		menuBar.getMenus().addAll(vierpunkt, themen);

		// Unterkategorien fuer "vierpunkt"
		MenuItem menu11 = new MenuItem("neues Spiel");
		MenuItem menu31 = new MenuItem("bereits gespielte Spiele");
		MenuItem menu13 = new MenuItem("Spiel beenden");
		vierpunkt.getItems().addAll(menu11, menu31, menu13);
		
		// Unterkategorien fuer "themen"
		MenuItem menu21 = new MenuItem("Suessigkeiten");
		MenuItem menu22 = new MenuItem("Halloween");
		MenuItem menu23 = new MenuItem("Food");
		MenuItem menu24 = new MenuItem("Sports");
		themen.getItems().addAll(menu21, menu22, menu23, menu24);

		/******************************************* UEBERSCHRIFT *********************************************************/

		// Ansicht der Ueberschrift
		ImageView ueberschrift = new ImageView();
		ueberschrift.setId("ueberschrift");
		ueberschrift.setFitWidth(breite / 2); // Ueberschrift soll die Haelfte des Bildschirms breit sein
		ueberschrift.setFitHeight((breite / 2) / 3.33); // Aufrechterhalten des Verhaeltnisses (Quotient: 3.33)
		ueberschrift.setPreserveRatio(true);

		// Container, in dem die Ueberschrift platziert wird
		HBox titelbox = new HBox();
		titelbox.setId("titel");

		// Rechtecke als Platzhalter, um die Ueberschrift mittig zu platzieren
		Rectangle containerlinks = new Rectangle(breite / 4, (breite / 2) / 3.33);
		containerlinks.setId("container");
		Rectangle containerrechts = new Rectangle(breite / 4, (breite / 2) / 3.33);
		containerrechts.setId("container");

		// Platzhalter und Ueberschrift in den Container einfuegen
		titelbox.getChildren().addAll(containerlinks, ueberschrift, containerrechts);

		/******************************************* CONTAINERBOXEN *********************************************************/

		// Hauptcontainer erhaelt den Inhalt
		root.getChildren().addAll(menuBar, titelbox, content);

		/******************************************* Sieger Popup ******************************************************/
		VBox boxrechts2 = new VBox();
		boxrechts2.setPrefWidth(breite / 4);
		boxrechts2.setSpacing(10);
		boxrechts2.setPadding(new Insets(50, 0, 0, 50));
		
		Label sieger = new Label("Sieger:");
		Text antwortsieger = new Text("Spieler 1");
		boxrechts2.getChildren().addAll(sieger, antwortsieger);
		

		/******************************************** CONTAINERBOXEN IN CONTENT ************************************************/
		// Erzeugen der rechten Containerbox
		VBox boxrechts = new VBox();
		boxrechts.setPrefWidth(breite / 4);
		boxrechts.setSpacing(10);
		boxrechts.setPadding(new Insets(50, 0, 0, 50));

		/******* INHALTE DER RECHTEN CONTAINERBOX *********************/

		Label spielstandanzeige = new Label("Spielstand: ");
		//Text antwortspielstand = new Text(spielstand);
		spielstand.setId("text");
		spielstandanzeige.setPadding(new Insets(20, 0, 0, 0));

		Label satzstatusanzeige = new Label("Satzstatus:");
		//Text antwortsatzstatus = new Text(satzstatus);
		satzstatus.setId("text");
		satzstatusanzeige.setPadding(new Insets(20, 0, 0, 0));
		
		
		spielstand.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
               
            	System.out.println("Label Text Changed");
            }
        }); 

		Label spielmodi = new Label("Spielmodus:");
		spielmodi.setPadding(new Insets(20, 0, 0, 0));

		// Slider, um einfach den Spielmodus einstellen zu koennen
		Slider spielmodus = new Slider(0, 2, 1); 			// Slider geht von 0 bis 2 in 1er Abstaenden
		spielmodus.setMinorTickCount(0);
		spielmodus.setMajorTickUnit(1); 					// Man kann nur auf den Zahlen 0, 1, 2 landen, nicht dazwischen
		spielmodus.setSnapToTicks(true); 					// Der Punkt rutzscht zur naechsten Zahl
		spielmodus.setShowTickMarks(true); 					// Markierungen anzeigen -
		spielmodus.setShowTickLabels(true); 				// Beschriftungen anzeigen
		spielmodus.setOrientation(Orientation.VERTICAL); 	// Vertikale Anordnung,standardmaessig horizontal
		spielmodus.setPrefHeight(120); 						// bevorzugte Hoehe des Sliders
		spielmodus.setMinHeight(120); 						// Mindesthoehe
		spielmodus.setValue(2);								// Default Value = 2
		
		// Methode, die die Zahlenbeschriftung durch entsprechenden Text ersetzt und die Rueckgabewerte festlegt
		spielmodus.setLabelFormatter(new StringConverter<Double>() {
			@Override
			public String toString(Double n) {
				if (n == 0)return "manuell";
				if (n > 0 && n < 2)return "gegen den Computer";
				if (n == 2)return "automatisch";
				return "automatisch";
			}
			@Override
			public Double fromString(String x) {
				switch (x) {
				case "manuell":return 0d;
				case "gegen den Computer":return 1d;
				case "automatisch":return 2d;
				default:return 2d;
				}
			}
		});
		
		spielmodus.valueProperty().addListener(new ChangeListener<Number>() {
		    @Override
		    public void changed(ObservableValue<? extends Number> observable,
		            Number oldValue, Number newValue) {
		    	if(newValue.intValue() == 2){
		    		createGrids_automatisch();
		    	}
		    	if(newValue.intValue() == 0){
		    		createGrids();
		    	}
		    }
		});
		
		// Platzhalter, damit der nachfolgende Button weiter unten angeordnet wird
		Rectangle platzhalter0 = new Rectangle(10, 40);
		platzhalter0.setOpacity(0);

		Button start = new Button("Spiel starten");
	
		// Einfuegen der Elemente in die rechte Box
		boxrechts.getChildren().addAll(spielstandanzeige, spielstand, satzstatusanzeige, satzstatus, spielmodi, spielmodus, platzhalter0, start);

		// Erzeugen der mittleren Containerbox
		VBox boxmitte = new VBox();
		boxmitte.setId("boxmitte");
		boxmitte.setPrefWidth(7 * l);
		boxmitte.setPadding(new Insets(30, 0, 50, 0));
		boxmitte.setMinWidth(7 * l);

		/******* INHALTE DER MITTLEREN CONTAINERBOX *********************/
		Rectangle platzhalter1 = new Rectangle(7 * l, l);
		platzhalter1.setOpacity(0); // Platzhalter nicht sichtbar
		
		/*********************************************************************************************************************
		 ******************************************* GRID FUER DAS SPIELFELD *********************************************************
		 ********************************************************************************************************************/
		
		// Erzeugen eines GridPanes spielfeld im uebergeordneten GridPane grid
		spielfeld.setId("spielfeld");
		
		
		// Erzeugen der Spalten (7)
		spielfeld.getColumnConstraints().addAll(new ColumnConstraints(l, l, Double.MAX_VALUE),
				new ColumnConstraints(l, l, Double.MAX_VALUE), new ColumnConstraints(l, l, Double.MAX_VALUE),
				new ColumnConstraints(l, l, Double.MAX_VALUE), new ColumnConstraints(l, l, Double.MAX_VALUE),
				new ColumnConstraints(l, l, Double.MAX_VALUE), new ColumnConstraints(l, l, Double.MAX_VALUE));
		// Erzeugen der Zeilen (6)
		spielfeld.getRowConstraints().addAll(new RowConstraints(l, l, Double.MAX_VALUE),
				new RowConstraints(l, l, Double.MAX_VALUE), new RowConstraints(l, l, Double.MAX_VALUE),
				new RowConstraints(l, l, Double.MAX_VALUE), new RowConstraints(l, l, Double.MAX_VALUE),
				new RowConstraints(l, l, Double.MAX_VALUE));
				

		// Einfuegen der Elemente in die mittlere Box
		boxmitte.getChildren().addAll(platzhalter1, spielfeld);
		
		/******* INHALTE DER LINKEN CONTAINERBOX ************************/
		// Erzeugen der linken Containerbox
		VBox boxlinks = new VBox();
		boxlinks.setId("boxlinks");
		boxlinks.setPrefWidth(breite / 4);
		boxlinks.setAlignment(Pos.BOTTOM_LEFT);
		
		// login Felder
		Label spieler1 = new Label("Name Spieler 1: ");
		TextField spielername1 = new TextField();
		if(spielername1.getText() != null && ! spielername1.getText().trim().isEmpty()){
			//names1 = spielername1.getText();
			setNames1(spielername1.getText());
		}
		
		Label spieler2 = new Label("Name Spieler 2: ");
		TextField spielername2 = new TextField();
		if(spielername2.getText() != null && ! spielername2.getText().trim().isEmpty()){
			//names2 = spielername2.getText();
			setNames2(spielername2.getText());
		}
		
	
		// Ueberschrift Spieler
		Label spielerfarben = new Label("Spieler");
		// Name mit Spielstein Spieler 1
		ImageView i1 = new ImageView(getImage1());
		i1.setFitWidth(l-20);	i1.setFitHeight(l-20);
		Label s1 = new Label(getNames1());
		// Name mit Spielstein Spieler 2
		ImageView i2 = new ImageView(getImage2());
		i2.setFitWidth(l-20);	i2.setFitHeight(l-20);
		Label s2 = new Label(getNames2());
		// Platzhalter
		Rectangle p = new Rectangle(20,20);
		p.setOpacity(0);
		Rectangle p1 = new Rectangle(20,20);
		p1.setOpacity(0);
		// zur Box Hinzufuegen
		HBox box3 = new HBox();
		box3.getChildren().addAll(s1, i1, s2, i2);
		
		ImageView bild = new ImageView();
		bild.setId("bild");
		bild.setFitWidth(breite / 4); // Breite soll ein Viertel des Fensters betragen
		bild.setPreserveRatio(true); // Das Verhaeltnis soll beibehalten werden
		
		
		Rectangle platzhalter2 = new Rectangle(l, 2*l);
		platzhalter2.setOpacity(0);
		
		/*************************************************************************************************************
		 *************************************************************************************************************
		 ***************************************       Einstellungen       *******************************************
		 *************************************************************************************************************
		 *************************************************************************************************************/
		// Schnittstelle
		Label schnittstelle = new Label("Schnittstelle");
		CheckBox file = new CheckBox("File");
		Text keinOrdner = new Text("ACHTUNG! Es wurde kein Ordner ausgewaehlt!");
		keinOrdner.setStyle("-fx-fill: red;");
		keinOrdner.setOpacity(0);
		HBox hb = new HBox();
		hb.getChildren().addAll(file, keinOrdner);
		
		CheckBox pusher1 = new CheckBox("Pusher");
		
		file.getStyleClass().add("checkBox");
		pusher1.getStyleClass().add("checkBox");
		
		// Zeit
		Slider zeit = new Slider(0, 5000, 100); 			// Slider geht von 0 bis 2 in 1er Abstaenden
		zeit.setMinorTickCount(0);
		zeit.setMajorTickUnit(100); 					// Man kann nur auf den Zahlen 0, 1, 2 landen, nicht dazwischen
		zeit.setSnapToTicks(true); 						// Der Punkt rutzscht zur naechsten Zahl
		zeit.setShowTickMarks(true); 					// Markierungen anzeigen -
		zeit.setOrientation(Orientation.HORIZONTAL); 	// Vertikale Anordnung,standardmaessig horizontal
		zeit.setValue(2000);								// Default Value = 2
		
		Label zeitlabel = new Label("Zugzeit");
		zeit.valueProperty().addListener(new ChangeListener<Number>() {
		    @Override
		    public void changed(ObservableValue<? extends Number> observable,
		            Number oldValue, Number newValue) {
		    	zeitlabel.setText("Zugzeit:   " + newValue + " ms");
		    	setZugzeit(newValue.intValue());
		    }
		});
		
		// Ueberschrift
        Text u1 = new Text("Einstellungen");
        u1.setId("textEinstellungen");
        
        // Pusher Credentials
        Label cred1 = new Label("App ID:");
        TextField app1 = new TextField ();
        
        HBox hb1 = new HBox();
        hb1.getChildren().addAll(cred1, app1);
        hb1.setSpacing(10);
        Label cred2 = new Label("App Key: ");
        TextField app2 = new TextField ();
        
        HBox hb2 = new HBox();
        hb2.getChildren().addAll(cred2, app2);
        hb2.setSpacing(10);
        Label cred3 = new Label("App Secret: ");
        TextField app3 = new TextField ();
       
        HBox hb3 = new HBox();
        hb3.getChildren().addAll(cred3, app3);
        hb3.setSpacing(10);
        
        hb1.setOpacity(0);
        hb2.setOpacity(0);
        hb3.setOpacity(0);
        
    	pusher1.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                if(file.isSelected()){
             	   file.setSelected(false); 
             	   keinOrdner.setOpacity(0);
             	   pusher1.setSelected(true);
                }else{pusher1.setSelected(true);}
                setSchnittstelle("pusher");
                hb1.setOpacity(1);
                hb2.setOpacity(1);
                hb3.setOpacity(1);
            }
    	});
	
		DirectoryChooser chooser = new DirectoryChooser();
	
		file.setOnMouseClicked(new EventHandler<MouseEvent>(){
	     @Override
	     public void handle(MouseEvent arg0) {
	         if(pusher1.isSelected()){
	      	   pusher1.setSelected(false);   
	      	   file.setSelected(true);
	         }else{file.setSelected(true);}
	         setSchnittstelle("file");
	         hb1.setOpacity(0);
	         hb2.setOpacity(0);
	         hb3.setOpacity(0);
	         
	         
	         
	         File dir = chooser.showDialog(primaryStage);
	 	    if (dir == null) {
	 	        System.out.println("Kein Ordner ausgewählt");
	 	        keinOrdner.setOpacity(1);
	 	    }else{
	 	    	fileString = dir.getPath();
	 	    	System.out.println(fileString);
	 	    	keinOrdner.setOpacity(0);
	 	    }
	     }
		});
			
		// X oder O
		ToggleGroup group = new ToggleGroup();
		ToggleButton tb1 = new ToggleButton("X");
		tb1.setToggleGroup(group);
		tb1.setSelected(true);
		tb1.getStyleClass().add("togglebutton");
		ToggleButton tb2 = new ToggleButton("O");
		tb2.setToggleGroup(group);
		tb2.getStyleClass().add("togglebutton");
		HBox hb4 = new HBox();
        hb4.getChildren().addAll(tb1, tb2);
        hb4.setSpacing(10);
       
		
		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
		    public void changed(ObservableValue<? extends Toggle> ov,
		        Toggle toggle, Toggle new_toggle) {
		            if (new_toggle == null)
		                setXodero('o');
		            else
		            	setXodero('x');
		         }
		});
		
		Button einstellungen = new Button("Einstellungen");
		einstellungen.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0){
				// neue Stage
				final Stage dialog = new Stage();
				dialog.setTitle("Einstellungen");
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(primaryStage);
                VBox dialogVbox = new VBox(20);
                dialogVbox.setPadding(new Insets(10, 10, 10, 10));                
                
                // Button zum Popup schließen
                Button ok = new Button("ok");
                
                // Button Action Event
                ok.setOnMouseClicked(new EventHandler<MouseEvent>(){
             	   @Override
                    public void handle(MouseEvent arg0) {
             		   appId = app1.getText();
             		   appKey = app2.getText();
             		   appSecret = app3.getText();
             		   System.out.println(appId + " " + appKey + " " + appSecret);
             		   dialog.close();
                }});
                
                // Einfuegen in die VBox
                dialogVbox.getChildren().addAll(u1, hb4, schnittstelle, hb, pusher1, hb1, hb2, hb3, p1, zeitlabel, zeit, ok);
                Scene dialogScene = new Scene(dialogVbox, 500, 800);
                
                if(thema == 1){ dialogScene.getStylesheets().add(TestGui.class.getResource("Halloween.css").toExternalForm());}
                if(thema == 2){ dialogScene.getStylesheets().add(TestGui.class.getResource("Food.css").toExternalForm());}
                if(thema == 3){dialogScene.getStylesheets().add(TestGui.class.getResource("Sport.css").toExternalForm());}
                if(thema == 4){ dialogScene.getStylesheets().add(TestGui.class.getResource("Sweets.css").toExternalForm());}
              
                dialog.setScene(dialogScene);
                dialog.show();	
			}
		});


		// Einfuegen der Elemente in die linke Box
		boxlinks.getChildren().addAll(spielerfarben, box3, p, einstellungen, bild, platzhalter2);

		/******* CONTAINERBOXEN EINFUEGEN ************************/
		content.getChildren().addAll(boxlinks, boxmitte, boxrechts);

		/*************************************************************************************************************
		 *************************************************************************************************************
		 ******************************************* EVENTHANDLER FUER DAS MENU **************************************
		 *************************************************************************************************************
		 *************************************************************************************************************/
		
		Scene scene = new Scene(root);
		
		menu13.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) { Platform.exit();}
		});
		
		menu11.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e) {
				primaryStage.setScene(scene);
				primaryStage.setFullScreen(true);
				spieler = 1;
				createGrids();
				}
		});
		
		menu21.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e){
				thema = 4;
				primaryStage.setScene(scene);
				primaryStage.setFullScreen(true);
				setColor(Color.PURPLE);
				setImage1(orange);
				setImage2(gruen);
				i1.setImage(orange);
				i2.setImage(gruen);
				scene.getStylesheets().clear();
				scene.getStylesheets().add(TestGui.class.getResource("Sweets.css").toExternalForm());
				createGrids();
			}
		});
		
		menu22.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e){
				thema = 1;
				setColor(Color.BLACK);
				setImage1(kuerbis);
				setImage2(fledermaus);
				i1.setImage(kuerbis);
				i2.setImage(fledermaus);
				scene.getStylesheets().clear();
				scene.getStylesheets().add(TestGui.class.getResource("Halloween.css").toExternalForm());
				createGrids();
			}
		});
		
		menu23.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e){
				thema = 2;
				primaryStage.setScene(scene);
				primaryStage.setFullScreen(true);
				scene.getStylesheets().clear();
				scene.getStylesheets().add(TestGui.class.getResource("Food.css").toExternalForm());
				setColor(Color.DARKGREEN);
				setImage1(pizza);
				setImage2(burger);
				i1.setImage(pizza);
				i2.setImage(burger);
				
				createGrids();
			}
		});
		
		menu24.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e){
				thema = 3;
				primaryStage.setScene(scene);
				primaryStage.setFullScreen(true);
				scene.getStylesheets().clear();
				scene.getStylesheets().add(TestGui.class.getResource("Sport.css").toExternalForm());
				setColor(Color.CADETBLUE);
				setImage1(basketball);
				setImage2(baseball);
				i1.setImage(basketball);
				i2.setImage(baseball);
				
				createGrids();
			}
		});
		
		/*************************************************************************************************************
		 *************************************************************************************************************
		 **************************************** Methodenaufruf je nach Slider **************************************
		 *************************************************************************************************************
		 *************************************************************************************************************/
		// manuell
		if(spielmodus.getValue() == 0 ){
			createGrids();
		}
		
		// automatisch
		if(spielmodus.getValue() == 2){
			createGrids_automatisch();
		}
		
		/*************************************************************************************************************
		 *************************************************************************************************************
		 *********************************************** diverse Stages **********************************************
		 *************************************************************************************************************
		 *************************************************************************************************************/
		
		// Login Stage
		Stage loginStage = new Stage();
		Button login = new Button("Spiel starten");
		VBox vb = new VBox();
		vb.setAlignment(Pos.CENTER);
		vb.setPadding(new Insets(10, 10, 10, 10));
		
		Label meldung = new Label("Bitte Spielernamen eingeben");
		meldung.setOpacity(0);
		meldung.setStyle("-fx-font-weight: lighter;");
		
		HBox hb5 = new HBox();
		spieler1.setPrefWidth(200);
		hb5.getChildren().addAll(spieler1, spielername1);
		hb5.setSpacing(10);
		
		HBox hb6 = new HBox();
		spieler2.setPrefWidth(200);
		hb6.getChildren().addAll(spieler2, spielername2);
		hb6.setSpacing(10);
		
		Rectangle p2 = new Rectangle(20, 15);
		p2.setOpacity(0);
		Rectangle p3 = new Rectangle(20, 15);
		p3.setOpacity(0);
		
		vb.getChildren().addAll(hb5, hb6, p2, meldung, p3, login);
		Scene scene2 = new Scene(vb, 400, 250);
		loginStage.setScene(scene2);
	    scene2.getStylesheets().add(TestGui.class.getResource("Gui.css").toExternalForm());
	    loginStage.initModality(Modality.APPLICATION_MODAL);
	    loginStage.setTitle("Spielernamen");
	    loginStage.setFullScreen(false);
	    
	    
	    // Login bei Enter, egal in welchem Feld man ist
	    spielername1.setOnKeyPressed(new EventHandler<KeyEvent>()
	    {
	        @Override
	        public void handle(KeyEvent ke)
	        {
	            if (ke.getCode().equals(KeyCode.ENTER))
	            {if(spielername1.getText() == null || spielername1.getText().trim().isEmpty() || spielername2.getText() == null ||  spielername2.getText().trim().isEmpty()){
					meldung.setOpacity(1);
				}else{
					loginStage.close();
					s1.setText(spielername1.getText());
					s2.setText(spielername2.getText());
					primaryStage.show();
				}
	            }
	        }
	    });
	    
	    spielername2.setOnKeyPressed(new EventHandler<KeyEvent>()
	    {
	        @Override
	        public void handle(KeyEvent ke)
	        {
	            if (ke.getCode().equals(KeyCode.ENTER))
	            {
	            	if(spielername1.getText() == null || spielername1.getText().trim().isEmpty() || spielername2.getText() == null ||  spielername2.getText().trim().isEmpty()){
						meldung.setOpacity(1);
					}else{
						loginStage.close();
						s1.setText(spielername1.getText());
						s2.setText(spielername2.getText());
						primaryStage.show();
					}
	            }
	        }
	    });
	    
	    login.setOnKeyPressed(new EventHandler<KeyEvent>()
	    {
	         public void handle(KeyEvent evt)
	         {
	              if (evt.getCode() == KeyCode.ENTER)
	            	loginStage.close();
					s1.setText(spielername1.getText());
					s2.setText(spielername2.getText());
					primaryStage.show();
	         }
	    });
	    
		login.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
            public void handle(MouseEvent arg0) {
				if(spielername1.getText() == null || spielername1.getText().trim().isEmpty() || spielername2.getText() == null ||  spielername2.getText().trim().isEmpty()){
					meldung.setOpacity(1);
				}else{
					loginStage.close();
					s1.setText(spielername1.getText());
					s2.setText(spielername2.getText());
					primaryStage.show();
				}
            }
		});
	    
		// Gewinnermeldung
		/*Stage gewinnermeldung = new Stage();
		FlowPane panegewinner = new FlowPane();
		Label gewinnernachricht = new Label();
		panegewinner.setPadding(new Insets(10, 10, 10, 10));
		panegewinner.getChildren().addAll(gewinnernachricht);
		Scene meldung1 = new Scene(panegewinner);
		gewinnermeldung.setScene(meldung1);		*/
		 
		// primary Stage
		primaryStage.setScene(scene);
		scene.getStylesheets().add(TestGui.class.getResource("Halloween.css").toExternalForm());
		
	    menu31.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e) {
				
				// neue Stage
				final Stage spieleStage = new Stage();
				spieleStage.setTitle("Einstellungen");
				spieleStage.initModality(Modality.APPLICATION_MODAL);
				spieleStage.initOwner(primaryStage);
                VBox spieleVbox = new VBox(20);
                spieleVbox.setPadding(new Insets(10, 10, 10, 10));                
                
                // Button zum Popup schliessen
                Button back = new Button("zurueck");
                
                Label spieleLabel = new Label("Bisherige Spiele:");
                
                ScrollPane listeSpiele = new ScrollPane();
        		Text beispieltext = new Text("Spiele ID 1 \n Spiele ID 2 \n Spiele ID 3 \n Spiele ID 4");
        		listeSpiele.setContent(beispieltext);
                
                // Button Action Event
                back.setOnMouseClicked(new EventHandler<MouseEvent>(){
             	   @Override
                    public void handle(MouseEvent arg0) {
             		   spieleStage.close();
                }});
                
                // Einfuegen in die VBox
                spieleVbox.getChildren().addAll(spieleLabel, listeSpiele, back);
                Scene spieleScene = new Scene(spieleVbox, 1200, 900);
                if(thema == 1){ spieleScene.getStylesheets().add(TestGui.class.getResource("Halloween.css").toExternalForm());}
                if(thema == 2){ spieleScene.getStylesheets().add(TestGui.class.getResource("Food.css").toExternalForm());}
                if(thema == 3){spieleScene.getStylesheets().add(TestGui.class.getResource("Sport.css").toExternalForm());}
                if(thema == 4){ spieleScene.getStylesheets().add(TestGui.class.getResource("Sweets.css").toExternalForm());}
              
                spieleStage.setScene(spieleScene);
                //spieleStage.setFullScreen(true);
                spieleStage.show();	
				}
		});
	    
		
		start.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
            public void handle(MouseEvent arg0) {
				if(spielmodus.getValue()==2){
					spieler = 1;
	            	createGrids_automatisch();
				}else{
					spieler = 1;
					createGrids();}
				
				fireStartEvent(getZugzeit(), getSchnittstelle(), getFileString(), getXodero());
				
				
				//satzgewinner(1);
				//System.out.println(getNames1() + names2);
				//gewinnermethode(1, spielername1.getText(), spielername2.getText());
            }
		});
	
		loginStage.show();
		
	}
	/*********************************************************************************************************************
	 *********************************************************************************************************************
	 **********************************************************************************************************************
	 *********************************************************************************************************************
     *******************************************  SPIELFELD ERZEUGEN METHODE  ********************************************
     ********************************************************************************************************************/
   
	public void satzgewinner(int spieler){
		// neue Stage
		final Stage satz = new Stage();
		satz.setTitle("Satzgewinner");
        satz.initModality(Modality.APPLICATION_MODAL);
        satz.initOwner(primaryStage);
        VBox satzVbox = new VBox(20);
        satzVbox.setPadding(new Insets(10, 10, 10, 10));                
        
        Label meldung = new Label();
        
        if(spieler==1){ meldung.setText(names1 + " hat den Satz gewonnen!");}
        else if (spieler == 2) {meldung.setText(names2 + " hat den Satz gewonnen!");
		}else{meldung.setText("Der Satz ist unentschieden ausgegangen.");}
        
        
        // Einfuegen in die VBox
        satzVbox.getChildren().addAll(meldung);
        Scene dialogScene = new Scene(satzVbox, 500, 800);
        
        if(thema == 1){ dialogScene.getStylesheets().add(TestGui.class.getResource("Halloween.css").toExternalForm());}
        if(thema == 2){ dialogScene.getStylesheets().add(TestGui.class.getResource("Food.css").toExternalForm());}
        if(thema == 3){dialogScene.getStylesheets().add(TestGui.class.getResource("Sport.css").toExternalForm());}
        if(thema == 4){ dialogScene.getStylesheets().add(TestGui.class.getResource("Sweets.css").toExternalForm());}
      
        satz.setScene(dialogScene);
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished( event -> satz.close() );
       
        
        delay.play();
        satz.show();
        
	}
	
	
	public void gewinnermethode(int spieler, String names1, String names2){
		
		Label gewinnernachricht = new Label();
		
		// neue Stage
		final Stage gewinnerStage = new Stage();
		gewinnerStage.setTitle("Gewinner");
		gewinnerStage.initModality(Modality.APPLICATION_MODAL);
		gewinnerStage.initOwner(primaryStage);
        VBox gewinnerVbox = new VBox(20);
        gewinnerVbox.setAlignment(Pos.CENTER);
        gewinnerVbox.setPadding(new Insets(10, 10, 10, 10));                
        
        // Button zum Popup schließen
        Button back = new Button("ok");
        
        // Button Action Event
        back.setOnMouseClicked(new EventHandler<MouseEvent>(){
     	   @Override
            public void handle(MouseEvent arg0) {
     		   gewinnerStage.close();
        }});
        if(spieler == 1){
			gewinnernachricht.setText(names1 + " hat gewonnen!");
		}else {
			if(spieler == 2){
				gewinnernachricht.setText(names2 + " hat gewonnen!");
			}else{
				gewinnernachricht.setText("Das spiel ist unentschieden ausgegangen.");
			}}
        // Einfuegen in die VBox
        gewinnerVbox.getChildren().addAll(gewinnernachricht, back);
        Scene gewinnerScene = new Scene(gewinnerVbox, 800, 600);
        if(thema == 1){ gewinnerScene.getStylesheets().add(TestGui.class.getResource("Halloween.css").toExternalForm());}
        if(thema == 2){ gewinnerScene.getStylesheets().add(TestGui.class.getResource("Food.css").toExternalForm());}
        if(thema == 3){ gewinnerScene.getStylesheets().add(TestGui.class.getResource("Sport.css").toExternalForm());}
        if(thema == 4){ gewinnerScene.getStylesheets().add(TestGui.class.getResource("Sweets.css").toExternalForm());}
       
        
        gewinnerStage.setFullScreen(false);
        gewinnerStage.setScene(gewinnerScene);
        gewinnerStage.show();	
		
	}
    
    public void createGrids_automatisch(){
    	spielfeld.getChildren().clear();
        for(anzahlzeilen=0;anzahlzeilen<spielfeld.getRowConstraints().size(); anzahlzeilen++){
            for(anzahlspalten=0; anzahlspalten<spielfeld.getColumnConstraints().size(); anzahlspalten++){
            
            // Darstellung des Rahmens/ der Zellen    
            Rectangle rect = new Rectangle(l,l);
            Circle circ = new Circle((l/2)-5);
            circ.centerXProperty().set(l/2);
            circ.centerYProperty().set(l/2);
            Shape cell = Path.subtract(rect, circ);
            cell.setId("cell");
            cell.setFill(color);
            cell.setStroke(color.darker());
            
            /*******************************************************************************************************************
             *******************************************  SPIELSTEINE  *********************************************************
             *******************************************************************************************************************/            
            
            // Ansicht der Spielsteine
            ImageView spielstein = new ImageView(image1);
            spielstein.setImage(image1);
            spielstein.setId("spielstein" + anzahlspalten);
            spielstein.setFitWidth(l-10);
            spielstein.setPreserveRatio(true);  
            
            // Vorschau der Spielsteine
            ImageView vorschauspielstein = new ImageView(image3);
            vorschauspielstein.setFitWidth(l-10);
            vorschauspielstein.setPreserveRatio(true); 
            vorschauspielstein.setOpacity(0.5);
            
            spielstein.setTranslateY(-(l*(anzahlzeilen+1)));
            
            // Zellen werden gefuellt
            StackPane stack = new StackPane();
            stack.getChildren().addAll(cell, vorschauspielstein, spielstein);   // Fuellen der Zelle mit Rahmen, Vorschau oder Spielstein
            spielfeld.add(stack, anzahlspalten, anzahlzeilen); 
            }
         }
    }

	public void createGrids(){
    	spielfeld.getChildren().clear();
        for(anzahlzeilen=0;anzahlzeilen<spielfeld.getRowConstraints().size(); anzahlzeilen++){
            for(anzahlspalten=0; anzahlspalten<spielfeld.getColumnConstraints().size(); anzahlspalten++){
            
            // Darstellung des Rahmens/ der Zellen    
            Rectangle rect = new Rectangle(l,l);
            Circle circ = new Circle((l/2)-5);
            circ.centerXProperty().set(l/2);
            circ.centerYProperty().set(l/2);
            Shape cell = Path.subtract(rect, circ);
            cell.setId("cell");
            cell.setFill(color);
            cell.setStroke(color.darker());
            
            /*******************************************************************************************************************
             *******************************************  SPIELSTEINE  *********************************************************
             *******************************************************************************************************************/            
            
            // Ansicht der Spielsteine
            ImageView spielstein = new ImageView(image1);
            spielstein.setImage(image1);
            spielstein.setId("spielstein" + anzahlspalten);
            spielstein.setFitWidth(l-10);
            spielstein.setPreserveRatio(true);  
            
            // Vorschau der Spielsteine
            ImageView vorschauspielstein = new ImageView(image3);
            vorschauspielstein.setImage(image1);
            vorschauspielstein.setFitWidth(l-10);
            vorschauspielstein.setPreserveRatio(true); 
            vorschauspielstein.setOpacity(0.5);
            
            /*******************************************************************************************************************
             *******************************************  ANZEIGE IM SPIELFELD  ************************************************
             *******************************************************************************************************************/ 
            
        	// Methode zur Vorschau - jeweiliger Spielstein wird angezeigt
               vorschauspielstein.setOnMouseEntered(new EventHandler<MouseEvent>(){
                   @Override
                   public void handle(MouseEvent arg0) {
                       if(spieler==1){vorschauspielstein.setImage(image1);      
                       }else{vorschauspielstein.setImage(image2);}
                   }
               });
               
               // Beim Verlassen des Vorschau-Spielsteins erscheint wieder der graue Hintergrund
                vorschauspielstein.setOnMouseExited(new EventHandler<MouseEvent>(){
                   @Override
                   public void handle(MouseEvent arg0) {
                       vorschauspielstein.setImage(image3);
                   }
               });
                
               spielstein.setTranslateY(-(l*(anzahlzeilen+1)));
            
               // Spielsteine oberhalb des Spielfelds - Anzeige der Vorschau
               spielstein.setOnMouseEntered(new EventHandler<MouseEvent>(){
               @Override
                   public void handle(MouseEvent arg0) {
                       vorschauspielstein.setImage(image3);
                       if(spieler==1){ vorschauspielstein.setImage(image1);
                       }else{vorschauspielstein.setImage(image2); }
                   }
               });
               
               // Beim Verlassen des Spielsteins erscheint wieder der graue Hintergrund
              spielstein.setOnMouseExited(new EventHandler<MouseEvent>(){
                   @Override
                   public void handle(MouseEvent arg0) {
                        vorschauspielstein.setImage(image3);
                   }
               });
              
              // Setzen der Spielsteine
              spielstein.setOnMouseClicked(new EventHandler<MouseEvent>(){
                   @Override public void handle(MouseEvent arg0) { spielsteinAnzeigen(spielstein);}
               });
              
               // Setzen der Spielsteine beim Klick auf die entsprechende Vorschau
               vorschauspielstein.setOnMouseClicked(new EventHandler<MouseEvent>(){
                   @Override public void handle(MouseEvent arg0) {spielsteinAnzeigen(spielstein);}
               });
           
            /*******************************************************************************************************************
             *******************************************  ZELLEN FUELLEN  ******************************************************
             *******************************************************************************************************************/
            // Zellen werden gefuellt
            StackPane stack = new StackPane();
            vorschauspielstein.setImage(image3);                         		// Hintergrund grau
            stack.getChildren().addAll(cell, vorschauspielstein, spielstein);   // Fuellen der Zelle mit Rahmen, Vorschau oder Spielstein
            spielfeld.add(stack, anzahlspalten, anzahlzeilen); 
            }
        }
    }
    public void setSpielstein(int zeile, int spalte){
    	spielsteinAnzeigen(getImageView((getNodeByRowColumnIndex(zeile, spalte))));
    }
    
    public void spielsteinAnzeigen(ImageView spielstein){
    	if(spielstein.getTranslateY()!=0){ 
    		//spielstein.setTranslateY(-(l*(anzahlzeilen+1)));
            final TranslateTransition translateTransition = new TranslateTransition(Duration.millis(300), spielstein);
            translateTransition.setToY(0);				//Runterfallen der Steine
            translateTransition.play();
            if(spieler==1){
                spielstein.setImage(image1);
                System.out.println((int)spielstein.getId().charAt(10)-48 + " " + spieler);
                spieler=2;
            }else{
                spielstein.setImage(image2);
                System.out.println((int)spielstein.getId().charAt(10)-48 + " " + spieler);
                spieler=1;
            }
        }
    }
    
    public StackPane getNodeByRowColumnIndex (final int row, final int column) {
        for (Node node : spielfeld.getChildren()) {
            if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                return (StackPane) node;
            }
        } return null;
    }
    
    public ImageView getImageView (StackPane stack) {
        ObservableList<Node> list = stack.getChildren();
        return (ImageView)list.get(2);
    }
    
    
	@Override
	public void zugGespielt(int zug)
	{
		setSpielstein(plaetzeFreiInReihe[zug], zug);
        plaetzeFreiInReihe[zug]--;
	}
	
	public void addNameListener(NameListener toAdd) {
		NameListeners.add(toAdd);
	}
	
	public static void fireNames (String name1, String name2, int isServer) {
		for (NameListener name: NameListeners) {
			name.startGame(name1, name2, isServer);
		}
	}
	
	public void addParamListener(ParamListener toAdd){
		listeners.add(toAdd);
	}
	
	
	public static void fireStartEvent(int Zugzeit, String Schnittstelle, String Kontaktpfad, char spielerKennung){
		for (ParamListener pl : listeners){
			pl.startParameterAuswerten(Zugzeit, Schnittstelle, Kontaktpfad, spielerKennung);
		}
	}
	
	public String getFileString(){
		return fileString;
	}
 
}

               
            