package JAVARuntime;

// Useful imports
import java.util.*;
import java.text.*;
import java.net.*;
import java.math.*;
import java.io.*;
import java.nio.*;

/** @Author */
public class CarWheel extends Component implements ICarWheel {

  public boolean torqueWheel = true;
  public boolean steerWheel = false;
  public boolean brakeWheel = true;

  @AutoWired
  private VehicleWheel vw;
  private float t, b, s;

  /// Run only once
  @Override
  public void start() {
    CarController car = myObject.mainParent.findComponentInChildren(CarController.class);
    car.addWheel(this);
  } 

  /// Repeat every frame
  @Override
  public void repeat() {
    if (vw != null) {
      vw.setTorque(t);
      vw.setBrake(b);
      vw.setSteer(s);
    }
  }

  /* Override */
  public void setTorque(float t) {
    this.t = t;
  }

  /* Override */
  public void setBrake(float b) {
    this.b = b;
  }
  
  /* Override */
  public void setSteer(float s) {
    this.s = s;
  }
 
  /* Override */
  public boolean isAlive() {
    return myObject != null;
  }
  
  /* Override */
  public boolean allowTorque() {
    return torqueWheel;
  }

  /* Override */
  public boolean allowBrake() {
    return brakeWheel;
  }
  
  /* Override */
  public boolean allowSteer() {
    return steerWheel;
  }
}
