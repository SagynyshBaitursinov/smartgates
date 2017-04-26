package models;

import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sagynysh on 4/24/17.
 */
@Entity
@Table(name = "request_")
public class Request extends GenericModel {

    public enum Status {
        pending,
        rejected,
        accepted
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id_")
    public Long id;

    @Column(name="fullname_")
    public String fullname;

    @Column(name="email_")
    public String email;

    @Column(name="reason_", columnDefinition="TEXT")
    public String reason;

    @Column(name="date_")
    public Date date;

    @Column(name="status_")
    @Enumerated(EnumType.STRING)
    public Status status;

    @Column(name="uuid_")
    public String uuid;

    @Transient
    public String getStringDate() {
        if (date != null) {
            return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(date);
        } else {
            return null;
        }
    }
}
