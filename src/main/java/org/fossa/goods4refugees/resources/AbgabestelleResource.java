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
import org.fossa.goods4refugees.db.AbgabestelleDAO;
import org.fossa.goods4refugees.db.HilfsgutDAO;
import org.fossa.goods4refugees.views.AbgabestelleView;

import com.google.common.collect.ImmutableMap;

@Path("/ort/{ortname}/{abgabestellename}")
@Produces(MediaType.APPLICATION_JSON)
public class AbgabestelleResource {
	
	@Context
	private UriInfo uriInfo;

	private final ImmutableMap<String, String> environmentConfig;
	private final AbgabestelleDAO abgabestelleDAO;
	private final HilfsgutDAO hilfsgutDAO;
    
    public AbgabestelleResource(ImmutableMap<String, String> environmentConfig, AbgabestelleDAO abgabestelleDAO, HilfsgutDAO hilfsgutDAO) {
    	this.environmentConfig = environmentConfig;
    	this.abgabestelleDAO = abgabestelleDAO;
    	this.hilfsgutDAO = hilfsgutDAO;
    }

    @GET
    @UnitOfWork
    @Produces(MediaType.TEXT_HTML)
    public View abgabestelle(@PathParam("ortname") String ortname, @PathParam("abgabestellename") String abgabestellename) throws URISyntaxException {
    	Abgabestelle abgabestelle = null;
    	for (Abgabestelle anAbgabestelle : abgabestelleDAO.findAll()) {
    		if (anAbgabestelle.getName().equals(abgabestellename) && anAbgabestelle.getOrt().getName().equals(ortname)) {
    			abgabestelle = anAbgabestelle;
    		}
    	}
    	if (abgabestelle == null) {
    		throw new WebApplicationException(404);
    	}
    	List<Hilfsgut> abgabestellenHilfsgueter = new ArrayList<Hilfsgut>();
    	for (Hilfsgut hilfsgut : hilfsgutDAO.findAllByAngelegtDatum()) {
    		if (hilfsgut.getAbgabestelle().equals(abgabestelle)) {
    			abgabestellenHilfsgueter.add(hilfsgut);
    		}
    	}
    	return new AbgabestelleView(AbgabestelleView.Template.MUSTACHE, environmentConfig, abgabestelle, abgabestellenHilfsgueter);
    }
}
