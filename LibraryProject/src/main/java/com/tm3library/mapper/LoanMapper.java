package com.tm3library.mapper;

import com.tm3library.domain.Book;
import com.tm3library.domain.Loan;
import com.tm3library.domain.User;
import com.tm3library.dto.request.LoanRequest;
import com.tm3library.dto.response.LoanAdminResponse;
import com.tm3library.dto.response.LoanResponse;
import com.tm3library.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoanMapper {


    @Mapping(target = "id", ignore = true)
    Loan loanRequestToLoan(LoanRequest loanRequest);

    //--------------------------------------

    //@Mapping(source = "book", target = "book", qualifiedByName = "getBookId")
    @Mapping(source = "user", target = "userId", qualifiedByName = "getUserId")
    LoanResponse loanToLoanResponse(Loan loan);


    @Named("getUserId")
    public static Long getUserId(User user){
        return user.getId();
    }

//    @Named("getBookId")
//    public static Long getBookId(Book book){
//        return book.getId();
//    }


    //@Mapping(source = "user", target = "userResponse", qualifiedByName = "getUser")
    //LoanAdminResponse loanToLoanAdminResponse(Loan loan);

//    @Named("getUser")
//    default UserResponse getUser(User user){
//        UserResponse userResponse=new UserResponse();
//        return new userResponse();
//    }


}
