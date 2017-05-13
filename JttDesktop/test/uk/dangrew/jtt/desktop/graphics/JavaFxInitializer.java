/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.graphics;

import java.util.concurrent.CountDownLatch;
import java.util.function.Supplier;

import org.junit.Assert;

import com.sun.javafx.application.PlatformImpl;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * {@link JavaFxInitializer} is responsible for launching JavaFx using the correct
 * practices, using {@link Application}, as opposed to JFXPanel.
 */
public class JavaFxInitializer extends Application {
   
   static Stage stage;
   static BorderPane content;

   /**
    * {@inheritDoc}
    */
   @Override public void start(Stage stage) throws Exception {
      Scene scene = new Scene( content, 400, 400 );
      stage.setScene( scene );
      stage.show();
      stage.setMaximized( true );
      JavaFxInitializer.stage = stage;
   }//End Method
      
   /**
    * Method to launch the given {@link Node} {@link Supplier} in a separate {@link javafx.stage.Window}.
    * @param runnable the {@link Supplier} for the {@link Node}.
    */
   public static void launchInWindow( Supplier< Node > runnable ) {
      JavaFxInitializer.startPlatform();
      
      final Stage previousStage = stage;
      
      CountDownLatch latch = new CountDownLatch( 1 );
      PlatformImpl.runLater( () -> {
         try {
            JavaFxInitializer.content = new BorderPane( runnable.get() );
            new JavaFxInitializer().start( new Stage() );
            latch.countDown();
         } catch ( Exception e ) {
            e.printStackTrace();
            Assert.fail( "Unable to launch window." );
         }
      } );
      try {
         latch.await();
      } catch ( InterruptedException e ) {
         Assert.fail( "Failed to wait for launch." );
      }
      
      shutdown( previousStage );
   }//End Method
   
   /**
    * Method to shutdown the given {@link Stage}.
    * @param stage the {@link Stage} to shutdown.
    */
   private static void shutdown( Stage stage ) {
      if ( stage == null ) return;
      
      CountDownLatch shutdownLatch = new CountDownLatch( 1 );
      PlatformImpl.runLater( () -> {
         stage.close();
         shutdownLatch.countDown();
      } );
      try {
         shutdownLatch.await();
      } catch ( InterruptedException e ) {
         Assert.fail( "Failed to wait for shutdown." );
      }
   }//End Method
   
   /**
    * Method to initialise JavaFx using a default {@link Scene} containing a message. The expectation is that
    * this {@link Scene} will be present for the entirety of the testing. This is expected to be used for 
    * running with popups or dialog type items.
    */
   @Deprecated
   public static void startPlatform(){
      //Should be safe to call if already launched.
      PlatformImpl.startup( () -> {} );
   }//End Method
   
   /**
    * Method to run the given {@link Runnable} and wait for it to complete.
    * @param runnable the {@link Runnable} to run.
    */
   public static void runAndWait( Runnable runnable ) {
      CountDownLatch latch = new CountDownLatch( 1 );
      startPlatform();
      PlatformImpl.runLater( () -> {
         runnable.run();
         latch.countDown();
      } );
      try {
         latch.await();
      } catch ( InterruptedException e ) {
         Assert.fail( "Runnable timed out." );
      }
   }//End Method
}//End Class
