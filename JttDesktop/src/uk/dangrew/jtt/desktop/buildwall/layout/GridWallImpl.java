/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.MapChangeListener;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallJobPolicy;
import uk.dangrew.jtt.desktop.buildwall.configuration.theme.BuildWallTheme;
import uk.dangrew.jtt.desktop.buildwall.configuration.theme.BuildWallThemeImpl;
import uk.dangrew.jtt.desktop.buildwall.panel.JobPanelImpl;
import uk.dangrew.jtt.desktop.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.utility.comparator.Comparators;

/**
 * The {@link GridWallImpl} provides an implementation of the {@link BuildWall} providing
 * {@link JenkinsJob}s in a {@link GridPane}.
 */
public class GridWallImpl extends GridPane implements BuildWall {

   private final JenkinsDatabase database;
   private final BuildWallConfiguration configuration;
   private final BuildWallTheme theme;
   private final Map< JenkinsJob, JobPanelImpl > jobPanels;
   private final BooleanProperty emptyProperty;
   
   /**
    * Constructs a new {@link GridWallImpl}.
    * @param configuration the {@link BuildWallConfiguration} for customising the {@link BuildWall}.
    * @param database the {@link JenkinsDatabase} for the {@link JenkinsJob}s.
    */
   public GridWallImpl( BuildWallConfiguration configuration, JenkinsDatabase database ) {
      this.jobPanels = new HashMap<>();
      this.database = database;
      this.configuration = configuration;
      this.theme = new BuildWallThemeImpl( "Default" );
      this.emptyProperty = new SimpleBooleanProperty( true );
      
      constructLayout();
      
      database.jenkinsJobs().addListener( ( Change< ? extends JenkinsJob > change ) -> constructLayout() );
      database.jenkinsJobProperties().addBuildResultStatusListener( ( job, old, updated ) -> constructLayout() );
      database.jenkinsJobProperties().addBuildStateListener( ( job, old, updated ) -> constructLayout() );
      
      configuration.numberOfColumns().addListener( ( source, old, updated ) -> constructLayout() );
      configuration.jobPolicies().addListener( ( MapChangeListener.Change< ? extends JenkinsJob, ? extends BuildWallJobPolicy > change ) -> {
         constructLayout();
      } );
   }//End Constructor

   /**
    * Method to construct the layout for the current {@link JenkinsJob}s and {@link BuildWallConfiguration}.
    */
   void constructLayout() {
      DecoupledPlatformImpl.runLater( () -> fxConstruction() ); 
   }//End Method
   
   /**
    * Separation of accessible method and actual construction. Note this must be completed on
    * the java fx thread.
    */
   private void fxConstruction(){
      removalAllPanelsAndCleanUp();
      
      if ( database.jenkinsJobs().isEmpty() ) {
         return;
      }
      
      List< JenkinsJob > jobsToShow = identifyDisplayedJobs();
      int numberOfColumns = calculateNumberOfColumnsToShow( jobsToShow );
      constructAndRedrawPanels( jobsToShow, numberOfColumns );
      
      emptyProperty.set( jobPanels.size() == 0 );
   }//End Method

   /** 
    * Method to remove all existing panels and tidy up any references to them.
    */
   private void removalAllPanelsAndCleanUp() {
      getChildren().forEach( GridPane::clearConstraints );
      getChildren().clear();
      getRowConstraints().clear();
      getColumnConstraints().clear();
      jobPanels.values().forEach( panel -> panel.detachFromSystem() );
      jobPanels.clear();
   }//End Method
   
   /**
    * Method to calculate the number of columns to show given the identified {@link JenkinsJob}s
    * and {@link BuildWallConfiguration} associated.
    * @param jobsToShow the {@link List} of {@link JenkinsJob}s to show.
    * @return the number of columns needed.
    */
   private int calculateNumberOfColumnsToShow( List< JenkinsJob > jobsToShow ) {
      int numberOfColumns = configuration.numberOfColumns().get();
      numberOfColumns = Math.min( jobsToShow.size(), numberOfColumns );
      numberOfColumns = Math.max( numberOfColumns, 1 );
      return numberOfColumns;
   }//End Method
   
