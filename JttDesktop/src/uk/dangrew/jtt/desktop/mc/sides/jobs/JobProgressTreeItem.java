/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.sides.jobs;

import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import uk.dangrew.jtt.desktop.javafx.tree.structure.TreeItemValue;
import uk.dangrew.jtt.model.jobs.JenkinsJob;

/**
 * The {@link JobProgressTreeItem} defines the content of a {@link javafx.scene.control.TreeItem}
 * in the {@link JobProgressTree}.
 */
public interface JobProgressTreeItem extends TreeItemValue {
   
   /**
    * Provides the {@link ObjectProperty} of the {@link Node} content.
    * @return the {@link ObjectProperty}.
    */
   public ObjectProperty< Node > contentProperty();
   
   /**
    * Getter for the associated {@link JenkinsJob}.
    * @return the {@link JenkinsJob}.
    */
   public JenkinsJob getJenkinsJob();
   
}//End Class
