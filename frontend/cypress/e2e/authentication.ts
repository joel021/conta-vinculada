import { environment } from "src/environments/environment"
import { testUsers } from "./test_constants"

export function login() {

    const options = {
        method: "POST",
        url: environment.apiUrl + "/users/signin",
        body: {
            siglaUnidade: null,
            usuario: testUsers.admin.username,
            senha: testUsers.admin.password
        },
        headers: {
            "Content-Type": "application/json",
        },
    }
    cy.request(options).then((response) => {

        window.localStorage.setItem(
            "currentUser",
            JSON.stringify(response.body.userDetails)
        )
        window.localStorage.setItem("access_token", response.body.userDetails.token)
        window.localStorage.setItem("id_token", response.body.userDetails.token)
        window.localStorage.setItem(
            "expires_at",
            "" + new Date().getTime() + 10 * 60 * 1000
        )

    })
}