   /**
    * Method to perform the construction of the panels.
    * @param jobsToShow the {@link JenkinsJob}s panels are needed for.
    * @param numberOfColumns the number of columns to display them in.
    */
   private void constructAndRedrawPanels( List< JenkinsJob > jobsToShow, int numberOfColumns ) {
      int columnCount = 0;
      int rowCount = 0;
      
      jobsToShow.sort( Comparators.stringExtractionComparater( job -> job.nameProperty().get() ) );
      
      for ( JenkinsJob job : jobsToShow ) {
         if ( columnCount == numberOfColumns ) {
            columnCount = 0;
            rowCount++;
         }
         
         JobPanelImpl panel = new JobPanelImpl( configuration, theme, job );
         jobPanels.put( job, panel );
         
         final int columnToAddTo = columnCount;
         final int rowToAddTo = rowCount;
         DecoupledPlatformImpl.runLater( () -> add( panel, columnToAddTo, rowToAddTo ) );
         
         columnCount++;
      }
      
      expandLastPanelIfNeeded( jobsToShow.size(), numberOfColumns, columnCount, rowCount );
      fixColumnWidths( numberOfColumns );
      fixRowHeights( rowCount + 1 ); //accounting for indices.
   }//End Method
   
   /**
    * Method to identify the {@link JenkinsJob}s that will be displayed on the {@link GridWallImpl}.
    * @return the {@link JenkinsJob}s to display.
    */
   private List< JenkinsJob > identifyDisplayedJobs(){
      List<  JenkinsJob > jobsToShow = new ArrayList<>();
      for ( JenkinsJob job : new ArrayList<>( database.jenkinsJobs() ) ) {
         BuildWallJobPolicy policy = configuration.jobPolicies().get( job );
         boolean displayJob = policy == null ? true : policy.shouldShow( job );
         if ( displayJob ) jobsToShow.add( job );
      }
      return jobsToShow;
   }//End Method

   /**
    * Method to expand the last {@link JobPanelImpl} if the number {@link JenkinsJob}s doesn't evenly fit
    * on the {@link GridWallImpl}.
    * @param numberOfJobs the number of {@link JenkinsJob} in the {@link JenkinsDatabase}.
    * @param numberOfColumns the number of columns to use.
    * @param columnCount the last column index.
    * @param rowCount the last row index.
    */
   private void expandLastPanelIfNeeded( int numberOfJobs, int numberOfColumns, int columnCount, int rowCount ) {
      int columnsToSpan = numberOfColumns - ( numberOfJobs % numberOfColumns );
      if ( columnsToSpan == numberOfColumns ) return;
      
      DecoupledPlatformImpl.runLater( () -> { 
            //handle multiple redraws in quick succession.
            if ( getChildren().isEmpty() ) return;
            
            Node child = getChildren().get( getChildren().size() - 1 );
            GridPane.setConstraints( 
                  child, 
                  columnCount - 1, 
                  rowCount,
                  columnsToSpan + 1, //plus the original slot
                  1
            );
      } );
   }//End Method

   /**
    * Method to fix the heights of the rows so that they are evenly distributed across the {@link GridPane}.
    * @param rowCount the number of rows.
    */
   private void fixRowHeights( int rowCount ) {
      double rowPercentageHeight = calculateProportion( rowCount ); 
      for ( int i = 0; i < rowCount; i++ ) {
         RowConstraints row = new RowConstraints();
         row.setPercentHeight( rowPercentageHeight );
         getRowConstraints().add( row );
      }
   }//End Method
   
   /**
    * Method to fix the widths of the columns so that they are evenly distributed across the {@link GridPane}.
    * @param numberOfColumns the number of columns.
    */
   private void fixColumnWidths( int numberOfColumns ){
      double columnPercentageWidth = calculateProportion( numberOfColumns ); 
      for ( int i = 0; i < numberOfColumns; i++ ) {
         ColumnConstraints column = new ColumnConstraints();
         column.setPercentWidth( columnPercentageWidth );
         getColumnConstraints().add( column );
      }
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public BooleanProperty emptyProperty() {
      return emptyProperty;
   }//End Method
   
   /**
    * Method to calculate the proportion of each item in the {@link GridPane}.
    * @param numberOfItems the number of items to display across a length.
    * @return the proportion to use.
    */
   static double calculateProportion( double numberOfItems ) {
      if ( numberOfItems == 0 ) return 200;
      /* 100 is conceptually correct, however when dealing with fractions
       * it leaves a small percentage not covered. If we double to 200 we
       * keep proportions correct and the values adding up to > 100% cause the
       * entire screen to be filled. */
      return 200 / numberOfItems;
   }//End Method

   /**
    * Getter for the {@link JobPanelImpl} associated with the given {@link JenkinsJob}.
    * @param job the {@link JenkinsJob} to get for.
    * @return the {@link JobPanelImpl} associated.
    */
   JobPanelImpl getPanelFor( JenkinsJob job ) {
      return jobPanels.get( job );
   }//End Method

}//End Class
