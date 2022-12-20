package eirvid;
/*
@author : Pedro 
*/
public class User {

    int id; //this will be auto incremented in database
    String userName;
    String password;
    String rentalHistory = "";

    public User(String _userName, String _password) {

        this.userName = _userName;
        this.password = _password;

    }

}
