#  [Fabflix website](http://3.128.203.199:8080/Project1/)

* Implementation of the Movie List page, Single Star page, and Single Movie page for the FabFlix web application.
* Project status: Done

## Video
#### [Fabflix Video Link](https://youtu.be/jgGGhv3XUOg)

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
ec2-3-128-203-199.us-east-2.compute.amazonaws.com
```
* Access tomcat at this link
```html
http://ec2-3-128-203-199.us-east-2.compute.amazonaws.com:8080/manager/html
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


## Contribution
* Araz - Worked on the movieList, singleMovie, and singleStar pages(servlet,js,jdbc,java) and (html/css,MYSQL) files.  Worked shopping list, login, shopping cart, checkout page.
* Harman- Worked on the movieList, singleMovie, and singleStar pages(servlet,js,jdbc,java) and (html/css,MYSQL) files. Worked shopping list, login, shopping cart, checkout page.
