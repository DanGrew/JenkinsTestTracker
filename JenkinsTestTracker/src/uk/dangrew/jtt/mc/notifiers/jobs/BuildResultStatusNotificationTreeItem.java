/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.notifiers.jobs;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import uk.dangrew.jtt.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.mc.model.Notification;
import uk.dangrew.jtt.mc.resources.ManagementConsoleImages;
import uk.dangrew.jtt.mc.view.item.NotificationTreeItem;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;

/**
 * {@link BuildResultStatusNotificationTreeItem} respresents a {@link BuildResultStatusNotification}
 * in the {@link uk.dangrew.jtt.mc.view.tree.NotificationTree}.
 */
public class BuildResultStatusNotificationTreeItem implements NotificationTreeItem {

   static final double STATUS_PROPORTION = 10;
   static final double TITLE_PROPORTION = 30;
   static final double DESCRIPTION_PROPORTION = 40;
   static final double PEOPLE_PROPORTION = 10;
   static final double CLOSE_PROPORTION = 10;
   static final double PREFERRED_ROW_HEIGHT = 20;
   static final double PREFERRED_IMAGE_HEIGHT = 20;
   static final double PREFERRED_IMAGE_WIDTH = 20;  
   
   static final String STILL_THE_SAME = "Build has remained at %s";
   static final String MAY_REQUIRE_ACTION = "Build has only achieved %s when it was %s and may require action";
   static final String PASSING = "Build has achieved %s from %s";
   
   private final ChangeIdentifier changeIdentifier;
   private final JavaFxStyle styling;
   private final ManagementConsoleImages images;
   private final BuildResultStatusNotification notification;
   private final ObjectProperty< Node > contentProperty;
   
   private final Node status;
   private final Node title;
   private final Node description;
   private final Node people;
   private final Node close;
   
   /**
    * Constructs a new {@link BuildResultStatusNotificationTreeItem}.
    * @param notification the {@link BuildResultStatusNotification} associated.
    */
   public BuildResultStatusNotificationTreeItem( BuildResultStatusNotification notification ) {
      this( notification, new ChangeIdentifier(), new JavaFxStyle(), new ManagementConsoleImages() );
   }//End Constructor
   
   /**
    * Constructs a new {@link BuildResultStatusNotificationTreeItem}.
    * @param notification the {@link BuildResultStatusNotification} associated.
    * @param changeIdentifier the {@link ChangeIdentifier} for identifying change type.
    * @param stying the {@link JavaFxStyle} to apply.
    * @param images the {@link ManagementConsoleImages} available.
    */
   BuildResultStatusNotificationTreeItem( 
            BuildResultStatusNotification notification, 
            ChangeIdentifier changeIdentifier, 
            JavaFxStyle stying,
            ManagementConsoleImages images
   ) {
      if ( notification == null ) {
         throw new IllegalArgumentException( "Notification must not be null." );
      }
      
      this.notification = notification;
      this.changeIdentifier = changeIdentifier;
      this.styling = stying;
      this.images = images;
      
      this.status = constructStatusImage();
      this.title = constructCenteredTitle();
      this.description = constructDescriptionLabel();
      this.people = constructPeopleImage();
      this.close = constructCloseImage();
      
      GridPane row = new GridPane();
      
      ColumnConstraints statusColumn = new ColumnConstraints();
      statusColumn.setPercentWidth( STATUS_PROPORTION );
      ColumnConstraints titleColumn = new ColumnConstraints();
      titleColumn.setPercentWidth( TITLE_PROPORTION );
      ColumnConstraints descriptionColumn = new ColumnConstraints();
      descriptionColumn.setPercentWidth( DESCRIPTION_PROPORTION );
      ColumnConstraints peopleColumn = new ColumnConstraints();
      peopleColumn.setPercentWidth( PEOPLE_PROPORTION );
      ColumnConstraints closeColumn = new ColumnConstraints();
      closeColumn.setPercentWidth( CLOSE_PROPORTION );
      row.getColumnConstraints().addAll( statusColumn, titleColumn, descriptionColumn, peopleColumn, closeColumn );
      
      row.add( status, 0, 0 );
      row.add( title, 1, 0 );
      row.add( description, 2, 0 );
      row.add( people, 3, 0 );
      row.add( close, 4, 0 );
      this.contentProperty = new SimpleObjectProperty<>( row );
   }//End Constructor
   
