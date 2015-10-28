package batfish.representation.juniper;

import batfish.representation.LineAction;
import batfish.representation.Prefix;
import batfish.representation.RouteFilterLengthRangeLine;
import batfish.representation.RouteFilterList;
import batfish.util.SubRange;

public final class RouteFilterLineUpTo extends RouteFilterLine {

   /**
    *
    */
   private static final long serialVersionUID = 1L;

   private final int _maxPrefixLength;

   public RouteFilterLineUpTo(Prefix prefix, int maxPrefixLength) {
      super(prefix);
      _maxPrefixLength = maxPrefixLength;
   }

   @Override
   public void applyTo(RouteFilterList rfl) {
      int prefixLength = _prefix.getPrefixLength();
      RouteFilterLengthRangeLine line = new RouteFilterLengthRangeLine(
            LineAction.ACCEPT, _prefix, new SubRange(prefixLength,
                  _maxPrefixLength));
      rfl.addLine(line);
   }

   @Override
   public boolean equals(Object o) {
      if (!this.getClass().equals(o.getClass())) {
         return false;
      }
      else {
         RouteFilterLineUpTo rhs = (RouteFilterLineUpTo) o;
         return _prefix.equals(rhs._prefix)
               && _maxPrefixLength == rhs._maxPrefixLength;
      }
   }

   public int getMaxPrefixLength() {
      return _maxPrefixLength;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + _maxPrefixLength;
      result = prime * result + _prefix.hashCode();
      return result;
   }

}
