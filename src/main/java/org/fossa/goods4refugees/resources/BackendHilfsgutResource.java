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

import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;
import io.dropwizard.views.View;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.fossa.goods4refugees.core.Abgabestelle;
import org.fossa.goods4refugees.core.BackendUser;
import org.fossa.goods4refugees.core.Hilfsgut;
import org.fossa.goods4refugees.db.AbgabestelleDAO;
import org.fossa.goods4refugees.db.HilfsgutDAO;
import org.fossa.goods4refugees.db.KategorieDAO;
import org.fossa.goods4refugees.views.BackendHilfsgutView;

import com.google.common.collect.ImmutableMap;

@Path("/backend/hilfsgut/")
@Produces(MediaType.APPLICATION_JSON)
public class BackendHilfsgutResource {
	
	@Context
	private UriInfo uriInfo;

	private final ImmutableMap<String, String> environmentConfig;
	private HilfsgutDAO hilfsgutDAO;
	private KategorieDAO kategorieDAO;
	private AbgabestelleDAO abgabestelleDAO;
    
    public BackendHilfsgutResource(ImmutableMap<String, String> environmentConfig, AbgabestelleDAO abgabestelleDAO, HilfsgutDAO hilfsgutDAO, KategorieDAO kategorieDAO) {
    	this.environmentConfig = environmentConfig;
    	this.hilfsgutDAO = hilfsgutDAO;
    	this.kategorieDAO = kategorieDAO;
    	this.abgabestelleDAO = abgabestelleDAO;
    }

    @GET
    @Path("{hilfsgutId}")
    @UnitOfWork
    @Produces(MediaType.TEXT_HTML)
    public View hilfsguteditor(@Auth BackendUser backendUser, @PathParam("hilfsgutId") LongParam hilfsgutId) throws URISyntaxException {
    	return createBackendHilfsgutView(backendUser, hilfsgutDAO.findById(hilfsgutId.get()).get());
    }
    
    @GET
    @Path("new")
    @UnitOfWork
    @Produces(MediaType.TEXT_HTML)
    public View hilfsgutanlage(@Auth BackendUser backendUser) throws URISyntaxException {
    	return createBackendHilfsgutView(backendUser, new Hilfsgut());
    }
    
    private BackendHilfsgutView createBackendHilfsgutView(BackendUser backendUser, Hilfsgut hilfsgut) {
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
    	return new BackendHilfsgutView(BackendHilfsgutView.Template.MUSTACHE, environmentConfig, hilfsgut, kategorieDAO.findAll(), meineAbgabestellen, backendUser);
	}

	@POST
    @UnitOfWork
    @Produces(MediaType.TEXT_HTML + ";charset=UTF-8")
    public Response postHilfsgut(
    		@FormParam("name") String name,
    		@FormParam("beschreibung") String beschreibung,
    		@FormParam("kategorieId") Long kategorieId,
    		@FormParam("abgabestelleId") Long abgabestelleId,
    		@FormParam("hilfsgutId") LongParam hilfsgutId
    	) throws URISyntaxException, ParseException {
    	Hilfsgut hilfsgut;
    	if (hilfsgutId.get() == 0) {
    		hilfsgut = new Hilfsgut();
    		hilfsgut.setAngelegtDatum(new Date());
    	} else {
    		hilfsgut = hilfsgutDAO.findById(hilfsgutId.get()).get();
    	}
    	hilfsgut.setName(name);
    	hilfsgut.setBeschreibung(beschreibung);
    	hilfsgut.setKategorie(kategorieDAO.findById(kategorieId).get());
    	hilfsgut.setAbgabestelle(abgabestelleDAO.findById(abgabestelleId).get());
    	hilfsgutDAO.create(hilfsgut);
    	URI location = new URI("/backend/");
    	return Response.seeOther(location).build();
    }
    
    @POST
    @UnitOfWork
    @Path("/{hilfsgutId}/delete")
    public Response deleteArbeitszeitEntry(@Auth BackendUser backendUser, @PathParam("hilfsgutId") LongParam hilfsgutId) throws URISyntaxException {
    	Hilfsgut hilfsgut = hilfsgutDAO.findById(hilfsgutId.get()).get();
    	hilfsgutDAO.delete(hilfsgut);
    	URI location = new URI("/backend/");
    	return Response.seeOther(location).build();
    }
}
