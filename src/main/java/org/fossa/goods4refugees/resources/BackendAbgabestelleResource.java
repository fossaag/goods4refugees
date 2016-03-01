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

package org.fossa.goods4refugees.resources;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.fossa.goods4refugees.core.Abgabestelle;
import org.fossa.goods4refugees.core.BackendUser;
import org.fossa.goods4refugees.core.Hilfsgut;
import org.fossa.goods4refugees.db.AbgabestelleDAO;
import org.fossa.goods4refugees.db.HilfsgutDAO;
import org.fossa.goods4refugees.views.BackendAbgabestelleView;

import com.google.common.collect.ImmutableMap;

import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.views.View;

@Path("/backend/")
@Produces(MediaType.APPLICATION_JSON)
public class BackendAbgabestelleResource {
	
	@Context
	private UriInfo uriInfo;

	private final ImmutableMap<String, String> environmentConfig;
	private AbgabestelleDAO abgabestelleDAO;
	private HilfsgutDAO hilfsgutDAO;
    
    public BackendAbgabestelleResource(ImmutableMap<String, String> environmentConfig, AbgabestelleDAO abgabestelleDAO, HilfsgutDAO hilfsgutDAO) {
    	this.environmentConfig = environmentConfig;
    	this.abgabestelleDAO = abgabestelleDAO;
    	this.hilfsgutDAO = hilfsgutDAO;
    }

    @GET
    @UnitOfWork
    @Produces(MediaType.TEXT_HTML)
    public View backendAbgabestelle(@Auth BackendUser backendUser) throws URISyntaxException {
    	List<Abgabestelle> meineAbgabestellen = new ArrayList<Abgabestelle>();
    	if (backendUser.getIsAdmin()) {
    		for (Abgabestelle abgabestelle : abgabestelleDAO.findAll()) {
    			meineAbgabestellen.add(abgabestelle);
    		}
    	} else {
    		for (Abgabestelle abgabestelle : abgabestelleDAO.findAll()) {
    			if (abgabestelle.getBackendUser().equals(backendUser)) {
    				meineAbgabestellen.add(abgabestelle);
    			}
    		}
    	}
    	List<Hilfsgut> meineHilfsgueter = new ArrayList<Hilfsgut>();
    	for (Hilfsgut hilfsgut : hilfsgutDAO.findAll()) {
    		if (meineAbgabestellen.contains(hilfsgut.getAbgabestelle())) {
    			meineHilfsgueter.add(hilfsgut);
    		}
    	}
    	return new BackendAbgabestelleView(BackendAbgabestelleView.Template.MUSTACHE, environmentConfig, meineHilfsgueter, backendUser);
    }
}
