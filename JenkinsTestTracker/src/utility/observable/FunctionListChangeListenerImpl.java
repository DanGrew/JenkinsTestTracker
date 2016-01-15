/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package utility.observable;

import java.util.List;
import java.util.function.Consumer;

import javafx.collections.ListChangeListener;

/**
 * The {@link FunctionListChangeListenerImpl} provides a {@link ListChangeListener} that accepts
 * two {@link Consumer}s to delete actions to when the {@link List} is changed.
 */
public class FunctionListChangeListenerImpl< TypeT > implements ListChangeListener< TypeT > {
   
   private final Consumer< TypeT > addFunction;
   private final Consumer< TypeT > removeFunction;

   /**
    * Constructs a new {@link FunctionListChangeListenerImpl}.
    * @param addFunction the {@link Consumer} to invoke when something is added.
    * @param removeFunction the {@link Consumer} to invoke when something is removed.
    */
   public FunctionListChangeListenerImpl( Consumer< TypeT > addFunction, Consumer< TypeT > removeFunction ) {
      this.addFunction = addFunction;
      this.removeFunction = removeFunction;
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public void onChanged( Change< ? extends TypeT > change ) {
      while ( change.next() ) {
         if ( change.wasAdded() ) {
            change.getAddedSubList().forEach( object -> addFunction.accept( object ) );
         }
         if ( change.wasRemoved() ) {
            change.getRemoved().forEach( object -> removeFunction.accept( object ) );
         }
      }
   }//End Method

}//End Class