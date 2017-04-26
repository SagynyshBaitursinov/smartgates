package dao;

import models.Request;
import play.db.jpa.JPA;

import java.util.List;

/**
 * Created by sagynysh on 4/25/17.
 */
public class RequestDao {

    public static List<Request> getRequestsByStatus(Request.Status status) {
        return JPA.em().createQuery("Select r from Request r where r.status = ?1", Request.class)
                .setParameter(1, status)
                .getResultList();
    }

    public static Request findRequestByUUID(String uuid) {
        try {
            return JPA.em().createQuery("Select r from Request r where r.uuid = ?1", Request.class).setParameter(1, uuid).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
