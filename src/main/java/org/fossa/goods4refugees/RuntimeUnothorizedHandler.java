package org.fossa.goods4refugees;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.dropwizard.auth.UnauthorizedHandler;

public class RuntimeUnothorizedHandler implements UnauthorizedHandler {
    private static final String CHALLENGE_FORMAT = "%s realm=\"%s\"";

    @Override
    public Response buildResponse(String prefix, String realm) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE, String.format(CHALLENGE_FORMAT, prefix, realm))
                .type(MediaType.TEXT_HTML_TYPE)
                .entity(RuntimeExceptionMapper.ERROR_UNAUTHORIZED_VIEW)
                .build();
    }
}