describe('Login Form Tests', () => {
  beforeEach(() => {
    // Set up the starting state for each test
    cy.visit('/login') // Adjust this URL to where your login component is served
  })

  it('should display all login form elements', () => {
    cy.get('img[alt="Sparesti logo"]').should('be.visible')
    cy.get('input[name="email"]').should('be.visible')
    cy.get('input[name="password"]').should('be.visible')
    cy.get('button[type="submit"]').contains('Logg inn').should('be.visible')
    cy.get('a[href="/registration"]').should('contain', 'Har du ikke bruker, register deg nå!')
  })

  it('shows username not found when incorrect username is given', () => {
    cy.intercept('POST', '/auth/login', {
      statusCode: 404,
      body: {
        message: 'Brukernavnet eksisterer ikke.'
      }
    }).as('loginFail')

    cy.get('input[name="email"]').type('wrong@example.com')
    cy.get('input[name="password"]').type('wrongPassword')
    cy.get('button[type="submit"]').click()

    cy.contains('Innlogging mislykket').should('be.visible')
    cy.contains('Brukernavnet eksisterer ikke.').should('be.visible')
  })

  it('shows an error toast on login failure', () => {
    // Mock the API response for a failed login
    cy.intercept('POST', '/auth/login', {
      statusCode: 401,
      body: {
        message: 'Feil brukernavn/passord.'
      }
    }).as('loginFail')

    // Type in the credentials and submit the form
    cy.get('input[name="email"]').type('wrong@example.com')
    cy.get('input[name="password"]').type('wrongPassword')
    cy.get('button[type="submit"]').click()

    // Check for error toast message
    cy.contains('Innlogging mislykket').should('be.visible')
    cy.contains('Feil brukernavn/passord.').should('be.visible')

    // Ensure we are still on the login page
    cy.url().should('include', '/login')
  })
})
