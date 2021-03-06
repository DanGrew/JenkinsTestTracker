/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.properties;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import uk.dangrew.kode.TestCommon;

/**
 * {@link DualWallConfigurationImpl} test.
 */
public class DualConfigurationImplTest {

   private DualWallConfiguration systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      systemUnderTest = new DualWallConfigurationImpl();
   }//End Method
   
   @Test public void shouldProvideDividerPositionProperty() {
      assertThat( systemUnderTest.dividerPositionProperty(), is( notNullValue() ) );
      assertThat( systemUnderTest.dividerPositionProperty().get(), is( closeTo( DualWallConfigurationImpl.DEFAULT_DIVIDER_POSITION, TestCommon.precision() ) ) );
   }//End Method
   
   @Test public void shouldProvideDividerOrientationProperty() {
      assertThat( systemUnderTest.dividerOrientationProperty(), is( notNullValue() ) );
      assertThat( systemUnderTest.dividerOrientationProperty().get(), is( DualWallConfigurationImpl.DEFAULT_DIVIDER_ORIENTATION ) );
   }//End Method

}//End Class
