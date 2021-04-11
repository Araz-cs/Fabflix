Youtube


Instructions
Download the repository or clone:
https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-57.git

SSH into AWS instance
Startup tomcat
Access the tomcat


Inside the repo with the pom.xml file build the warby using 
mvn package


Then copy the war file
cp ./target/*.war /var/lib/tomcat9/webapps/

Refresh tomcatserver and it should be up.
Click the project link and the website should be there.




Contributions
Araz - Single Movie query.Worked on movie page, single movie page, single star page(servlet,js,html). Implementing the jump functionality. CSS
Harman- Movielist query, Single Star query.Worked on movie page, single movie page, single star page(servlet,js,html).




Sample of the page ran on local host
https://gyazo.com/52835bc133ded65f7d924ed5e3306be2
https://gyazo.com/ab5724ce1df3be6825ad0c51382d4a09
https://gyazo.com/850cba1900ccd4cf273825fe1b46a384
https://gyazo.com/62c77cfb99c4d2cee0ffbf4d43546092
