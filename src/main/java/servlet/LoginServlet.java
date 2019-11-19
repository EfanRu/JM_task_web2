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

        //For test
        System.out.println("In LoginServlet method POST");
        System.out.println("User checking: " + user.getEmail() + " " +
                user.getPassword() + " is " + userService.isExistsThisUser(user));
        for (User u : userService.getAllUsers()) {
            System.out.println("User registration in database: " + u.getEmail() + " " + u.getPassword());
        }
        System.out.println(userService);


        if (userService.isExistsThisUser(user)) {
            for (User u : userService.getAllUsers()) {
                if (u.getEmail().equals(user.getEmail())) {
                    user = u;
                }
            }

            //For testing
            System.out.println("User checking: " + user.getEmail() + " " +
                    user.getPassword());


            if (userService.isUserAuthById(user.getId())) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Login is already authorization user by id : " +
                        user.getId());
            } else {
                userService.addUser(user);
                resp.getWriter().println("User " + user.getId() +" successful login");
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        } else {
            resp.getWriter().println("User " + user.getEmail() +" didn't registered");
            resp.setStatus(HttpServletResponse.SC_OK);
        }

        //For testing
        for (User u : userService.getAllUsers()) {
            System.out.println("User registration : " + u.getEmail() + " " + u.getPassword());
        }
        for (User u : userService.getAllAuth()) {
            System.out.println("User registration : " + u.getEmail() + " " + u.getPassword());
        }


        resp.setContentType("text/html;charset=utf-8");
    }

        @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = createPageVariablesMap(req);
        //For test
        System.out.println("In LoginServlet method GET");

        resp.getWriter().println(PageGenerator.getInstance().getPage("authPage.html", pageVariables));

        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }


    private static Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("method", request.getMethod());
        pageVariables.put("URL", request.getRequestURL().toString());
        //Need change to login and password
//        pageVariables.put("Parameter", request.getRequestURL().toString());
        return pageVariables;
    }
}
