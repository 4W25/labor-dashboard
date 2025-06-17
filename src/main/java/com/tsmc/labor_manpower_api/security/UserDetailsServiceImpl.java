package com.tsmc.labor_manpower_api.security;

import com.tsmc.labor_manpower_api.model.entity.User;
import com.tsmc.labor_manpower_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
// import java.util.stream.Collectors; // Keep for multi-role if needed


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        Collection<? extends GrantedAuthority> authorities;
        if (user.getRole() != null && user.getRole().getRoleName() != null) { // Added null check for role name
            authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName().toUpperCase()));
        } else {
            // Log a warning or handle cases where a user might not have a role
            // For example, assign a default "ROLE_USER" or throw an exception if roles are mandatory
            authorities = Collections.emptyList();
        }

        // For multiple roles, it would be:
        // Collection<? extends GrantedAuthority> authorities = user.getRoles().stream()
        // .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName().toUpperCase()))
        // .collect(Collectors.toList());


        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPasswordHash(),
                user.isActive(), // Using isActive directly as User entity has boolean isActive
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                authorities);
    }
}
