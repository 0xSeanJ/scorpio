package top.jshanet.scorpio.framework.common.repository;

import top.jshanet.scorpio.framework.common.entity.ScorpioBaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author seanjiang
 * @date 2019/12/25
 */
@NoRepositoryBean
public interface ScorpioBaseRepository<T extends ScorpioBaseEntity>
    extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
}
