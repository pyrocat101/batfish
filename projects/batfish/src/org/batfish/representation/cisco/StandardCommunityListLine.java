package org.batfish.representation.cisco;

import java.io.Serializable;
import java.util.List;

import org.batfish.representation.LineAction;

public class StandardCommunityListLine implements Serializable {

   private static final long serialVersionUID = 1L;

   private LineAction _action;
   private List<Long> _communities;

   public StandardCommunityListLine(LineAction action, List<Long> communities) {
      _action = action;
      _communities = communities;
   }

   public LineAction getAction() {
      return _action;
   }

   public List<Long> getCommunities() {
      return _communities;
   }

}
