package com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.controller;

import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.constants.Constants;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.dto.embedded.oauth2.CustomPrincipalJsonConverted;
import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@org.springframework.context.annotation.Profile({Constants.EXTERNAL_OAUTH2, Constants.EXTERNAL_ADFS, Constants.EMBEDDED_OAUTH2})
@RestController
@CrossOrigin(origins = "*")
@Tag(name = "User Profile", description = "The API provides the User Info with roles etc. as returned by the OAuth2 provider")
public class Profile {

    @GetMapping("/userinfo")
    @Operation(summary = "Provides User Profile Info", description = "Returns JSON formatted User Profile Info", tags = { "user" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned User Profile Info",
                    content = @Content(schema = @Schema(implementation = Principal.class))) ,
            @ApiResponse(responseCode = "401", description = "Invalid Access Token", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Returned when an unexpected error occurs on server side", content = @Content())})
    @ResponseBody
    public ResponseEntity<Principal> getUser(Principal principal)
    {
        return new ResponseEntity(principal, HttpStatus.OK);
    }
}
