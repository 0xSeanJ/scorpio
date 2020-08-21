package top.jshanet.scorpio.framework.repository;

import top.jshanet.scorpio.framework.entity.ScorpioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author seanjiang
 * @since 2019/12/25
 */
@NoRepositoryBean
public interface ScorpioRepository<T extends ScorpioEntity>
    extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
}
