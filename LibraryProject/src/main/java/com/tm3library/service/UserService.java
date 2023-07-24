package com.tm3library.service;

import com.tm3library.domain.Publisher;
import com.tm3library.domain.Role;
import com.tm3library.domain.User;
import com.tm3library.domain.enums.RoleTypes;
import com.tm3library.dto.request.RegisterRequest;
import com.tm3library.dto.request.UserRequest;
import com.tm3library.dto.response.PublisherResponse;
import com.tm3library.dto.response.UserResponse;
import com.tm3library.exception.BadRequestException;
import com.tm3library.exception.ConflictException;
import com.tm3library.exception.ResourceNotFoundException;
import com.tm3library.exception.message.ErrorMessage;
import com.tm3library.mapper.UserMapper;
import com.tm3library.repository.UserRepository;
import com.tm3library.security.SecurityUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;


    // !!! Eğer Password encoder @Lazy yapılmazsa security sonsuz döngüye giriyor!!!
    public UserService(UserRepository userRepository, RoleService roleService, @Lazy PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;

        this.userMapper = userMapper;
    }

    public User getUserByEmail(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.USER_NOT_FOUND_EXCEPTION, email)));

        return user;

    }

    public UserResponse findById(Long id) {

        User user = getUser(id);

        return userMapper.userToUserResponse(user);
    }

    // Yardımcı method
    public User getUser(Long id) {

        User user = userRepository.findUserById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.USER_NOT_FOUND_EXCEPTION, id)));

        return user;
    }

    public void saveUser(RegisterRequest registerRequest) {

        // Kullanıcı daha önce kayıtlı mı değil mi kontrol ettik
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_EXIST_MESSAGE, registerRequest.getEmail()));
        }

        // !!! yeni kullanıcın rol bilgisini default olarak "MEMBER" atıyorum
        Role role = roleService.findByType(RoleTypes.ROLE_MEMBER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        // Şifre encode etme
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

        // Kullanıcıyı oluşturuyoruz artık!!!

        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encodedPassword);
        user.setScore(0);
        user.setAddress(registerRequest.getAddress());
        user.setPhone(registerRequest.getPhone());
        user.setBirthDate(registerRequest.getBirthDate());
        user.setCreateDate(LocalDateTime.now());
        //user.getResetPasswordCode();
        user.setRoles(roles);

        userRepository.save(user);
    }


    public UserResponse getPrincipal() {

        User user = getCurrentUser();

        return userMapper.userToUserResponse(user);
    }

    // Yardımcı method
    public User getCurrentUser() {
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() ->
                new ResourceNotFoundException(ErrorMessage.PRINCIPAL_FOUND_MESSAGE));
        User user = getUserByEmail(email);

        return user;
    }

    public Page<UserResponse> findAllUsersWithPage(Pageable pageable) {

        Page<User> userPage = userRepository.findAll(pageable);


        return userPage.map(user -> userMapper.userToUserResponse(user));

    }


    public UserResponse creatUser(UserRequest userRequest) {


        // Kullanıcı daha önce kayıtlı mı değil mi kontrol ettik
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_EXIST_MESSAGE, userRequest.getEmail()));
        }

        // !!! yeni gelen şifreyi encode edilecek
        String encodedPassword = passwordEncoder.encode(userRequest.getPassword());


        //!!! Role
        Set<String> userStrRoles = userRequest.getRoles();

        Set<Role> roles = convertRoles(userStrRoles);

        User user = new User();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(encodedPassword);
        user.setScore(0);
        user.setAddress(userRequest.getAddress());
        user.setPhone(userRequest.getPhone());
        user.setBirthDate(userRequest.getBirthDate());
        user.setCreateDate(LocalDateTime.now());

        // Bu işlemi yapan kişinin rolünü kontrol etme
        Set<Role> roleControl = getCurrentUser().getRoles();
        roleControl.forEach(r ->
        {
            if (r.getType().equals(RoleTypes.ROLE_EMPLOYEE)) {
                userRequest.getRoles().forEach(w -> {
                    if (w.equalsIgnoreCase(RoleTypes.ROLE_EMPLOYEE.getName()) ||
                            (w.equalsIgnoreCase(RoleTypes.ROLE_ADMIN.getName()))) {

                        throw new BadRequestException(ErrorMessage.USER_DOES_NOT_HAVE_PERMISSION_EXCEPTION);

                    }
                });
            }
        });

        user.setRoles(roles);

        userRepository.save(user);

        return userMapper.userToUserResponse(user);

    }


    // !!! Yardımcı method !!!
    private Set<Role> convertRoles(Set<String> pRoles) { // pRoles={"Customer","Administrator"}
        Set<Role> roles = new HashSet<>();


        if (pRoles == null) {
            Role userRole = roleService.findByType(RoleTypes.ROLE_MEMBER);
            roles.add(userRole);
        } else {
            pRoles.forEach(roleStr -> {
                if (roleStr.equalsIgnoreCase(RoleTypes.ROLE_ADMIN.getName())) { // Administrator
                    Role adminRole = roleService.findByType(RoleTypes.ROLE_ADMIN);
                    roles.add(adminRole); //ROLE_ADMIN
                } else if (roleStr.equalsIgnoreCase(RoleTypes.ROLE_EMPLOYEE.getName())) {
                    Role userRole = roleService.findByType(RoleTypes.ROLE_EMPLOYEE);// Employee
                    roles.add(userRole);//ROLE_EMPLOYEE
                } else if (roleStr.equalsIgnoreCase(RoleTypes.ROLE_MEMBER.getName())) {
                    Role userRole = roleService.findByType(RoleTypes.ROLE_MEMBER);// Member
                    roles.add(userRole);//ROLE_MEMBER
                } else {
                    throw new BadRequestException(ErrorMessage.ROLEE_NOT_FOUND_EXCEPTION);
                }
            });
        }
        return roles;

    }

    public UserResponse deleteUser(Long id) {

        User user= getUser(id);

        // BuiltIn check !!!
        if (user.getBuiltIn()){
            throw new BadRequestException(ErrorMessage.NOT_PERMITED_METHOD_MESSAGE);
        }

        // !!!! Loan kontrolü yapılacak !!!!
        if (user.getLoanList().size()>0){
            throw new BadRequestException(String.format(ErrorMessage.THIS_USER_HAS_LOAN_EXCEPTION));
        }

        UserResponse userResponse=userMapper.userToUserResponse(user);

        userRepository.deleteById(id);

        return userResponse;

    }

    public UserResponse updateUser(Long id, UserRequest userRequest) {

        User user= getUser(id);

        // Bu işlemi yapan kişinin rolünü kontrol etme
        Set<Role> roleControl = getCurrentUser().getRoles();
        roleControl.forEach(r ->
        {
            if (r.getType().equals(RoleTypes.ROLE_EMPLOYEE)) {
                userRequest.getRoles().forEach(w -> {
                    if (w.equalsIgnoreCase(RoleTypes.ROLE_EMPLOYEE.getName()) ||
                            (w.equalsIgnoreCase(RoleTypes.ROLE_ADMIN.getName()))) {

                        throw new BadRequestException(ErrorMessage.USER_DOES_NOT_HAVE_PERMISSION_EXCEPTION);

                    }
                });
            }
        });


        // BuiltIn check !!!
        if (user.getBuiltIn()){
            throw new BadRequestException(ErrorMessage.NOT_PERMITED_METHOD_MESSAGE);
        }

        // email ve password control !!!
        boolean emailExist= userRepository.existsByEmail(userRequest.getEmail());
        if (emailExist && !(userRequest.getEmail().equals(user.getEmail()))){
            throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_EXIST_MESSAGE,userRequest.getEmail()));
        }

        if (userRequest.getPassword()==null){
            userRequest.setPassword(user.getPassword());
        } else {
           String encodedNewPassword= passwordEncoder.encode(userRequest.getPassword());
           userRequest.setPassword(encodedNewPassword);
        }

        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setPhone(userRequest.getPhone());
        user.setAddress(userRequest.getAddress());
        user.setBirthDate(userRequest.getBirthDate());
        user.setBuiltIn(userRequest.getBuiltIn());
        user.setScore(userRequest.getScore());

        Set<String> roles=userRequest.getRoles();
        Set<Role> newRole=convertRoles(roles);

        user.setRoles(newRole);

        userRepository.save(user);

        return userMapper.userToUserResponse(user);
    }

    public List<User> getAllMembers() {

        List<User> users=getAllUsers();

        List<User> members=new ArrayList<>();

        // users.stream().map(t->t.getRoles().toString()).filter("Member");

        for (User w:users){
            for (Role x:w.getRoles()){
                if (x.getType().getName().equalsIgnoreCase("Member")){
                    members.add(w);
                }
            }
        }

        return members;

    }

    public List<User> getAllUsers(){

        return userRepository.getAllBy();

    }
}
