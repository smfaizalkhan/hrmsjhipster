Feature: User management

  Scenario: Retrieve all authorities
    When i login as 'admin' with password 'admin' 
    And I'm able to access getAuthorities method
    Then  I get List of Authorities
     |ROLE_ADMIN | ROLE_HR | ROLE_USER |
 
  Scenario: Should get 403 when admin role method is accessed as user
   When i login as 'user' with password 'user'
   And i access getAuthorities method
   Then  i get 403 status code
  
    

