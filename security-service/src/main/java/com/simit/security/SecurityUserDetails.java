package com.simit.security;

import com.simit.entity.Permission;
import com.simit.entity.Role;
import com.simit.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class SecurityUserDetails  extends User implements UserDetails {

    private static final long serialVersionUID = 1L;

    private List<Permission> permissions;

    private List<Role> roles;

    public SecurityUserDetails(User user) {

        if(user!=null) {
            // Principal用户信息
            this.setUsername(user.getUsername());
            this.setPassword(user.getPassword());

            this.permissions  = user.getPermissions();
            this.roles = user.getRoles();
        }
    }

    /**
     * 添加用户拥有的权限和角色
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorityList = new ArrayList<>();
        // 添加请求权限
        if(permissions!=null&&permissions.size()>0){
            for (Permission permission : permissions) {
//                if(StrUtil.isNotBlank(permission.getTitle()) &&StrUtil.isNotBlank(permission.getPath())) {
//                    authorityList.add(new SimpleGrantedAuthority(permission.getTitle()));
//                }
            }
        }
        // 添加角色
        if(roles!=null&&roles.size()>0){
            // lambda表达式
            roles.forEach(item -> {
//                if(StrUtil.isNotBlank(item.getName())){
//                    authorityList.add(new SimpleGrantedAuthority(item.getName()));
//                }
            });
        }
        return authorityList;
    }

    /**
     * 账户是否过期
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    /**
     * 是否禁用
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    /**
     * 密码是否过期
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    /**
     * 是否启用
     * @return
     */
    @Override
    public boolean isEnabled() {

        return true;
    }
}
