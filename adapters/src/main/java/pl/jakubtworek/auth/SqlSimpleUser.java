package pl.jakubtworek.auth;

import pl.jakubtworek.auth.dto.SimpleUser;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USERS")
public class SqlSimpleUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    public static SqlSimpleUser fromUser(SimpleUser source) {
        SqlSimpleUser result = new SqlSimpleUser();
        result.id = source.getId();
        result.username = source.getUsername();
        return result;
    }

    public SimpleUser toUser() {
        return new SimpleUser(id, username);
    }
}
