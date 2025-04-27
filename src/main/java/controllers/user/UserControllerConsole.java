package controllers.user;

import DAO.UserDAO;
import entity.User;

import java.util.List;

public class UserControllerConsole {

    private final UserDAO userDAO;

    public UserControllerConsole() {
        this.userDAO = new UserDAO();
    }

    public boolean ajouterUser(User user) {
        return userDAO.insert(user);
    }

    public List<User> getAllUsers() {
        return userDAO.getAll();
    }

    public boolean supprimerUser(int id) {
        return userDAO.delete(id);
    }
}
