package org.batfish.collections;

import java.util.Collection;
import java.util.TreeSet;
import java.util.regex.Pattern;

public class NodeSet extends TreeSet<String> {

   /**
    *
    */
   private static final long serialVersionUID = 1L;

   public NodeSet() {
      super();
   }

   public NodeSet(Collection<String> collection) {
      super(collection);
   }

   public static NodeSet filter(NodeSet set, String filter) {
      if (filter == null) {
         return new NodeSet(set);
      }
      Pattern pat = Pattern.compile(filter);
      NodeSet filtered = new NodeSet();
      for (String node : set) {
         if (pat.matcher(node).matches()) {
            filtered.add(node);
         }
      }
      return filtered;
   }
}
