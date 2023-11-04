import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.CentralProcessor;

import java.util.List;

public class teste {
    public static void main(String[] args) {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();

        List<oshi.hardware.GraphicsCard> gpuList = hal.getGraphicsCards();
        oshi.hardware.GraphicsCard gpu = gpuList.get(0);
        System.out.println(gpu);
        System.out.println(gpu.getName());
        System.out.println(gpu.getVRam());
    }
}


