package JAVARuntime;

// Useful imports
import java.util.*;
import java.text.*;
import java.net.*;
import java.math.*;
import java.io.*;
import java.nio.*;

/**
 * @Author 
*/
public class KeyHandler {
    private Key k;
    private String n;

    public KeyHandler(String name) {
      this.n = name;
      k = Input.getKey(name);
    }

    public Key get() {
      if (k == null) {
        k = Input.getKey(n);
      }
      return k;
    }

    public boolean isDown() {
      get();
      if (k != null) {
        return k.down;
      }
      return false;
    }

    public boolean isPressed() {
      get();
      if (k != null) {
        return k.pressed;
      }
      return false;
    }
  }
