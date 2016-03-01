/**
 * Copyright (c) 2015 Frank Kaddereit, Anne Lachnitt, http://www.fossa.de/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.fossa.goods4refugees;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.basic.BasicAuthFactory;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.fossa.goods4refugees.auth.BackendAuthenticator;
import org.fossa.goods4refugees.core.Abgabestelle;
import org.fossa.goods4refugees.core.BackendUser;
import org.fossa.goods4refugees.core.Hilfsgut;
import org.fossa.goods4refugees.core.Kategorie;
import org.fossa.goods4refugees.core.Ort;
import org.fossa.goods4refugees.db.AbgabestelleDAO;
import org.fossa.goods4refugees.db.BackendUserDAO;
import org.fossa.goods4refugees.db.HilfsgutDAO;
import org.fossa.goods4refugees.db.KategorieDAO;
import org.fossa.goods4refugees.db.OrtDAO;
import org.fossa.goods4refugees.resources.AbgabestelleResource;
import org.fossa.goods4refugees.resources.BackendAbgabestelleResource;
import org.fossa.goods4refugees.resources.BackendHilfsgutResource;
import org.fossa.goods4refugees.resources.DankeResource;
import org.fossa.goods4refugees.resources.DatenschutzhinweisResource;
import org.fossa.goods4refugees.resources.HilfsgutResource;
import org.fossa.goods4refugees.resources.HomeResource;
import org.fossa.goods4refugees.resources.ImpressumResource;
import org.fossa.goods4refugees.resources.KategorieResource;
import org.fossa.goods4refugees.resources.OrtResource;
import org.fossa.goods4refugees.resources.SucheResource;
import org.hibernate.SessionFactory;

import com.google.common.collect.ImmutableMap;

public class Goods4RefugeesApplication extends Application<Goods4RefugeesConfiguration> {
    public static void main(String[] args) throws Exception {
        new Goods4RefugeesApplication().run(args);
    }

    private final HibernateBundle<Goods4RefugeesConfiguration> hibernateBundle =
            new HibernateBundle<Goods4RefugeesConfiguration>(
            		BackendUser.class,
            		Ort.class,
            		Hilfsgut.class,
            		Kategorie.class,
            		Abgabestelle.class
            		) {
                @Override
                public DataSourceFactory getDataSourceFactory(Goods4RefugeesConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    @Override
    public String getName() {
        return "goods4refugees";
    }

    @Override
    public void initialize(Bootstrap<Goods4RefugeesConfiguration> bootstrap) {
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );

        bootstrap.addBundle(new AssetsBundle());
        bootstrap.addBundle(new MigrationsBundle<Goods4RefugeesConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(Goods4RefugeesConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        bootstrap.addBundle(hibernateBundle);
        bootstrap.addBundle(new ViewBundle<Goods4RefugeesConfiguration>() {
            @Override
            public ImmutableMap<String, ImmutableMap<String, String>> getViewConfiguration(Goods4RefugeesConfiguration configuration) {
                return configuration.getViewRendererConfiguration();
            }
        });
        bootstrap.addBundle(new SwaggerBundle<Goods4RefugeesConfiguration>() {
            @Override
			public SwaggerBundleConfiguration getSwaggerBundleConfiguration(Goods4RefugeesConfiguration configuration) {
                return configuration.swaggerBundleConfiguration;
            }
        });
    }

    @Override
    public void run(Goods4RefugeesConfiguration configuration, Environment environment) {
    	SessionFactory factory = hibernateBundle.getSessionFactory();
        final BackendUserDAO backendUserDAO = new BackendUserDAO(factory);
        final OrtDAO ortDAO = new OrtDAO(factory);        
		final KategorieDAO kategorieDAO = new KategorieDAO(factory);
		final HilfsgutDAO hilfsgutDAO = new HilfsgutDAO(factory);
		final AbgabestelleDAO abgabestelleDAO = new AbgabestelleDAO(factory);
        
        final ImmutableMap<String, String> environmentConfig = configuration.getEnvironmentConfiguration();

        final FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        
        environment.jersey().register(new RuntimeExceptionMapper());
        environment.jersey().register(new DankeResource(environmentConfig));
        environment.jersey().register(new ImpressumResource(environmentConfig));
        environment.jersey().register(new DatenschutzhinweisResource(environmentConfig));
        environment.jersey().register(new HomeResource(environmentConfig, ortDAO, abgabestelleDAO, hilfsgutDAO));
        BasicAuthFactory<BackendUser> authFact = new BasicAuthFactory<>(new BackendAuthenticator(backendUserDAO),
                "Backend-Login G4R",
                BackendUser.class);
        authFact.responseBuilder(new RuntimeUnothorizedHandler());
        environment.jersey().register(AuthFactory.binder(authFact));
        environment.jersey().register(new OrtResource(environmentConfig, ortDAO, hilfsgutDAO, abgabestelleDAO));
        environment.jersey().register(new AbgabestelleResource(environmentConfig, abgabestelleDAO, hilfsgutDAO));
        environment.jersey().register(new KategorieResource(environmentConfig, hilfsgutDAO));
        environment.jersey().register(new HilfsgutResource(environmentConfig, hilfsgutDAO));
        environment.jersey().register(new BackendAbgabestelleResource(environmentConfig, abgabestelleDAO, hilfsgutDAO));
        environment.jersey().register(new BackendHilfsgutResource(environmentConfig, abgabestelleDAO, hilfsgutDAO, kategorieDAO));
        environment.jersey().register(new SucheResource(environmentConfig, hilfsgutDAO, abgabestelleDAO));
    }
}
