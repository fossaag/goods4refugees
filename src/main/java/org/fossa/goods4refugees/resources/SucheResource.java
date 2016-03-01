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

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.fossa.goods4refugees.core.Abgabestelle;
import org.fossa.goods4refugees.core.Hilfsgut;
import org.fossa.goods4refugees.db.AbgabestelleDAO;
import org.fossa.goods4refugees.db.HilfsgutDAO;
import org.fossa.goods4refugees.views.SucheView;

import com.google.common.collect.ImmutableMap;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.views.View;

@Path("/suche/")
@Produces(MediaType.APPLICATION_JSON)
public class SucheResource {
	
	@Context
	private UriInfo uriInfo;

	private final ImmutableMap<String, String> environmentConfig;
	private HilfsgutDAO hilfsgutDAO;
	private AbgabestelleDAO abgabestelleDAO;
    
    public SucheResource(ImmutableMap<String, String> environmentConfig, HilfsgutDAO hilfsgutDAO, AbgabestelleDAO abgabestelleDAO) {
    	this.environmentConfig = environmentConfig;
    	this.hilfsgutDAO = hilfsgutDAO;
    	this.abgabestelleDAO = abgabestelleDAO;
    }

    @POST
    @UnitOfWork
    @Produces(MediaType.TEXT_HTML)
    public View postSuche(@FormParam("suchbegriff") String suchbegriff) {
    	return buildSucheView(suchbegriff);
    }
    
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getSuche() throws URISyntaxException {
    	throw new WebApplicationException(405);
    }
    
    @GET
    @Path("{suchbegriff}")
    @UnitOfWork
    @Produces(MediaType.TEXT_HTML)
    public View getSuche(@PathParam("suchbegriff") String suchbegriff) {
    	return buildSucheView(suchbegriff);
    }

	private View buildSucheView(String suchbegriff) {
		List<Hilfsgut> hilfsgueter = new ArrayList<Hilfsgut>();
    	for (Hilfsgut hilfsgut : hilfsgutDAO.findAllByAngelegtDatum()) {
    		if (hilfsgut.getName().toUpperCase().contains(suchbegriff.toUpperCase()) || hilfsgut.getBeschreibung().toUpperCase().contains(suchbegriff.toUpperCase())) {
    			hilfsgueter.add(hilfsgut);
    		}
    	}
    	List<Abgabestelle> abgabestellen = new ArrayList<Abgabestelle>();
    	for (Abgabestelle abgabestelle : abgabestelleDAO.findAll()) {
    		if (
    				abgabestelle.getName().toUpperCase().contains(suchbegriff.toUpperCase()) ||
    				abgabestelle.getAnschrift().toUpperCase().contains(suchbegriff.toUpperCase()) ||
    				abgabestelle.getAnsprechpartner().toUpperCase().contains(suchbegriff.toUpperCase()) ||
    				abgabestelle.getEmail().toUpperCase().contains(suchbegriff.toUpperCase()) ||
    				abgabestelle.getInternet().toUpperCase().contains(suchbegriff.toUpperCase()) ||
    				abgabestelle.getKurzvorstellung().toUpperCase().contains(suchbegriff.toUpperCase()) ||
    				abgabestelle.getOeffnungszeiten().toUpperCase().contains(suchbegriff.toUpperCase()) ||
    				abgabestelle.getTelefon().toUpperCase().contains(suchbegriff.toUpperCase())
    			) {
    			abgabestellen.add(abgabestelle);
    		}
    	}
    	return new SucheView(SucheView.Template.MUSTACHE, environmentConfig, suchbegriff, hilfsgueter, abgabestellen);
	}
}
