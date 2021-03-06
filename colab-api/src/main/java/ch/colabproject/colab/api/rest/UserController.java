/*
 * The coLAB project
 * Copyright (C) 2021 AlbaSim, MEI, HEIG-VD, HES-SO
 *
 * Licensed under the MIT License
 */
package ch.colabproject.colab.api.rest;

import ch.colabproject.colab.api.ejb.UserManagement;
import ch.colabproject.colab.api.exceptions.ColabErrorMessage;
import ch.colabproject.colab.api.model.user.AuthInfo;
import ch.colabproject.colab.api.model.user.AuthMethod;
import ch.colabproject.colab.api.model.user.SignUpInfo;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * User controller
 *
 * @author maxence
 */
@Path("users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    /**
     * Users related business logic
     */
    @Inject
    private UserManagement userManagement;

    /**
     * Get the authentication method and its parameters a user shall use to authenticate with the
     * given email address
     *
     * @param email email address user want to sign in with
     *
     * @return Auth method to user
     */
    @GET
    @Path("/AuthMethod/{email}")
    public AuthMethod getAuthMethod(@PathParam("username") String email) {
        return userManagement.getAuthenticationMethod(email);
    }

    /**
     * Create a new local account
     *
     * @param signup all data required to create a local account
     *
     * @throws ColabErrorMessage if creation fails for any reason
     */
    @POST
    @Path("SignUp")
    public void signUp(SignUpInfo signup) throws ColabErrorMessage {
        userManagement.signup(signup);
    }

    /**
     * Authenticate with local-account credentials
     *
     * @param authInfo credentials
     *
     * @throws ColabErrorMessage if authentication fails for any reason
     */
    @POST
    @Path("/SignIn")
    public void signIn(AuthInfo authInfo) throws ColabErrorMessage {
        userManagement.authenticate(authInfo);
    }

    /**
     * Sign out
     */
    @POST
    public void signOut() {
        userManagement.logout();
    }
}
