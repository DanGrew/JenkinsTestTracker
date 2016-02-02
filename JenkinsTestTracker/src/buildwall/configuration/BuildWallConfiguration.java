/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.configuration;

import buildwall.layout.BuildWall;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * The {@link BuildWallConfiguration} provides a mechanism for configuring the appearance
 * and layout of the build wall.
 */
public interface BuildWallConfiguration {

   /**
    * Property for the {@link Font} for the build number.
    * @return the {@link ObjectProperty}.
    */
   public ObjectProperty< Font > buildNumberFont();
   
   /**
    * Property for the {@link Color} for the build number.
    * @return the {@link ObjectProperty}. 
    */
   public ObjectProperty< Color > buildNumberColour(); 

   /**
    * Property for the {@link Font} for the completion estimate.
    * @return the {@link ObjectProperty}.
    */
   public ObjectProperty< Font > completionEstimateFont();
   
   /**
    * Property for the {@link Color} for the completion estimate.
    * @return the {@link ObjectProperty}. 
    */
   public ObjectProperty< Color > completionEstimateColour(); 
   
   /**
    * Property for the job name {@link Font}.
    * @return the jobNameFont the {@link ObjectProperty}.
    */
   public ObjectProperty< Font > jobNameFont(); 
   
   /**
    * Property for the job name {@link Color}.
    * @return the jobNameColour the {@link ObjectProperty}.
    */
   public ObjectProperty< Color > jobNameColour();

   /**
    * Property for the number of columns in the {@link BuildWall}.
    * @return the number of columns {@link IntegerProperty}.
    */
   public IntegerProperty numberOfColumns();
}//End Interface