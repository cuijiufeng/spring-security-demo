package io.inferiority.demo.springsecurity;

import com.baomidou.mybatisplus.extension.ddl.IDdl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author cuijiufeng
 * @date 2023/5/22 19:34
 */
@Order(0)
@Component
public class DdlApplicationRunner extends com.baomidou.mybatisplus.autoconfigure.DdlApplicationRunner {

    public DdlApplicationRunner(@Autowired(required = false) List<IDdl> ddlList) {
        super(ddlList);
    }
}
