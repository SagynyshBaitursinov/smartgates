package controllers;

import com.google.gson.Gson;
import dao.RequestDao;
import dto.RequestDto;
import models.RecaptchaResponse;
import models.Request;
import play.mvc.Controller;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.Date;

/**
 * Created by sagynysh on 4/24/17.
 */
public class Application extends Controller {

    public static void index() {
        String error = flash.get("error");
        String success = flash.get("success");
        render(error, success);
    }

    public static void acceptRequest(String fullname, String reason, String email) {
        String gRecaptchaResponse = request.params.get("g-recaptcha-response");
        if (gRecaptchaResponse == null || gRecaptchaResponse.trim().isEmpty() || fullname == null || fullname.trim().isEmpty() || reason == null || reason.trim().isEmpty() || email == null || email.isEmpty()) {
            flash.put("error", "error");
            index();
        }
        if (!recaptchaIsValid("secret=6LfMkB4UAAAAAGnvidjg08OXWikCgsyItbVy6nqX&response=" + gRecaptchaResponse)) {
            flash.put("error", "error");
            index();
        }
        Request request = new Request();
        request.reason = reason;
        request.fullname = fullname;
        request.email = email;
        request.status = Request.Status.pending;
        request.date = new Date();
        request.save();
        flash.put("success", "success");
        index();
    }

    public static void verify(String uuid) {
        Request item = RequestDao.findRequestByUUID(uuid);
        if (item == null) {
            notFound();
        }
        renderJSON(new RequestDto(item));
    }

    public static void image(String name) {
        File file = new File("/opt/smartgates/" + name);
        if (file.exists()) {
            renderBinary(file);
        } else {
            notFound();
        }
    }

    private static boolean recaptchaIsValid(String urlParameters) {
        HttpsURLConnection connection = null;
        try {
            URL url = new URL("https://www.google.com/recaptcha/api/siteverify");
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length",
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.close();
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return new Gson().fromJson(response.toString(), RecaptchaResponse.class).success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
