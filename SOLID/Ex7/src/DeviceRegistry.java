import java.util.*;

public class DeviceRegistry {
    private final List<Object> devices = new ArrayList<>();
//After ISP refactoring we do not have one common parent interface like SmartClassroomDevice
//Now we rely on specific interfaces to retrieve device capabilities
 //Every class in Java extends Object so List<Object> can hold any device type
    public void add(Object d) { devices.add(d); }
//<T>=>This method works with ANY type T.
//Controller:BrightnessControllable lights =reg.getFirst(BrightnessControllable.class);
//T = BrightnessControllable
//capability = BrightnessControllable.class
//BrightnessControllable.class=>Give me ANY device that supports brightness
    public <T> T getFirst(Class<T> capability) {
        for (Object d : devices) {
            if(capability.isInstance(d)){
                return capability.cast(d);
                //cast it to Controller
            }
        }
        throw new IllegalStateException("Missing: " + capability.getSimpleName());
    }
}
