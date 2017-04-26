package controllers;

import dto.RequestDto;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import play.db.jpa.JPA;
import play.i18n.Lang;
import play.mvc.*;

import models.*;
import utils.Hash;
import utils.QRGenerator;

import java.util.List;
import java.util.UUID;

/**
 * Created by sagynysh on 4/24/17.
 */
public class Administration extends Controller {

    @Before(unless = {"login", "authorize", "logout"})
    static void secutity() {
        renderArgs.put("lang", Lang.get());
        if (authorizedUser() == null) {
            if (Admin.count() == 0) {
                Admin admin = new Admin();
                admin.login = "admin";
                try {
                    admin.passwordSalt = Hash.getSaltString();
                    admin.password = Hash.getHashString("qwerty123", admin.passwordSalt);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                admin.save();
            }
            login();
        }
    }

    private static Admin authorizedUser() {
        String id = session.get("logged");
        Admin admin = null;
        if (id != null) {
            admin = Admin.findById(Long.parseLong(id));
        }
        return admin;
    }

    public static void index() {
        pending();
    }

    public static void login() {
        if (authorizedUser() == null) {
            render();
        } else {
            pending();
        }
    }

    public static void logout() {
        session.clear();
        pending();
    }

    public static void authorize(String username, String password) {
        if (username == null) {
            login();
        }
        Admin admin = Admin.find("login = ?", username).first();
        if (admin != null) {
            try {
                String hash = Hash.getHashString(password, admin.passwordSalt);
                if (admin.password.equals(hash)) {
                    session.put("logged", admin.id);
                    pending();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        login();
    }

    public static void changePassword(String password1, String password2) {
        if (!password1.equals(password2) || authorizedUser() == null) {
            pending();
        }
        Admin admin = Admin.findById(authorizedUser().id);
        try {
            admin.password = Hash.getHashString(password1, admin.passwordSalt);
            admin.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
        pending();
    }

    public static void overview(Long id) {
        Request item = Request.findById(id);
        if (item == null) {
            pending();
        }
        render(item);
    }

    public static void accept(Long id) {
        Request item = Request.findById(id);
        if (item == null || !item.status.equals(Request.Status.pending)) {
            pending();
        }
        String uuid = UUID.randomUUID().toString().substring(0, 7);
        try {
            String fileName = QRGenerator.generate(uuid);
            sendEmail(item.email, item.fullname, fileName);
        } catch (Exception e) {
            overview(id);
        }
        item.uuid = uuid;
        item.status = Request.Status.accepted;
        item.save();
        overview(id);
    }

    public static void reject(Long id) {
        Request item = Request.findById(id);
        if (item == null || !item.status.equals(Request.Status.pending)) {
            pending();
        }
        item.status = Request.Status.rejected;
        item.save();
        try {
            sendEmail(item.email, item.fullname);
        } catch (EmailException e) {
        }
        overview(id);
    }

    public static void pending() {
        List<Request> requests = RequestDto.getRequestsByStatus(Request.Status.pending);
        render(requests);
    }

    public static void rejected() {
        List<Request> requests = RequestDto.getRequestsByStatus(Request.Status.rejected);
        render(requests);
    }

    public static void accepted() {
        List<Request> requests = RequestDto.getRequestsByStatus(Request.Status.accepted);
        render(requests);
    }

    public static void administrators() {
        List<Admin> admins = Admin.findAll();
        render(admins);
    }

    private static void sendEmail(String email, String name) throws EmailException {
        HtmlEmail httpEmail = new HtmlEmail();
        httpEmail.setHostName("smtp.gmail.com");
        httpEmail.setSmtpPort(587);
        httpEmail.setAuthenticator(new DefaultAuthenticator("code.smartgates@gmail.com", "qwerasdf1234"));
        httpEmail.setSSLOnConnect(true);
        httpEmail.addTo(email);
        httpEmail.setFrom("code.smartgates@gmail.com", "Code Smartgates");
        httpEmail.setSubject("Your request to Smartgates");
        httpEmail.setHtmlMsg("<center><p>Hello, " + name + "!</p><p>Your request is not accepted. Please, try again.</p></center>");
        httpEmail.send();
    }

    private static void sendEmail(String email, String name, String filename) throws EmailException {
        HtmlEmail httpEmail = new HtmlEmail();
        httpEmail.setHostName("smtp.gmail.com");
        httpEmail.setSmtpPort(587);
        httpEmail.setAuthenticator(new DefaultAuthenticator("code.smartgates@gmail.com", "qwerasdf1234"));
        httpEmail.setSSLOnConnect(true);
        httpEmail.addTo(email);
        httpEmail.setFrom("code.smartgates@gmail.com", "Code Smartgates");
        httpEmail.setSubject("Your request to Smartgates");
        httpEmail.setHtmlMsg("<center><p>Hello, " + name + "!</p><p>Your request is accepted. Show the qr code to our security guard</p><div><img src=\"http://138.68.109.140/application/image?name=" + filename + "\"></div></center>");
        httpEmail.send();
    }
}