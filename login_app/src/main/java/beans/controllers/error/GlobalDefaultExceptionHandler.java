package beans.controllers.error;

import exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.view.RedirectView;

import javax.naming.AuthenticationException;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    protected static final Logger LOG = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RedirectView notFoundPageHandler() {
        return new RedirectView("/");
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String serviceExceptionHandler(ServiceException e) {
        LOG.warn("Was thrown ServiceException: " + e);
        return e.getBusinessMessage();
    }

    @ExceptionHandler({AuthenticationException.class, BadCredentialsException.class,
            DisabledException.class, AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public String authenticationExceptionHandler(Exception e) {
        LOG.warn("Was thrown AuthenticationException: " + e);
        return e.getLocalizedMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleError(Exception e) {
        LOG.warn("Was thrown Exception: " + e);
        return e.getLocalizedMessage();
    }
}
