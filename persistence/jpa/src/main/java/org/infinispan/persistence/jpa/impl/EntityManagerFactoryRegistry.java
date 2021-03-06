package org.infinispan.persistence.jpa.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.spi.PersistenceProvider;

import org.infinispan.commons.util.Util;
import org.infinispan.util.logging.Log;
import org.infinispan.util.logging.LogFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 *
 * @author <a href="mailto:rtsang@redhat.com">Ray Tsang</a>
 *
 */
public class EntityManagerFactoryRegistry {
   private static final Log log = LogFactory.getLog(EntityManagerFactoryRegistry.class);

   private Map<String, EntityManagerFactory> registry = new HashMap<String, EntityManagerFactory>();
   private Map<String, AtomicInteger> usage = new HashMap<String, AtomicInteger>();

   public EntityManagerFactory getEntityManagerFactory(String persistenceUnitName) {
      synchronized (this) {
         if (!registry.containsKey(persistenceUnitName)) {
            EntityManagerFactory emf = createEntityManagerFactory(persistenceUnitName);
            registry.put(persistenceUnitName, emf);
            usage.put(persistenceUnitName, new AtomicInteger(1));
            return emf;
         } else {
            incrementUsage(persistenceUnitName);
            return registry.get(persistenceUnitName);
         }
      }
   }

   public void closeEntityManagerFactory(String persistenceUnitName) {
      synchronized (this) {
         if (!registry.containsKey(persistenceUnitName)) {
            return;
         }

         int count = decrementUsage(persistenceUnitName);
         if (count == 0) {
            EntityManagerFactory emf = registry.remove(persistenceUnitName);
            try {
               if (emf.isOpen()) emf.close();
            } catch (IllegalStateException e) {
               log.warn("Entity manager factory was already closed: " + persistenceUnitName);
            }
         }
      }
   }

   public void closeAll() {
      synchronized (this) {
         for (Entry<String, EntityManagerFactory> entry : registry.entrySet()) {
            try {
               if (entry.getValue().isOpen())
                  entry.getValue().close();
            } catch (IllegalStateException e) {
               log.warn("Entity manager factory was already closed: " + entry.getKey());
            }
         }
      }
   }

   protected int incrementUsage(String persistenceUnitName) {
      synchronized (this) {
         return usage.get(persistenceUnitName).incrementAndGet();
      }
   }

   protected int decrementUsage(String persistenceUnitName) {
      synchronized (this) {
         return usage.get(persistenceUnitName).decrementAndGet();
      }
   }

   protected int getUsage(String persistenceUnitName) {
      synchronized (this) {
         return usage.get(persistenceUnitName).intValue();
      }
   }

   private static EntityManagerFactory createEntityManagerFactory(String persistenceUnitName) {
      if (Util.isOSGiContext()) {
         Bundle thisBundle = FrameworkUtil.getBundle(EntityManagerFactoryRegistry.class);
         BundleContext context = thisBundle.getBundleContext();

         ServiceReference<?> serviceReference = context.getServiceReference(PersistenceProvider.class.getName());
         PersistenceProvider persistenceProvider = (PersistenceProvider) context.getService(serviceReference);

         return persistenceProvider.createEntityManagerFactory(persistenceUnitName, null);
      } else {
         return Persistence.createEntityManagerFactory(persistenceUnitName);
      }
   }
}
