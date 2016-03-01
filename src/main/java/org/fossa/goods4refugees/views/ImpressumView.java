/**
 * Copyright (c) 2015 Frank Kaddereit, Jens Vogel, http://www.fossa.de/
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

package org.fossa.goods4refugees.views;

import io.dropwizard.views.View;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;

public class ImpressumView extends View {
	
	private final ImmutableMap<String,String> environmentConfig;
    
    public enum Template{
    	MUSTACHE("mustache/impressum.mustache");
    	
    	private String templateName;
    	private Template(String templateName){
    		this.templateName = templateName;
    	}
    	
    	public String getTemplateName(){
    		return templateName;
    	}
    }

    public ImpressumView(ImpressumView.Template template, ImmutableMap<String,String> environmentConfig) {
        super(template.getTemplateName(), Charsets.UTF_8);
        this.environmentConfig = environmentConfig;
    }
        
    public String getUrlStylesheet() {
		return environmentConfig.get("urlStylesheet");
	}
}