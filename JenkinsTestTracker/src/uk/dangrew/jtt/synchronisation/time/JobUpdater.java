/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.synchronisation.time;

import java.util.Timer;

import uk.dangrew.jtt.api.handling.JenkinsProcessing;
import uk.dangrew.jtt.synchronisation.model.TimeKeeper;

/**
 * The {@link JobUpdater} provides a {@link TimeKeeper} for updating the job details
 * from the {@link JenkinsApiImpl}.
 */
public class JobUpdater extends TimeKeeper {
   
   static final long UPDATE_DELAY = TimeKeeper.TASK_DELAY;
   static final long INTERVAL = 5000L;
   
   /**
    * Constructs a new {@link JobUpdater}.
    * @param timer the {@link Timer} to time events.
    * @param jenkinsProcessing the {@link JenkinsProcessing} to request job updates on.
    * @param interval the interval between updates.
    */
   public JobUpdater( Timer timer, JenkinsProcessing jenkinsProcessing, Long interval ) {
      super( 
               timer, 
               () -> jenkinsProcessing.fetchJobsAndUpdateDetails(),
               interval
      );
   }//End Constructor
   
   /**
    * Constructs a new {@link JobUpdater}.
    * @param jenkinsProcessing the {@link JenkinsProcessing} to request job updates on.
    */
   public JobUpdater( JenkinsProcessing jenkinsProcessing ) {
      super( () -> jenkinsProcessing.fetchJobsAndUpdateDetails() );
   }//End Constructor

}//End Class