package top.jshanet.scorpio.framework.tenant.aspect;

import top.jshanet.scorpio.framework.common.util.ScorpioContextUtil;
import top.jshanet.scorpio.framework.tenant.support.TenantFilter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
@Aspect
public class TenantAspect {

    @PersistenceContext
    private EntityManager entityManager;

    @Pointcut("@annotation(top.jshanet.scorpio.framework.tenant.support.TenantService)")
    public void tenantMode() {}

    @Around("io.github.jshanet.scorpio.framework.tenant.aspect.tenantMode() " +
            "&& @annotation(top.jshanet.scorpio.framework.tenant.support.TenantService)")
    public Object doProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Filter filter = entityManager.unwrap(Session.class).enableFilter(TenantFilter.NAME);
            filter.setParameter(TenantFilter.ID, ScorpioContextUtil.getTenantId());
            return joinPoint.proceed();
        } finally {
            entityManager.unwrap(Session.class).disableFilter(TenantFilter.NAME);
        }
    }

}
