package JAVARuntime;

// Useful imports
import java.util.*;
import java.text.*;
import java.net.*;
import java.math.*;
import java.io.*;
import java.nio.*;

/** @Author */
public class CarController extends Component {

  private List<ICarWheel> wheels = new ArrayList();

  @Order(idx = {1})
  public String throttleKey = "throttle";

  @Order(idx = {2})
  public String brakeKey = "brake";

  @Order(idx = {3})
  public String leftKey = "left";

  @Order(idx = {4})
  public String rightKey = "right";

  @Order(idx = {5})
  public float horsePower = 100;

  @Order(idx = {6})
  public float brakeForce = 100;

  @Order(idx = {7})
  public float maxSpeed = 120;

  @Order(idx = {8})
  public float maxReverseSpeed = 40;

  @Order(idx = {9})
  public float maxSteer = 25;

  @Order(idx = {10})
  public float minSteer = 5;

  @Order(idx = {11})
  public float steerLerp = 25;

  private KeyHandler throttleH;
  private KeyHandler brakeH;
  private KeyHandler leftH;
  private KeyHandler rightH;

  private float currentSteer = 0;
  private VehiclePhysics vp;

  /// Run only once
  @Override
  public void start() {
    vp = myObject.physics.vehiclePhysics;
    throttleH = new KeyHandler(throttleKey);
    brakeH = new KeyHandler(brakeKey);
    leftH = new KeyHandler(leftKey);
    rightH = new KeyHandler(rightKey);
  }

  /// Repeat every frame
  @Override
  public void repeat() {
    removeDeadWheels();
    workThrottleAndBrake();
    handlerSteer();
  }

  void handlerSteer() {
    float ws = 0;
    float speedProgress = Math.abs(vp.getSpeedKMH()) / maxSpeed;
    speedProgress = 1f - speedProgress;
    float activeSteer = ((maxSteer - minSteer) * speedProgress) + minSteer;

    if (leftH.isPressed()) {
      ws = activeSteer;
    } else if (rightH.isPressed()) {
      ws = -activeSteer;
    }

    Console.log(ws);
    currentSteer = Math.lerpInSeconds(currentSteer, ws, steerLerp);
    setSteer(currentSteer);
  }

  void workThrottleAndBrake() {
    if (throttleH.isPressed()) {
      if (vp.getSpeedKMH() > -5) {
        float speedProgress = Math.abs(vp.getSpeedKMH()) / maxSpeed;
        speedProgress = 1f - speedProgress;
        float maxTorque = horsePower * 10 * speedProgress;
        setTorque(maxTorque);
        setBrake(0);
      } else {
        setTorque(0);
        setBrake(brakeForce * 2.5f);
      } 
    } else {
      setTorque(0);
    }

    if (brakeH.isPressed()) {
      if (vp.getSpeedKMH() > 5) {
        float maxBrake = brakeForce * 2.5f;
        setBrake(maxBrake);
      } else {
        setBrake(0);
        float speedProgress = Math.abs(vp.getSpeedKMH()) / maxReverseSpeed;
        speedProgress = 1f - speedProgress;
        float maxTorque = -horsePower * 10 * speedProgress;
        setTorque(maxTorque);
      }
    } else {
      setBrake(0);
    }
  }

  void setSteer(float s) {
    for (int x = 0; x < wheels.size(); x++) {
      ICarWheel w = wheels.get(x);
      if (w.allowSteer()) {
        w.setSteer(s);
      }
    }
  }

  void setTorque(float t) {
    float count = 0;
    for (int x = 0; x < wheels.size(); x++) {
      ICarWheel w = wheels.get(x);
      if (w.allowTorque()) {
        count++;
      }
    }

    float perWheelTorque = t / count;
    for (int x = 0; x < wheels.size(); x++) {
      ICarWheel w = wheels.get(x);
      if (w.allowTorque()) {
        w.setTorque(perWheelTorque);
      }
    }
  }

  void setBrake(float t) {
    float count = 0;
    for (int x = 0; x < wheels.size(); x++) {
      ICarWheel w = wheels.get(x);
      if (w.allowBrake()) {
        count++;
      }
    }

    float perWheelBrake = t / count;
    for (int x = 0; x < wheels.size(); x++) {
      ICarWheel w = wheels.get(x);
      if (w.allowBrake()) {
        w.setBrake(perWheelBrake);
      }
    }
  }

  void removeDeadWheels() {
    for (int x = 0; x < wheels.size(); x++) {
      ICarWheel w = wheels.get(x);
      if (!w.isAlive()) {
        wheels.remove(x);
        removeDeadWheels();
        break;
      }
    }
  }

  public void addWheel(ICarWheel w) {
    wheels.add(w);
  }
}
