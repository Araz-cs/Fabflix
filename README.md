#  [Fabflix website](http://ec2-3-143-237-60.us-east-2.compute.amazonaws.com:8080/Project1/login.html)

* Implementation of the Movie List page, Single Star page, and Single Movie page for the FabFlix web application.
* Project status: Done

## Video
#### [Fabflix Video Link](https://www.youtube.com/watch?v=5VGDgMAWDd4)

## Github/Repo
* clone the repository inside the `AWS` terminal

```html
 git clone https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57.git
```
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
## Starting App
* clone the repository inside the `AWS` terminal

```html
 git clone https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57.git
```

* SSH onto AWS instance

```html
ec2-3-143-237-60.us-east-2.compute.amazonaws.com
```
* Access tomcat at this link
```html
http://ec2-3-143-237-60.us-east-2.compute.amazonaws.com:8080/manager/html
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

## Substring match design
Takes in user input with the SQL tables. Ex of substring design is 
```html
like %something%
```
## Prepared statements
*[Dashboard](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57/blob/main/Fabflix/Project1/src/main/java/Dashboard.java)
*[LoginServlet](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57/blob/main/Fabflix/Project1/src/main/java/LoginServlet.java)
*[mainPage](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57/blob/main/Fabflix/Project1/src/main/java/mainPage.java)
*[movieList](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57/blob/main/Fabflix/Project1/src/main/java/movieList.java)
*[singleMovie](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57/blob/main/Fabflix/Project1/src/main/java/singleMovie.java)
*[singleStar](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57/blob/main/Fabflix/Project1/src/main/java/singleStar.java)


## Optimization
We did the XML parser but does not function on AWS.

XMLParser before optimization
```html
~6:00
```
After optimization
```html
~5:00
```
Optimization methods
*For casts124.xml we added indexes on stars(name) and movies(title,director) since it looked through both.
*Batch inserts were also used to on casts since there was bulk inserting.

Inconsitiences can be found [here](https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57/blob/main/xmlparser/inconsistencies.txt)

## Contribution
* Araz - Worked on the movieList, singleMovie, and singleStar pages(servlet,js,jdbc,java) and (html/css,MYSQL) files.  Worked shopping list, login, shopping cart, checkout page. Dashboard. XML
* Harman- Worked on the movieList, singleMovie, and singleStar pages(servlet,js,jdbc,java) and (html/css,MYSQL) files. Worked shopping list, login, shopping cart, checkout page. Dashboard. XML
