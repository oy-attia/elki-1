package de.lmu.ifi.dbs.elki.evaluation.clustering;

import de.lmu.ifi.dbs.elki.evaluation.clustering.ClusterContingencyTable.Util;
import de.lmu.ifi.dbs.elki.utilities.documentation.Reference;

/**
 * Edit distance measures
 * 
 * <p>
 * Pantel, P. and Lin, D.<br />
 * Document clustering with committees<br/>
 * Proc. 25th ACM SIGIR conference on Research and development in information
 * retrieval
 * </p>
 * 
 * @author Sascha Goldhofer
 */
@Reference(authors = "Pantel, P. and Lin, D.", title = "Document clustering with committees", booktitle = "Proc. 25th ACM SIGIR conference on Research and development in information retrieval", url = "http://dx.doi.org/10.1145/564376.564412")
public class EditDistance {
  /**
   * Edit operations for first clustering to second clustering.
   */
  int editFirst = -1;

  /**
   * Edit operations for second clustering to first clustering.
   */
  int editSecond = -1;

  /**
   * Baseline for edit operations
   */
  int editOperationsBaseline;

  protected EditDistance(ClusterContingencyTable table) {
    super();
    editOperationsBaseline = table.contingency[table.size1][table.size2];
    {
      editFirst = 0;

      // iterate over first clustering
      for(int i1 = 0; i1 < table.size1; i1++) {
        // get largest cell
        int largestLabelSet = 0;
        for(int i2 = 0; i2 < table.size2; i2++) {
          largestLabelSet = Math.max(largestLabelSet, table.contingency[i1][i2]);
        }

        // merge: found (largest) cluster to second clusterings cluster
        editFirst++;
        // move: wrong objects from this cluster to correct cluster (of second
        // clustering)
        editFirst += table.contingency[i1][table.size2] - largestLabelSet;
      }
    }
    {
      editSecond = 0;

      // iterate over second clustering
      for(int i2 = 0; i2 < table.size2; i2++) {
        // get largest cell
        int largestLabelSet = 0;
        for(int i1 = 0; i1 < table.size1; i1++) {
          largestLabelSet = Math.max(largestLabelSet, table.contingency[i1][i2]);
        }

        // merge: found (largest) cluster to second clusterings cluster
        editSecond++;
        // move: wrong objects from this cluster to correct cluster (of second
        // clustering)
        editSecond += table.contingency[table.size1][i2] - largestLabelSet;
      }
    }
  }

  /**
   * Get the baseline editing Operations ( = total Objects)
   * 
   * @return worst case amount of operations
   */
  public int editOperationsBaseline() {
    return editOperationsBaseline;
  }

  /**
   * Get the editing operations required to transform first clustering to
   * second clustering
   * 
   * @return Editing operations used to transform first into second clustering
   */
  public int editOperationsFirst() {
    return editFirst;
  }

  /**
   * Get the editing operations required to transform second clustering to
   * first clustering
   * 
   * @return Editing operations used to transform second into first clustering
   */
  public int editOperationsSecond() {
    return editSecond;
  }

  /**
   * Get the editing distance to transform second clustering to first
   * clustering (normalized, 1=equal)
   * 
   * @return Editing distance first into second clustering
   */
  public double editDistanceFirst() {
    return 1.0 * editOperationsFirst() / editOperationsBaseline();
  }

  /**
   * Get the editing distance to transform second clustering to first
   * clustering (normalized, 1=equal)
   * 
   * @return Editing distance second into first clustering
   */
  public double editDistanceSecond() {
    return 1.0 * editOperationsSecond() / editOperationsBaseline();
  }

  /**
   * Get the edit distance F1-Measure
   * 
   * @return Edit Distance F1-Measure
   */
  public double f1Measure() {
    return Util.f1Measure(editDistanceFirst(), editDistanceSecond());
  }
}