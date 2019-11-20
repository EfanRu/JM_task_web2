package servlet;

import model.User;
import service.UserService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginServlet extends HttpServlet {
    UserService userService = UserService.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = createPageVariablesMap(req);
        User user = new User(req.getParameter("email"), req.getParameter("password"));
//mb need user with id???
        for (User u : userService.getAllUsers()) {
            if (u.getEmail().equals(user.getEmail()) &&
                    u.getPassword().equals(user.getPassword())) {
                if (userService.isExistsThisUser(user)) {
                   //Mb need check from id???
//                    user = u;
                    if (userService.isUserAuthById(u.getId())) {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        resp.getWriter().println("Login is already authorization user by id : " +
                                u.getId());
                    } else {
                        userService.authUser(u);
                        resp.getWriter().println("User " + u.getId() +" successful login");
                        resp.setStatus(HttpServletResponse.SC_OK);
                    }
                } else {
                    resp.getWriter().println("User " + user.getEmail() +" didn't registered");
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
                break;
            }
        }
        resp.setContentType("text/html;charset=utf-8");
    }

        @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = createPageVariablesMap(req);

        resp.getWriter().println(PageGenerator.getInstance().getPage("authPage.html", pageVariables));

        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }


    private static Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("method", request.getMethod());
        pageVariables.put("URL", request.getRequestURL().toString());
        return pageVariables;
    }
}
