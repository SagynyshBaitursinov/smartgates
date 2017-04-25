package models;

import play.db.jpa.GenericModel;

import javax.persistence.*;

/**
 * Created by sagynysh on 4/24/17.
 */
@Entity
@Table(name = "admin_")
public class Admin extends GenericModel {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_")
    public Long id;

    @Column(name="login_")
    public String login;

    @Column(name="password_")
    public String password;

    @Column(name="password_salt_")
    public String passwordSalt;
}