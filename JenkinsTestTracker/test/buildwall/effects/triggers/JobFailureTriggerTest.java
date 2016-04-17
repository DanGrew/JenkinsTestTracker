/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.effects.triggers;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import buildwall.effects.flasher.control.ImageFlasherControls;
import buildwall.effects.flasher.control.ImageFlasherControlsImpl;
import model.jobs.BuildResultStatus;
import model.jobs.JenkinsJob;
import model.jobs.JenkinsJobImpl;
import storage.database.JenkinsDatabase;
import storage.database.JenkinsDatabaseImpl;

/**
 * {@link JobFailureTrigger} test.
 */
public class JobFailureTriggerTest {
   
   private JenkinsJob firstJob;
   private JenkinsJob secondJob;
   private JenkinsJob thirdJob;
   private JenkinsDatabase database;
   
   private ImageFlasherControls imageFlasherControls;
   private JobFailureTrigger systemUnderTest;

   @Before public void initialiseSystemUnderTest(){
      database = new JenkinsDatabaseImpl();
      firstJob = new JenkinsJobImpl( "first" );
      secondJob = new JenkinsJobImpl( "second" );
      thirdJob = new JenkinsJobImpl( "third" );
      database.store( firstJob );
      database.store( secondJob );
      database.store( thirdJob );
      database.jenkinsJobs().forEach( job -> job.lastBuildStatusProperty().set( BuildResultStatus.SUCCESS ) );
      
      imageFlasherControls = new ImageFlasherControlsImpl();
      systemUnderTest = new JobFailureTrigger( database, imageFlasherControls );
   }//End Method
   
   @Test public void whenAnyJobFailsSwitchShouldBeFlipped() {
      assertThat( imageFlasherControls.flashingSwitch().get(), is( false ) );
      firstJob.lastBuildStatusProperty().set( BuildResultStatus.FAILURE );
      assertThat( imageFlasherControls.flashingSwitch().get(), is( true ) );
      
      imageFlasherControls.flashingSwitch().set( false );
      secondJob.lastBuildStatusProperty().set( BuildResultStatus.ABORTED );
      assertThat( imageFlasherControls.flashingSwitch().get(), is( true ) );
      
      imageFlasherControls.flashingSwitch().set( false );
      thirdJob.lastBuildStatusProperty().set( BuildResultStatus.UNSTABLE );
      assertThat( imageFlasherControls.flashingSwitch().get(), is( true ) );
   }//End Method
   
   @Test public void whenAnyJobFailsAgainSwitchShouldNotBeFlipped() {
      assertThat( imageFlasherControls.flashingSwitch().get(), is( false ) );
      firstJob.lastBuildStatusProperty().set( BuildResultStatus.FAILURE );
      assertThat( imageFlasherControls.flashingSwitch().get(), is( true ) );
      
      imageFlasherControls.flashingSwitch().set( false );
      firstJob.lastBuildStatusProperty().set( BuildResultStatus.FAILURE );
      assertThat( imageFlasherControls.flashingSwitch().get(), is( false ) );
   }//End Method

}//End Class
