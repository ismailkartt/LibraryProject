package com.tm3library.exception.message;

import java.util.Locale;

public class ErrorMessage {

    public final static String RESOURCE_NOT_FOUND_EXCEPTION = "Resource is not found";
    public final static String USER_NOT_FOUND_EXCEPTION = "User with email: %s not found";
    public final static String JWTTOKEN_ERROR_MESSAGE = "JWT Token validation error: %s";
    public final static String EMAIL_ALREADY_EXIST_MESSAGE = "Email: %s already exist";
    public final static String ROLE_NOT_FOUND_EXCEPTION = "Role : %s not found";
    public final static String AUTHOR_NOT_FOUND_EXCEPTION = "Author is not found";
    public final static String AUTHOR_CANNOT_DELETE_EXCEPTION = "This author cannot be deleted because of has book";

    public final static String PUBLISHER_NOT_FOUND_EXCEPTION = "Publisher is not found";
    public final static String PUBLISHER_CANNOT_DELETE_EXCEPTION = "This publisher cannot be deleted";
    public final static String CATEGORY_ALREADY_EXIST_EXCEPTION = "This category is already exist";

    public final static String CATEGORY_NOT_FOUND_EXCEPTION = "Category is not found";
    public final static String CATEGORY_CANNOT_DELETE_EXCEPTION = "Category cannot found";
    public final static String IMAGE_NOT_FOUND_EXCEPTION = "Image cannot found";
    public final static String IMAGE_ALREADY_USING_EXCEPTION = "This Image is already being used";

    public final static String BOOK_NOT_FOUND_EXCEPTION = "Book is not found";

    public static final String BOOK_CANNOT_UPDATE_EXCEPTION = "Book with this id %s cannot updatable " ;
    public static final String IMAGE_HAS_ALREADY_BEEN_USED = "This image is already use another book";

    public static final String BOOK_IS_IN_LOAN_EXCEPTION = "This book is loaned by member";
    public static final String PRINCIPAL_FOUND_MESSAGE = "User not found";
    public static final String ROLEE_NOT_FOUND_EXCEPTION ="Role not found";
    public static final String USER_DOES_NOT_HAVE_PERMISSION_EXCEPTION = "User does not have permission" ;
    public static final String NOT_PERMITED_METHOD_MESSAGE = "Do not have permission to delete this user";
    public static final String THIS_USER_HAS_LOAN_EXCEPTION = "This user has Loan/Loans";
    public static final String THIS_BOOK_IS_IN_LOAN = "This book is already in loan";
    public static final String THIS_USER_HAS_EXPIRED_LOAN = "This user has expired loan/loans";
}
