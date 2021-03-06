
package api.v1

import controller.PersonController
import data.Person
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import withRole

fun Route.personenManagement() {
    withRole("admin") {
        delete("persons/{id}") {
            val personId = call.parameters["id"]
            val removedPerson = PersonController.removePerson(personId!!.toInt())
            call.respond(removedPerson)
        }
        post("persons") {
            val receivedPerson = call.receive<Person>()
            val addedPerson = PersonController.addPerson(receivedPerson)
            call.respond(addedPerson)
            call.request.header("Authorization")
        }
        put("persons/{id}") {
            val personId = call.parameters["id"]
            val newPersonValues = call.receive<Person>()
            val updatedPerson = PersonController.updatePerson(personId!!.toInt(), newPersonValues)
            call.respond(updatedPerson)
        }
    }

    withRole("admin", "user") {
        get("persons") {
            val storedPersons = PersonController.getAllPersons()
            call.respond(storedPersons)
        }
        get("persons/{id}") {
            val personId = call.parameters["id"]
            val storedPersons = PersonController.getPerson(personId!!.toInt())
            call.respond(storedPersons)
        }
    }
}
