/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.effects.flasher;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import uk.dangrew.jtt.desktop.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.desktop.graphics.PlatformDecouplerImpl;
import uk.dangrew.kode.TestCommon;
import uk.dangrew.kode.launch.TestApplication;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * {@link ImageFlasherImpl} test.
 */
public class ImageFlasherImplTest {
   
   private static final Image ALERT_IMAGE = new Image( ImageFlasherImplTest.class.getResourceAsStream( "alert-image.png" ) );
   private static final Image ALTERNATE_ALERT_IMAGE = new Image( ImageFlasherImplTest.class.getResourceAsStream( "alert-image.png" ) );
   private ImageFlasherProperties properties;
   private ImageFlasherImpl systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      properties = new ImageFlasherPropertiesImpl();
      properties.imageProperty().set( ALERT_IMAGE );
      
      systemUnderTest = new ImageFlasherImpl( properties );
   }//End Method
   
   @Ignore
   @Test public void manualInspection() throws InterruptedException{
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
      
      TestApplication.launch( () -> {
         
         ImageFlasherImpl imageFlasher = new ImageFlasherImpl( properties );
         return imageFlasher;
      } );
      
      properties.flashingSwitch().set( true );
      
      Thread.sleep( 2000000 );
   }//End Method

   @Test public void shouldProvideImageViewWithImage() {
      systemUnderTest.flashOn();
      
      assertThat( systemUnderTest.getCenter(), instanceOf( ImageView.class ) );
      
      ImageView imageView = ( ImageView )systemUnderTest.getCenter();
      assertThat( imageView.getImage(), is( ALERT_IMAGE ) );
      assertThat( imageView, is( systemUnderTest.imageView() ) );
   }//End Method
   
   @Test public void shouldFlashOnAndOff(){
      systemUnderTest.flashOn();
      
      ImageView imageView = ( ImageView )systemUnderTest.getCenter();
      assertThat( imageView, is( notNullValue() ) );
      
      systemUnderTest.flashOff();
      assertThat( systemUnderTest.getCenter(), is( nullValue() ) );
      
      systemUnderTest.flashOn();
      assertThat( systemUnderTest.getCenter(), is( imageView ) );
   }//End Method
   
   @Test public void shouldUseConfiguredTransparency(){
      ImageView imageView = systemUnderTest.imageView();
      assertThat( imageView.getOpacity(), closeTo( properties.transparencyProperty().get(), TestCommon.precision() ) );
      
      properties.transparencyProperty().set( 0.1 );
      assertThat( imageView.getOpacity(), closeTo( properties.transparencyProperty().get(), TestCommon.precision() ) );
   }//End Method
   
   @Test public void shouldDetachTransparencyFromSystem() {
      ImageView imageView = systemUnderTest.imageView();
      assertThat( imageView.getOpacity(), closeTo( properties.transparencyProperty().get(), TestCommon.precision() ) );
      
      systemUnderTest.detachFromSystem();
      
      final double original = properties.transparencyProperty().get();
      properties.transparencyProperty().set( 0.1 );
      assertThat( imageView.getOpacity(), closeTo( original, TestCommon.precision() ) );
   }//End Method
   
   @Test public void shouldUseConfiguredImage(){
      ImageView imageView = systemUnderTest.imageView();
      assertThat( imageView.getImage(), is( properties.imageProperty().get() ) );
      
      properties.imageProperty().set( null );
      assertThat( imageView.getImage(), is( nullValue() ) );
      
      properties.imageProperty().set( ALTERNATE_ALERT_IMAGE );
      assertThat( imageView.getImage(), is( ALTERNATE_ALERT_IMAGE ) );
   }//End Method
   
   @Test public void shouldDetachImageFromSystem() {
      ImageView imageView = systemUnderTest.imageView();
      assertThat( imageView.getImage(), is( properties.imageProperty().get() ) );
      
      systemUnderTest.detachFromSystem();
      
      final Image original = properties.imageProperty().get();
      properties.imageProperty().set( null );
      assertThat( imageView.getImage(), is( original ) );
      
      properties.imageProperty().set( ALTERNATE_ALERT_IMAGE );
      assertThat( imageView.getImage(), is( original ) );
   }//End Method
   
   @Test public void shouldSetPickOnBoundsFalse(){
      assertThat( systemUnderTest.isPickOnBounds(), is( false ) );
   }//End Method
   
}//End Class
