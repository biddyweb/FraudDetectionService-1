package com.ofg.fraud

import com.ofg.infrastructure.web.resttemplate.fluent.ServiceRestClient
import com.wordnik.swagger.annotations.Api
import groovy.json.JsonBuilder
import groovy.transform.TypeChecked
import groovy.util.logging.Slf4j
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import javax.validation.constraints.NotNull

import static org.springframework.web.bind.annotation.RequestMethod.PUT

@Slf4j
@RestController
@RequestMapping('/api/loanApplication/{loanApplicationId}')
@TypeChecked
@Api(value = "loanApplicationId", description = "Defines if application is fraud")
class FraudController {

    ServiceRestClient serviceRestClient;

    @RequestMapping(
            value = '{loanApplicationId}',
            method = PUT)

    ResponseEntity<Void> checkApplication(
            @PathVariable @NotNull long loanApplicationId,
            @RequestBody @NotNull String firstName,
            @RequestBody @NotNull String lastName,
            @RequestBody @NotNull String job, @RequestBody @NotNull long amount, @RequestBody @NotNull long age) {

        def fraudStatus = "GREEN";

        if (age <= 21 || age >= 65) {
            fraudStatus = "RED"
        }

        def json = new JsonBuilder()
        def root = json firstName: firstName, lastName: lastName, job: job, amount: amount, fraudStatus: fraudStatus

        serviceRestClient.forService('decision-maker').put()
                .onUrl('/api/loanApplication/' + loanApplicationId)
                .body(root)
                .withHeaders()
                .contentTypeJson()
                .andExecuteFor()
                .aResponseEntity()
                .ofType(String)

    }

}
