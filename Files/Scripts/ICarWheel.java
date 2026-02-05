package JAVARuntime;

// Useful imports
import java.util.*;
/**
 * @Author 
*/
public interface ICarWheel { 
   public void setTorque(float t);
   public void setBrake(float b);
   public void setSteer(float s);
   public boolean allowTorque();
   public boolean allowBrake();
   public boolean allowSteer();
   public boolean isAlive();
}
