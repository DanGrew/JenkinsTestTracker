/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.sides.jobs;


import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.dangrew.jtt.desktop.javafx.contextmenu.ContextMenuWithCancel;
import uk.dangrew.jtt.desktop.mc.sides.users.UserAssignment;
import uk.dangrew.jtt.desktop.mc.sides.users.UserAssignmentEvent;
import uk.dangrew.jtt.desktop.mc.sides.users.shared.AssignmentMenu;
import uk.dangrew.jtt.desktop.styling.SystemStyling;
import uk.dangrew.jtt.desktop.utility.time.InstantProvider;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.TestJenkinsDatabaseImpl;
import uk.dangrew.jtt.model.users.JenkinsUser;
import uk.dangrew.jtt.model.users.JenkinsUserImpl;
import uk.dangrew.kode.event.structure.Event;
import uk.dangrew.kode.event.structure.EventSubscription;
import uk.dangrew.kode.javafx.platform.JavaFxThreading;
import uk.dangrew.kode.launch.TestApplication;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static uk.dangrew.jtt.desktop.mc.sides.jobs.JobProgressTreeContextMenu.constructDescriptionFor;

/**
 * {@link JobProgressTreeContextMenu} test.
 */
public class JobProgressTreeContextMenuTest {
   
   private static final long TIMESTAMP = 39487534L;
   
   private UserAssignmentEvent assignments;
   @Mock private EventSubscription< UserAssignment > subscription;
   @Captor private ArgumentCaptor<Event< UserAssignment >> captor;
   
   private JenkinsUser user1;
   private JenkinsUser user2;
   
   private JenkinsJob job1;
   private JenkinsJob job2;
   
   private JenkinsDatabase database;
   private JobProgressTree tree;
   @Mock private InstantProvider instantProvider;
   private JobProgressTreeContextMenu systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      TestApplication.startPlatform();
      SystemStyling.initialise();
      MockitoAnnotations.initMocks( this );
      
      assignments = new UserAssignmentEvent();
      assignments.clearAllSubscriptions();
      assignments.register( subscription );
      
      database = new TestJenkinsDatabaseImpl();
      database.store( user1 = new JenkinsUserImpl( "Dan" ) );
      database.store( user2 = new JenkinsUserImpl( "Liz" ) );
      database.store( job1 = new JenkinsJobImpl( "Job1" ) );
      database.store( job2 = new JenkinsJobImpl( "Job2" ) );
      
