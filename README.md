- # General
    - #### Team#: 57
    
    - #### Names: Harman Reehal, Araz Sultanian
    
    - #### Project 5 Video Demo Link: https://www.youtube.com/watch?v=do8-hbe3_6Q

    - #### Instruction of deployment:
    ## MYSQL & Tomcat Login Info
1. **Mysql**
```
 Username = mytestuser
 Password = My6$Password
```
2. **Tomcat**
```
 Username = tomcat
 Password = 123tomcat
```
* clone the repository inside the `AWS` terminal

```html
 git clone https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57.git
```

* SSH onto AWS instance

```html
3.22.241.130
```
* Access tomcat at this link
```html
https://3.22.241.130:8443/manager/html
```
## Deploy App
* Inside the repo that contains the pom.xml, build the war file with the command
```html
mvn package
```
* Copy war file with the command
```html
sudo cp ./target/*.war /var/lib/tomcat9/webapps/
```
* Refresh the application manager and click on Project1 to deploy.

## Deploy Android app
* Import cs122b-spring21-project4-android-example on android studio as gradle project.
* Match url to aws instance url (Already done for the current instance)
* In AVD configurations, setup emulator to Pixel 4a API 30
* Run emulator

 - #### Collaborations and Work Distribution:
     * Araz - Worked on the movieList, singleMovie, and singleStar pages(servlet,js,jdbc,java) and (html/css,MYSQL) files.  Worked shopping list, login, shopping cart, checkout page. Dashboard. XML. Updating for fulltext search and autocomplete. Android App. Connection pooling. Master/Slave, GCP.
     * Harman- Worked on the movieList, singleMovie, and singleStar pages(servlet,js,jdbc,java) and (html/css,MYSQL) files. Worked shopping list, login, shopping cart, checkout page. Dashboard. XML. Updating for fulltext search and autocomplete. Android App. Connection pooling. Master/Slave.


- # Connection Pooling
    - #### Include the filename/path of all code/configuration files in GitHub of using JDBC Connection Pooling.
    - #### [AndroidMovieList](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57/blob/main/Fabflix/Project1/src/main/java/AndroidMovieList.java)
    - #### [Dashboard](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57/blob/main/Fabflix/Project1/src/main/java/Dashboard.java)
    - #### [LoginServlet](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57/blob/main/Fabflix/Project1/src/main/java/LoginServlet.java)
    - #### [checkoutPage](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57/blob/main/Fabflix/Project1/src/main/java/checkoutPage.java)
    - #### [confirmationPage](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57/blob/main/Fabflix/Project1/src/main/java/confirmationPage.java)
    - #### [mainPage](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57/blob/main/Fabflix/Project1/src/main/java/mainPage.java)
    - #### [movieList](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57/blob/main/Fabflix/Project1/src/main/java/movieList.java)
    - #### [shoppingCart](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57/blob/main/Fabflix/Project1/src/main/java/shoppingCart.java)
    - #### [singleMovie](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57/blob/main/Fabflix/Project1/src/main/java/singleMovie.java)
    - #### [singleStar](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57/blob/main/Fabflix/Project1/src/main/java/singleStar.java)
    - #### [suggestion](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57/blob/main/Fabflix/Project1/src/main/java/suggestion.java)
    
    - #### Explain how Connection Pooling is utilized in the Fabflix code.
    - #### The way connection pooling is utilized in the fabflix code is by allowing seperate requests to use the same connection in the backend. Connection pooling maintains connections that any new request may create. These requests will recycle previous connections to the database. For example, if a query is sent and it is already an exisiting connection, the connection pool will return the existing connection. If there is not an exisiting connection then it will add it to the pool.
    
    - #### Explain how Connection Pooling works with two backend SQL.
    - #### All read/wrtie requests will be sent to the master instance, meaning the existing servlets will work the same way as if it was deployed on a single instance. The connection pooling is not shared between the master/slave. Read requests will be sent to load balancer which will determine where they will end up in the Master or Slave. If a read request is sent it will check the instance for an existing connection and if it is not there then a new connection will be added to the connection pool.
    

- # Master/Slave
    - #### Include the filename/path of all code/configuration files in GitHub of routing queries to Master/Slave SQL.
    - #### Include the filename/path of all code/configuration files in GitHub of using JDBC Connection Pooling.
    - #### [AndroidMovieList](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57/blob/main/Fabflix/Project1/src/main/java/AndroidMovieList.java) Read (Master/Slave)
    - #### [Dashboard](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57/blob/main/Fabflix/Project1/src/main/java/Dashboard.java)  Read/Write (Master)
    - #### [LoginServlet](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57/blob/main/Fabflix/Project1/src/main/java/LoginServlet.java) Read (Master/Slave)
    - #### [checkoutPage](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57/blob/main/Fabflix/Project1/src/main/java/checkoutPage.java)Read/Write (Master)
    - #### [confirmationPage](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57/blob/main/Fabflix/Project1/src/main/java/confirmationPage.java) Read (Master/Slave)
    - #### [mainPage](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57/blob/main/Fabflix/Project1/src/main/java/mainPage.java) Read (Master/Slave)
    - #### [movieList](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57/blob/main/Fabflix/Project1/src/main/java/movieList.java) Read (Master/Slave)
    - #### [shoppingCart](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57/blob/main/Fabflix/Project1/src/main/java/shoppingCart.java) Read (Master/Slave)
    - #### [singleMovie](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57/blob/main/Fabflix/Project1/src/main/java/singleMovie.java) Read (Master/Slave)
    - #### [singleStar](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57/blob/main/Fabflix/Project1/src/main/java/singleStar.java) Read (Master/Slave)
    - #### [suggestion](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57/blob/main/Fabflix/Project1/src/main/java/suggestion.java) Read (Master/Slave)
    - #### [web](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57/blob/main/Fabflix/Project1/web/WEB-INF/web.xml) Defines Master/Slave
    - #### [context](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57/blob/main/Fabflix/Project1/web/META-INF/context.xml) Registers Master/Slave

    - #### How read/write requests were routed to Master/Slave SQL?
    - Read only servlets routed to `jdbc/moviedb` Datasource. Moviedb redirects to the local which is handled by the load balancer. Read/Write servlets routed to `jdbc/masterdb`. Masterdb redirects to the MasterSQL IP.
    


