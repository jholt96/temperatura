package edge.temperatura.temperatura.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import edge.temperatura.temperatura.models.Users;
import lombok.Getter;

public class UserDetailsImpl implements UserDetails{

	private static final long serialVersionUID = 1L;
    private ObjectId _id;
    private String password;
    private String username;
    private String email;


    private final Collection<? extends GrantedAuthority> grantedAuthority;


    public UserDetailsImpl(ObjectId id, String username, String email, String password,
    Collection<? extends GrantedAuthority> authorities){
        this._id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.grantedAuthority = authorities;
    }

    public static UserDetailsImpl build(Users user) {
		List<GrantedAuthority> authorities = user.getRoles()
                .stream()
				.map(role -> new SimpleGrantedAuthority(role.name()))
				.collect(Collectors.toList());

		return new UserDetailsImpl(
				user.get_id(), 
				user.getUsername(), 
				user.getEmail(),
				user.getPassword(), 
				authorities);
	}


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthority;
    }
    public Object getId() {
		return _id;
	}

	public String getEmail() {
		return email;
	}

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {return true;}

    @Override
    public boolean isAccountNonLocked() {return true;}

    @Override
    public boolean isCredentialsNonExpired() {return true;}

@Override
public boolean isEnabled() {return true;}

}