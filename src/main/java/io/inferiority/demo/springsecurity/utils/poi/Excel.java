package io.inferiority.demo.springsecurity.utils.poi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author cuijiufeng
 * @Class Excel
 * @Date 2023/5/25 13:31
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Excel {
    String title();

    int order();

    String dateFormat();

    Type type();

    enum Type {
        ALL, EXPORT, IMPORT
    }
}
