package top.jshanet.scorpio.framework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Optional;

/**
 * @author seanjiang
 * @date 2019/12/25
 */
@Data
// AuditingEntityListener 监听器，提供GeneratedValue, CreatedDate, LastModifiedDate 等注解
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class ScorpioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private Date createTime;

    @LastModifiedDate
    private Date updateTime;

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

}
