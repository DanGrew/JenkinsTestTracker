/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.core.testupdating;
import org.junit.Ignore;

/**
 * {@link JenkinsTestTrackerCoreImpl} test.
 */
@Ignore
public class JttCoreTestUpdatingTest {

//   @Test public void shouldHaveDatabase(){
//      Assert.assertNotNull( new JttTestCoreImpl( Mockito.mock( ExternalApi.class ) ).getJenkinsDatabase() );
//   }//End Method
//   
//   @Test public void shouldHaveTimeKeeper(){
//      Assert.assertNotNull( new JttTestCoreImpl( Mockito.mock( ExternalApi.class ) ).getJobUpdater() );
//   }//End Method
//   
//   @Test public void shouldPullInChangesFromExternalApiAndUpdateDatabase(){
//      final ExternalApi api = Mockito.mock( ExternalApi.class );
//      JenkinsTestTrackerCoreImpl core = new JttTestCoreImpl( api );
//      
//      JenkinsDatabase database = core.getJenkinsDatabase();
//      TimeKeeper timeKeeper = core.getJobUpdater();
//      
//      Assert.assertTrue( database.hasNoJenkinsJobs() );
//      Assert.assertTrue( database.hasNoTestClasses() );
//      
//      //Pull in the only JenkinsJob...
//      String response = TestCommon.readFileIntoString( getClass(), "job.json" );
//      Mockito.when( api.getJobsList() ).thenReturn( response );
//      timeKeeper.poll();
//      
//      //...and prove that nothing else changes, and the job is initially built...
//      Assert.assertFalse( database.hasNoJenkinsJobs() );
//      Assert.assertTrue( database.hasNoTestClasses() );
//      Assert.assertEquals( 1, database.jenkinsJobs().size() );
//      Assert.assertEquals( BuildState.Built, database.jenkinsJobs().get( 0 ).buildStateProperty().get() );
//      
//      //...then simulate the building state...
//      response = TestCommon.readFileIntoString( getClass(), "building-state.json" );
//      Mockito.when( api.getLastBuildBuildingState( Mockito.any() ) ).thenReturn( response );
//      timeKeeper.poll();
//      
//      //...and prove the job starts building...
//      Assert.assertFalse( database.hasNoJenkinsJobs() );
//      Assert.assertTrue( database.hasNoTestClasses() );
//      Assert.assertEquals( 1, database.jenkinsJobs().size() );
//      Assert.assertEquals( BuildState.Building, database.jenkinsJobs().get( 0 ).buildStateProperty().get() );
//      
//      //...finish the build and provide test results...
//      response = TestCommon.readFileIntoString( getClass(), "built-state.json" );
//      Mockito.when( api.getLastBuildBuildingState( Mockito.any() ) ).thenReturn( response );
//      response = TestCommon.readFileIntoString( getClass(), "all-passing-tests.json" );
//      Mockito.when( api.getLatestTestResultsWrapped( Mockito.any() ) ).thenReturn( response );
//      response = TestCommon.readFileIntoString( getClass(), "job-details-22.json" );
//      Mockito.when( api.getLastBuildJobDetails( Mockito.any() ) ).thenReturn( response );
//      timeKeeper.poll();
//      
//      //...prove the build is complete and test results are parsed...
//      Assert.assertFalse( database.hasNoJenkinsJobs() );
//      Assert.assertFalse( database.hasNoTestClasses() );
//      Assert.assertEquals( 1, database.jenkinsJobs().size() );
//      Assert.assertEquals( 36, database.testClasses().size() );
//      Assert.assertEquals( BuildState.Built, database.jenkinsJobs().get( 0 ).buildStateProperty().get() );
//      Assert.assertEquals( TestResultStatus.PASSED, database.testClasses().get( 0 ).testCasesList().get( 1 ).statusProperty().get() );
//      
//      //...then provide a new build with some failures...
//      response = TestCommon.readFileIntoString( getClass(), "some-failures-tests.json" );
//      Mockito.when( api.getLatestTestResultsWrapped( Mockito.any() ) ).thenReturn( response );
//      response = TestCommon.readFileIntoString( getClass(), "job-details-23.json" );
//      Mockito.when( api.getLastBuildJobDetails( Mockito.any() ) ).thenReturn( response );
//      timeKeeper.poll();
//      
//      //...and prove the failures are picked up...
//      Assert.assertFalse( database.hasNoJenkinsJobs() );
//      Assert.assertFalse( database.hasNoTestClasses() );
//      Assert.assertEquals( 1, database.jenkinsJobs().size() );
//      Assert.assertEquals( 36, database.testClasses().size() );
//      Assert.assertEquals( TestResultStatus.FAILED, database.testClasses().get( 0 ).testCasesList().get( 1 ).statusProperty().get() );
//      
//      //...then pass all the tests in a further build...
//      response = TestCommon.readFileIntoString( getClass(), "all-passing-tests.json" );
//      Mockito.when( api.getLatestTestResultsWrapped( Mockito.any() ) ).thenReturn( response );
//      response = TestCommon.readFileIntoString( getClass(), "job-details-24.json" );
//      Mockito.when( api.getLastBuildJobDetails( Mockito.any() ) ).thenReturn( response );
//      timeKeeper.poll();
//      
//      //...and prove that they are picked up...
//      Assert.assertFalse( database.hasNoJenkinsJobs() );
//      Assert.assertFalse( database.hasNoTestClasses() );
//      Assert.assertEquals( 1, database.jenkinsJobs().size() );
//      Assert.assertEquals( 36, database.testClasses().size() );
//      Assert.assertEquals( TestResultStatus.PASSED, database.testClasses().get( 0 ).testCasesList().get( 1 ).statusProperty().get() );
//      
//      //...finally fail a large number of tests...
//      response = TestCommon.readFileIntoString( getClass(), "large-failures-tests.json" );
//      Mockito.when( api.getLatestTestResultsWrapped( Mockito.any() ) ).thenReturn( response );
//      response = TestCommon.readFileIntoString( getClass(), "job-details-25.json" );
//      Mockito.when( api.getLastBuildJobDetails( Mockito.any() ) ).thenReturn( response );
//      timeKeeper.poll();
//      
//      //...and prove that they are picked up.
//      Assert.assertFalse( database.hasNoJenkinsJobs() );
//      Assert.assertFalse( database.hasNoTestClasses() );
//      Assert.assertEquals( 1, database.jenkinsJobs().size() );
//      Assert.assertEquals( 36, database.testClasses().size() );
//      Assert.assertEquals( TestResultStatus.FAILED, database.testClasses().get( 0 ).testCasesList().get( 1 ).statusProperty().get() );
//   }//End Method
//   
//   @Test public void shouldBringInMultipleJobsAndKeepThemUpdated(){
//      final ExternalApi api = Mockito.mock( ExternalApi.class );
//      JenkinsTestTrackerCoreImpl core = new JttTestCoreImpl( api );
//      JenkinsDatabase database = core.getJenkinsDatabase();
//      TimeKeeper timeKeeper = core.getJobUpdater();
//      
//      Assert.assertTrue( database.hasNoJenkinsJobs() );
//      Assert.assertTrue( database.hasNoTestClasses() );
//      
//      //Pull in the only JenkinsJob...
//      String response = TestCommon.readFileIntoString( getClass(), "multiple-jobs.json" );
//      Mockito.when( api.getJobsList() ).thenReturn( response );
//      timeKeeper.poll();
//      
//      //...and prove that nothing else changes, and the job is initially built...
//      Assert.assertFalse( database.hasNoJenkinsJobs() );
//      Assert.assertTrue( database.hasNoTestClasses() );
//      Assert.assertEquals( 2, database.jenkinsJobs().size() );
//      Assert.assertEquals( BuildState.Built, database.jenkinsJobs().get( 0 ).buildStateProperty().get() );
//      
//      final JenkinsJob first = database.jenkinsJobs().get( 0 );
//      final JenkinsJob second = database.jenkinsJobs().get( 1 );
//      
//      //...then simulate the building state...
//      response = TestCommon.readFileIntoString( getClass(), "building-state.json" );
//      Mockito.when( api.getLastBuildBuildingState( first ) ).thenReturn( response );
//      timeKeeper.poll();
//      
//      //...and prove the job starts building...
//      Assert.assertFalse( database.hasNoJenkinsJobs() );
//      Assert.assertTrue( database.hasNoTestClasses() );
//      Assert.assertEquals( 2, database.jenkinsJobs().size() );
//      Assert.assertEquals( BuildState.Building, first.buildStateProperty().get() );
//      
//      //...finish the build and provide test results...
//      response = TestCommon.readFileIntoString( getClass(), "built-state.json" );
//      Mockito.when( api.getLastBuildBuildingState( first ) ).thenReturn( response );
//      response = TestCommon.readFileIntoString( getClass(), "all-passing-tests.json" );
//      Mockito.when( api.getLatestTestResultsWrapped( first ) ).thenReturn( response );
//      response = TestCommon.readFileIntoString( getClass(), "job-details-22.json" );
//      Mockito.when( api.getLastBuildJobDetails( first ) ).thenReturn( response );
//      timeKeeper.poll();
//      
//      //...prove the build is complete and test results are parsed...
//      Assert.assertFalse( database.hasNoJenkinsJobs() );
//      Assert.assertFalse( database.hasNoTestClasses() );
//      Assert.assertEquals( 2, database.jenkinsJobs().size() );
//      Assert.assertEquals( 36, database.testClasses().size() );
//      Assert.assertEquals( BuildState.Built, database.jenkinsJobs().get( 0 ).buildStateProperty().get() );
//      Assert.assertEquals( TestResultStatus.PASSED, database.testClasses().get( 0 ).testCasesList().get( 1 ).statusProperty().get() );
//      
//      //...then provide a new build with some failures...
//      response = TestCommon.readFileIntoString( getClass(), "some-failures-tests.json" );
//      Mockito.when( api.getLatestTestResultsWrapped( second ) ).thenReturn( response );
//      response = TestCommon.readFileIntoString( getClass(), "job-details-23.json" );
//      Mockito.when( api.getLastBuildJobDetails( second ) ).thenReturn( response );
//      timeKeeper.poll();
//      
//      //...and prove the failures are picked up...
//      Assert.assertFalse( database.hasNoJenkinsJobs() );
//      Assert.assertFalse( database.hasNoTestClasses() );
//      Assert.assertEquals( 2, database.jenkinsJobs().size() );
//      Assert.assertEquals( 36, database.testClasses().size() );
//      Assert.assertEquals( TestResultStatus.FAILED, database.testClasses().get( 0 ).testCasesList().get( 1 ).statusProperty().get() );
//      
//      //...then pass all the tests in a further build...
//      response = TestCommon.readFileIntoString( getClass(), "all-passing-tests.json" );
//      Mockito.when( api.getLatestTestResultsWrapped( first ) ).thenReturn( response );
//      response = TestCommon.readFileIntoString( getClass(), "job-details-24.json" );
//      Mockito.when( api.getLastBuildJobDetails( first ) ).thenReturn( response );
//      timeKeeper.poll();
//      
//      //...and prove that they are picked up...
//      Assert.assertFalse( database.hasNoJenkinsJobs() );
//      Assert.assertFalse( database.hasNoTestClasses() );
//      Assert.assertEquals( 2, database.jenkinsJobs().size() );
//      Assert.assertEquals( 36, database.testClasses().size() );
//      Assert.assertEquals( TestResultStatus.PASSED, database.testClasses().get( 0 ).testCasesList().get( 1 ).statusProperty().get() );
//      
//      //...finally fail a large number of tests...
//      response = TestCommon.readFileIntoString( getClass(), "large-failures-tests.json" );
//      Mockito.when( api.getLatestTestResultsWrapped( second ) ).thenReturn( response );
//      response = TestCommon.readFileIntoString( getClass(), "job-details-25.json" );
//      Mockito.when( api.getLastBuildJobDetails( second ) ).thenReturn( response );
//      timeKeeper.poll();
//      
//      //...and prove that they are picked up.
//      Assert.assertFalse( database.hasNoJenkinsJobs() );
//      Assert.assertFalse( database.hasNoTestClasses() );
//      Assert.assertEquals( 2, database.jenkinsJobs().size() );
//      Assert.assertEquals( 36, database.testClasses().size() );
//      Assert.assertEquals( TestResultStatus.FAILED, database.testClasses().get( 0 ).testCasesList().get( 1 ).statusProperty().get() );
//   }//End Method
   
}//End Class
