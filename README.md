Example for custom Keycloak authenticator and Authorization blog
================================================================

1) Download keycloak demo distribution from http://www.keycloak.org and unzip it somewhere (directory `keycloak` inside the demo distribution will be referred as $KEYCLOAK_HOME). 
Recommended keycloak version is 2.4.1.Final.

2) Build this sample with command:

````
mvn clean install
````

3) Copy custom authentication provider to Keycloak:

````
cp auth-secure-question/target/auth-secure-question-1.0-SNAPSHOT.jar $KEYCLOAK_HOME/providers/
````

4) Run Keycloak server

````
cd $KEYCLOAK_HOME/bin
./standalone.sh
````

5) Go to `http://localhost:8080/auth/` and setup admin account (See Keycloak docs for more details). Then login to admin console `http://localhost:8080/auth/admin` as admin.
 
6) Import `jbug-demo.json` realm (File in current directory) via Keycloak admin console

7) Some manual step is required now, so you can try to create your policy and permissions. In admin console go to:
Clients -> authz-blog -> Authorization -> Policies -> Create policy of type `User`
 Fill:
 Name: john-policy
 User: john
 Login: Positive
 
 Save it.
 
8) Return to "Authorization" and then go to "Permissions". Create permission and select `Scope based` . Then fill:
Name: create-blogpost-permission
Resource: blog
Scopes: create-blogpost
Apply policy: john-policy
Decision strategy: Unanimous

9) Create anouther Permission of `Scope based` . Fill:
Name: View-blogpost-permission
Resource: Any resource
Scope: view-blogpost
Apply policy: john-policy , view-blogpost (Note that both policies will be added).
Decision strategy: Affirmative

10) Deploy application:

````
cp webapp/target/webapp.war $KEYCLOAK_HOME/standalone/deployments/
````

11) Go to `http://localhost:8080/webapp/create` and login as `john` with password `password` . 
On next screen, you must fill favourite shop `Albert` . 

This screen is our custom authenticator `auth-secure-question` . It expects that you fill the value `Albert`, which corresponds to the attribute
`favourite.shop` of the particular user. See the source code of authenticator of more details. Also check the attribute of user `john` and check the configuration 
 of Authenticators for realm `jbug` in admin console.
 
12) You were redirected to the screen `http://localhost:8080/webapp/create` after successful login. Create some messages. For example:
Message: "m1" Shared with: "nobody"
Message: "m2" Shared with: "bill"
Message: "m3" Shared with: "tess"

Go to `http://localhost:8080/webapp/view` and check that user john is able to see all the messages (Because of `view-blogpost-permission` and `john-policy`, which allows john to 
see all the messages
 
13) Logout, then login as `bill` ,Password: `password` , Favourite shop: `Billa`
You can see just message `m2` shared with bill. That's because of `view-blogpost-permission` and `view-blogpost` policy. 

14) Check in admin console and go to `Resources` and see that application dynamically created resources for new messages.



