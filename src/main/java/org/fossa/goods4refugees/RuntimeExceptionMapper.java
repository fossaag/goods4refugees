package org.fossa.goods4refugees;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.fossa.goods4refugees.views.ErrorView;

public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

	public static final ErrorView ERROR_UNAUTHORIZED_VIEW = new ErrorView(401, "Ohne Authentifizierung ist Ihnen der Zugriff auf diese Seite nicht erlaubt. <a href=\"/backend/\">Bitte loggen Sie sich ein.</a>");

	@Override
	  public Response toResponse(RuntimeException runtime) {
	    Response defaultResponse = Response
	      .serverError()
	      .entity(new ErrorView(500, "Ein Server-Fehler ist aufgetreten."))
	      .build();

	    if (runtime instanceof WebApplicationException) {

	      return handleWebApplicationException(runtime, defaultResponse);
	    }

	    return defaultResponse;
	  }

	  private Response handleWebApplicationException(RuntimeException exception, Response defaultResponse) {
	    WebApplicationException webAppException = (WebApplicationException) exception;

	    if (webAppException.getResponse().getStatus() == 401) {
	      return Response
	        .status(Response.Status.UNAUTHORIZED)
	        .entity(ERROR_UNAUTHORIZED_VIEW)
	        .build();
	    }
	    if (webAppException.getResponse().getStatus() == 404) {
	      return Response
	        .status(Response.Status.NOT_FOUND)
	        .entity(new ErrorView(404, "Die angeforderte Seite konnte nicht gefunden werden. Bitte überprüfe nochmals die URL."))
	        .build();
	    }
	    if (webAppException.getResponse().getStatus() == 405) {
		      return Response
		        .status(Response.Status.METHOD_NOT_ALLOWED)
		        .entity(new ErrorView(405, "Diese Methode ist nicht erlaubt."))
		        .build();
		    }
	    return defaultResponse;
	  }

	}