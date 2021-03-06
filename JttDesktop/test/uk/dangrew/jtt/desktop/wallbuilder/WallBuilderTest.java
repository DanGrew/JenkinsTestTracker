/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.wallbuilder;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.dangrew.kode.launch.TestApplication;

public class WallBuilderTest {
   
   private static final double PREFERRED_WIDTH = 600;
   private static final double PREFERRED_HEIGHT = 400;

   private ContentArea initial;
   
   @Mock private ContentAreaSelector selector;
   @Mock private ContentAreaRemover remover;
   @Mock private BoundaryCollisions collisions;
   private WallBuilder systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      TestApplication.startPlatform();
      MockitoAnnotations.initMocks( this );
      systemUnderTest = new WallBuilder( selector, remover, collisions );
      systemUnderTest.setPrefSize( PREFERRED_WIDTH, PREFERRED_HEIGHT );
      initial = getContent( 0 );
   }//End Method
   
   @Ignore
   @Test public void manual() throws InterruptedException{
      TestApplication.launch( () -> new WallBuilder() );
      
      Thread.sleep( 5000 );
      
      systemUnderTest.splitVertically();
      
      Thread.sleep( 5000 );
      
      systemUnderTest.splitHorizontally();
      
      Thread.sleep( 1000000 );
   }//End Method
   
   @Test public void shouldSetNodesOnSelector(){
      verify( selector ).setNodes( systemUnderTest.getChildren() );
   }//End Method
   
   @Test public void shouldSetNodesOnRemover(){
      verify( remover ).setNodes( systemUnderTest.getChildren() );
   }//End Method
   
   @Test public void shouldFixBoundariesInitially(){
      assertThat( initial.leftBoundary().isFixed(), is( true ) );
      assertThat( initial.rightBoundary().isFixed(), is( true ) );
      assertThat( initial.topBoundary().isFixed(), is( true ) );
      assertThat( initial.bottomBoundary().isFixed(), is( true ) );
   }//End Method
   
   @Test public void shouldUpdateContentAreasWithDimensionUpdates(){
      systemUnderTest.resize( 100, 101 );
      assertThat( initial.getWidth(), is( 100.0 ) );
      assertThat( initial.getHeight(), is( 101.0 ) );
   }//End Method

   @Test public void shouldHaveSingleContentAreaInitially() {
      assertThat( initial.getWidth(), is( systemUnderTest.getWidth() ) );
      assertThat( initial.getHeight(), is( systemUnderTest.getHeight() ) );
      assertThat( initial.getTranslateX(), is( 0.0 ) );
      assertThat( initial.getTranslateY(), is( 0.0 ) );
   }//End Method
   
   @Test public void shouldIgnoreSplitVeritcallyIfNoSelection(){
      systemUnderTest.splitVertically();
   }//End Method
   
   @Test public void shouldIgnoreSplitHorizontllyIfNoSelection(){
      systemUnderTest.splitHorizontally();
   }//End Method
   
   @Test public void shouldSplitInitialContentVertically(){
      when( selector.getSelection() ).thenReturn( initial );
      
      systemUnderTest.splitVertically();
      new ContentAreaAsserter( initial )
         .withTopLeft( 0, 0 )
         .withBottomRight( 50, 100 )
         .assertArea();
      new ContentAreaAsserter( getContent( 1 ) )
         .withTopLeft( 50, 0 )
         .withBottomRight( 100, 100 )
         .assertArea();
   }//End Method
   
   @Test public void shouldSplitInitialContentHorizontally(){
      when( selector.getSelection() ).thenReturn( initial );
      
      systemUnderTest.splitHorizontally();
      new ContentAreaAsserter( initial )
         .withTopLeft( 0, 0 )
         .withBottomRight( 100, 50 )
         .assertArea();
      new ContentAreaAsserter( getContent( 1 ) )
         .withTopLeft( 0, 50 )
         .withBottomRight( 100, 100 )
         .assertArea();
   }//End Method
   
   @Test public void shouldSplitVerticallyAndShareBoundaries(){
      when( selector.getSelection() ).thenReturn( initial );
      systemUnderTest.splitVertically();
      
      ContentArea other = getContent( 1 );
      assertThat( initial.topBoundary(), is( not( other.bottomBoundary() ) ) );
      assertThat( initial.bottomBoundary(), is( not( other.bottomBoundary() ) ) );
      assertThat( initial.bottomBoundary(), is( other.topBoundary() ) );
      
      assertThat( initial.rightBoundary(), is( other.rightBoundary() ) );
      assertThat( initial.leftBoundary(), is( other.leftBoundary() ) );
   }//End Method
   
   @Test public void shouldSplitHorizontallyAndShareBoundaries(){
      when( selector.getSelection() ).thenReturn( initial );
      systemUnderTest.splitHorizontally();
      
      ContentArea other = getContent( 1 );
      assertThat( initial.leftBoundary(), is( not( other.leftBoundary() ) ) );
      assertThat( initial.rightBoundary(), is( not( other.rightBoundary() ) ) );
      assertThat( initial.rightBoundary(), is( other.leftBoundary() ) );
      
      assertThat( initial.topBoundary(), is( other.topBoundary() ) );
      assertThat( initial.bottomBoundary(), is( other.bottomBoundary() ) );
   }//End Method
   
   @Test public void shouldSplitContentVerticallyWithTranslation(){
      unfixBoundsForTesting();
      
      initial.leftBoundary().changePosition( 20 );
      initial.topBoundary().changePosition( 40 );
      
      new ContentAreaAsserter( initial )
         .withTopLeft( 40, 20 )
         .withBottomRight( 100, 100 )
         .assertArea();
      
      when( selector.getSelection() ).thenReturn( initial );
      systemUnderTest.splitVertically();
      
      new ContentAreaAsserter( initial )
         .withTopLeft( 40, 20 )
         .withBottomRight( 70, 100 )
         .assertArea();
      new ContentAreaAsserter( getContent( 1 ) )
         .withTopLeft( 70, 20 )
         .withBottomRight( 100, 100 )
         .assertArea();
   }//End Method
   
   @Test public void shouldSplitInitialContentHorizontallyWithTranslation(){
      unfixBoundsForTesting();
      
      initial.leftBoundary().changePosition( 20 );
      initial.topBoundary().changePosition( 40 );
      
      new ContentAreaAsserter( initial )
         .withTopLeft( 40, 20 )
         .withBottomRight( 100, 100 )
         .assertArea();
      
      when( selector.getSelection() ).thenReturn( initial );
      systemUnderTest.splitHorizontally();
      
      new ContentAreaAsserter( initial )
         .withTopLeft( 40, 20 )
         .withBottomRight( 100, 60 )
         .assertArea();
      new ContentAreaAsserter( getContent( 1 ) )
         .withTopLeft( 40, 60 )
         .withBottomRight( 100, 100 )
         .assertArea();
   }//End Method
   
   @Test public void shouldPushBoundary(){
      unfixBoundsForTesting();
      
      initial.leftBoundary().changePosition( 40 );
      when( selector.getSelection() ).thenReturn( initial );

      systemUnderTest.push( SquareBoundary.Left, 15 );
      assertThat( initial.leftBoundary().positionPercentage(), is( 25.0 ) );
      
      Collection< ContentArea > areas = SquareBoundary.Left.boundary( initial ).boundedAreas();
      verify( collisions ).mergeBoundaries( SquareBoundary.Left.boundary( initial ) );
      verify( remover ).verifyBoundaries( areas );
   }//End Method
   
   @Test public void shouldPullBoundary(){
      unfixBoundsForTesting();
      
      initial.leftBoundary().changePosition( 40 );
      when( selector.getSelection() ).thenReturn( initial );

      systemUnderTest.pull( SquareBoundary.Left, 15 );
      assertThat( initial.leftBoundary().positionPercentage(), is( 55.0 ) );
      
      Collection< ContentArea > areas = SquareBoundary.Left.boundary( initial ).boundedAreas();
      verify( collisions ).mergeBoundaries( SquareBoundary.Left.boundary( initial ) );
      verify( remover ).verifyBoundaries( areas );
   }//End Method
   
   /**
    * Method to unfix the {@link ContentBoundary}s surrounding the initial {@link ContentArea}.
    */
   private void unfixBoundsForTesting(){
      initial.leftBoundary().setFixed( false );
      initial.rightBoundary().setFixed( false );
      initial.topBoundary().setFixed( false );
      initial.bottomBoundary().setFixed( false );
   }//End Method
   
   /**
    * Convenience method to get the {@link ContentArea} at the given position in the {@link WallBuilder}.
    * @param index the index of the {@link ContentArea}.
    * @return the {@link ContentArea}. Assertion failure if not present.
    */
   private ContentArea getContent( int index ) {
      assertThat( systemUnderTest.getChildren(), hasSize( greaterThanOrEqualTo( index + 1 ) ) );
      return ( ContentArea )systemUnderTest.getChildren().get( index );
   }//End Method

}//End Class
