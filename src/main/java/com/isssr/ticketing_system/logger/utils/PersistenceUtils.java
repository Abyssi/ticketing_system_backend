package it.uniroma2.ticketingsystem.logger.utils;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;


public abstract class PersistenceUtils {

    /**
     * Initialize an object instance from its Proxy
     *
     * @param entity the proxy entity
     * @param <T> the object instance
     * @return the object instance of the proxy entity
     */
    public static <T> T initializeAndUnproxy(T entity) {
        if (entity == null) {
            throw new
                    NullPointerException("Entity passed for initialization is null");
        }

        Hibernate.initialize(entity);
        if (entity instanceof HibernateProxy) {
            entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer()
                    .getImplementation();
        }
        return entity;
    }
}
