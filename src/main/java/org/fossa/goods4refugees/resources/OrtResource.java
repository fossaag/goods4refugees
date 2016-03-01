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

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.fossa.goods4refugees.core.Abgabestelle;
import org.fossa.goods4refugees.core.Hilfsgut;
import org.fossa.goods4refugees.core.Ort;
import org.fossa.goods4refugees.db.AbgabestelleDAO;
import org.fossa.goods4refugees.db.HilfsgutDAO;
import org.fossa.goods4refugees.db.OrtDAO;
import org.fossa.goods4refugees.views.OrtView;

import com.google.common.collect.ImmutableMap;

@Path("/ort/")
@Produces(MediaType.APPLICATION_JSON)
public class OrtResource {
	
	@Context
	private UriInfo uriInfo;

	private final ImmutableMap<String, String> environmentConfig;
	private OrtDAO ortDAO;
	private HilfsgutDAO hilfsgutDAO;
	private AbgabestelleDAO abgabestelleDAO;
    
    public OrtResource(ImmutableMap<String, String> environmentConfig, OrtDAO ortDAO, HilfsgutDAO hilfsgutDAO, AbgabestelleDAO abgabestelleDAO) {
    	this.environmentConfig = environmentConfig;
    	this.ortDAO = ortDAO;
    	this.hilfsgutDAO = hilfsgutDAO;
    	this.abgabestelleDAO = abgabestelleDAO;
    }

    @GET
    @UnitOfWork
    @Path("{ortname}")
    @Produces(MediaType.TEXT_HTML)
    public View ortsansicht(@PathParam("ortname") String ortname) throws URISyntaxException {
    	return createOrtView(ortname);
    }
    
    @GET
    @UnitOfWork
    @Produces(MediaType.TEXT_HTML)
    public View ortsansicht() throws URISyntaxException {
    	return createOrtView("");
    }

	private View createOrtView(String ortnamensteil) {
		List<Ort> orte = ortDAO.filterByName(ortnamensteil);
    	if (orte.isEmpty()) {
    		throw new WebApplicationException(404);
    	}
    	List<Hilfsgut> hilfsgueter = new ArrayList<Hilfsgut>();
    	List<String> hilfsgutnamen = new ArrayList<String>();
    	for (Hilfsgut hilfsgut : hilfsgutDAO.findAllByAngelegtDatum()) {
    		if (orte.contains(hilfsgut.getAbgabestelle().getOrt()) && !hilfsgutnamen.contains(hilfsgut.getName())) {
    			hilfsgutnamen.add(hilfsgut.getName());
    			hilfsgueter.add(hilfsgut);
    		}
    	}
    	List<Abgabestelle> abgabestellen = new ArrayList<Abgabestelle>();
    	for (Abgabestelle abgabestelle : abgabestelleDAO.findAll()) {
    		if (orte.contains(abgabestelle.getOrt())) {
    			abgabestellen.add(abgabestelle);
    		}
    	}
    	String ortname = "";
    	for (Ort ort : orte) {
    		if (!orte.get(0).equals(ort)) {
    			ortname +=  ", ";
    		}
    		ortname += ort.getName();
    	}
    	return new OrtView(OrtView.Template.MUSTACHE, environmentConfig, ortname, hilfsgueter, abgabestellen);
	}
}
