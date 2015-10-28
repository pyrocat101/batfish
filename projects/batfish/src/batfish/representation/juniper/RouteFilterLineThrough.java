package batfish.representation.juniper;

import batfish.representation.Ip;
import batfish.representation.LineAction;
import batfish.representation.Prefix;
import batfish.representation.RouteFilterLengthRangeLine;
import batfish.representation.RouteFilterList;
import batfish.util.SubRange;

public final class RouteFilterLineThrough extends RouteFilterLine {

   /**
    *
    */
   private static final long serialVersionUID = 1L;

   private final Prefix _throughPrefix;

   public RouteFilterLineThrough(Prefix prefix, Prefix throughPrefix) {
      super(prefix);
      _throughPrefix = prefix;
   }

   @Override
   public void applyTo(RouteFilterList rfl) {
      int low = _prefix.getPrefixLength();
      int high = _throughPrefix.getPrefixLength();
      for (int i = low; i <= high; i++) {
         Ip currentNetworkAddress = _throughPrefix.getAddress()
               .getNetworkAddress(i);
         RouteFilterLengthRangeLine line = new RouteFilterLengthRangeLine(
               LineAction.ACCEPT, currentNetworkAddress, i, new SubRange(i, i));
         rfl.addLine(line);
      }
   }

   @Override
   public boolean equals(Object o) {
      if (!this.getClass().equals(o.getClass())) {
         return false;
      }
      else {
         RouteFilterLineThrough rhs = (RouteFilterLineThrough) o;
         return _prefix.equals(rhs._prefix)
               && _throughPrefix.equals(rhs._throughPrefix);
      }
   }

   public Prefix getThroughPrefix() {
      return _throughPrefix;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + _throughPrefix.hashCode();
      result = prime * result + _prefix.hashCode();
      return result;
   }

}
