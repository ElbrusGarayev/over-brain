package app.exception.handler;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class GlobalAppExHandler implements ErrorController {

  @Override
  public String getErrorPath() {
    return "/error";
  }

  @GetMapping("/error")
  public String handleError(HttpServletRequest rq) {
    return "404";
  }

}
