/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.theme;

import org.junit.Before;
import org.junit.Test;
import uk.dangrew.kode.synchronization.SynchronizedObservableMap;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class BuildWallThemeImplTest {

   private static final String NAME = "Some name";
   private BuildWallThemeImpl systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      systemUnderTest = new BuildWallThemeImpl( NAME );
   }//End Method
   
   @Test ( expected = IllegalArgumentException.class ) public void shouldNotAllowNullName(){
      new BuildWallThemeImpl( null );
   }//End Method
   
   @Test ( expected = IllegalArgumentException.class ) public void shouldNotAllowInvalidName(){
      new BuildWallThemeImpl( "    " );
   }//End Method
   
   @Test public void shouldProvideNameProperty(){
      assertThat( systemUnderTest.nameProperty(), is( notNullValue() ) );
      assertThat( systemUnderTest.nameProperty(), is( systemUnderTest.nameProperty() ) );
   }//End Method

   @Test public void shouldProvideSynchronisedMapForBarColours() {
      assertThat( systemUnderTest.barColoursMap(), is( instanceOf( SynchronizedObservableMap.class ) ) );
      assertThat( systemUnderTest.barColoursMap(), is( systemUnderTest.barColoursMap() ) );
   }//End Method
   
   @Test public void shouldProvideSynchronisedMapForTrackColours() {
      assertThat( systemUnderTest.trackColoursMap(), is( instanceOf( SynchronizedObservableMap.class ) ) );
      assertThat( systemUnderTest.trackColoursMap(), is( systemUnderTest.trackColoursMap() ) );
   }//End Method
   
   @Test public void shouldProvideSynchronisedMapForJobNameColours() {
      assertThat( systemUnderTest.jobNameColoursMap(), is( instanceOf( SynchronizedObservableMap.class ) ) );
      assertThat( systemUnderTest.jobNameColoursMap(), is( systemUnderTest.jobNameColoursMap() ) );
   }//End Method
   
   @Test public void shouldProvideSynchronisedMapForBuildNumberColours() {
      assertThat( systemUnderTest.buildNumberColoursMap(), is( instanceOf( SynchronizedObservableMap.class ) ) );
      assertThat( systemUnderTest.buildNumberColoursMap(), is( systemUnderTest.buildNumberColoursMap() ) );
   }//End Method
   
   @Test public void shouldProvideSynchronisedMapForCompletionEstimateColours() {
      assertThat( systemUnderTest.completionEstimateColoursMap(), is( instanceOf( SynchronizedObservableMap.class ) ) );
      assertThat( systemUnderTest.completionEstimateColoursMap(), is( systemUnderTest.completionEstimateColoursMap() ) );
   }//End Method
   
   @Test public void shouldProvideSynchronisedMapForDetailColours() {
      assertThat( systemUnderTest.detailColoursMap(), is( instanceOf( SynchronizedObservableMap.class ) ) );
      assertThat( systemUnderTest.detailColoursMap(), is( systemUnderTest.detailColoursMap() ) );
   }//End Method

}//End Class
