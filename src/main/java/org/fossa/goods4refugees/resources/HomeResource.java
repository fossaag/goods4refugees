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

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.views.View;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.fossa.goods4refugees.core.Hilfsgut;
import org.fossa.goods4refugees.db.AbgabestelleDAO;
import org.fossa.goods4refugees.db.HilfsgutDAO;
import org.fossa.goods4refugees.db.OrtDAO;
import org.fossa.goods4refugees.views.HomeView;

import com.google.common.collect.ImmutableMap;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class HomeResource {
	
	@Context
	private UriInfo uriInfo;

	private final ImmutableMap<String, String> environmentConfig;
	private final OrtDAO ortDAO;
	private final AbgabestelleDAO abgabestelleDAO;
	private final HilfsgutDAO hilfsgutDAO;
    
    public HomeResource(ImmutableMap<String, String> environmentConfig, OrtDAO ortDAO, AbgabestelleDAO abgabestelleDAO, HilfsgutDAO hilfsgutDAO) {
    	this.environmentConfig = environmentConfig;
    	this.ortDAO = ortDAO;
    	this.abgabestelleDAO = abgabestelleDAO;
    	this.hilfsgutDAO = hilfsgutDAO;
    }

    @GET
    @UnitOfWork
    @Produces(MediaType.TEXT_HTML)
    public View home() throws URISyntaxException {
    	List<Hilfsgut> lastFive = new ArrayList<Hilfsgut>();
    	int count = 0;
    	for (Hilfsgut hilfsgut : hilfsgutDAO.findAllByAngelegtDatum()) {
    		count += 1;
    		lastFive.add(hilfsgut);
    		if (count>=5) {
    			break;
    		}
    	}
    	return new HomeView(HomeView.Template.MUSTACHE, environmentConfig, ortDAO.findAllAlphabetically(), lastFive, hilfsgutDAO.findAll().size(), abgabestelleDAO.findAll().size(), ortDAO.findAllAlphabetically().size());
    }
    
    @POST
    @UnitOfWork
    @Produces(MediaType.TEXT_HTML + ";charset=UTF-8")
    public Response postTask(
    		@FormParam("ortValue") String ortValue
    	) throws URISyntaxException, ParseException {
    	URI location = new URI("/ort/" + ortValue);
    	return Response.seeOther(location).build();
    }
    
    @GET
    @Path("robots.txt")
    public Response robots() throws URISyntaxException {
    	URI location = new URI("/assets/robots.txt");
    	return Response.seeOther(location).build();
    }

    @GET
    @Path("favicon.ico")
    public Response favicon() throws URISyntaxException {
    	URI location = new URI("/assets/favicon.ico");
    	return Response.seeOther(location).build();
    }

    @GET
    @Path("sitemap.xml")
    public Response sitemap() throws URISyntaxException {
    	URI location = new URI("/assets/sitemap.xml");
    	return Response.seeOther(location).build();
    }

    @GET
    @Path("google0d83a3c67a30d299.html")
    public Response google() throws URISyntaxException {
    	URI location = new URI("/assets/google0d83a3c67a30d299.html");
    	return Response.seeOther(location).build();
    }

}
