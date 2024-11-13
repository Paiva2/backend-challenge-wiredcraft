# Backend Developer Challenge

#### Ref - https://github.com/Wiredcraft/test-backend

This is my solution of the above Backend Challenge using Java, it's a open-source challenge listed
in https://github.com/CollabCodeTech/backend-challenges

### [Solution Docs](docs.md)

### Technologies

- Java 17
- Spring Boot
- Spring Security
- Swagger
- Postgres
- Docker

### Requirements

Build a RESTful API that can `get/create/update/delete` user data from a persistence database

### User Model

```
{
  "id": "xxx",                  // user ID 
  "name": "test",               // user name
  "dob": "",                    // date of birth
  "address": "",                // user address
  "description": "",            // user description
  "createdAt": ""               // user created date
}
```

- The API should follow typical RESTful API design pattern.
- The data should be saved in the DB.
- Provide proper unit test.
- Provide proper API document.

### Bonus

- Write clear documentation on how it's designed and how to run the code.
- Write good in-code comments.
- Write good commit messages.
- An online demo is always welcome.

### Advanced requirements

- Provide a complete user auth (authentication/authorization/etc.) strategy, such as OAuth.
- Provide a complete logging (when/how/etc.) strategy.
- Imagine we have a new requirement right now that the user instances need to link to each other, i.e., a list of "
  followers/following" or "friends". Can you find out how you would design the model structure and what API you would
  build for querying or modifying it?
- Related to the requirement above, suppose the address of user now includes a geographic coordinate(i.e., latitude and
  longitude), can you build an API that,
    - given a user name
    - return the nearby friends
