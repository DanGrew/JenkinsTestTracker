/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.releases;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.Supplier;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javafx.scene.control.Button;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.versioning.Versioning;
import uk.dangrew.jupa.update.download.ReleasesDownloader;
import uk.dangrew.jupa.update.view.panel.ReleaseNotificationTimeout;

/**
 * {@link ReleasesWrapper} test.
 */
public class ReleasesWrapperTest {

   private Supplier< ReleaseNotificationTimeout> timeoutSupplier;
   @Mock private ReleaseNotificationTimeout timeout;
   @Mock private Versioning versioning;
   @Mock private ReleasesDownloader downloader;
   private ReleasesWrapper systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      timeoutSupplier = () -> timeout;
      when( versioning.getVersionNumber() ).thenReturn( "some version" );
      systemUnderTest = new ReleasesWrapper( downloader, versioning, timeoutSupplier );
   }//End Method
   
   @Ignore
   @Test public void manual() throws InterruptedException{
      JavaFxInitializer.launchInWindow( () -> {
         ReleasesWrapper wrapper = new ReleasesWrapper();
         Button button = new Button();
         button.setMaxHeight( Double.MAX_VALUE );
         button.setMaxWidth( Double.MAX_VALUE );
         wrapper.setContent( button );
         return wrapper;
      } );
      Thread.sleep( 10000000 );
   }//End Method
   
   @Test public void downloaderShouldUseLocationAssociated() {
      systemUnderTest = new ReleasesWrapper();
      assertThat( systemUnderTest.downloader().getDownloadLocation(), is( ReleasesWrapper.RELEASES_LOCATION ) );
   }//End Method
   
   @Test public void taskShouldBeConfiguredToUseDownloaderAndNotifySut(){
      when( downloader.downloadContent() ).thenReturn( 
               "Release, \"1.4.103\"\n"
               + "Download, \"somewhere\"\n"
               + "Description, \"This is the first downloadable for testing purposes.\"\n"
      );
      systemUnderTest.task().run();
      verify( downloader, atLeastOnce() ).downloadContent();
      assertThat( systemUnderTest.isShowing(), is( true ) );
   }//End Method
   
   @Test public void shouldHaveNotificationSchedulerForTaskAndPeriod(){
      assertThat( systemUnderTest.scheduler().isTask( systemUnderTest.task() ), is( true ) );
      assertThat( systemUnderTest.scheduler().getNotificationPeriod(), is( ReleasesWrapper.NOTIFICATION_PERIOD ) );
   }//End Method
   
   @Test public void shouldScheduleTimeoutOnShowNotHide(){
      systemUnderTest.show();
      verify( timeout ).schedule( Mockito.any(), Mockito.eq( ReleasesWrapper.NOTIFICATION_TIMEOUT ), Mockito.eq( systemUnderTest ) );
      
      systemUnderTest.hide();
      verify( timeout ).schedule( Mockito.any(), Mockito.eq( ReleasesWrapper.NOTIFICATION_TIMEOUT ), Mockito.eq( systemUnderTest ) );
      
      systemUnderTest.show();
      verify( timeout, times( 2 ) ).schedule( Mockito.any(), Mockito.eq( ReleasesWrapper.NOTIFICATION_TIMEOUT ), Mockito.eq( systemUnderTest ) );
   }//End Method

}//End Class