package io.inferiority.demo.springsecurity.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

/**
 * @author cuijiufeng
 * @Class Role
 * @Date 2023/4/14 15:55
 */
@Data
@TableName("sys_role")
public class Role implements Serializable, GrantedAuthority {
    @TableId
    private String id;
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
