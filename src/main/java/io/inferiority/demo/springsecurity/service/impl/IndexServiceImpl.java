package io.inferiority.demo.springsecurity.service.impl;

import io.inferiority.demo.springsecurity.model.vo.SystemInfoVo;
import io.inferiority.demo.springsecurity.service.IIndexService;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OSFileStore;

import java.util.List;

/**
 * @author cuijiufeng
 * @Class IndexServiceImpl
 * @Date 2023/6/8 11:43
 */
@Service
public class IndexServiceImpl implements IIndexService {
    private long[] oldTicks;

    @Override
    public SystemInfoVo systemInfo() {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        CentralProcessor processor = hardware.getProcessor();
        GlobalMemory memory = hardware.getMemory();
        List<OSFileStore> fileStores = systemInfo.getOperatingSystem().getFileSystem().getFileStores();
        return new SystemInfoVo(processor.getPhysicalProcessorCount(),
                getSystemCpuLoadBetweenTicks(processor),
                memory.getTotal(),
                1.0 * (memory.getTotal() - memory.getAvailable()) / memory.getTotal(),
                fileStores);
    }

    private double getSystemCpuLoadBetweenTicks(CentralProcessor processor) {
        if (oldTicks == null) {
            oldTicks = processor.getSystemCpuLoadTicks();
        }
        long[] ticks = processor.getSystemCpuLoadTicks();
        long total = 0;
        for (int i = 0; i < ticks.length; i++) {
            total += ticks[i] - oldTicks[i];
        }
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] + ticks[CentralProcessor.TickType.IOWAIT.getIndex()]
                - oldTicks[CentralProcessor.TickType.IDLE.getIndex()] - oldTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        oldTicks = ticks;
        return total > 0 ? (double) (total - idle) / total : 0d;
    }
}
