package org.batfish.representation.cisco;

import java.io.Serializable;

import org.batfish.representation.LineAction;
import org.batfish.representation.Prefix;
import org.batfish.util.SubRange;

public class PrefixListLine implements Serializable {

   private static final long serialVersionUID = 1L;

   private LineAction _action;

   private SubRange _lengthRange;

   private Prefix _prefix;

   public PrefixListLine(LineAction action, Prefix prefix, SubRange lengthRange) {
      _action = action;
      _prefix = prefix;
      _lengthRange = lengthRange;
   }

   public LineAction getAction() {
      return _action;
   }

   public SubRange getLengthRange() {
      return _lengthRange;
   }

   public Prefix getPrefix() {
      return _prefix;
   }

}