      tree = new JobProgressTree( database );
      when( instantProvider.get() ).thenReturn( TIMESTAMP );
      systemUnderTest = new JobProgressTreeContextMenu( tree, database, instantProvider );
   }//End Method
   
   @Test public void shouldHaveExpectedMenus() {
      assertThat( systemUnderTest.getItems(), hasSize( 3 ) );
   }//End Method
   
   @Test public void shouldProvideAssignOption() {
      assertThat( systemUnderTest.getItems().get( 0 ), is( instanceOf( AssignmentMenu.class ) ) );
   }//End Method
   
   @Test public void shouldProvideSeparator() {
      assertThat( systemUnderTest.getItems().get( 1 ), is( instanceOf( SeparatorMenuItem.class ) ) );
   }//End Method
   
   @Test public void shouldProvideCancelOption() {
      assertThat( systemUnderTest, is( instanceOf( ContextMenuWithCancel.class ) ) );
   }//End Method
   
   @Test public void shouldHideWhenCancelled() throws InterruptedException {
      BorderPane pane = new BorderPane();
      TestApplication.launch( () -> pane );

       JavaFxThreading.runAndWait( () -> systemUnderTest.show( pane, 0, 0 ) );
      assertThat( systemUnderTest.isShowing(), is( true ) );
       JavaFxThreading.runAndWait( () -> systemUnderTest.getItems().get( 2 ).fire() );
      assertThat( systemUnderTest.isShowing(), is( false ) );
   }//End Method
   
   @Test public void shouldAutoHide() {
      assertThat( systemUnderTest.isAutoHide(), is( true ) );
   }//End Method
   
   @Test public void shouldBeConnectedToDatabase(){
      assertThat( systemUnderTest.isConnectedTo( database ), is( true ) );
      assertThat( systemUnderTest.isConnectedTo( new TestJenkinsDatabaseImpl() ), is( false ) );
   }//End Method
   
   @Test public void shouldRaiseSingleEventForSingleSelection(){
      tree.getSelectionModel().select( 3 );
      
      systemUnderTest.assignMenu().getItems().get( 1 ).fire();
      verify( subscription ).notify( captor.capture() );
      
      UserAssignment assignment = captor.getValue().getValue();
      assertThat( assignment.getJenkinsUser(), is( user2 ) );
      assertThat( assignment.timestampProperty().get(), is( TIMESTAMP ) );
      assertThat( assignment.descriptionProperty().get(), is( constructDescriptionFor( job1 ) ) );
      assertThat( assignment.detailProperty().get(), is( JobProgressTreeContextMenu.ENTER_DETAIL ) );
   }//End Method
   
   @Test public void shouldRaiseMultipleEventForMultipleSelection(){
      tree.getSelectionModel().selectIndices( 3, 4 );
      
      systemUnderTest.assignMenu().getItems().get( 0 ).fire();
      verify( subscription, times( 2 ) ).notify( captor.capture() );
      
      UserAssignment assignment = captor.getAllValues().get( 0 ).getValue();
      assertThat( assignment.getJenkinsUser(), is( user1 ) );
      assertThat( assignment.timestampProperty().get(), is( TIMESTAMP ) );
      assertThat( assignment.descriptionProperty().get(), is( constructDescriptionFor( job1 ) ) );
      assertThat( assignment.detailProperty().get(), is( JobProgressTreeContextMenu.ENTER_DETAIL ) );
      
      assignment = captor.getAllValues().get( 1 ).getValue();
      assertThat( assignment.getJenkinsUser(), is( user1 ) );
      assertThat( assignment.timestampProperty().get(), is( TIMESTAMP ) );
      assertThat( assignment.descriptionProperty().get(), is( constructDescriptionFor( job2 ) ) );
      assertThat( assignment.detailProperty().get(), is( JobProgressTreeContextMenu.ENTER_DETAIL ) );
   }//End Method
   
   @Test public void shouldIgnoreNoSelection(){
      systemUnderTest.assignMenu().getItems().get( 0 ).fire();
      verify( subscription, times( 0 ) ).notify( captor.capture() );
   }//End Method
   
   @Test public void shouldIgnoreNoNotificationItem(){
      tree.getSelectionModel().select( 0 );
      
      systemUnderTest.assignMenu().getItems().get( 0 ).fire();
      verify( subscription, times( 0 ) ).notify( captor.capture() );
   }//End Method
   
   @Test public void shouldRaiseMultipleEventForMultipleSelectionIncludingHeaderItems(){
      tree.getSelectionModel().selectIndices( 0, 3, 4 );
      
      systemUnderTest.assignMenu().getItems().get( 0 ).fire();
      verify( subscription, times( 2 ) ).notify( captor.capture() );
      
      UserAssignment assignment = captor.getAllValues().get( 0 ).getValue();
      assertThat( assignment.getJenkinsUser(), is( user1 ) );
      assertThat( assignment.timestampProperty().get(), is( TIMESTAMP ) );
      assertThat( assignment.descriptionProperty().get(), is( constructDescriptionFor( job1 ) ) );
      assertThat( assignment.detailProperty().get(), is( JobProgressTreeContextMenu.ENTER_DETAIL ) );
      
      assignment = captor.getAllValues().get( 1 ).getValue();
      assertThat( assignment.getJenkinsUser(), is( user1 ) );
      assertThat( assignment.timestampProperty().get(), is( TIMESTAMP ) );
      assertThat( assignment.descriptionProperty().get(), is( constructDescriptionFor( job2 ) ) );
      assertThat( assignment.detailProperty().get(), is( JobProgressTreeContextMenu.ENTER_DETAIL ) );
   }//End Method
   
   @Test public void shouldConstructReadableMessage(){
      assertThat( JobProgressTreeContextMenu.constructDescriptionFor( job1 ), is( "Job1 with state NOT_BUILT" ) );
   }//End Method
   
}//End Class
