package com.example.algamoney.api.config.security;

import com.example.algamoney.api.model.UserApp;
import com.example.algamoney.api.repository.UserAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UserAppRepository userAppRepository;

    @Autowired
    public AppUserDetailsService(UserAppRepository userAppRepository) {
        this.userAppRepository = userAppRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserApp> userAppOptional = userAppRepository.findByEmail(email);
        UserApp userApp = userAppOptional.orElseThrow(() -> new UsernameNotFoundException("User or password incorrect"));
        return new User(email, userApp.getPassword(), getPermissions(userApp));
    }

    private Collection<? extends GrantedAuthority> getPermissions(UserApp userApp) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        userApp.getPermissions().forEach(p -> authorities.add(new SimpleGrantedAuthority(p.getDescription().toUpperCase())));
        return authorities;
    }


}
