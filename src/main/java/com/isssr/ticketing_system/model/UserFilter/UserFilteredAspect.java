package com.isssr.ticketing_system.model.UserFilter;

import com.isssr.ticketing_system.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
@Aspect
public class UserFilteredAspect {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserService userService;

    @Around("@annotation(userFiltered)")
    public Object userFilteredMethod(ProceedingJoinPoint joinPoint, UserFiltered userFiltered) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (hasPrivilege(authentication, "READ_ALL_PRIVILEGE"))
            return joinPoint.proceed();

        Session session = (Session) this.entityManager.getDelegate();

        try {
            if (session.isOpen()) {
                Long id = userService.findByEmail(authentication.getName()).get().getId();
                session.enableFilter("user_filter").setParameter("user_id", id);
            }
            return joinPoint.proceed();
        } finally {
            if (session.isOpen())
                session.disableFilter("user_filter");
        }
    }

    private boolean hasPrivilege(Authentication authentication, String privilege) {
        for (GrantedAuthority auth : authentication.getAuthorities())
            if (auth.getAuthority().equals(privilege))
                return true;

        return false;
    }
}