   /**
    * Method to construct the status {@link Image}.
    * @return the {@link ImageView}.
    */
   private Node constructStatusImage(){
      ImageView view = changeIdentifier.identifyChangeType( 
               notification.getPreviousBuildResultStatus(), 
               notification.getNewBuildResultStatus() 
      ).constructImage();
      view.setFitWidth( PREFERRED_IMAGE_WIDTH );
      view.setFitHeight( PREFERRED_IMAGE_HEIGHT );
      return view;
   }//End Method
   
   /**
    * Method to construct the title {@link Node}.
    * @return the {@link Node}.
    */
   private Node constructCenteredTitle(){
      return new BorderPane( styling.createBoldLabel( notification.getJenkinsJob().nameProperty().get() ) );
   }//End Method
   
   /**
    * Method to construct the description {@link Node}.
    * @return the {@link Node}.
    */
   private Node constructDescriptionLabel(){
      Label actualLabel = styling.createWrappedTextLabel( formatBuildResultStatusChange( 
               notification.getPreviousBuildResultStatus(), notification.getNewBuildResultStatus() 
      ) );
      actualLabel.setPrefHeight( PREFERRED_ROW_HEIGHT );
      return new BorderPane( actualLabel );
   }//End Method
   
   /**
    * Method to create {@link Button}s in the same style.
    * @param image the {@link Image} to place on the {@link Button}.
    * @return the {@link Button}.
    */
   private Button constructControl( Image image ){
      Button button = new Button();
      ImageView view = new ImageView( image );
      view.setFitHeight( PREFERRED_IMAGE_HEIGHT );
      view.setFitWidth( PREFERRED_IMAGE_WIDTH );
      button.setGraphic( view );
      button.setPrefSize( PREFERRED_ROW_HEIGHT, PREFERRED_ROW_HEIGHT );
      button.setAlignment( Pos.CENTER );
      styling.removeBackgroundAndColourOnClick( button, Color.GRAY );
//      actualButton.setOnAction( event -> System.out.println( "Pressed " + actualButton.toString() ) );
      return button;
   }//End Method
   
   /**
    * Method to construct the people {@link Button}.
    * @return the {@link Node}.
    */
   private Node constructPeopleImage(){
      Button button = constructControl( images.constuctPeopleImage() );
      return new BorderPane( button );
   }//End Method
   
   /**
    * Method to construct the close {@link Button}.
    * @return the {@link Node}.
    */
   private Node constructCloseImage(){
      Button button = constructControl( images.constuctCloseImage() );
      return new BorderPane( button );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Node > contentProperty() {
      return contentProperty;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public Notification getNotification() {
      return notification;
   }//End Method
   
   /**
    * Method to format the given change in {@link BuildResultStatus}.
    * @param previous the previous {@link BuildResultStatus}.
    * @param current the new {@link BuildResultStatus}.
    * @return the {@link String} description of the change.
    */
   String formatBuildResultStatusChange( BuildResultStatus previous, BuildResultStatus current ) {
      switch ( changeIdentifier.identifyChangeType( previous, current ) ) {
         case ActionRequired:
            return String.format( MAY_REQUIRE_ACTION, current.name(), previous.name() );
         case Passed:
            return String.format( PASSING, current.name(), previous.name() );
         case Unchanged:
            return String.format( STILL_THE_SAME, previous.name() );
         default:
            return "Unkown";
      }
   }//End Method 
   
   Node status(){
      return status;
   }//End Method
   
   Node title(){
      return title;
   }//End Method
   
   Node description(){
      return description;
   }//End Method
   
   Node people(){
      return people;
   }//End Method
   
   Node close(){
      return close;
   }//End Method
   
}//End Class
