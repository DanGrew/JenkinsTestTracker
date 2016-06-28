/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.configuration.system;

import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfigurationImpl;
import uk.dangrew.jtt.buildwall.configuration.properties.DualWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.properties.DualWallConfigurationImpl;

/**
 * The {@link SystemConfiguration} collects the different configuration items
 * which can be provided as and when they are needed.
 */
public class SystemConfiguration {
   
   private final DualWallConfiguration dualConfiguration;
   private final BuildWallConfiguration leftConfiguration;
   private final BuildWallConfiguration rightConfiguration;
   
   public SystemConfiguration() {
      this.dualConfiguration = new DualWallConfigurationImpl();
      this.leftConfiguration = new BuildWallConfigurationImpl();
      this.rightConfiguration = new BuildWallConfigurationImpl();
   }//End Constructor

   /**
    * Getter for the {@link DualWallConfiguration}.
    * @return the {@link DualWallConfiguration}.
    */
   public DualWallConfiguration getDualConfiguration() {
      return dualConfiguration;
   }//End Method
   
   /**
    * Getter for the left {@link BuildWallConfiguration}.
    * @return the {@link BuildWallConfiguration}.
    */
   public BuildWallConfiguration getLeftConfiguration() {
      return leftConfiguration;
   }//End Method
   
   /**
    * Getter for the right {@link BuildWallConfiguration}.
    * @return the {@link BuildWallConfiguration}.
    */
   public BuildWallConfiguration getRightConfiguration() {
      return rightConfiguration;
   }//End Method
   
}//End Class
