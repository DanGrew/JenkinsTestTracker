/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.components;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.junit.*;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallConfigurationImpl;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallJobPolicy;
import uk.dangrew.jtt.desktop.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.desktop.graphics.TestPlatformDecouplerImpl;
import uk.dangrew.jtt.desktop.styling.FontFamilies;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.kode.TestCommon;
import uk.dangrew.kode.javafx.style.JavaFxStyleTest;
import uk.dangrew.kode.launch.TestApplication;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * {@link BuildWallConfigurationPanelImpl} test.
 */
public class BuildWallConfigurationPaneImplTest {

   private static final String TEST_TITLE = "Some Title";
   private BuildWallConfiguration configuration;
   private BuildWallConfigurationPanelImpl systemUnderTest;
   
   @BeforeClass public static void debugFontFamilies(){
      for ( String family : FontFamilies.getUsableFontFamilies() ) {
         System.out.println( family );
         System.out.println( Font.font( family ).getFamily() );
      }
   }
   
   @BeforeClass public static void initialisePlatform(){
      TestApplication.startPlatform();
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
   }//End Method
   
   @Before public void initialiseSystemUnderTest(){
      configuration = new BuildWallConfigurationImpl();
      systemUnderTest = new BuildWallConfigurationPanelImpl( TEST_TITLE, configuration );
   }//End Method
   
   @Ignore //For manual inspection.
   @Test public void manualInspection() throws InterruptedException {
      TestApplication.launch( () -> {
         for ( int i = 0; i < 100; i++ ) {
            configuration.jobPolicies().put( new JenkinsJobImpl( "job " + i ), BuildWallJobPolicy.values()[ i % 3 ] );
         }
         return new BuildWallConfigurationPanelImpl( TEST_TITLE, configuration ); 
      } );
      
      Thread.sleep( 10000000 );
   }//End Method
   
   @Test public void shouldUseConfiguration(){
      assertThat( systemUnderTest.usesConfiguration( configuration ), is( true ) );
      assertThat( systemUnderTest.usesConfiguration( mock( BuildWallConfiguration.class ) ), is( false ) );
   }//End Method
   
   @Test public void shouldContainNecessaryElements(){
      Label label = systemUnderTest.titleLabel();
      Assert.assertTrue( systemUnderTest.getChildren().contains( label ) );
      
      TitledPane dimensionsPane = systemUnderTest.dimensionsPane();
      Assert.assertTrue( systemUnderTest.getChildren().contains( dimensionsPane ) );
      
      TitledPane fontPane = systemUnderTest.fontPane();
      Assert.assertTrue( systemUnderTest.getChildren().contains( fontPane ) );
      
      TitledPane policiesPane = systemUnderTest.jobPoliciesPane();
      Assert.assertTrue( systemUnderTest.getChildren().contains( policiesPane ) );
      
      TitledPane colourPane = systemUnderTest.colourPane();
      Assert.assertTrue( systemUnderTest.getChildren().contains( colourPane ) );
   }//End Method
   
   @Test public void shouldUseBoldLabels(){
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.titleLabel().getFont().getStyle() ) );
   }//End Method
   
   @Test public void eachPaneShouldCoverEntireWidth() {
      Assert.assertEquals( 1, systemUnderTest.getColumnConstraints().size() );
      Assert.assertEquals( 100, systemUnderTest.getColumnConstraints().get( 0 ).getPercentWidth(), TestCommon.precision() );
      Assert.assertEquals( Priority.ALWAYS, systemUnderTest.getColumnConstraints().get( 0 ).getHgrow() );
   }//End Method
   
   @Test public void eachPaneShouldShareWidthAmongstColumns() {
      assertColumnConstraints( ( GridPane )systemUnderTest.dimensionsPane().getContent() );
      assertColumnConstraints( ( GridPane )systemUnderTest.jobPoliciesPane().getContent() );
      assertColumnConstraints( ( GridPane )systemUnderTest.fontPane().getContent() );
      assertColumnConstraints( ( GridPane )systemUnderTest.colourPane().getContent() );
   }//End Method
   
   /**
    * Method to assert that the {@link ColumnConstraints} are present in the given {@link GridPane}.
    * @param grid the {@link GridPane} to check.
    */
   private void assertColumnConstraints( GridPane grid ){
      Assert.assertEquals( 
               ConfigurationPanelDefaultsTest.CONTROLS_PERCENTAGE_WIDTH, 
               grid.getColumnConstraints().get( 1 ).getPercentWidth(),
               TestCommon.precision()
      );
      Assert.assertEquals( Priority.ALWAYS, grid.getColumnConstraints().get( 1 ).getHgrow() );
      
      Assert.assertTrue( 
               grid.getColumnConstraints().get( 0 ).getPercentWidth() +
               grid.getColumnConstraints().get( 1 ).getPercentWidth() 
               >= 100
      );
   }//End Method
   
   @Test public void shouldCreateTitleWithExpectedProperties(){
      Label titleLabel = systemUnderTest.titleLabel();
      assertThat( titleLabel.getFont().getSize(), closeTo( JavaFxStyleTest.TITLE_FONT_SIZE, TestCommon.precision() ) );
      assertThat( GridPane.getColumnIndex( titleLabel ), is( 0 ) );
      assertThat( GridPane.getRowIndex( titleLabel ), is( 0 ) );
      assertThat( GridPane.getColumnSpan( titleLabel ), is( 2 ) );
      assertThat( GridPane.getRowSpan( titleLabel ), is( 1 ) );
      assertThat( GridPane.getHalignment( titleLabel ), is( HPos.CENTER ) );
      assertThat( GridPane.getValignment( titleLabel ), is( VPos.CENTER ) );
   }//End Method
   
   @Test public void titledPanesShouldBeClosedByDefault(){
      assertThat( systemUnderTest.dimensionsPane().isExpanded(), is( true ) );
      assertThat( systemUnderTest.jobPoliciesPane().isExpanded(), is( false ) );
      assertThat( systemUnderTest.fontPane().isExpanded(), is( true ) );
      assertThat( systemUnderTest.colourPane().isExpanded(), is( true ) );
   }//End Method
   
}//End Class
