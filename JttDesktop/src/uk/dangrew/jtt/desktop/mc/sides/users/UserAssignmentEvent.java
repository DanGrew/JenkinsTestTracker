/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.sides.users;

import uk.dangrew.kode.event.structure.EventManager;
import uk.dangrew.kode.event.structure.EventSubscription;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The {@link UserAssignmentEvent} is responsible for managing events raised to put
 * {@link UserAssignment}s in the {@link UserAssignmentsTree}.
 */
public class UserAssignmentEvent extends EventManager< UserAssignment > {

   private static final Collection<EventSubscription< UserAssignment >> subscriptions
      = new LinkedHashSet<>();
   private static final ReentrantLock lock = new ReentrantLock();
   
   /**
    * Constructs a new {@link UserAssignmentEvent}.
    */
   public UserAssignmentEvent() {
      super( subscriptions, lock );
   }//End Constructor

}//End Class
