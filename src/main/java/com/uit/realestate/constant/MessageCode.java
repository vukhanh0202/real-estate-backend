package com.uit.realestate.constant;

public interface MessageCode {

    String ERROR = "message.error";

    interface System {
        String INVALID = "message.system.Invalid";
    }

    interface User {
        String USER_WRONG = "message.user.Wrong";
        String NOT_FOUND = "message.user.NotFound";
        String EXIST = "message.user.Exist";
        String EMAIL_EXIST = "message.user.EmailExist";
    }
    interface Token {
        String INVALID_TOKEN = "message.token.InvalidToken";
        String NOT_PERMISSION = "message.token.NotPermission";
    }
    interface Category {
        String NOT_FOUND = "message.category.NotFound";
        String EXIST = "message.category.Exist";
    }
    interface Apartment {
        String NOT_FOUND = "message.apartment.NotFound";
        String NOT_PENDING = "message.apartment.NotPending";
    }

    interface Address {
        String INVALID = "message.address.Invalid";
    }

    interface Province {
        String INVALID = "message.province.Invalid";
    }

    interface Area {
        String INVALID = "message.area.Invalid";
    }

    interface Price {
        String INVALID = "message.price.Invalid";
    }

    interface District {
        String NOT_HAVE_PROVINCE = "message.district.NotHaveProvince";
        String INVALID = "message.district.Invalid";
    }

    interface UserTarget {
        String NOT_FOUND = "message.userTarget.NotFound";
    }
}
