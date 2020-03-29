package pl.devgroup.restapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    Integer userId;

    @Column(name = "nick_name")
    String nickName;

    @Column(name = "email")
    String email;

    @Column(name = "password")
    String password;

    @Column(name = "gender")
    String gender;

    @Column(name = "age")
    Integer age;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_table_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "user_role_id"))
    private Set<Role> roles;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Location> locations;
    
}
