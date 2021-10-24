# GLBCI

GLBCI is a simple CRUD to manage Users creation

##Instalation
Download the code from the repository and execute the following command from the root folder

```bash
gradlew run
```

You can also build a jar file with the application
```bash
gradlew build
```
After it finish you will find it under build\libs. To run it execute
```bash
java -jar {file name}
```

## REST API
By default the application run in the port 8081.

All password must follow the format of One upercase letter, lowercase case letters and two digits

###Create User
####Request
`POST /api/users`

```
{
"name": "Juan Rodriguez",
"email": "jsusasn@rodriguezs.org",
"password": "Ssdsds52",
"phones": [
    {
        "number": "1234567",
        "citycode": "1",
        "countrycode": "57"
    }
    ]
}
```

####Response
`Status 201 OK`
```
{
    "id": "1f49111f-da82-44ca-bbf9-f4b03908ad09",
    "name": "Juan Rodriguez",
    "email": "jsusasn@rodriguezs.org",
    "password": "Ssdsds52",
    "phones": [
        {
            "number": "1234567",
            "citycode": "1",
            "countrycode": "57"
        }
    ],
    "created": "2021-10-24T21:02:30.265+00:00",
    "modified": "2021-10-24T21:02:30.265+00:00",
    "lastLogin": "2021-10-24T21:02:30.265+00:00",
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqc3VzYXNuQHJvZHJpZ3VlenMub3JnIiwiZXhwIjoxNjM1MTUyNTUwLCJpYXQiOjE2MzUxMDkzNTB9.u4fOMMU02WphKBAzXaYYbm26I8a2sknw3oFLHP23qSnZZSTmjHyjBJYYPesnMEskCtexwNWhcQsyhcV5qxoQDg",
    "active": true
}
```

###Get User
####Request
`GET /api/users/{user id}`

####Response
`Status 200 OK`
```
{
    "id": "1f49111f-da82-44ca-bbf9-f4b03908ad09",
    "name": "Juan Rodriguez",
    "email": "jsusasn@rodriguezs.org",
    "password": "Ssdsds52",
    "phones": [
        {
            "number": "1234567",
            "citycode": "1",
            "countrycode": "57"
        }
    ],
    "created": "2021-10-24T21:02:30.265+00:00",
    "modified": "2021-10-24T21:02:30.265+00:00",
    "lastLogin": "2021-10-24T21:02:30.265+00:00",
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqc3VzYXNuQHJvZHJpZ3VlenMub3JnIiwiZXhwIjoxNjM1MTUyNTUwLCJpYXQiOjE2MzUxMDkzNTB9.u4fOMMU02WphKBAzXaYYbm26I8a2sknw3oFLHP23qSnZZSTmjHyjBJYYPesnMEskCtexwNWhcQsyhcV5qxoQDg",
    "active": true
}
```

###Update User
####Request
`PUT /api/users`

```
{
"id": "02a066fd-7ebc-4b7d-89bf-d50c25f9d8ef",
"name": "Juan Rodriguez",
"email": "jsusasn@rodriguezs.org",
"password": "Newpwd56",
"phones": [
    {
        "number": "1234567",
        "citycode": "1",
        "countrycode": "57"
    }
    ],
"token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqc3VzYXNuQHJvZHJpZ3VlenMub3JnIiwiZXhwIjoxNjM1MTUyNTUwLCJpYXQiOjE2MzUxMDkzNTB9.u4fOMMU02WphKBAzXaYYbm26I8a2sknw3oFLHP23qSnZZSTmjHyjBJYYPesnMEskCtexwNWhcQsyhcV5qxoQDg",
"active": true
}
```

####Response
`Status 200 OK`
```
{
    "id": "1f49111f-da82-44ca-bbf9-f4b03908ad09",
    "name": "Juan Rodriguez",
    "email": "jsusasn@rodriguezs.org",
    "password": "Newpwd56",
    "phones": [
        {
            "number": "1234567",
            "citycode": "1",
            "countrycode": "57"
        }
    ],
    "created": "2021-10-24T21:02:30.265+00:00",
    "modified": "2021-10-24T21:02:30.265+00:00",
    "lastLogin": "2021-10-24T21:02:30.265+00:00",
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqc3VzYXNuQHJvZHJpZ3VlenMub3JnIiwiZXhwIjoxNjM1MTUyNTUwLCJpYXQiOjE2MzUxMDkzNTB9.u4fOMMU02WphKBAzXaYYbm26I8a2sknw3oFLHP23qSnZZSTmjHyjBJYYPesnMEskCtexwNWhcQsyhcV5qxoQDg",
    "active": true
}
```

###Delete User
####Request
`DELETE /api/users/{user id}`

####Response
`Status 200 OK`
```
{
    "message": "User with ID f0647b4c-e75b-4ccd-b241-8aab6354740f was deleted.",
    "date": "2021-10-24T21:28:17.783+00:00"
}
```