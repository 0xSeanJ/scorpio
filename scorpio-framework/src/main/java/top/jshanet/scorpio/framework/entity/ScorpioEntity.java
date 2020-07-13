package top.jshanet.scorpio.framework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author seanjiang
 * @date 2019/12/25
 */
@Data
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class ScorpioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private Date createdTime;

    @LastModifiedDate
    private Date updatedTime;

    @JsonIgnore
    private boolean deleted;

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this) {

            @Override
            protected boolean accept(Field field) {
                FetchType fetchType = Optional.ofNullable(field.getAnnotation(OneToMany.class))
                        .map(OneToMany::fetch).orElse(null);
                return super.accept(field) && fetchType != FetchType.LAZY;
            }
        }.toString();
    }

    @Transient
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> extras;


    public void addExtraProperty(String name, Object value) {
        if (extras == null) {
            extras = new HashMap<>();
        }
        extras.put(name, value);
    }

    public void addExtraProperties(Map<String ,Object> properties) {
        if (extras == null) {
            extras = new HashMap<>();
        }
        extras.putAll(properties);
    }

    public Object getExtraProperty(String name) {
        if (extras != null) {
            return extras.get(name);
        }
        extras = new HashMap<>();
        return null;
    }

    public Object getExtraPropertyOrDefault(String name, Object defaultValue) {
        if (extras != null) {
            return extras.getOrDefault(name, defaultValue);
        }
        extras = new HashMap<>();
        return defaultValue;
    }

}
