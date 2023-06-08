package io.inferiority.demo.springsecurity.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import oshi.software.os.OSFileStore;

import java.util.List;

/**
 * @author cuijiufeng
 * @Class SystemInfoVo
 * @Date 2023/6/8 11:56
 */
@AllArgsConstructor
@Data
public class SystemInfoVo {
    private Integer processorCount;
    private Double processorOccupancy;
    private Long memoryTotal;
    private Double memoryOccupancy;
    private List<OSFileStore> fileStores;
}
