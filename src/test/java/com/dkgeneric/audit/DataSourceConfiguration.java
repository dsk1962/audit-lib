package com.dkgeneric.audit;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import jakarta.persistence.EntityManagerFactory;


/**
 * The Class DataSourceConfiguration. Provides access to database to add audit information about p8 operations<br>
 */

//@Configuration
//@EnableJpaRepositories(basePackages = "com.davita.jpa.taxonomy", entityManagerFactoryRef = "taxonomyEntityManager", transactionManagerRef = "taxonomyTransactionManager")
public class DataSourceConfiguration {

	/**
	 * Hibernate ddl-auto value. Defined as spring boot
	 * configuration : @Value("${com.davita.ecm.p8.audit.jpa.hibernate.ddl-auto:#{\"validate\"}}")
	 * 
	 * @param jpaDdlAuto the new  ddl-auto value
	 * @return Current ddl-auto value
	 */
	@Value("${com.davita.jpa.taxonomy.hibernate.ddl-auto:#{\"validate\"}}")
	private String jpaDdlAuto;

	/**
	 * Hibernate dialect value. Defined as spring boot
	 * configuration : @Value("${com.davita.ecm.p8.audit.jpa.properties.hibernate.dialect:#{\"org.hibernate.dialect.Oracle12cDialect\"}}")
	 * 
	 * @param jpaDialect the new dialect value
	 * @return Current dialect value
	 */
	@Value("${com.davita.jpa.taxonomy.properties.hibernate.dialect:#{\"org.hibernate.dialect.Oracle12cDialect\"}}")
	private String jpaDialect;

	/**
	 * Hibernate show-sql value. Defined as spring boot
	 * configuration : @Value("${com.davita.ecm.p8.audit.jpa.show-sql:#{\"false\"}}")
	 * 
	 * @param jpaShowSql the new show-sql value
	 * @return Current show-sql value
	 */
	@Value("${com.davita.jpa.taxonomy.show-sql:#{\"false\"}}")
	private String jpaShowSql;

	/**
	 * Additional jpa properties.
	 *
	 * @return the map
	 */
	Map<String, String> additionalJpaProperties() {
		Map<String, String> map = new HashMap<>();

		map.put("hibernate.hbm2ddl.auto", jpaDdlAuto);
		map.put("hibernate.dialect", jpaDialect);
		map.put("hibernate.show_sql", jpaShowSql);

		return map;
	}

	/**
	 * Transaction manager to be used for p8 audit logging.
	 *
	 * @param domainsEntityManager the domains entity manager
	 * @return the jpa transaction manager
	 */
	@Bean(name = "taxonomyTransactionManager")
	public JpaTransactionManager auditTransactionManager(
			@Qualifier("taxonomyEntityManager") EntityManagerFactory domainsEntityManager) {
		return new JpaTransactionManager(domainsEntityManager);
	}

	/**
	 * Entity manager factory builder.
	 * 
	 * We need qualified EntityManagerFactoryBuilder instance to avoid conflicts with possible other unqualified definitions  
	 *
	 * @return the entity manager factory builder
	 */
	@Bean(name = "taxonomyEntityManagerFactoryBuilder")
	public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
		return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), additionalJpaProperties(), null);
	}

	/**
	 * Gets the commons entity manager to be used for p8 audit logging.
	 *
	 * @param builder the builder
	 * @param taxonomyDataSource the audit data source
	 * @return the commons entity manager
	 */
	@Bean(name = "taxonomyEntityManager")
	public LocalContainerEntityManagerFactoryBean getAuditEntityManager(
			@Qualifier("taxonomyEntityManagerFactoryBuilder") EntityManagerFactoryBuilder builder,
			@Qualifier("taxonomyDataSource") DataSource taxonomyDataSource) {

		return builder.dataSource(taxonomyDataSource).packages("com.davita.jpa.taxonomy")
				.properties(additionalJpaProperties()).build();
	}

	/**
	 * Data source to be used for p8 audit logging.
	 *
	 * @return the data source
	 */
	@Bean
	@ConfigurationProperties(prefix = "com.davita.jpa.taxonomy.datasource")
	public DataSource taxonomyDataSource() {
		return DataSourceBuilder.create().build();
	}
}
