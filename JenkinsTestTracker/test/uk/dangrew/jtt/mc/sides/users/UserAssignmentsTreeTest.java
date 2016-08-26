/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.sides.users;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.model.users.JenkinsUser;
import uk.dangrew.jtt.model.users.JenkinsUserImpl;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.storage.database.JenkinsDatabaseImpl;

/**
 * {@link UserAssignmentsTree} test.
 */
public class UserAssignmentsTreeTest {

   private JenkinsDatabase database;
   private UserAssignmentsTree systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      database = new JenkinsDatabaseImpl();
      database.store( new JenkinsUserImpl( "Me" ) );
      database.store( new JenkinsUserImpl( "You" ) );
      database.store( new JenkinsUserImpl( "Irene" ) );
      systemUnderTest = new UserAssignmentsTree( database );
   }//End Method
   
   @Ignore
   @Test public void manual() throws InterruptedException{
      JavaFxInitializer.launchInWindow( () -> systemUnderTest );
      
      JenkinsUser dan = new JenkinsUserImpl( "Dan" );
      systemUnderTest.getController().add( dan );
      systemUnderTest.getController().add( new JenkinsUserImpl( "Liz" ) );
      systemUnderTest.getController().add( new JenkinsUserImpl( "Jeffrey" ) );
      
      systemUnderTest.getController().add( new UserAssignment( dan, 397463876, "This is an issue", "" ) );
      systemUnderTest.getController().add( new UserAssignment( dan, 94857, "This is another issue", "" ) );
      systemUnderTest.getController().add( new UserAssignment( dan, 29458123, "Dan's very busy!", "" ) );
      
      Thread.sleep( 1000000 );
   }//End Method
   
   @Test public void shouldProvideLayout() {
      assertThat( systemUnderTest.getLayoutManager(), is( notNullValue() ) );
   }//End Method
   
   @Test public void shouldProvideController() {
      assertThat( systemUnderTest.getController(), is( notNullValue() ) );
      assertThat( systemUnderTest.getController().getLayoutManager(), is( systemUnderTest.getLayoutManager() ) );
   }//End Method

   @Test public void shouldConstructLayoutWithUsers() {
      assertThat( systemUnderTest.getRoot().getChildren(), hasSize( database.jenkinsUsers().size() ) );
   }//End Method
   
   @Test public void shouldInsertRequiredColumns(){
      assertThat( systemUnderTest.getColumns(), hasSize( 2 ) );
      
      UserAssignmentsTreeItem item = mock( UserAssignmentsTreeItem.class ); 
      systemUnderTest.getColumns().get( 0 ).getCellValueFactory().call( new CellDataFeatures<>( systemUnderTest, null, new TreeItem<>( item ) ) );
      verify( item ).firstColumnProperty();
      
      systemUnderTest.getColumns().get( 1 ).getCellValueFactory().call( new CellDataFeatures<>( systemUnderTest, null, new TreeItem<>( item ) ) );
      verify( item ).secondColumnProperty();
   }//End Method
}//End Class
