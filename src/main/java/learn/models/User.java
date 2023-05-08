package learn.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class User implements UserDetails {
    private int userId;
    private String username;
    private String password;
    private String email;
    private boolean enabled;
    private Collection<GrantedAuthority> authorities = new ArrayList<>();

    public User() {
    }

    public User(int userId, String username, String password, String email, boolean enabled, List<String> roles) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.enabled = enabled;
        addAuthorities(roles);
    }

    @Override
    public String getPassword() {  return password; }

    @Override
    public String getUsername() { return username; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    public void addAuthorities(Collection<String> authorityNames) {
        authorities.clear();
        for (String name : authorityNames) {
            authorities.add(new SimpleGrantedAuthority(name));
        }
    }


    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return new ArrayList<>(authorities);
    }


    public int getUserId() { return userId; }

    public void setUserId(int userId) { this.userId = userId; }

    public void setUsername(String username) { this.username = username; }

    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public void setEnabled(boolean enabled) { this.enabled = enabled; }


}
