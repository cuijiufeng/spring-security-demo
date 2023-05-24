package io.inferiority.demo.springsecurity.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.inferiority.demo.springsecurity.config.ApplicationContextHolder;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;
import java.util.function.BiFunction;

/**
 * @Class: BaseErrorEnum
 * @Date: 2021/7/20 16:40
 * @auth: cuijiufeng
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public interface BaseErrorEnum extends Serializable {
    BiFunction<String, String, BaseErrorEnum> BUILD = (c, m) -> new BaseErrorEnum() {
        @Override
        public String getCode() {
            return c;
        }
        @Override
        public String getMessage() {
            return m;
        }
    };

    String getCode();

    @JsonSerialize(using = BaseErrorEnum.MessageStringJsonSerialize.class)
    String getMessage();

    default String toLogString() {
        return "[code=" + getCode() + ", message=" + getMessage() + "]";
    }

    class MessageStringJsonSerialize extends JsonSerializer<String> {
        @Override
        public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            Locale locale = LocaleContextHolder.getLocale();
            MessageSource messageSource = ApplicationContextHolder.getApplicationContext().getBean(MessageSource.class);
            gen.writeString(messageSource.getMessage(value, null, locale));
        }
    }
}
